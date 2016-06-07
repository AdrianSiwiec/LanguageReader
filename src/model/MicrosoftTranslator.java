package model;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import controller.App;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pierre on 14/05/16.
 */
public class MicrosoftTranslator implements LanguageTranslator {
    static ArrayList<LanguageClass> availableLanguages = new ArrayList<>();
    ConcurrentHashMap<LanguagePair, Integer> lastSerializationSize = new ConcurrentHashMap<>();

    ConcurrentHashMap<LanguagePair, ConcurrentHashMap<String, String>> alreadyTranslated = new ConcurrentHashMap<>();
    LanguagePair currentLanguages = new LanguagePair(Language.ENGLISH, Language.POLISH);

    public MicrosoftTranslator() {
        if(alreadyTranslated.get(currentLanguages)==null) {
            alreadyTranslated.put(currentLanguages, new ConcurrentHashMap<>());
        }
        App.getController().getDaemonExecutorService().execute(() -> deserialize(new LanguagePair(currentLanguages)));
        Translate.setClientId("languagereader"); //zarejestrowalem sie na swoim koncie w microsoft azure
        Translate.setClientSecret("Xq5cFU9uWVUPXJoiyu3UEoOO7AtC8l1j+Uichl8DooU="); //2M chars/month za darmo
    }

    public class LanguagePair {
        Language from, to;
        LanguagePair(Language from, Language to) {
            this.from = from;
            this.to = to;
        }
        LanguagePair(LanguagePair lang) {
            this.from = lang.from;
            this.to = lang.to;
        }
        @Override
        public int hashCode() {
            return from.toString().hashCode()*103+to.toString().hashCode();
        }
        @Override
        public boolean equals(Object o) {
            if(o instanceof LanguagePair) {
                LanguagePair l = (LanguagePair) o;
                return l.from.equals(from) && l.to.equals(to);
            }
            return false;
        }
        public String getFrom(){
            String name = null;
            try{
                name = from.getName(Language.ENGLISH);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return name;
        }

        public String getTo(){
            String name = null;
            try{
                name = to.getName(Language.ENGLISH);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return name;
        }
    }

    @Override
    public java.lang.String translate(java.lang.String word) {
        if(!alreadyTranslated.get(currentLanguages).containsKey(word))
        try {
            alreadyTranslated.get(currentLanguages).put(word, Translate.execute(word, currentLanguages.from, currentLanguages.to));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if(lastSerializationSize.get(currentLanguages)==null) lastSerializationSize.put(currentLanguages, 0);
//        if(alreadyTranslated.get(currentLanguages).keySet().size()>=lastSerializationSize.get(currentLanguages)+100)
//            App.getController().getExecutorService().execute(() -> this.serialize(new LanguagePair(currentLanguages)));
        return alreadyTranslated.get(currentLanguages).get(word);
    }

    @Override
    public List<LanguageClass> getAvailableLanguages() {
        return availableLanguages;
    }

    @Override
    public void setLanguageTo(LanguageClass lang) {
        currentLanguages = new LanguagePair(currentLanguages.from, Language.fromString(lang.code));
        if(alreadyTranslated.get(currentLanguages)==null) {
            alreadyTranslated.put(currentLanguages, new ConcurrentHashMap<>());
        }
        App.getController().getDaemonExecutorService().execute(() -> deserialize(new LanguagePair(currentLanguages)));
    }

    @Override
    public void setLanguageFrom(LanguageClass lang) {
        currentLanguages = new LanguagePair(Language.fromString(lang.code), currentLanguages.to);
        if(alreadyTranslated.get(currentLanguages)==null) {
            alreadyTranslated.put(currentLanguages, new ConcurrentHashMap<>());
        }
        App.getController().getDaemonExecutorService().execute(() -> deserialize(new LanguagePair(currentLanguages)));
    }

    @Override
    public String getLanguageFrom(){
        return currentLanguages.getFrom();
    }

    @Override
    public String getLanguageTo(){
        return currentLanguages.getTo();
    }

    public void serialize() {
        if(alreadyTranslated.get(currentLanguages).keySet().size()>lastSerializationSize.get(currentLanguages))
            App.getController().getExecutorService().execute(() -> this.serialize(new LanguagePair(currentLanguages)));
    }

    @Override
    public void deserialize() {
        //todo
    }

    @Override
    public void correctWord(String word, String translation) {
        //todo
    }

    private void serialize(LanguagePair language) {
        try {
            new File("cache").mkdirs();
            FileOutputStream fileOut = new FileOutputStream(getSerializationFilename(getTranslatorName(language)));
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(alreadyTranslated.get(language));
            out.close();
            fileOut.close();
            lastSerializationSize.put(language, alreadyTranslated.get(language).keySet().size());
            System.out.println("Serialization successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deserialize(LanguagePair language) {
        try {
            System.out.println("Deserializing Dictionary");
            new File("cache").mkdirs();
            FileInputStream fileIn = new FileInputStream(getSerializationFilename(getTranslatorName(language)));
            ObjectInputStream in = new ObjectInputStream(fileIn);
            alreadyTranslated.put(language, (ConcurrentHashMap<String, String>) in.readObject());
            in.close();
            fileIn.close();
            lastSerializationSize.put(language, alreadyTranslated.get(language).keySet().size());
            new File("cache/backup").mkdirs();
            Files.copy(Paths.get(getSerializationFilename(getTranslatorName(language))),
                    Paths.get(getSerializationBackupFilename(getTranslatorName(language))), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Deserialization succesful, dictionary size: " + alreadyTranslated.get(currentLanguages).keySet().size());
        }
        catch (EOFException e) {
            System.out.println("Dictionary corrupted, trying to fix");
            try {
                Files.copy(Paths.get(getSerializationBackupFilename(getTranslatorName(language))),
                        Paths.get(getSerializationFilename(getTranslatorName(language))),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception f) {
                System.out.println("Dictionary fix did not helped, deleting dictionary");
                try {
                    Files.delete(Paths.get(getSerializationFilename(getTranslatorName(language))));
                } catch (Exception g) {
                    //
                }
            }
            deserialize(language);
        } catch (Exception e) {
            System.out.println("Deserialization Failed while trying to read: "+getSerializationFilename(getTranslatorName(language)));
//            e.printStackTrace();
            alreadyTranslated.put(language, new ConcurrentHashMap<>());
        }
    }

    private java.lang.String getTranslatorName(LanguagePair language) {
        return "MicrosoftTranslatorCache_"+language.from.toString()+"_"+language.to.toString();
    }

    private String getSerializationFilename(String translatorLanguage) {
        return "cache/"+translatorLanguage+".ser";
    }

    private String getSerializationBackupFilename(String translatorLanguage) {
        return "cache/backup/"+translatorLanguage+".ser";
    }

    static {
        availableLanguages.add(new LanguageClass("English","en"));
        availableLanguages.add(new LanguageClass("Polish","pl"));
        availableLanguages.add(new LanguageClass("French","fr"));
        availableLanguages.add(new LanguageClass("Spanish","es"));
        availableLanguages.add(new LanguageClass("Arabic","ar"));
        availableLanguages.add(new LanguageClass("Bulgarian","bg"));
        availableLanguages.add(new LanguageClass("Catalan","ca"));
        availableLanguages.add(new LanguageClass("Czech","cs"));
        availableLanguages.add(new LanguageClass("Danish","da"));
        availableLanguages.add(new LanguageClass("Dutch","nl"));
        availableLanguages.add(new LanguageClass("Estonian","et"));
        availableLanguages.add(new LanguageClass("Finnish","fi"));
        availableLanguages.add(new LanguageClass("German","de"));
        availableLanguages.add(new LanguageClass("Greek","el"));
        availableLanguages.add(new LanguageClass("Haitian Creole","ht"));
        availableLanguages.add(new LanguageClass("Hebrew","he"));
        availableLanguages.add(new LanguageClass("Hindi","hi"));
        availableLanguages.add(new LanguageClass("Hmong_daw","mww"));
        availableLanguages.add(new LanguageClass("Hungarian","hu"));
        availableLanguages.add(new LanguageClass("Indonesian","id"));
        availableLanguages.add(new LanguageClass("Italian","it"));
        availableLanguages.add(new LanguageClass("Japanese","ja"));
        availableLanguages.add(new LanguageClass("Korean","ko"));
        availableLanguages.add(new LanguageClass("Latvian","lv"));
        availableLanguages.add(new LanguageClass("Lithuanian","lt"));
        availableLanguages.add(new LanguageClass("Malay","ms"));
        availableLanguages.add(new LanguageClass("Norwegian","no"));
        availableLanguages.add(new LanguageClass("Persian","fa"));
        availableLanguages.add(new LanguageClass("Portuguese","pt"));
        availableLanguages.add(new LanguageClass("Romanian","ro"));
        availableLanguages.add(new LanguageClass("Russian","ru"));
        availableLanguages.add(new LanguageClass("Slovak","sk"));
        availableLanguages.add(new LanguageClass("Slovenian","sl"));
        availableLanguages.add(new LanguageClass("Swedish","sv"));
        availableLanguages.add(new LanguageClass("Thai","th"));
        availableLanguages.add(new LanguageClass("Turkish","tr"));
        availableLanguages.add(new LanguageClass("Ukrainian","uk"));
        availableLanguages.add(new LanguageClass("Urdu","ur"));
        availableLanguages.add(new LanguageClass("Vietnamese","vi"));
    }
}

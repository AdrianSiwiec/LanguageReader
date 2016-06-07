package model;

import com.memetix.mst.language.Language;
import controller.App;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pierre on 07/06/16.
 */
public abstract class OnlineTranslator implements LanguageTranslator {
    static ArrayList<LanguageClass> availableLanguages = new ArrayList<>();
    ConcurrentHashMap<LanguagePair, ConcurrentHashMap<String, String>> alreadyTranslated = new ConcurrentHashMap<>();
    LanguagePair currentLanguages = new LanguagePair(Language.ENGLISH, Language.POLISH);
    protected abstract String getOnlineTranslation(String word) throws Exception;
    protected abstract String getTranslatorName(LanguagePair language);
    ArrayList<Serializer> serializers = new ArrayList<>();

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
    }

    @Override
    public String translate(String word) {
        if(!alreadyTranslated.get(currentLanguages).containsKey(word))
        try {
            alreadyTranslated.get(currentLanguages).put(word, getOnlineTranslation(word));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
//        if(alreadyTranslated.get(currentLanguages).keySet().size()>=lastSerializationSize.get(currentLanguages)+100)
//            App.getController().getExecutorService().execute(() -> this.serialize(new LanguagePair(currentLanguages)));
        return alreadyTranslated.get(currentLanguages).get(word);
    }

    @Override
    public void correctWord(String word, String translation) {
        alreadyTranslated.get(currentLanguages).put(word, translation);
        serialize();
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
        App.getController().getDaemonExecutorService().execute(this::deserialize);
    }

    @Override
    public void setLanguageFrom(LanguageClass lang) {
        currentLanguages = new LanguagePair(Language.fromString(lang.code), currentLanguages.to);
        if(alreadyTranslated.get(currentLanguages)==null) {
            alreadyTranslated.put(currentLanguages, new ConcurrentHashMap<>());
        }
        App.getController().getDaemonExecutorService().execute(this::deserialize);
    }

    public void serialize() {
        for(Serializer s: serializers) {
            App.getController().getExecutorService().execute(() -> s.serializeDictionary(new LanguagePair(currentLanguages)));
        }
    }

    public void deserialize() {
        for(Serializer s: serializers) {
            App.getController().getDaemonExecutorService().execute(() -> s.deserializeDictionary(new LanguagePair(currentLanguages)));
        }
    }

    protected class PlainSerializer implements Serializer {
        ConcurrentHashMap<LanguagePair, Integer> lastSerializationSize = new ConcurrentHashMap<>();
        String serializationDirectory = "cache/plainSerializations/";
        String serializationBackupDirectory = serializationDirectory + "backup/";
        String serializationExtension = ".ser";

        @Override
        public void serializeDictionary(LanguagePair language) {
            try {
                new File(serializationDirectory).mkdirs();
                FileOutputStream fileOut = new FileOutputStream(
                        serializationDirectory + getTranslatorName(language) + serializationExtension);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(alreadyTranslated.get(language));
                out.close();
                fileOut.close();
                lastSerializationSize.put(language, alreadyTranslated.get(language).keySet().size());
                System.out.println("Plain serialization successful");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void deserializeDictionary(LanguagePair language) {
            try {
                System.out.println("Deserializing plain Dictionary");
                new File(serializationDirectory).mkdirs();
                FileInputStream fileIn = new FileInputStream(
                        serializationDirectory + getTranslatorName(language) + serializationExtension);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                alreadyTranslated.put(language, (ConcurrentHashMap<String, String>) in.readObject());
                in.close();
                fileIn.close();
                lastSerializationSize.put(language, alreadyTranslated.get(language).keySet().size());
                new File(serializationBackupDirectory).mkdirs();
                Files.copy(Paths.get(serializationDirectory + getTranslatorName(language) + serializationExtension),
                        Paths.get(serializationBackupDirectory + (getTranslatorName(language) + serializationExtension)), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Deserialization succesful, dictionary size: " + alreadyTranslated.get(currentLanguages).keySet().size());
            } catch (EOFException e) {
                System.out.println("Dictionary corrupted, trying to fix");
                try {
                    Files.copy(Paths.get(serializationBackupDirectory + getTranslatorName(language) + serializationExtension),
                            Paths.get(serializationDirectory + getTranslatorName(language) + serializationExtension),
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception f) {
                    System.out.println("Dictionary fix did not helped, deleting dictionary");
                    try {
                        Files.delete(Paths.get(serializationDirectory + getTranslatorName(language) + serializationExtension));
                    } catch (Exception g) {
                        //
                    }
                }
                deserializeDictionary(language);
            } catch (Exception e) {
                System.out.println("Deserialization Failed while trying to read: " + serializationDirectory + getTranslatorName(language));
//            e.printStackTrace();
                alreadyTranslated.put(language, new ConcurrentHashMap<>());
            }
        }

    }
    protected class TextFileSerializer implements Serializer {
        ConcurrentHashMap<LanguagePair, Integer> lastSerializationSize = new ConcurrentHashMap<>();
        String serializationDirectory = "Dictionaries/";
        String serializationExtension = ".txt";
        public void serializeDictionary(LanguagePair language) {
            if(true) return; //read only
            System.out.println("Serializing text Dictionary");
            try {
                new File(serializationDirectory).mkdirs();
                PrintWriter writer = new PrintWriter(
                        serializationDirectory + getTranslatorName(language) + serializationExtension, "UTF-8");
                for(String s: alreadyTranslated.get(language).keySet()) {
                    writer.println(s+";"+alreadyTranslated.get(language).get(s));
                }
                writer.close();

                System.out.println("Serialization succesful");
            } catch (Exception e) {
                System.out.println("Serialization unsuccesful");
            }
        }

        @Override
        public void deserializeDictionary(LanguagePair language) {
            System.out.println("Deserializing text Dictionary");
            try {
                Scanner scanner = new Scanner(new File(serializationDirectory+getTranslatorName(language)+serializationExtension));
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    alreadyTranslated.get(language).put(line.substring(0, line.lastIndexOf(";")),
                            line.substring(line.lastIndexOf(";")+1));
                    System.out.println("Added: "+line.substring(0, line.lastIndexOf(";"))+" : "+line.substring(line.lastIndexOf(";")+1));
                }

                System.out.println("Serialization succesful");
            } catch (Exception e) {
                System.out.println("Serialization unsuccesful");
            }
        }
    }
}

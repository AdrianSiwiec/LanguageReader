package model;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import controller.App;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by pierre on 14/05/16.
 */
public class MicrosoftTranslator implements LanguageTranslator {
    Language from = Language.ENGLISH;
    Language to = Language.POLISH;
    int lastSerializationSize;

    ConcurrentHashMap<String, String> alreadyTranslated = new ConcurrentHashMap<>();
    boolean serialized = false;

    public MicrosoftTranslator() { //TODO set language in constructor
        deserialize();
        Translate.setClientId("languagereader"); //zarejestrowalem sie na swoim koncie w microsoft azure
        Translate.setClientSecret("Xq5cFU9uWVUPXJoiyu3UEoOO7AtC8l1j+Uichl8DooU="); //2M chars/month za darmo
    }

    @Override
    public String translate(String word) {
        if(!alreadyTranslated.containsKey(word))
        try {
            alreadyTranslated.put(word, Translate.execute(word, from, to));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if(alreadyTranslated.keySet().size()>=lastSerializationSize+100)
            App.getController().getExecutorService().execute(this::serialize);
        return alreadyTranslated.get(word);
    }

    private void serialize() {
        try {
            new File("cache").mkdirs();
            FileOutputStream fileOut = new FileOutputStream("cache/"+getTranslatorName()+".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(alreadyTranslated);
            out.close();
            fileOut.close();
            lastSerializationSize = alreadyTranslated.keySet().size();
            System.out.println("Serialization successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream("cache/"+getTranslatorName()+".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            alreadyTranslated = (ConcurrentHashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
            lastSerializationSize = alreadyTranslated.keySet().size();
            System.out.println("Deserialization succesful, dictionary size: "+alreadyTranslated.keySet().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTranslatorName() {
        return "MicrosoftTranslatorCache_"+from.toString()+"_"+to.toString();
    }
}

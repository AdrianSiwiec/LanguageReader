package model;

import controller.App;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pawel on 10.05.16.
 */
public class Page {

    private ArrayList<String> words;

    public Page(String Text){
        words = new ArrayList<>(Arrays.asList(Text.split("\\s")));
        App.getController().getDaemonExecutorService().execute(() -> {
            System.out.println("Caching page translation");
            for (String s : words)
                App.getController().getTranslation(s);
            System.out.println("Cached whole page translation for later use");
        });
    }

    public Page(ArrayList<String> words){
        this.words = words;
    }

    public int getNumberOfWords(){
        return words.size();
    }

    public ArrayList<String> getWords(){
        return words;
    }
}

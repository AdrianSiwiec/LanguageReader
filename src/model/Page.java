package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pawel on 10.05.16.
 */
public class Page {

    private ArrayList<String> words;

    public Page(String Text){
        words = new ArrayList<>(Arrays.asList(Text.split("\\s")));
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

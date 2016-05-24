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
        words = new ArrayList(Arrays.asList(Text.split("[ \t\\x0B\f\r]+")));
        refactor();
        App.getController().getDaemonExecutorService().execute(() -> {
            System.out.println("Caching page translation");
            for (String s : words) {
                if(!s.equals("\n"))
                    App.getController().getTranslation(s);
            }
            System.out.println("Cached whole page translation for later use");
        });
    }

    public Page(ArrayList<String> words){
        this.words = words;
    }

    private void refactor(){
        ArrayList<String> word = new ArrayList<>();
        for(String s: words){
            if(s.lastIndexOf('\n')!=-1){
                boolean czy = false;
                int x = s.lastIndexOf('\n');
                int y = s.indexOf('\n');
                String a = s.substring(0, y);
                word.add(a);
                int licznik=0;
                while(licznik < (x-y+1)){
                    word.add("\n");
                    licznik++;
                }
                if(x+1<s.length()) {
                    a = s.substring(x+1, s.length());
                    word.add(a);
                }
            }
            else
                word.add(s);
        }
        words = word;
    }

    public int getNumberOfWords(){
        return words.size();
    }

    public ArrayList<String> getWords(){
        return words;
    }
}

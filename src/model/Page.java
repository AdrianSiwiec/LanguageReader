package model;

import controller.App;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pawel on 10.05.16.
 */


public class Page {
    private ArrayList<Lines> linesArray;
    private ArrayList<String> words;
    public Page(String Text){
        words = new ArrayList(Arrays.asList(Text.split("[ \t\\x0B\f\r]+")));
        refactor();
    }

    public void cacheTranslation() {
        if(words!=null)
            internalCacheTranslation(words);
        if(linesArray!=null)
            internalCacheTranslation(linesArray, 0);
    }

    private void internalCacheTranslation(ArrayList<String> list) {
        App.getController().getDaemonExecutorService().execute(() -> {

            System.out.println("Caching page translation");
            for (String s : list) {
                if(!s.equals("\n")) {
                    String c = s.replaceAll("[^A-Za-z']+", "");
                    App.getController().getTranslation(c);
                }
            }
            System.out.println("Cached whole page translation for later use");
        });
    }

    private void internalCacheTranslation(ArrayList<Lines> list, Integer integer) {
        App.getController().getDaemonExecutorService().execute(() -> {
            System.out.println("Caching page translation for later");
            for(Lines l: list) {
                for(String s: l.getList()) {
                    if(!s.equals("\n")) {
                        String c = s.replaceAll("[^A-Za-z']+", "");
                        App.getController().getTranslation(s);
                    }
                }
            }
            System.out.println("Cached whole page translation for later use");
            App.getController().serializeDictionary();
        });
    }

    public Page(ArrayList<Lines> linesArray) { this.linesArray = linesArray; }

    private String find(String a){
        ArrayList<String> bum = new ArrayList(Arrays.asList(a.split("\\P{Alpha}+")));
        return bum.get(0);
    }


    private void refactor(){
        ArrayList<String> word = new ArrayList<>();
        ArrayList<Lines> lines = new ArrayList<>();
        Lines line = new Lines();
        for(String s: words){
            if(s.lastIndexOf('\n')!=-1){
                boolean czy = false;
                int x = s.lastIndexOf('\n');
                int y = s.indexOf('\n');
                String a = s.substring(0, y);
                word.add(a);
                line.add(a);
                lines.add(line);
                line = new Lines();
                word.add("\n");
                if(x+1<s.length()) {
                    a = s.substring(x+1, s.length());
                    word.add(a);
                    line.add(a);
                }
            }
            else {
                word.add(s);
                line.add(s);
            }
        }
        lines.add(line);
        words = word;
        linesArray = lines;
    }

    public int getNumberOfWords(){
        return words.size();
    }

    public int getNumberOfLines(){
        return linesArray.size();
    }

    public ArrayList<String> getWords(){
        return words;
    }

    public ArrayList<Lines> getLines() { return linesArray; }
}
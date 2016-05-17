package model;

import controller.App;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pawel on 10.05.16.
 */


public class Page {

    private ArrayList<String> words;
    private ArrayList<Pair<String, Integer>> splited;
    public Page(String Text){
        words = new ArrayList(Arrays.asList(Text.split("\\s+")));
        splited = new ArrayList<>();
        process();
        App.getController().getDaemonExecutorService().execute(() -> {
            System.out.println("Caching page translation");
            for (Pair<String, Integer> s : splited) {
                if(s.getType() == (Integer)0)
                    App.getController().getTranslation(s.getWord());
            }
            System.out.println("Cached whole page translation for later use");
        });
    }

    public Page(ArrayList<Pair<String, Integer>> splited){
        this.splited = splited;
    }


    public void process(){

        for(String x : words){
            if(x.matches("[a-zA-Z']+"))
                splited.add(new Pair<>(x+" ", 0));
            else{
                int i=0;
                boolean which=true;
                StringBuilder y = new StringBuilder();
                while(i<x.length()){
                    char c = x.charAt(i);
                    if(c=='\'' || Character.isLetter(c)) {
                        if(which) {
                            y.append(c);
                        }
                        else{
                            splited.add(new Pair<>(y.toString(), 1));
                            y = new StringBuilder();
                            y.append(c);
                            which = true;
                        }
                    }
                    else{
                        if(which){
                            splited.add(new Pair<>(y.toString(), 0));
                            y = new StringBuilder();
                            y.append(c);
                            which = false;
                        }
                        else
                            y.append(c);
                    }
                    i++;
                }
                if(which)
                    splited.add(new Pair<>(y.toString(), 0));
                else
                    splited.add(new Pair<>(y.toString(), 1));
            }
        }
        System.out.println(splited.size());
    }

    public int getNumberOfWords(){
        return splited.size();
    }

    public ArrayList<Pair<String, Integer>> getWords(){
        return splited;
    }
}

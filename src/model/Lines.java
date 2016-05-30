package model;

import java.util.ArrayList;

/**
 * Created by pawel on 24.05.16.
 */
public class Lines {
    private ArrayList<String> line;

    public Lines(){
        line = new ArrayList<>();
    }

    public void add(String a){
        line.add(a);
    }

    public String get(int i){
        return line.get(i);
    }

    public int size(){
        return line.size();
    }
}

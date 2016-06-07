package model;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pawel on 07.06.16.
 */
public class WordsToFile implements ExportWords{

    String extension, from, to, fileName, path;
    boolean whatToDo=false;
    File file;
    HashMap<String, String> mapa;
    public WordsToFile(String from, String to, String extension){
        this.extension = extension;
        this.from = from;
        this.to = to;
        fileName = from+'_'+to+'.'+extension;
        String userDirectory = Paths.get(".").toAbsolutePath().normalize().toString();
        path = userDirectory+"\\"+fileName;
        createFile();
        mapa = new HashMap<>();
    }
    @Override
    public void addWord(String word, String translation){
        if(mapa.containsKey(word)){
            if(!mapa.get(word).equals(translation)){
                mapa.remove(word);
                mapa.put(word, translation);
            }
        }
        else
            mapa.put(word, translation);
    }
    @Override
    public void export(){
        FileWriter fw = null;
        boolean czy = false;
        try {
            fw = new FileWriter(file);
            czy=true;
        }
        catch(Exception e){
            czy = false;
            e.printStackTrace();
        }
        System.out.println(czy+" "+mapa.size());
        if(czy) {
            Iterator it = mapa.entrySet().iterator();
            StringBuilder nowy = new StringBuilder();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
               // System.out.println(pair.getKey() + " = " + pair.getValue());
                String para = pair.getKey() + ": " + pair.getValue() + "\n";
                System.out.println(para);
                nowy.append(para);
                it.remove();
            }
            try {
                fw.write(nowy.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                fw.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void createFile(){
        file = new File(path);
        try{
            whatToDo = file.createNewFile();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}

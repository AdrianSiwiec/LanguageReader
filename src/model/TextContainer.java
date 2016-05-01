package model;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pierre on 19/04/16.
 */
public class TextContainer {
    private String text;
    private ArrayList<String> words;
    public TextContainer(File file) {
        String filename = file.getAbsolutePath();
        String extension = "";
        int i = filename.lastIndexOf('.');
        if(i>0) {
            extension = filename.substring(i+1);
        }
        if(!extension.equalsIgnoreCase("pdf")) {
            System.out.println("Text Container needs PDF");
            text="";
            return;
        }
        try {
            PdfReader reader = new PdfReader(filename);
            TextExtractionStrategy strategy= new SimpleTextExtractionStrategy();
            StringBuilder builder = new StringBuilder();
            System.out.println("Reading pdf: "+file.toString()+"\nIt has "+reader.getNumberOfPages()+" pages");
            String newPage;
            String pageNumber;
            for(int page = 1; page<=reader.getNumberOfPages(); page++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page, strategy));
                newPage=PdfTextExtractor.getTextFromPage(reader, page, strategy).toString();
                pageNumber=Integer.toString(page)+".txt";
                File out=new File(pageNumber);
                FileWriter fw = new FileWriter(out);
                fw.write(newPage);
                fw.close();
            //    System.out.println(":)"+newPage);

            }
            text = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        words = new ArrayList<>(Arrays.asList(text.split("\\s")));
    //S    System.out.println(text);
    }

    public String toString(){
        return text;
    }

    public ArrayList<String> getWords() {
        return words;
    }
}

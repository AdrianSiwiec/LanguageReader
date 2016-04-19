package model;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.File;
import java.io.IOException;

/**
 * Created by pierre on 19/04/16.
 */
public class TextContainer {
    private String text;
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
            for(int page = 1; page<=reader.getNumberOfPages(); page++) {
                builder.append(PdfTextExtractor.getTextFromPage(reader, page, strategy));
            }
            text = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(text);
    }

    public String getText(){
        return text;
    }
}

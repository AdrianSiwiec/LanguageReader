package model;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by pierre on 19/04/16.
 */
public class TextContainer {
    private String text;
    private ArrayList<String> words;
    private ArrayList<Page> pages;
    public String filname = null;
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
            filname = file.getName();
            PdfReader reader = new PdfReader(filename);
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);
            TextExtractionStrategy strategy;
            //Rectangle rect = new Rectangle(50, 50, 490, 490);
            //   RenderFilter filter = new RegionTextRenderFilter(rect);
            TextMarginFinder finder;
            StringBuilder builder = new StringBuilder();
            System.out.println("Reading pdf: "+file.toString()+"\nIt has "+reader.getNumberOfPages()+" pages");
           // String pageNumber;
            pages=new ArrayList<>();
            for(int page = 1; page<=reader.getNumberOfPages(); page++) {
                finder = parser.processContent(page, new TextMarginFinder());
                Rectangle rec = new Rectangle(finder.getLlx(), finder.getLly(),
                        finder.getWidth(), finder.getHeight());

                PdfReader reader1=new PdfReader(filename);
                reader1.selectPages(String.valueOf(page));
                Rectangle cropbox = reader.getCropBox(page);
                RenderFilter filter = new RegionTextRenderFilter(cropbox);
                strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
              //  strategy = parser.processContent(page, new LocationTextExtractionStrategy());

            //    builder.append(PdfTextExtractor.getTextFromPage(reader, page, strategy));
               // Page nextPage = new Page(strategy.getResultantText());
                Page nextPage=new Page(PdfTextExtractor.getTextFromPage(reader1, 1, strategy).toString());
               // PdfTextExtractor.getTextFromPage(reader, page, new SemText());
                pages.add(nextPage);
              /*  pageNumber=Integer.toString(page)+".txt";
                File out=new File(pageNumber);
                FileWriter fw = new FileWriter(out);
                fw.write(PdfTextExtractor.getTextFromPage(reader1, 1).toString());
                fw.close();*/
            }
            //PageFactory PF = new PageFactory(400, pages);
            //pages = PF.refactorCollection();
            text = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    //    words = new ArrayList<>(Arrays.asList(text.split("\\s")));
    }

    public String toString(){
        return text;
    }

    public int getNumberOfPages() { return pages.size();}

    public ArrayList<String> getWords(int pageNumber) {
        return pages.get(pageNumber).getWords();
    }
}

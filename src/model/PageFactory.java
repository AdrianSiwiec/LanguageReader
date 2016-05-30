package model;


import java.util.ArrayList;

/**
 * Created by pawel on 10.05.16.
 */
public class PageFactory {
    int linesPerPage;
    private ArrayList<Lines> linesList;
    private ArrayList<Page> pages;
    private ArrayList<Page> smallPages;

    PageFactory(int linesPerPage, ArrayList<Page> pages){
        this.linesPerPage = linesPerPage;
        this.pages=pages;
        smallPages=new ArrayList<>();
        linesList = new ArrayList<>();
    }

    private void concatLines(Page next){
        for(Lines l : next.getLines()){
            if(linesList.size() < linesPerPage){
                linesList.add(l);
            }
            else{
                smallPages.add(new Page(linesList));
                linesList = new ArrayList<>();
                linesList.add(l);
            }
        }
    }

    private void refactorList(){
        for(Page p : pages){
            concatLines(p);
        }
        if(linesList.size() < linesPerPage){
            smallPages.add(new Page(linesList));
        }
    }
    public ArrayList<Page> refactorCollection(){
        refactorList();
        return smallPages;
    }

}
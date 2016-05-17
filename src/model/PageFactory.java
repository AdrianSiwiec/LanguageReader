package model;


import java.util.ArrayList;

/**
 * Created by pawel on 10.05.16.
 */
public class PageFactory {
    int wordsPerPage;
    private ArrayList<Page> pages;
    private ArrayList<Page> smallPages;
    PageFactory(int wordsPerPage, ArrayList<Page> pages){
        this.wordsPerPage=wordsPerPage;
        this.pages=pages;
        smallPages=new ArrayList<>();
    }

    private Page refactorPage(Page next, int position){
        if(next.getNumberOfWords()<=wordsPerPage){
            return next;
        }
        else{
            ArrayList<Pair<String, Integer>> temp = next.getWords();
            ArrayList<Pair<String, Integer>> temp2 = new ArrayList<>();
            for(int i=position; i<position+wordsPerPage && i<temp.size(); i++){
                temp2.add(temp.get(i));
            }
            Page newOne=new Page(temp2);
            return newOne;
        }

    }
    private void refactorList(){
        for(Page p : pages){
            int number=0;
            Page temp = refactorPage(p, number);
            number = temp.getNumberOfWords();
            if(number == p.getNumberOfWords()){
                smallPages.add(p);
            }
            else {
                smallPages.add(temp);
                while (number < p.getNumberOfWords()){
                    temp = refactorPage(p, number);
                    number += temp.getNumberOfWords();
                    smallPages.add(temp);
                }
            }
        }
    }
    public ArrayList<Page> refactorCollection(){
        refactorList();
        return smallPages;
    }

}

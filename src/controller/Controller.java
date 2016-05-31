package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.*;
import view.LanguageMenuItem;
import view.OurButton;
import view.View;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pierre on 30/04/16.
 */
public class Controller {
    private View view;
    private Model model;
    private boolean paginationStatus = false;
    ExecutorService daemonExecutorService = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });
    ExecutorService executorService = Executors.newCachedThreadPool();

    public Controller(Model model) {
        this.model = model;
    }

    public void setTranslator(LanguageTranslator translator){
        model.setTranslator(translator);
    }

    public void addListeners() {
        view.addOpenFileListener(new OpenFileListener());
        for(MenuItem lang: view.getLanguagesFrom().getItems()) {
            view.addLanguageButtonListener((LanguageMenuItem) lang,
                    new LanguageButtonListener(((LanguageMenuItem) lang).getLanguagePair(), true));
        }
        for(MenuItem lang: view.getLanguagesTo().getItems()) {
            view.addLanguageButtonListener((LanguageMenuItem) lang,
                    new LanguageButtonListener(((LanguageMenuItem) lang).getLanguagePair(), false));
        }
    }

    public class OpenFileListener implements EventHandler<ActionEvent> {
        FileChooser fileChooser = new FileChooser();
        @Override
        public void handle(ActionEvent event) {
            File file = fileChooser.showOpenDialog(view.getPrimaryStage());
            model.openFile(file);
            TextContainer text = model.getText();
            view.name = text.filname;
            view.createPagination(text.getNumberOfPages());
          //  view.showText(text, 0);
        }
    }

    public class LanguageButtonListener implements EventHandler<ActionEvent> {
        LanguageClass language;
        boolean type;
        LanguageButtonListener(LanguageClass language, boolean type) {
            this.language = language;
            this.type = type;
        }
        @Override
        public void handle(ActionEvent event) {
            if(type) {
                model.setLanguageFrom(language);
            } else {
                model.setLanguageTo(language);
            }
            cacheCurrentPage();
        }
    }

    public void cacheCurrentPage() {
        if(model.getText()!=null)
            model.getText().cacheTranslation(view.getCurrentPageNumber());
    }

    public void paginationSetPageFactory() {
        view.changePageInPagination(new PaginationControl());
    }

    public void PaginationStatus(){
        if(paginationStatus == true)
            view.deletePagination();
        paginationStatus =  true;
    }

    public class PaginationControl implements Callback<Integer, Node> {
        final int numberOfPages=model.getText().getNumberOfPages();
        final TextContainer text = model.getText();
        @Override
        public Node call(Integer pageIndex) {
            if(pageIndex>=numberOfPages)
                return null;
            else{
                return view.showText(text, pageIndex);
            }
        }
    }

    public void showPopup(OurButton button) {
        view.showPopup(button.localToScene(0, 0).getX(), button.localToScene(0, 0).getY(),
                button.type == 0 ? model.getTranslation(button.getText()) : button.getText());
    }

    public void serializeDictionary() {
        model.serializeDictionary();
    }

    public void deletePopups() {
        view.deletePopups();
    }

    public java.lang.String getTranslation(java.lang.String word) {
        return model.getTranslation(word);
    }

    public void setView(View view) {
        this.view = view;
    }

    public ExecutorService getDaemonExecutorService() {
        return daemonExecutorService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public List<LanguageClass> getAvailableLanguages(){
        return model.getAvailableLanguages();
    }
}

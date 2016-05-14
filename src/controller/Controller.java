package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import model.Model;
import model.TextContainer;
import view.View;
import view.WordButton;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pierre on 30/04/16.
 */
public class Controller {
    private View view;
    private Model model;
    ExecutorService daemonExecutorService = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });
    ExecutorService executorService = Executors.newCachedThreadPool();

    public Controller(Model model) {
        this.model = model;
    }

    public void addListeners() {
        view.addOpenFileListener(new OpenFileListener());
    }

    public class OpenFileListener implements EventHandler<ActionEvent> {
        FileChooser fileChooser = new FileChooser();
        @Override
        public void handle(ActionEvent event) {
            File file = fileChooser.showOpenDialog(view.getPrimaryStage());
            model.openFile(file);
            TextContainer text = model.getText();
            view.createPagination(text.getNumberOfPages());
          //  view.showText(text, 0);
        }
    }

    public void paginationSetPageFactory() {
        view.changePageInPagination(new PaginationControl());
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


    public void showPopup(WordButton button) {
        view.showPopup(button.localToScene(0, 0).getX(), button.localToScene(0, 0).getY(),
                model.getTranslation(button.getText()));
    }

    public void deletePopups() {
        view.deletePopups();
    }

    public String getTranslation(String word) {
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
}

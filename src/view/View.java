package view;

import controller.App;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.TextContainer;


/**
 * Created by pierre on 30/04/16.
 */
public class View extends Application {
    public static Integer i =0;
    Scene scene;
    Button openButton;
    GridPane gridPane, popupPane;
    FlowPane wordsPane;
    FileChooser fileChooser;
    HBox mainHBox;
    Stage primaryStage;
    StackPane stackPane;
    Pagination pagination;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting View");

        App.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hello World!");

        openButton = new Button();
        openButton.setText("Open file");


        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.add(openButton, 0, 0);

        fileChooser = new FileChooser();

        mainHBox = new HBox();
        mainHBox.getChildren().add(gridPane);
        mainHBox.setId("mainHBox");

        popupPane = new GridPane();
        popupPane.setPickOnBounds(false);
        popupPane.setMinWidth(800);
        popupPane.setMinHeight(400);

        stackPane = new StackPane();
        stackPane.getChildren().add(mainHBox);
        stackPane.getChildren().add(popupPane);

        scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setWidth(650);
        primaryStage.show();

        scene.getStylesheets().add(View.class.getResource("ViewStylesheet.css").toExternalForm());

        App.getController().addListeners();

        System.out.println("View successfully started");
    }

    public void launchView(String args[]) {
        launch(args);
    }

    public void addOpenFileListener(EventHandler<ActionEvent> eventHandler) {
        openButton.setOnAction(eventHandler);
    }

    public void changePageInPagination(Callback<Integer, javafx.scene.Node> callback) { pagination.setPageFactory(callback);};

    public void createPagination(int numberOfPages){
        pagination = new Pagination(numberOfPages);
        pagination.setMaxPageIndicatorCount(5);
        App.getController().paginationSetPageFactory();
        mainHBox.getChildren().add(pagination);
    }

    public FlowPane showText(TextContainer text, int pageNumber) {
        wordsPane = new FlowPane();
        wordsPane.setId("wordsPane");
        for(int i=0; i<400 && i<text.getWords(pageNumber).size(); i++) {
            wordsPane.getChildren().add(new WordButton(text.getWords(pageNumber).get(i)));
        }
        return wordsPane;
    }

    public void showPopup(double x, double y, String message) {
        if(popupPane.getChildren().size()!=0)
            popupPane.getChildren().remove(0);
        popupPane.add(new Button(message), 0, 0);
        popupPane.getChildren().get(0).setTranslateX(x);
        popupPane.getChildren().get(0).setTranslateY(y);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

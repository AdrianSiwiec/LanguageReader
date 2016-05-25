package view;

import controller.App;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    VBox mainVBox;
    Stage primaryStage;
    StackPane stackPane;
    AnchorPane anchor;
    Pagination pagination;
    MenuBar menuBar;
    Menu menuFile, menuEdit, menuView;
    MenuItem open;
    Text text;
    public String name = "Language Reader";
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting View");

        App.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle(name);

      /*  openButton = new Button();
        openButton.setText("Open file");*/

        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuEdit = new Menu("Edit");
        menuView = new Menu("View");
        open = new MenuItem("Open");
        menuFile.getItems().addAll(open);

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 0));

       // gridPane.add(menuBar, 0, 0);
    //    gridPane.add(openButton, 2, 1);

        text = new Text();
        text.setText("So far our application supports only file with pdf extension. \n" +
                "To open pdf file, choose: File -> Open\n");

        text.setFill(Color.BLACK);
        text.setFont(Font.font("Book Antiqua", 14));

        fileChooser = new FileChooser();
        mainVBox = new VBox();
        mainVBox.setPadding(new Insets(0, 0, 0, 0));
     /*   mainVBox.getChildren().add(menuBar);
        mainVBox.getChildren().add(text);
        mainVBox.setId("mainVBox");*/
        menuBar.autosize();

        popupPane = new GridPane();
        popupPane.setMouseTransparent(true);
//        popupPane.setMinWidth(800);
//        popupPane.setMinHeight(400);

        stackPane = new StackPane();
        stackPane.getChildren().add(text);
        stackPane.getChildren().add(popupPane);


        mainVBox = new VBox();
        mainVBox.getChildren().addAll(menuBar, stackPane);

        scene = new Scene(mainVBox);

        primaryStage.setScene(scene);
        primaryStage.setWidth(450);
        stackPane.setMinSize(300, 300);
        stackPane.setAlignment(text, Pos.CENTER);
        primaryStage.show();
        scene.getStylesheets().add(View.class.getResource("ViewStylesheet.css").toExternalForm());

        App.getController().addListeners();

        System.out.println("View successfully started");
    }

    public void launchView(String args[]) {
        launch(args);
    }

    public void addOpenFileListener(EventHandler<ActionEvent> eventHandler) {
        open.setOnAction(eventHandler);
    }

    public void changePageInPagination(Callback<Integer, javafx.scene.Node> callback) { pagination.setPageFactory(callback);};

    public void createPagination(int numberOfPages){
        App.getController().PaginationStatus();
        pagination = new Pagination(numberOfPages);
        pagination.setMaxPageIndicatorCount(5);
        App.getController().paginationSetPageFactory();
        anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 20.0);
        AnchorPane.setRightAnchor(pagination, 20.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 40.0);
        anchor.getChildren().addAll(pagination);
        stackPane.getChildren().remove(text);
        stackPane.getChildren().remove(popupPane);
        stackPane.getChildren().add(anchor);
        stackPane.setAlignment(anchor, Pos.TOP_CENTER);
        stackPane.getChildren().add(popupPane);
    }


    public void deletePagination(){
        stackPane.getChildren().remove(anchor);
        stackPane.getChildren().add(text);
    }

    public VBox showText(TextContainer text, int pageNumber) {
        VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(0, 10, 5, 5));
        wordsPane = new FlowPane();
        wordsPane.setId("wordsPane");
        vBox.setId("linesVBox");
        for(int i=0; i<text.getWords(pageNumber).size(); i++) {
            HBox tempHBox = new HBox();
            while(i < text.getWords(pageNumber).size() && !text.getWords(pageNumber).get(i).equals("\n")){
                tempHBox.getChildren().add(new WordButton(text.getWords(pageNumber).get(i)));
                i++;
            }
            vBox.getChildren().add(tempHBox);
        }
        return vBox;
    }

    public void showPopup(double x, double y, String message) {
        if(popupPane.getChildren().size()!=0)
            popupPane.getChildren().remove(0);
        popupPane.add(new PopupButton(message), 0, 0);
        popupPane.getChildren().get(0).setTranslateX(x);
        popupPane.getChildren().get(0).setTranslateY(y-10);
    }

    public void deletePopups() {
        popupPane.getChildren().clear();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

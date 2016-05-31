package view;

import controller.App;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
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
import model.LanguageClass;
import model.TextContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by pierre on 30/04/16.
 */
public class View extends Application {
    public static Integer i =0;
    public String fontSiz = new String("12");
    public String fontStyl = new String("MetalMacabre");
    String size = "8,10,12,14,16,18,20";
    List<String> listaStyle = Font.getFamilies();

    ArrayList<String> listaSize = new ArrayList(Arrays.asList(size.split("[,]+")));
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
    ScrollPane sp;
    Menu menuFile, menuLanguage, fontStyle, fontSize, menuView, languagesTo, languagesFrom, addToFile;
    ContextMenu contextMenu;
    public ToggleGroup fontStyleToggleGroup, fontSizeToggleGroup, languagesToToggleGroup, languagesFromToggleGroup;
    MenuItem open;
    Text text;
    int currentPageNumber;
    public java.lang.String name = "Language Reader";
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting View");
       // Font.loadFont(View.class.getResource("LiberationSerif-Italic.ttf").toExternalForm(), 14);
        App.setView(this);

        this.primaryStage = primaryStage;
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setTitle(name);

      /*  openButton = new Button();
        openButton.setText("Open file");*/

        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuLanguage = new Menu("Language");
        menuView = new Menu("View");
        open = new MenuItem("Open");
        fontSize = new Menu("Font Size");
        fontStyle = new Menu("Font Style");
        menuView.getItems().addAll(fontSize, fontStyle);
        menuFile.getItems().addAll(open);

        languagesFrom = new Menu("Source Language");
        languagesTo = new Menu("Translation Language");
        languagesFromToggleGroup = new ToggleGroup();
        languagesToToggleGroup = new ToggleGroup();

        menuLanguage.getItems().addAll(languagesFrom, languagesTo);

        for(LanguageClass lang: App.getController().getAvailableLanguages()) {
            languagesFrom.getItems().add(new LanguageMenuItem(lang));
            languagesTo.getItems().add(new LanguageMenuItem(lang));
        }
        languagesFrom.getItems().forEach(menuItem -> ((LanguageMenuItem)menuItem).setToggleGroup(languagesFromToggleGroup));
        languagesTo.getItems().forEach(menuItem -> ((LanguageMenuItem)menuItem).setToggleGroup(languagesToToggleGroup));
        ((LanguageMenuItem)languagesFrom.getItems().get(0)).setSelected(true);
        ((LanguageMenuItem)languagesTo.getItems().get(1)).setSelected(true);

        fontStyleToggleGroup = new ToggleGroup();
        for(String s: listaStyle){
            RadioMenuItem item = new RadioMenuItem(s);
            item.setUserData(s);
            item.setToggleGroup(fontStyleToggleGroup);
            item.setStyle("-fx-font-family: "+ "\""+s+"\""+";");
            fontStyle.getItems().add(item);
        }

        fontSizeToggleGroup = new ToggleGroup();
        for(String s: listaSize){
            RadioMenuItem item = new RadioMenuItem(s);
            item.setUserData(s);
            item.setToggleGroup(fontSizeToggleGroup);
            fontSize.getItems().add(item);
            if(s.equals("12"))
                item.setSelected(true);
        }

        menuBar.getMenus().addAll(menuFile, menuLanguage, menuView);

        contextMenu = new ContextMenu();
        addToFile = new Menu("Add word to file");
        contextMenu.getItems().add(addToFile);

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 0));

       // gridPane.add(menuBar, 0, 0);
    //    gridPane.add(openButton, 2, 1);

        text = new Text();
        text.setText("So far our application supports only file with pdf extension. \n" +
                "To open pdf file, choose: File -> Open\n");

        text.setFill(Color.BLACK);
        text.setFont(Font.font(14));

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
        App.getController().addFontStyle();
        App.getController().addFontSize();

        System.out.println("View successfully started");
    }

    public void launchView(java.lang.String args[]) {
        launch(args);
    }

    public void addOpenFileListener(EventHandler<ActionEvent> eventHandler) {
        open.setOnAction(eventHandler);
    }

    public void addLanguageButtonListener(LanguageMenuItem menuItem, EventHandler<ActionEvent> eventHandler) {
        menuItem.setOnAction(eventHandler);
    }

    public void changePageInPagination(Callback<Integer, javafx.scene.Node> callback) {
        pagination.setPageFactory(callback);
    }



    public void createPagination(int numberOfPages){
        App.getController().PaginationStatus();
        pagination = new Pagination(numberOfPages);
        pagination.setMaxPageIndicatorCount(5);
        App.getController().paginationSetPageFactory();
        anchor = new AnchorPane();
        sp = new ScrollPane();
        sp.setContent(pagination);
        sp.fitToHeightProperty();
        sp.fitToWidthProperty();
        sp.setStyle("-fx-background:white;");
        AnchorPane.setTopAnchor(pagination, 20.0);
        AnchorPane.setRightAnchor(pagination, 20.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 40.0);
        anchor.getChildren().addAll(pagination);
        stackPane.getChildren().remove(text);
        stackPane.getChildren().remove(popupPane);
        stackPane.getChildren().add(anchor);
        stackPane.getChildren().add(sp);
        stackPane.setAlignment(anchor, Pos.TOP_CENTER);
        stackPane.getChildren().add(popupPane);
    }


    public void deletePagination(){
        stackPane.getChildren().remove(anchor);
        stackPane.getChildren().add(text);
    }

    public VBox showText(TextContainer text, int pageNumber) {
        currentPageNumber = pageNumber;
        VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(0, 10, 5, 5));
        wordsPane = new FlowPane();
        wordsPane.setId("wordsPane");
        vBox.setId("linesVBox");
        for(int i=0; i<text.getLines(pageNumber).size(); i++) {
            HBox tempHBox = new HBox();
            for (int j = 0; j < text.getLines(pageNumber).get(i).size(); j++) {
                WordButton word = new WordButton(text.getLines(pageNumber).get(i).get(j));
                word.setStyle("-fx-font-size: "+ fontSiz+"pt; "+
                        "-fx-font-family: "+ "\""+fontStyl+"\""+";"+
                        "-fx-background-color: white; -fx-padding: 1 2;"
                );
                tempHBox.getChildren().add(word);
            }
            vBox.getChildren().add(tempHBox);
        }
        text.cacheTranslation(pageNumber);
        return vBox;
    }

    public void selectTogglePropertyStyleView(ChangeListener<Toggle> change) { fontStyleToggleGroup.selectedToggleProperty().addListener(change);  }

    public void selectTogglePropertySizeView(ChangeListener<Toggle> change) { fontSizeToggleGroup.selectedToggleProperty().addListener(change);  }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void showPopup(double x, double y, java.lang.String message) {
        if(popupPane.getChildren().size()!=0)
            popupPane.getChildren().remove(0);
        popupPane.add(new PopupButton(message), 0, 0);
        popupPane.getChildren().get(0).setTranslateX(x);
        popupPane.getChildren().get(0).setTranslateY(y-10);
    }

    public void addContextMenu(double x, double y, OurButton button){
        contextMenu.show(button, x+10, y+10);
    }

    public void deletePopups() {
        popupPane.getChildren().clear();
    }

    public Menu getLanguagesFrom() {
        return languagesFrom;
    }

    public Text getText() {
        return text;
    }

    public Menu getLanguagesTo() {
        return languagesTo;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

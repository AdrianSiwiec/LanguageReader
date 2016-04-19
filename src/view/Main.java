package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TextContainer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by pierre on 19/04/16.
 */
public class Main extends Application {
    private String path=new String();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Choose *.pdf file");
        GridPane grid=new GridPane();
        grid.setPadding(new Insets(25, 25, 25, 25));
        FileChooser fileChooser = new FileChooser();

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("A");
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    new TextContainer(file);
                }
            }
        });
        grid.add(btn, 0, 0);
        path = new File(".").getAbsolutePath();
        String textFile=prepareFile(1);
        Text text=new Text();
        text.setFont(new Font(12));
        grid.add(text, 1, 1, 10, 10);
        if(!textFile.equals("blad")){
            text.setText(textFile);
            text.autosize();
        }
        primaryStage.setScene(new Scene(grid, 300, 250));
        primaryStage.show();
    }

    private String readFile(String pathname) throws IOException {

        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }
    private String prepareFile(int pageNumber){
        String A, B, C=new String();
        B=path.substring(0, path.length()-1);
        B=B+Integer.toString(pageNumber)+".txt";
        try{
            C=readFile(B);
        }
        catch(IOException e){
            C="blad";
        }
        return C;
    }
}
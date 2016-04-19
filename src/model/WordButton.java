package model;

import javafx.scene.control.Button;

/**
 * Created by pierre on 19/04/16.
 */
public class WordButton extends Button{
    public WordButton(String text) {
        super(text);
        setId("wordButton");
        setOnMouseEntered(event -> {
           System.out.println(getLayoutX()+", "+ getLayoutY());
        });
    }
}

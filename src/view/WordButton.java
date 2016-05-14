package view;

import controller.App;
import javafx.scene.control.Button;

/**
 * Created by pierre on 19/04/16.
 */
public class WordButton extends Button{
    public WordButton(String text) {
        super(text);
        setId("wordButton");
        setOnMouseClicked(event ->
            App.getController().showPopup(this)
        );
        setOnMouseExited(event ->
            App.getController().deletePopups()
        );
    }
}

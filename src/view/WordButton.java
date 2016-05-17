package view;

import controller.App;
import javafx.geometry.Insets;

/**
 * Created by pierre on 19/04/16.
 */
public class WordButton extends OurButton{
    public WordButton(String text) {
        super(text, 0);
        setId("wordButton");
        setOnMouseClicked(event ->
            App.getController().showPopup(this)
        );
        setOnMouseExited(event ->
            App.getController().deletePopups()
        );
        this.setPadding(Insets.EMPTY);
    }
}

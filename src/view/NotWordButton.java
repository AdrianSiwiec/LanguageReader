package view;

import controller.App;
import javafx.geometry.Insets;

/**
 * Created by pawel on 17.05.16.
 */
public class NotWordButton extends OurButton {
    public NotWordButton(String text){
        super(text, 1);
        setId("NotWordButton");
        setOnMouseClicked(event ->
                App.getController().showPopup(this)
        );
        setOnMouseExited(event ->
                App.getController().deletePopups()
        );
        this.setPadding(Insets.EMPTY);
    }
}

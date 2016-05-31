package view;

import controller.App;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;


/**
 * Created by pierre on 19/04/16.
 */
public class WordButton extends OurButton{
    public WordButton(String text) {
        super(text, 0);
        setId("wordButton");
        addEventHandler(MouseEvent.MOUSE_CLICKED, App.getController().buttonClick(this));
        setOnMouseExited(event ->
            App.getController().deletePopups()
        );
        addEventHandler(MouseEvent.MOUSE_CLICKED, App.getController().buttonClick(this));

        this.setPadding(Insets.EMPTY);
    }
}

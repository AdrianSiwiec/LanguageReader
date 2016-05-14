package view;

import controller.App;
import javafx.scene.control.Button;

/**
 * Created by pierre on 14/05/16.
 */
public class PopupButton extends Button {

    public PopupButton(String text) {
        super(text);
        setId("popupButton");
    }
}

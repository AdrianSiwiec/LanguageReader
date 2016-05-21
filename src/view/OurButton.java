package view;

import javafx.scene.control.Button;

/**
 * Created by pawel on 17.05.16.
 */
public class OurButton extends Button {
    public int type;
    public OurButton(String text, int type){
        super(text);
        this.type = type;
    }
}

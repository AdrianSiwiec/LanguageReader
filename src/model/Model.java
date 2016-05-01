package model;

import java.io.File;

/**
 * Created by pierre on 30/04/16.
 */
public class Model {
    TextContainer text;

    public void openFile(File file) {
        text = new TextContainer(file);
    }

    public TextContainer getText() {
        return text;
    }
}


package model;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pierre on 30/04/16.
 */
public class Model {
    TextContainer text;
    LanguageTranslator translator = new MicrosoftTranslator();

    public void openFile(File file) {
        text = new TextContainer(file);
    }

    public TextContainer getText() {
        return text;
    }

    public String getTranslation(String word) {
        return translator.translate(word);
    }
}


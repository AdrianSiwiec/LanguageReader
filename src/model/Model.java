package model;

import java.io.File;
import java.util.List;

/**
 * Created by pierre on 30/04/16.
 */
public class Model {
    TextContainer text;

    LanguageTranslator translator;

    public void openFile(File file) {
        text = new TextContainer(file);
    }

    public TextContainer getText() {
        return text;
    }

    public String getTranslation(String word) {
        return translator.translate(word.replaceAll("[^A-Za-z']+", ""));
    }

    public void serializeDictionary() {
        translator.serialize();
    }

    public void setLanguageFrom(LanguageClass lang) {
        translator.setLanguageFrom(lang);
    }

    public void setLanguageTo(LanguageClass lang) {
        translator.setLanguageTo(lang);
    }

    public void setTranslator(LanguageTranslator translator) {
        this.translator = translator;
    }

    public List<LanguageClass> getAvailableLanguages() {
        return translator.getAvailableLanguages();
    }
}


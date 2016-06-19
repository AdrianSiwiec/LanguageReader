package model;

import controller.App;

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

    public String getProperWord(String word) { return word.replaceAll("[^A-Za-z']+", ""); }

    public void serializeDictionary() {
        translator.serialize();
    }

    public void setLanguageFrom(LanguageClass lang) {
        translator.setLanguageFrom(lang);
        App.settings.setLanguageFrom(lang);
    }

    public void setLanguageTo(LanguageClass lang) {
        translator.setLanguageTo(lang);
        App.settings.setLanguageTo(lang);
    }

    public String getLanguageFrom(){ return translator.getLanguageFrom(); }

    public String getLanguageTo() {return translator.getLanguageTo(); }

    public void setTranslator(LanguageTranslator translator) {
        this.translator = translator;
    }

    public List<LanguageClass> getAvailableLanguages() {
        return translator.getAvailableLanguages();
    }
}


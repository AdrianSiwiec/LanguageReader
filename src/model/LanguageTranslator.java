package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pierre on 14/05/16.
 */
public interface LanguageTranslator {
    String translate(String word);
    List<LanguageClass> getAvailableLanguages();
    void setLanguageTo(LanguageClass lang);
    void setLanguageFrom(LanguageClass lang);
    void serialize();
    void deserialize();
    void correctWord(String word, String translation);
    ArrayList<Serializer> serializers = new ArrayList<>();
}

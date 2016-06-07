package model;

/**
 * Created by pierre on 07/06/16.
 */
public interface Serializer {
    void serializeDictionary(OnlineTranslator.LanguagePair language);
    void deserializeDictionary(OnlineTranslator.LanguagePair language);
}

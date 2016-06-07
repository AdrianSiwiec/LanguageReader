package model;

import com.memetix.mst.translate.Translate;
import controller.App;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pierre on 14/05/16.
 */
public class MicrosoftTranslator extends OnlineTranslator{

    public MicrosoftTranslator() {
        if(alreadyTranslated.get(currentLanguages)==null) {
            alreadyTranslated.put(currentLanguages, new ConcurrentHashMap<>());
        }
        serializers.add(new PlainSerializer());
        serializers.add(new TextFileSerializer());
        App.getController().getDaemonExecutorService().execute(this::deserialize);
        Translate.setClientId("languagereader"); //zarejestrowalem sie na swoim koncie w microsoft azure
        Translate.setClientSecret("Xq5cFU9uWVUPXJoiyu3UEoOO7AtC8l1j+Uichl8DooU="); //2M chars/month za darmo
    }

    @Override
    protected String getOnlineTranslation(String word) throws Exception{
        return Translate.execute(word, currentLanguages.from, currentLanguages.to);
    }

    @Override
    protected String getTranslatorName(LanguagePair language) {
        return "MicrosoftTranslatorCache_"+language.from.toString()+"_"+language.to.toString();
    }

    static {
        availableLanguages.add(new LanguageClass("English","en"));
        availableLanguages.add(new LanguageClass("Polish","pl"));
        availableLanguages.add(new LanguageClass("French","fr"));
        availableLanguages.add(new LanguageClass("Spanish","es"));
        availableLanguages.add(new LanguageClass("Arabic","ar"));
        availableLanguages.add(new LanguageClass("Bulgarian","bg"));
        availableLanguages.add(new LanguageClass("Catalan","ca"));
        availableLanguages.add(new LanguageClass("Czech","cs"));
        availableLanguages.add(new LanguageClass("Danish","da"));
        availableLanguages.add(new LanguageClass("Dutch","nl"));
        availableLanguages.add(new LanguageClass("Estonian","et"));
        availableLanguages.add(new LanguageClass("Finnish","fi"));
        availableLanguages.add(new LanguageClass("German","de"));
        availableLanguages.add(new LanguageClass("Greek","el"));
        availableLanguages.add(new LanguageClass("Haitian Creole","ht"));
        availableLanguages.add(new LanguageClass("Hebrew","he"));
        availableLanguages.add(new LanguageClass("Hindi","hi"));
        availableLanguages.add(new LanguageClass("Hmong_daw","mww"));
        availableLanguages.add(new LanguageClass("Hungarian","hu"));
        availableLanguages.add(new LanguageClass("Indonesian","id"));
        availableLanguages.add(new LanguageClass("Italian","it"));
        availableLanguages.add(new LanguageClass("Japanese","ja"));
        availableLanguages.add(new LanguageClass("Korean","ko"));
        availableLanguages.add(new LanguageClass("Latvian","lv"));
        availableLanguages.add(new LanguageClass("Lithuanian","lt"));
        availableLanguages.add(new LanguageClass("Malay","ms"));
        availableLanguages.add(new LanguageClass("Norwegian","no"));
        availableLanguages.add(new LanguageClass("Persian","fa"));
        availableLanguages.add(new LanguageClass("Portuguese","pt"));
        availableLanguages.add(new LanguageClass("Romanian","ro"));
        availableLanguages.add(new LanguageClass("Russian","ru"));
        availableLanguages.add(new LanguageClass("Slovak","sk"));
        availableLanguages.add(new LanguageClass("Slovenian","sl"));
        availableLanguages.add(new LanguageClass("Swedish","sv"));
        availableLanguages.add(new LanguageClass("Thai","th"));
        availableLanguages.add(new LanguageClass("Turkish","tr"));
        availableLanguages.add(new LanguageClass("Ukrainian","uk"));
        availableLanguages.add(new LanguageClass("Urdu","ur"));
        availableLanguages.add(new LanguageClass("Vietnamese","vi"));
    }
}
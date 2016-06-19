package model;

import controller.App;

import java.io.*;
import java.util.AbstractCollection;

/**
 * Created by pierre on 19/06/16.
 */
public class Settings implements Serializable {
    File book;
    int page;
    LanguageClass languageTo, languageFrom;
    String serializationName= "cache/settings.sr";


    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream(serializationName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
            System.out.println("Settings serialization Succesful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Settings serialization Unsuccesful");
        }
    }

    public void restore() {
        try {
            FileInputStream fileIn = new FileInputStream(serializationName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Settings tmp;
            tmp=(Settings) in.readObject();
            in.close();
            fileIn.close();

            restoreLanguages(tmp);
            restoreBook(tmp);
            restorePage(tmp);
            System.out.println("Settings restoration Successful");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Settings restoration Unsuccessful");
        }
    }

    public void restoreLanguages(Settings tmp) {
        languageFrom = tmp.languageFrom;
        languageTo = tmp.languageTo; //TODO: change radio buttons to show proper language
        App.getModel().setLanguageTo(languageTo);
        App.getModel().setLanguageFrom(languageFrom);
    }

    public void restoreBook(Settings tmp) {
        book = tmp.book;
        App.getController().openBook(book);
    }

    public void restorePage(Settings tmp) {
        page = tmp.page;
        App.getController().setPage(page);
    }

    public LanguageClass getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(LanguageClass languageTo) {
        this.languageTo = languageTo;
    }

    public LanguageClass getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(LanguageClass languageFrom) {
        this.languageFrom = languageFrom;
    }

    public File getBook() {
        return book;
    }

    public void setBook(File book) {
        this.book = book;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

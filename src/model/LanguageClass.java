package model;

import java.io.Serializable;

/**
 * Created by pierre on 30/05/16.
 */
public class LanguageClass implements Serializable {
    java.lang.String name, code;
    public LanguageClass(){}
    public LanguageClass(java.lang.String name, java.lang.String code) {
        this.name = name;
        this.code = code;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getCode() {
        return code;
    }
}

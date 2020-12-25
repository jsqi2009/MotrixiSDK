package com.motrixi.datacollection.network.models;

import java.io.Serializable;

/**
 * author : Jason
 * date   : 2020/12/8 10:50 AM
 * desc   :
 */
 public class LanguageInfo implements Serializable {

     public String code;
     public String language;


    @Override
    public String toString() {
        return "LanguageInfo{" +
                "code='" + code + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}

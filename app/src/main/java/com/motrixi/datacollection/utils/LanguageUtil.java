package com.motrixi.datacollection.utils;

import android.content.Context;

import java.util.Locale;

/**
 * author : Jason
 * date   : 2020/12/8 2:43 PM
 * desc   :
 */
public class LanguageUtil {

    public static String getLanguageInfo() {

        Locale locale = Locale.getDefault();
        String lan = locale.getLanguage();
        String country = locale.getCountry();
        return lan + "/" + country;

    }

    public static String getCountryCode(Context context){
        String countryCode = "";
        Locale locale = context.getResources().getConfiguration().locale;
        countryCode = locale.getCountry();
        return  countryCode;
    }
}

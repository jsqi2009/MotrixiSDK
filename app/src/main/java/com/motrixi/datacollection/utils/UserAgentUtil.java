package com.motrixi.datacollection.utils;

import android.content.Context;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 3:00 PM
 * desc   :
 */
public class UserAgentUtil {

    public static String getUserAgent( FREContext context) {

        try {
            //        val userAgent = WebView(context).settings.userAgentString
            String userAgent = System.getProperty("http.agent");
            return userAgent;
        } catch ( Exception e) {
            return "";
        }
    }

    public static String getUserAgent( Context context) {

        try {
            //        val userAgent = WebView(context).settings.userAgentString
            String userAgent = System.getProperty("http.agent");
            return userAgent;
        } catch ( Exception e) {
            return "";
        }
    }
}

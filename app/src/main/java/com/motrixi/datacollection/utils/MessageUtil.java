package com.motrixi.datacollection.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : Jason
 * date   : 2020/12/8 11:06 AM
 * desc   :
 */
public class MessageUtil {

    public static String logMessage(int type, boolean success, String msg) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            jsonObject.put("success", success);
            jsonObject.put("info", msg);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String uploadLog(String msg) {

        try {
            JSONObject jsonObject =  new JSONObject();
            jsonObject.put("info", msg);

//            return jsonObject.toString()
            return msg;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}

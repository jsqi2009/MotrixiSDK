package com.motrixi.datacollection.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 3:23 PM
 * desc   :
 */
public class SimInfoUtil {

    private static String getSimInfo(FREContext context){
        TelephonyManager iPhoneManager = (TelephonyManager) context.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return iPhoneManager.getSimOperator();
    }

    public static String getMCC(FREContext context){

        String simInfo = getSimInfo(context);
        if (!TextUtils.isEmpty(simInfo)) {
            return simInfo.substring(0, 3);
        } else {
            return "";
        }

    }

    public static String getMNC(FREContext context) {

        String simInfo = getSimInfo(context);
        if (!TextUtils.isEmpty(simInfo)) {
            return simInfo.substring(3 ,simInfo.length());
        } else {
            return "";
        }


    }
}

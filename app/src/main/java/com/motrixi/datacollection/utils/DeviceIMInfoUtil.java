package com.motrixi.datacollection.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.adobe.fre.FREContext;

/**
 * author : Jason
 * date   : 2020/12/8 1:23 PM
 * desc   :
 */
 public class DeviceIMInfoUtil {

    public static String getDeviceIMEI(FREContext context) {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getDeviceIMEI(Context context) {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * fetch device IMSI
     */
    public static String getDeviceIMSI(FREContext context) {

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

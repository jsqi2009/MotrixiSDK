package com.motrixi.datacollection.utils;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.extensions.MotrixiSDKInit;
import com.motrixi.datacollection.network.PostMethodUtils;
import com.motrixi.datacollection.network.models.DataInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * author : Jason
 * date   : 2020/12/8 12:47 PM
 * desc   :
 */
public class UploadCollectedData {

    private static String advertisingId = "";
    private static Session mSession;

    public static void formatData(FREContext context) {
        mSession = new Session(context);
        DataInfo info = new DataInfo();

        info.appKey = getAppkey(context);


        //getAdvertisingId(context);
        Log.e("ad id", Contants.advertisingID);
        info.advertisingId = Contants.advertisingID;

        info.email = getEmailAccount(context).toString();
        info.imei = getIMEI(context);
        info.operationSystem = getSystemInfo(context);
        info.language = getLanguage(context);
        info.installedApplication = getInstalledApp(context);
//        info.installedApplication = ""
//        info.location = ""
        info.location = getLocationInfo(context);
        info.userAgent = getUserAgent(context);
        info.deviceMake = getDeviceMake(context);
        info.deviceModel = getDeviceModel(context);
        info.ipAddress = getCurrentIP(context);
        info.mcc  = getMCCAndMNC(context);
        info.deviceID  = getDeviceId(context);
        info.serial  = getSerial(context);
        info.androidID  = getAndroidId(context);
        info.consentFormID = mSession.getConsentFormID();

        Log.d("info", info.toString());

        if (Contants.onLogListener != null) {
            Contants.onLogListener.onLogListener(info.toString());
        }

        //Toast.makeText(context, "Uploading..", Toast.LENGTH_LONG).show()
        //uploadInfo(context, info);
    }

    public static void formatData(Context context) {

        mSession = new Session(context);
        DataInfo info = new DataInfo();
        info.appKey = getAppkey(context);
        getAdvertisingId(context);
        //Log.e("ad id", Contants.advertisingID);
        info.advertisingId = mSession.getAdvertisingID();

        info.email = getEmailAccount(context).toString();
        info.imei = getIMEI(context);
        info.operationSystem = getSystemInfo(context);
        info.language = getLanguage(context);
        info.installedApplication = getInstalledApp(context);
//        info.installedApplication = ""
//        info.location = ""
        info.location = getLocationInfo(context);
        info.userAgent = getUserAgent(context);
        info.deviceMake = getDeviceMake(context);
        info.deviceModel = getDeviceModel(context);
        info.ipAddress = getCurrentIP(context);
        info.mcc  = getMCCAndMNC(context);
        info.deviceID  = getDeviceId(context);
        info.serial  = getSerial(context);
        info.androidID  = getAndroidId(context);
        info.consentFormID = mSession.getConsentFormID();

        Log.d("info", info.toString());

        if (Contants.onLogListener != null) {
            Contants.onLogListener.onLogListener(info.toString());
        }

        //Toast.makeText(context, "Uploading..", Toast.LENGTH_LONG).show()
        //uploadData(context, info);

        uploadInfo(context, info);
    }

    private static void uploadInfo(final Context context, final DataInfo info) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("app_key", info.appKey);
                map.put("advertising_id", info.advertisingId);
                map.put("email", info.email);
                map.put("i_m_e_i", info.imei);
                map.put("operating_system", info.operationSystem);
                map.put("language_setting", info.language);
                map.put("applications_installed", info.installedApplication);
                map.put("location", info.location);
                map.put("user_agent", info.userAgent);
                map.put("device_make", info.deviceMake);
                map.put("device_model", info.deviceModel);
                map.put("ip_address", info.ipAddress);
                map.put("m_c_c/_m_n_c", info.mcc);
                map.put("device_id", info.deviceID);
                map.put("serial", info.serial);
                map.put("android_id", info.androidID);
                if (!TextUtils.isEmpty(mSession.getConsentFormID())) {
                    map.put("consent_form_id", mSession.getConsentFormID());
                }

                String msg = PostMethodUtils.httpPost(Contants.SUBMIT_INFO_API, map);
                Log.e("info response", msg);

                if (msg != Contants.RESPONSE_ERROR) {
                    if (Contants.mFREContext != null) {
                        String result = MessageUtil.logMessage(Contants.UPLOAD_DATA_CODE, true, msg);
                        MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("upload info", result);
                    }
                } else {
                    if (Contants.mFREContext != null) {
                        String result = MessageUtil.logMessage(Contants.UPLOAD_DATA_CODE, false, "cancel failure");
                        MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("upload info", result);
                    }
                }


            }
        }).start();
    }

    private static String getAppkey(FREContext context){
        if (!TextUtils.isEmpty(Contants.APP_KEY)) {
            return Contants.APP_KEY;
        }

        return "";
    }

    private static String getAppkey(Context context){
        if (!TextUtils.isEmpty(mSession.getAppKey())) {
            return mSession.getAppKey();
        }

        return "";
    }

    /*private static void getAdvertisingId(final FREContext context) {
        Executors.newSingleThreadExecutor().execute(new Runnable(){
            @Override
            public void run() {
                try {
                    String googleId = AdvertisingIdUtil.getGoogleAdId(context);
                    Log.d("google Id:", googleId);
                    advertisingId = googleId;

                    UploadLogUtil.uploadLogData(context, googleId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private static void getAdvertisingId(final Context context) {
        Executors.newSingleThreadExecutor().execute(new Runnable(){
            @Override
            public void run() {
                try {
                    String googleId = AdvertisingIdUtil.getGoogleAdId(context);
                    Log.d("google Id:", googleId);
                    advertisingId = googleId;

                    //UploadLogUtil.uploadLogData(context, googleId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static ArrayList<String> getEmailAccount(FREContext context) {
        try {
            Account[] accounts = EmailAccountUtil.getEmailAccount(context);
            if (accounts == null || accounts.length == 0) {

                return new ArrayList();
            }
            ArrayList<String> emailAccount = new ArrayList();
            //Log.e("email list", emailAccount.toString())
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            if (accounts.length > 0) {
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        Log.e("email account", account.name);
                        emailAccount.add(account.name);
                    }
                }
            }

            UploadLogUtil.uploadLogData(context, emailAccount.toString());

            return emailAccount;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    private static ArrayList<String> getEmailAccount(Context context) {
        try {
            Account[] accounts = EmailAccountUtil.getEmailAccount(context);
            if (accounts == null || accounts.length == 0) {

                return new ArrayList();
            }
            ArrayList<String> emailAccount = new ArrayList();
            //Log.e("email list", emailAccount.toString())
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            if (accounts.length > 0) {
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        Log.e("email account", account.name);
                        emailAccount.add(account.name);
                    }
                }
            }

            //UploadLogUtil.uploadLogData(context, emailAccount.toString());

            return emailAccount;
        } catch (Exception e) {
            return new ArrayList();
        }
    }


    private static String getIMEI(FREContext context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "";
        }
        String imeiInfo = DeviceIMInfoUtil.getDeviceIMEI(context);

        UploadLogUtil.uploadLogData(context, imeiInfo);
        return imeiInfo;
    }

    private static String getIMEI(Context context){

        String imeiInfo = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return "";
            }
            imeiInfo = DeviceIMInfoUtil.getDeviceIMEI(context);
            if (imeiInfo == null) {
                imeiInfo = "";
            }

            //UploadLogUtil.uploadLogData(context, imeiInfo);
            return imeiInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getSystemInfo(FREContext context){

        String systemVersion = SystemInfoUtils.getDeviceAndroidVersion();
        return systemVersion;
    }

    private static String getSystemInfo(Context context){

        String systemVersion = SystemInfoUtils.getDeviceAndroidVersion();
        return systemVersion;
    }

    private static String getLanguage( FREContext context){

        String languageInfo = LanguageUtil.getLanguageInfo();
        UploadLogUtil.uploadLogData(context, languageInfo);
        return languageInfo;
    }

    private static String getLanguage( Context context){

        String languageInfo = LanguageUtil.getLanguageInfo();
        //UploadLogUtil.uploadLogData(context, languageInfo);
        return languageInfo;
    }

    private static String getInstalledApp(FREContext context){

        JSONObject jsonObject = new JSONObject();
        ArrayList<String> applicationList = InstalledPackagesUtil.getInstalledPackageList(context);
        for (int i = 0; i < applicationList.size(); i++) {
            try {
                jsonObject.put(i + "", applicationList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    private static String getInstalledApp(Context context){

        JSONObject jsonObject = new JSONObject();
        ArrayList<String> applicationList = InstalledPackagesUtil.getInstalledPackageList(context);
        for (int i = 0; i < applicationList.size(); i++) {
            try {
                jsonObject.put(i + "", applicationList.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject.toString();
    }

    private static String getLocationInfo( FREContext context){
        try {
            JSONObject jsonObject = new JSONObject();
            Location gpsLocation = GPSLocationUtil.getLocation(context);
            if (gpsLocation != null) {

                jsonObject.put("longitude",gpsLocation.getLongitude());
                jsonObject.put("latitude",gpsLocation.getLatitude());

                List<Address> result = GPSLocationUtil.getAddress(gpsLocation, context);
                if (result != null && result.size() >0) {

                    jsonObject.put("countryName", result.get(0).getCountryName());
                    jsonObject.put("locality", result.get(0).getLocality());
                    jsonObject.put("countryCode", result.get(0).getCountryCode());
                }

            }
            Log.d("location info", jsonObject.toString());
            //UploadLogUtil.uploadLogData(context, jsonObject.toString());
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getLocationInfo( Context context){
        try {
            JSONObject jsonObject = new JSONObject();
            Location gpsLocation = GPSLocationUtil.getLocation(context);
            if (gpsLocation != null) {

                jsonObject.put("longitude",gpsLocation.getLongitude());
                jsonObject.put("latitude",gpsLocation.getLatitude());

                List<Address> result = GPSLocationUtil.getAddress(gpsLocation, context);
                if (result != null && result.size() >0) {

                    jsonObject.put("countryName", result.get(0).getCountryName());
                    jsonObject.put("locality", result.get(0).getLocality());
                    jsonObject.put("countryCode", result.get(0).getCountryCode());
                }

            }
            Log.d("location info", jsonObject.toString());
            //UploadLogUtil.uploadLogData(context, jsonObject.toString());
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getUserAgent(FREContext context){

        String userAgent = UserAgentUtil.getUserAgent(context);

        return userAgent;
    }

    private static String getUserAgent(Context context){

        String userAgent = UserAgentUtil.getUserAgent(context);

        return userAgent;
    }

    private static String getDeviceMake(FREContext context) {

        String deviceMake = SystemInfoUtils.getDeviceManufacturer();
        return deviceMake;
    }

    private static String getDeviceMake(Context context) {

        String deviceMake = SystemInfoUtils.getDeviceManufacturer();
        return deviceMake;
    }

    private static String getDeviceModel(FREContext context) {

        String deviceModel = SystemInfoUtils.getDeviceModel();
        return deviceModel;
    }

    private static String getDeviceModel(Context context) {

        String deviceModel = SystemInfoUtils.getDeviceModel();
        return deviceModel;
    }

    private static String getCurrentIP(FREContext context){

        String ipAddress = IPAddressUtils.getCurrentIPAddress(context);
        return ipAddress;
    }

    private static String getCurrentIP(Context context){

        String ipAddress = IPAddressUtils.getCurrentIPAddress(context);
        return ipAddress;
    }

    private static String getMCCAndMNC(FREContext context){

        try {
            JSONObject jsonObject = new JSONObject();
            String mcc = SimInfoUtil.getMCC(context);
            String mnc = SimInfoUtil.getMNC(context);
            jsonObject.put("MCC", mcc);
            jsonObject.put("MNC", mnc);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getMCCAndMNC(Context context){

        try {
            JSONObject jsonObject = new JSONObject();
            String mcc = SimInfoUtil.getMCC(context);
            String mnc = SimInfoUtil.getMNC(context);
            jsonObject.put("MCC", mcc);
            jsonObject.put("MNC", mnc);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("MissingPermission")
    private static String getDeviceId(FREContext context) {

        try {
            String deviceId = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return deviceId;
            }
            TelephonyManager manager = (TelephonyManager) context.getActivity().getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager == null) {
                return "";
            }
            deviceId = manager.getDeviceId();
            return deviceId;
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint("MissingPermission")
    private static String getDeviceId(Context context) {

        try {
            String deviceId = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return deviceId;
            }
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager == null) {
                return "";
            }
            deviceId = manager.getDeviceId();
            return deviceId;
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint("MissingPermission")
    private static String getSerial(FREContext context){

        String serial = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return serial;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            serial = Build.getSerial();
        }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            serial = Build.SERIAL;
        }
        return serial;
    }

    @SuppressLint("MissingPermission")
    private static String getSerial(Context context){

        try {
            String serial = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return serial;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                serial = Build.getSerial();
            }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                serial = Build.SERIAL;
            }
            return serial;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getAndroidId(FREContext context){
        String androidId = Settings.System.getString(context.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        UploadLogUtil.uploadLogData(context, androidId);

        return androidId;
    }

    private static String getAndroidId(Context context){
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        //UploadLogUtil.uploadLogData(context, androidId);

        return androidId;
    }
}

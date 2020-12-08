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

import com.google.gson.JsonObject;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.network.HttpClient;
import com.motrixi.datacollection.network.models.DataInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author : Jason
 * date   : 2020/12/8 12:47 PM
 * desc   :
 */
public class UploadCollectedData {

    private static String advertisingId = "";
    private static Session mSession;

    public static void formatData(Context context) {

        DataInfo info = new DataInfo();

        info.appKey = getAppkey(context);
        mSession = new Session(context);

        getAdvertisingId(context);
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
        uploadData(context, info);
    }

    private static void uploadData( Context context, DataInfo info) {

        Call<JsonObject> call = HttpClient.uploadData(context, info);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(
                                    MessageUtil.logMessage(
                                            Contants.UPLOAD_DATA_CODE, true,
                                            responseObject.optJSONObject("result").toString()
                                    )
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject error = new JSONObject(String.valueOf(response.errorBody()));
                        Contants.onLogListener.onLogListener(
                                MessageUtil.logMessage(
                                        Contants.UPLOAD_DATA_CODE, false,
                                        error.optString("message")
                                )
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    private static String getAppkey(Context context){
        if (!TextUtils.isEmpty(Contants.APP_KEY)) {
            return Contants.APP_KEY;
        }

        return "";
    }

    private static void getAdvertisingId(final Context context) {
        Executors.newSingleThreadExecutor().execute(new Runnable(){
            @Override
            public void run() {
                try {
                    String googleId = AdvertisingIdUtil.getGoogleAdId(context.getApplicationContext());
                    Log.d("google Id:", googleId);
                    advertisingId = googleId;

                    UploadLogUtil.uploadLogData(context, googleId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

            UploadLogUtil.uploadLogData(context, emailAccount.toString());

            return emailAccount;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    private static String getIMEI(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "";
        }
        String imeiInfo = DeviceIMInfoUtil.getDeviceIMEI(context);

        UploadLogUtil.uploadLogData(context, imeiInfo);
        return imeiInfo;
    }

    private static String getSystemInfo(Context context){

        String systemVersion = SystemInfoUtils.getDeviceAndroidVersion();
        return systemVersion;
    }

    private static String getLanguage( Context context){

        String languageInfo = LanguageUtil.getLanguageInfo();
        UploadLogUtil.uploadLogData(context, languageInfo);
        return languageInfo;
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

    private static String getLocationInfo( Context context){
        try {
            JSONObject jsonObject = new JSONObject();
            Location gpsLocation = GPSLocationUtil.getLocation(context);
            if (gpsLocation != null) {

                jsonObject.put("longitude",gpsLocation.getLongitude());
                jsonObject.put("latitude",gpsLocation.getLatitude());

                List<Address> result = GPSLocationUtil.getAddress(gpsLocation);
                if (result != null && result.size() >0) {

                    jsonObject.put("countryName", result.get(0).getCountryName());
                    jsonObject.put("locality", result.get(0).getLocality());
                    jsonObject.put("countryCode", result.get(0).getCountryCode());
                }

            }
            Log.d("location info", jsonObject.toString());
            UploadLogUtil.uploadLogData(context, jsonObject.toString());
            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String getUserAgent(Context context){

        String userAgent = UserAgentUtil.getUserAgent(context);

        return userAgent;
    }

    private static String getDeviceMake(Context context) {

        String deviceMake = SystemInfoUtils.getDeviceManufacturer();
        return deviceMake;
    }

    private static String getDeviceModel(Context context) {

        String deviceModel = SystemInfoUtils.getDeviceModel();
        return deviceModel;
    }

    private static String getCurrentIP(Context context){

        String ipAddress = IPAddressUtils.getCurrentIPAddress(context);
        return ipAddress;
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
    private static String getSerial(Context context){

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

    private static String getAndroidId(Context context){
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        UploadLogUtil.uploadLogData(context, androidId);

        return androidId;
    }
}

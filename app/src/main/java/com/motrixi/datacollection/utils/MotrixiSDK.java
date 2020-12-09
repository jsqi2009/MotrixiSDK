package com.motrixi.datacollection.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.google.gson.JsonObject;
import com.motrixi.datacollection.DataCollectionActivity;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.listener.OnLogListener;
import com.motrixi.datacollection.network.HttpClient;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.service.MotrixiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author : Jason
 * date   : 2020/12/8 3:52 PM
 * desc   :
 */
public class MotrixiSDK {

    private static Session mSession;
    //private var onAppkeyListener: OnAppkeyListener? = null

    public static void init(FREContext context, String appKey){

        Contants.mFREContext = context;

        HttpClient.init(context);
        mSession = new Session(context);

        //get consent form data
        getConsentDataList(context, appKey);

        //mSession!!.appKey = appKey
        Contants.APP_KEY = appKey;
        startService(context);
        //init ad id
        getAdvertisingId(context);

        //verifyAppkey(context, appKey)
    }


    /**
     * set listener
     */
    public static void setOnLogListener(OnLogListener listener) {
        Contants.onLogListener = listener;
    }

    /**
     * start the foreground service
     */
    private static void startService(FREContext context) {
        Intent startService = new Intent(context.getActivity(), MotrixiService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            context.getActivity().startForegroundService(startService);
            UploadLogUtil.uploadLogData(context, "startForegroundService ");
        } else {
            context.getActivity().startService(startService);
            UploadLogUtil.uploadLogData(context, "startService");
        }
    }

    /**
     * verify the app key via API
     */
    private static void verifyAppkey(final FREContext context , String key) {

        Log.d("app_key", key);
        UploadLogUtil.uploadLogData(context, key);
        final Call<JsonObject>  call = HttpClient.verifyAppkey(context, key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("verify success", response.body().toString());
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        JSONObject resultObject = responseObject.optJSONObject("result");
                        String appID = resultObject.optString("id");
                        mSession.setAppID(appID);
                        Log.d("app_id", appID);

                        if (Contants.onLogListener != null) {
                            boolean flag = responseObject.optBoolean("success");

                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.APP_KEY_CODE, flag, responseObject.optString("message")));
                        }

                        if (responseObject.optBoolean("success")) {
                            checkIsAgree(context);
                        }

                    } else {
                        JSONObject error = new JSONObject(String.valueOf(response.errorBody()));
                        Log.d("verify failure", error.optString("message"));
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.APP_KEY_CODE, false, error.optString("message")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("verify key", "failure");
            }
        });
    }

    /**
     * get the AdvertisingId
     */
    private static void getAdvertisingId(final FREContext context) {
        try {
            Executors.newSingleThreadExecutor().execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        String googleId = AdvertisingIdUtil.getGoogleAdId(context);
                        Log.d("google Id:", googleId);
                        Contants.advertisingID = googleId;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch ( Exception e) {
        }
    }

    private static void checkIsAgree(FREContext context ) {

        try {
            boolean flag = mSession.getAgreeFlag();
            if (!flag) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context.getActivity(), DataCollectionActivity.class);
                context.getActivity().startActivity(intent);
            } else {
                Log.d("is agree:", "already agree");
                //UploadCollectedData.formatData(context)
            }
        } catch (Throwable e) {
            Log.e("start error", e.getMessage().toString());
        }

    }

    /**
     * reset the consent form data
     */
    public static void resetConsentForm(FREContext mContext) {
        //UploadLogUtil.uploadLogData(mContext, "call reset consent form API");

        Intent intent = new Intent(mContext.getActivity(), DataCollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.getActivity().startActivity(intent);
    }

    private static void getConsentDataList(final FREContext context, final String appKey) {

        final Call<ConsentDetailInfo> call = HttpClient.fetchConsentData(context);
        call.enqueue(new  Callback<ConsentDetailInfo>() {
            @Override
            public void onResponse(Call<ConsentDetailInfo> call, Response<ConsentDetailInfo> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("result", String.valueOf(response.body().result));
                        mSession.setConsentDataInfo(response.body().result);

                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, true, response.body().result.toString()));
                        }

                        UploadLogUtil.uploadLogData(context, "get consent form data success ");

                        verifyAppkey(context, appKey);
                    } else {

                        UploadLogUtil.uploadLogData(context, "get consent form data failure ");
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, false, response.body().result.toString()));
                        }
                    }
                } catch (Exception e) {
                    UploadLogUtil.uploadLogData(context, e.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<ConsentDetailInfo> call, Throwable t) {
                Log.d("fetch consent", "failure");
                UploadLogUtil.uploadLogData(context, t.getMessage().toString());
            }
        });
    }

    public static void setOnLogListener() {

    }
}

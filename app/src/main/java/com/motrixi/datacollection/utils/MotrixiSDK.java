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
import com.motrixi.datacollection.network.GetMethodUtils;
import com.motrixi.datacollection.network.HttpClient;
import com.motrixi.datacollection.network.PostMethodUtils;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.service.MotrixiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
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

    //private static Session mSession;
    //private var onAppkeyListener: OnAppkeyListener? = null

    public static void init(FREContext context, String appKey) {

        Contants.mFREContext = context;
        Contants.APP_KEY = appKey;

        //HttpClient.init();
        //mSession = new Session(context);

        //get consent form data
        consentFormDetails(context, appKey);

        verifyKey(context);

        //mSession!!.appKey = appKey

        //startService(context);
        //init ad id

        //verifyAppkey(context, appKey)
    }

    public static void init(Context context, String appKey) {

        Contants.APP_KEY = appKey;
        //HttpClient.init();
        //mSession = new Session(context);

        //get consent form data
        //getConsentDataList(context, appKey);
        consentFormDetails(context, appKey);

        //mSession!!.appKey = appKey

        //startService(context);
        //init ad id

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
            //UploadLogUtil.uploadLogData(context, "startForegroundService ");
        } else {
            context.getActivity().startService(startService);
            //UploadLogUtil.uploadLogData(context, "startService");
        }
    }

    /**
     * start the foreground service
     */
    private static void startService(Context context) {
        Intent startService = new Intent(context, MotrixiService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(startService);
            //UploadLogUtil.uploadLogData(context, "startForegroundService ");
        } else {
            context.startService(startService);
            //UploadLogUtil.uploadLogData(context, "startService");
        }
    }

    /**
     * verify the app key via API
     */
    private static void verifyAppkey(final FREContext context, String key) {

        Log.d("app_key", key);
        //UploadLogUtil.uploadLogData(context, key);
        final Call<JsonObject> call = HttpClient.verifyAppkey(key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("verify success", response.body().toString());
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        JSONObject resultObject = responseObject.optJSONObject("result");
                        String appID = resultObject.optString("id");
                        Contants.APP_ID = appID;
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

    private static void verifyAppkey(final Context context, String key) {

        Log.d("app_key", key);
        //UploadLogUtil.uploadLogData(context, key);
        final Call<JsonObject> call = HttpClient.verifyAppkey(key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("verify success", response.body().toString());
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        JSONObject resultObject = responseObject.optJSONObject("result");
                        String appID = resultObject.optString("id");
                        Contants.APP_ID = appID;
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

    private static void verifyKey(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("app_key", Contants.APP_KEY);

                String msg = PostMethodUtils.httpPost(Contants.VERIFY_KEY_API, map);
                Log.d("verify key", msg);

                formatKeyResponse(context, msg);
            }
        }).start();
    }

    private static void verifyKey(final FREContext context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("app_key", Contants.APP_KEY);

                String msg = PostMethodUtils.httpPost(Contants.VERIFY_KEY_API, map);
                Log.d("verify key", msg);

                formatKeyResponse(context, msg);
            }
        }).start();
    }

    private static void formatKeyResponse(Context context, String detail) {
        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                String appID = resultObject.optString("id");
                Contants.APP_ID = appID;
                if (object.optBoolean("success")) {
                    checkIsAgree(context);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void formatKeyResponse(final FREContext context, final String detail) {
        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                String appID = resultObject.optString("id");
                Contants.APP_ID = appID;
                if (object.optBoolean("success")) {
                    checkIsAgree(context);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkIsAgree(final FREContext context) {

        context.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean flag = Contants.agreeFlag;
                    if (!flag) {
                        Intent intent = new Intent(context.getActivity(), DataCollectionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getActivity().startActivity(intent);
                    } else {
                        Log.d("is agree:", "already agree");
                        //UploadCollectedData.formatData(context)
                    }
                } catch (Throwable e) {
                    Log.e("start error", e.getMessage().toString());
                }
            }
        });


    }

    private static void checkIsAgree(Context context) {

        try {
            boolean flag = Contants.agreeFlag;
            if (!flag) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(context, DataCollectionActivity.class);
                context.startActivity(intent);
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

    /**
     * reset the consent form data
     */
    public static void resetConsentForm(Context mContext) {
        //UploadLogUtil.uploadLogData(mContext, "call reset consent form API");

        Intent intent = new Intent(mContext, DataCollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private static void consentFormDetails(final Context context, String appKey) {

        Locale locale = Locale.getDefault();
        final String lan = locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = GetMethodUtils.httpGet(Contants.CONSENT_DETAIL_API, lan);
                Log.d("form details", msg);
                formatConsentData(context, msg);
            }
        }).start();
    }

    private static void consentFormDetails(final FREContext context, String appKey) {

        Locale locale = Locale.getDefault();
        final String lan = locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = GetMethodUtils.httpGet(Contants.CONSENT_DETAIL_API, lan);
                Log.d("form details", msg);
                formatConsentData(context, msg);
            }
        }).start();
    }

    private static void formatConsentData(Context context, String detail) {

        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                JSONObject valueObject = resultObject.optJSONObject("value");

                Contants.terms_content = valueObject.optString("terms_content");
                Contants.options = valueObject.optString("options");
                Contants.cancel_button_text = valueObject.optString("cancel_button_text");
                Contants.confirm_button_text = valueObject.optString("confirm_button_text");
                Contants.option_button_text = valueObject.optString("option_button_text");
                Contants.back_button_text = valueObject.optString("back_button_text");
                Contants.terms_page_title = valueObject.optString("terms_page_title");
                Contants.link_page_title = valueObject.optString("link_page_title");
                Contants.option_page_title = valueObject.optString("option_page_title");

                Log.e("options", Contants.options);

                verifyKey(context);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void formatConsentData(final FREContext context, final String detail) {

        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                JSONObject valueObject = resultObject.optJSONObject("value");

                Contants.terms_content = valueObject.optString("terms_content");
                Contants.options = valueObject.optString("options");
                Contants.cancel_button_text = valueObject.optString("cancel_button_text");
                Contants.confirm_button_text = valueObject.optString("confirm_button_text");
                Contants.option_button_text = valueObject.optString("option_button_text");
                Contants.back_button_text = valueObject.optString("back_button_text");
                Contants.terms_page_title = valueObject.optString("terms_page_title");
                Contants.link_page_title = valueObject.optString("link_page_title");
                Contants.option_page_title = valueObject.optString("option_page_title");

                Log.e("options", Contants.options);

                //verifyKey(context);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private static void getConsentDataList(final FREContext context, final String appKey) {

        final Call<ConsentDetailInfo> call = HttpClient.fetchConsentData();
        call.enqueue(new Callback<ConsentDetailInfo>() {
            @Override
            public void onResponse(Call<ConsentDetailInfo> call, Response<ConsentDetailInfo> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("result", String.valueOf(response.body().result));
                        //mSession.setConsentDataInfo(response.body().result);
                        Contants.formInfo = response.body().result;

                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, true, response.body().result.toString()));
                        }

                        //UploadLogUtil.uploadLogData(context, "get consent form data success ");

                        verifyAppkey(context, appKey);
                    } else {

                        //UploadLogUtil.uploadLogData(context, "get consent form data failure ");
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, false, response.body().result.toString()));
                        }
                    }
                } catch (Exception e) {
                    //UploadLogUtil.uploadLogData(context, e.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<ConsentDetailInfo> call, Throwable t) {
                Log.d("fetch consent", "failure");
                //UploadLogUtil.uploadLogData(context, t.getMessage().toString());
            }
        });
    }

    private static void getConsentDataList(final Context context, final String appKey) {

        final Call<ConsentDetailInfo> call = HttpClient.fetchConsentData();
        call.enqueue(new Callback<ConsentDetailInfo>() {
            @Override
            public void onResponse(Call<ConsentDetailInfo> call, Response<ConsentDetailInfo> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.d("result", String.valueOf(response.body().result));
                        //mSession.setConsentDataInfo(response.body().result);
                        Contants.formInfo = response.body().result;

                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, true, response.body().result.toString()));
                        }

                        //UploadLogUtil.uploadLogData(context, "get consent form data success ");

                        verifyAppkey(context, appKey);
                    } else {

                        //UploadLogUtil.uploadLogData(context, "get consent form data failure ");
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, false, response.body().result.toString()));
                        }
                    }
                } catch (Exception e) {
                    //UploadLogUtil.uploadLogData(context, e.getMessage().toString());
                }
            }

            @Override
            public void onFailure(Call<ConsentDetailInfo> call, Throwable t) {
                Log.d("fetch consent", "failure");
                //UploadLogUtil.uploadLogData(context, t.getMessage().toString());
            }
        });
    }

    public static void setOnLogListener() {

    }
}

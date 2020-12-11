package com.motrixi.datacollection;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.listener.OnLogListener;
import com.motrixi.datacollection.network.GetMethodUtils;
import com.motrixi.datacollection.network.PostMethodUtils;
import com.motrixi.datacollection.service.MotrixiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class MotrixiActivity extends FragmentActivity {

    private static MotrixiActivity mActivity = null;
    private String appkey;
    private Session mSession;

    public static MotrixiActivity getSharedMainActivityOrNull() {
        return mActivity;
    }

    public static MotrixiActivity getSharedMainActivity() {
        MotrixiActivity mainActivity = getSharedMainActivityOrNull();
        if (mainActivity == null)
            throw new IllegalStateException("Activity doesn't exist");
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null) {
            mActivity = this; // new WeakReference<>(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mSession = new Session(this);
        appkey = getIntent().getStringExtra("key");
        init(appkey);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void init(String appKey) {
        mSession.setAppKey(appKey);
        //Contants.APP_KEY = appKey;
        consentFormDetails();

        //startService();
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
    private static void startService() {
        try {
            Intent startService = new Intent(mActivity, MotrixiService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                mActivity.startForegroundService(startService);
                //UploadLogUtil.uploadLogData(context, "startForegroundService ");
            } else {
                mActivity.startService(startService);
                //UploadLogUtil.uploadLogData(context, "startService");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * reset the consent form data
     */
    public void resetConsentForm() {
        //UploadLogUtil.uploadLogData(mContext, "call reset consent form API");


        Intent intent = new Intent(mActivity, DataCollectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    private void consentFormDetails() {

        Locale locale = Locale.getDefault();
        final String lan = locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase();

        //Toast.makeText(mActivity, "start get consent details", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = GetMethodUtils.httpGet(Contants.CONSENT_DETAIL_API, lan);
                Log.d("form details", msg);
                formatConsentData(msg);
            }
        }).start();
    }

    private void formatConsentData(final String detail) {

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

                mSession.setTermsContent(valueObject.optString("terms_content"));
                mSession.setOption(valueObject.optString("options"));
                mSession.setCancelButton(valueObject.optString("cancel_button_text"));
                mSession.setConfirmButton(valueObject.optString("confirm_button_text"));
                mSession.setOptionButton(valueObject.optString("option_button_text"));
                mSession.setBackButton(valueObject.optString("back_button_text"));
                mSession.setTermsTitle(valueObject.optString("terms_page_title"));
                mSession.setLinkTitle(valueObject.optString("link_page_title"));
                mSession.setOptionTitle(valueObject.optString("option_page_title"));

                Log.e("options", Contants.options);

                /*mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, Contants.options, Toast.LENGTH_SHORT).show();
                    }
                });*/

                verifyKey();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void verifyKey() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("app_key", mSession.getAppKey());

                String msg = PostMethodUtils.httpPost(Contants.VERIFY_KEY_API, map);
                Log.d("verify key", msg);

                formatKeyResponse(msg);
            }
        }).start();
    }

    private void formatKeyResponse(final String detail) {
        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                String appID = resultObject.optString("id");
                //Contants.APP_ID = appID;
                mSession.setAppID(appID);

                /*mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mActivity, Contants.APP_ID, Toast.LENGTH_SHORT).show();
                    }
                });*/

                if (object.optBoolean("success")) {
                    checkIsAgree();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkIsAgree() {

        try {
//            boolean flag = Contants.agreeFlag;
            boolean flag = mSession.getAgreeFlag();
            if (!flag) {
                Intent intent = new Intent(MotrixiActivity.this, DataCollectionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MotrixiActivity.this.startActivity(intent);

                MotrixiActivity.this.finish();
            } else {
                Log.d("is agree:", "already agree");
                this.finish();
                //UploadCollectedData.formatData(context)
            }
        } catch (Throwable e) {
            Log.e("start error", e.getMessage().toString());
            this.finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
}
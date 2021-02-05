package com.motrixi.datacollection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.extensions.MotrixiSDKInit;
import com.motrixi.datacollection.fragment.PrivacyStatementFragment;
import com.motrixi.datacollection.network.GetMethodUtils;
import com.motrixi.datacollection.network.PostMethodUtils;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.network.models.LanguageInfo;
import com.motrixi.datacollection.utils.AdvertisingIdUtil;
import com.motrixi.datacollection.utils.MessageUtil;
import com.motrixi.datacollection.utils.UploadCollectedData;
import com.motrixi.datacollection.utils.UploadLogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataCollectionActivity extends FragmentActivity {

    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.FOREGROUND_SERVICE};
    private int request_code = 1;
    private ArrayList<String> mPermissionList = new  ArrayList();
    public static  Session mSession;
    private RelativeLayout rootLayout;
    private FrameLayout frameLayout;
    private LinearLayout actionBarLayout;
    public static ConsentDetailInfo.ResultInfo info;
    public static String[] optionArray;
    public static String[] selectedOptionList;
    private FragmentManager fm;

    public ArrayList<LanguageInfo> lanList = new  ArrayList();
    private static final String BUNDLE_FRAGMENTS_KEY = "android:support:fragments";
    public static List<String> selectedValue = new ArrayList<>();
    public static List optionList;
    private LocationManager mLocationManager;

    private static DataCollectionActivity mSharedMainActivity = null;
    public static DataCollectionActivity getSharedMainActivityOrNull() {
        return mSharedMainActivity;
    }

    public static DataCollectionActivity getSharedMainActivity() {
        DataCollectionActivity mainActivity = getSharedMainActivityOrNull();
        if (mainActivity == null)
            throw new IllegalStateException("Activity doesn't exist");
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null && this.clearFragmentsTag()) {
            //重建时清除 fragment的状态
            savedInstanceState.remove(BUNDLE_FRAGMENTS_KEY);
        }
        super.onCreate(savedInstanceState);
        if (mSharedMainActivity == null) {
            mSharedMainActivity = this; // new WeakReference<>(this);
        }

        mSession = new Session(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initLayout();
        setContentView(rootLayout);

        //initView();

        consentFormDetails("en", 0);
        getAdvertisingId(mSharedMainActivity);

        checkGPSStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSharedMainActivity == null) {
            mSharedMainActivity = this; // new WeakReference<>(this);
        }

        getLanguageList();

        //getAdvertisingId(this);
        //getAdvertisingId();
    }

    private void checkGPSStatus(){
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Contants.mFREContext != null) {
                String result = MessageUtil.logMessage(Contants.SET_GPS, false, "The GPS is disabled, please set the GPS to enable");
                MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("GPS disabled", result);
            }
        }
    }

    /**
     * get the AdvertisingId
     */
    private void getAdvertisingId() {
        try {

            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        String googleId = AdvertisingIdUtil.getGoogleAdId(mSharedMainActivity);
                        Log.e("google Id:", googleId);
                        mSession.setAdvertisingID(googleId);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, TimeUnit.MILLISECONDS);
        } catch ( Exception e) {
        }
    }

    /**
     * get the AdvertisingId
     */
    private void getAdvertisingId(final Context context) {
        try {
            Executors.newSingleThreadExecutor().execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        Log.e("google Id:", "getting....");
                        String googleId = AdvertisingIdUtil.getGoogleAdId(context.getApplicationContext());
                        Log.e("google Id:", googleId);

//                        Contants.advertisingID = googleId;
                        mSession.setAdvertisingID(googleId);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getMessage());
                    }
                }
            });
        } catch ( Exception e) {
        }
    }

    private void initLayout() {

        rootLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams rootParams = new  RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        rootLayout.setLayoutParams(rootParams);

        frameLayout  = new FrameLayout(this);
        FrameLayout.LayoutParams itemParams = new  FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        frameLayout.setId(Contants.HOME_CONTAINER_ID);
        frameLayout.setLayoutParams(itemParams);
        rootLayout.addView(frameLayout);

    }

    private void initView(int flag) {

        //info = mSession.getConsentDataInfo();

        if (!TextUtils.isEmpty(mSession.getOption())) {
            if (mSession.getOption().contains("|")) {
                optionArray = mSession.getOption().replace("|", "=").split("=");
                Log.d("option array", optionArray.length + "");

                selectedOptionList = mSession.getOption().replace("|", "=").split("=");
            } else {
                optionArray = new String[1];
                optionArray[0] = mSession.getOption();

                selectedOptionList = new String[1];
                selectedOptionList[0] = mSession.getOption();

            }
            selectedValue = Arrays.asList(selectedOptionList);
            optionList = new ArrayList(selectedValue);

        }

        // 获取碎片管理器
        //FragmentManager fm = getSupportFragmentManager();
        if (fm == null) {
            fm = getSupportFragmentManager();
        }


        //fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("","")).commit();
        //fm.beginTransaction().add(Contants.HOME_CONTAINER_ID,PrivacyStatementFragment.newInstance("","")).commit();

        try {
            if (flag == 1) {
                fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("", "")).commit();
    //            fm.beginTransaction().replace(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("", "")).commitAllowingStateLoss();
    //            fm.beginTransaction().replace(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("","")).commitAllowingStateLoss();
            } else {
                fm.beginTransaction().add(Contants.HOME_CONTAINER_ID,PrivacyStatementFragment.newInstance("","")).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("exception",e.getMessage());
            //fm.beginTransaction().add(Contants.HOME_CONTAINER_ID,PrivacyStatementFragment.newInstance("","")).commit();
        }
    }



    public void submitFormData() {
//        final String value = getValue();
        final String value = getOptionValue();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("value", value);
                map.put("app_id",mSession.getAppID());

                String msg = PostMethodUtils.httpPost(Contants.SUBMIT_FORM_API, map);
                Log.e("form response", msg);

                handleFormData(msg);

            }
        }).start();
    }

    private void handleFormData(String detail) {

        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONObject resultObject = object.optJSONObject("result");
                String consentFormID = resultObject.optString("id");
//                Contants.consentFormID = consentFormID;
                mSession.setConsentFormID(consentFormID);

                if (Contants.mFREContext != null) {
                    String result = MessageUtil.logMessage(Contants.CONSENT_FORM_CODE, true, detail);
                    MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("upload form data", result);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPermission();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (Contants.mFREContext != null) {
                String result = MessageUtil.logMessage(Contants.CONSENT_FORM_CODE, false, "upload form data failure");
                MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("upload form data", result);
            }
        }
    }

    public void cancelConsent() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg;
                String androidID = Settings.System.getString(getSharedMainActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
                HashMap<String, String> map = new HashMap();
                map.put("app_key", mSession.getAppKey());
                map.put("android_id",androidID);

                msg = PostMethodUtils.httpPost(Contants.CANCEL_CONSENT_API, map);
                Log.e("cancel response", msg);

                /*if (Contants.onLogListener != null) {
                    Contants.onLogListener.onLogListener(
                            MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true, responseObject.optString("message"))
                    );
                }*/

                if (msg != Contants.RESPONSE_ERROR) {
                    if (Contants.mFREContext != null) {
                        String result = MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true, msg);
                        MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("cancel", result);
                    }
                } else {
                    if (Contants.mFREContext != null) {
                        String result = MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, false, "cancel failure");
                        MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("cancel", result);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DataCollectionActivity.mSharedMainActivity.finish();
                    }
                });


                Log.e("cancel ", "test");

                mSharedMainActivity.finish();
                //System.exit(0);
                //android.os.Process.killProcess(android.os.Process.myPid());

            }
        }).start();
    }

    private String getValue() {

        String formValue = "";
        if (optionArray != null && optionArray.length > 0) {

            for (int i = 0; i < optionArray.length; i++) {
                if (i == optionArray.length - 1) {
                    formValue = formValue + optionArray[i];
                } else {
                    formValue = formValue + optionArray[i] + "|";
                }
            }
        }
        return  formValue;
    }

    private String getOptionValue() {

        String formValue = "";
        if (optionList != null && optionList.size() > 0) {

            for (int i = 0; i < optionList.size(); i++) {
                if (i == optionList.size() - 1) {
                    formValue = formValue + optionList.get(i);
                } else {
                    formValue = formValue + optionList.get(i) + "|";
                }
            }
        }
        return  formValue;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void initPermission() {
        mSession.setSyncTime(new Date().getTime());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();

            //Toast.makeText(this, "request permissions", Toast.LENGTH_SHORT).show();
        } else {

            UploadCollectedData.formatData(this);
            mSharedMainActivity.finish();
        }
    }

    private void requestPermission() {

        mPermissionList.clear();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                mPermissionList.add(permission);
            }
        }

        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, request_code);
        } else {

            //Toast.makeText(this, "already grant all the permissions", Toast.LENGTH_SHORT).show();

            //upload data
            UploadCollectedData.formatData(this);
            mSharedMainActivity.finish();
        }

        //Toast.makeText(this, "grant all the permissions", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mSession.setPermissionFlag(true);
        boolean hasPermission = false;
        if (request_code == requestCode) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == -1) {
                        hasPermission = true;
                    }
                }
            }

        }

        if (hasPermission) {

        }

        //Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();

        UploadCollectedData.formatData(this);
        mSharedMainActivity.finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void consentFormDetails(final String language, final int flag) {

        Locale locale = Locale.getDefault();
        final String lan = locale.getLanguage().toLowerCase() + "-" + locale.getCountry().toLowerCase();

        //Toast.makeText(mActivity, "start get consent details", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = GetMethodUtils.httpGet(Contants.CONSENT_DETAIL_API, language);
                Log.d("form details", msg);
                formatConsentData(msg, flag);
            }
        }).start();
    }

    private void formatConsentData(final String detail, final int flag) {

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
                if (valueObject.has("language_button_text")) {
                    mSession.setLanguageButton(valueObject.optString("language_button_text"));
                } else {
                    mSession.setLanguageButton("Language");
                }
                //mSession.setLanguageButton(valueObject.optString("language_button_text"));

                Log.e("options", Contants.options);

                if (Contants.mFREContext != null) {
                    String result = MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, true, detail);
                    MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("consent details", result);
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //initView(flag);
                    }
                });
                initView(flag);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            if (Contants.mFREContext != null) {
                String result = MessageUtil.logMessage(Contants.FETCH_CONSENT_DATA, false, "get consent form data failure");
                MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("consent details", result);
            }
        }
    }

    public void getLanguageList() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = GetMethodUtils.httpGet(Contants.FETCH_LANGUAGE_API, "");
                Log.d("language details", msg);
                formatLanguageData(msg);
            }
        }).start();
    }

    private void formatLanguageData(final String detail) {

        lanList.clear();
        if (!detail.equals(Contants.RESPONSE_ERROR)) {
            try {
                JSONObject object = new JSONObject(detail);
                JSONArray resultArray = object.optJSONArray("result");

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject item = resultArray.optJSONObject(i);
                    JSONObject valueObj  = item.optJSONObject("value");
                    JSONObject lanObj  = valueObj.optJSONObject("select_language");

                    if (lanObj.has("code") && lanObj.has("language")) {
                        String code = lanObj.optString("code");
                        String language = lanObj.optString("language");

                        LanguageInfo info = new LanguageInfo();
                        info.code = code;
                        info.language = language;
                        lanList.add(info);
                        Log.e("language info", code + "-" + language);
                    }
                }

                if (Contants.mFREContext != null) {
                    String result = MessageUtil.logMessage(Contants.GET_LANGUAGE_LIST, true, lanList.toString());
                    MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("language details", result);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (Contants.mFREContext != null) {
                String result = MessageUtil.logMessage(Contants.GET_LANGUAGE_LIST, false, "get language list data failure");
                MotrixiSDKInit.sdkContext.dispatchStatusEventAsync("language details", result);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState","sve");
        if (outState != null && this.clearFragmentsTag()) {
            //销毁时不保存fragment的状态
            outState.remove(BUNDLE_FRAGMENTS_KEY);
        }
    }

    protected boolean clearFragmentsTag() {
        return true;
    }
}
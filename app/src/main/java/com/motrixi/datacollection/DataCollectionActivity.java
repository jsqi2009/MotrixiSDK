package com.motrixi.datacollection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
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
import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.google.gson.JsonObject;
import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.fragment.PrivacyStatementFragment;
import com.motrixi.datacollection.network.HttpClient;
import com.motrixi.datacollection.network.PostMethodUtils;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.utils.AdvertisingIdUtil;
import com.motrixi.datacollection.utils.MessageUtil;
import com.motrixi.datacollection.utils.UploadCollectedData;
import com.motrixi.datacollection.utils.UploadLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCollectionActivity extends FragmentActivity {

    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.FOREGROUND_SERVICE};
    private int request_code = 1;
    private ArrayList<String> mPermissionList = new  ArrayList();
    private static Session mSession;
    private RelativeLayout rootLayout;
    private FrameLayout frameLayout;
    private LinearLayout actionBarLayout;
    public static ConsentDetailInfo.ResultInfo info;
    public static String[] optionArray;

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
        super.onCreate(savedInstanceState);
        if (mSharedMainActivity == null) {
            mSharedMainActivity = this; // new WeakReference<>(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initLayout();
        setContentView(rootLayout);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getAdvertisingId(this);
    }

    /**
     * get the AdvertisingId
     */
    private static void getAdvertisingId(Context context) {
        try {
            Executors.newSingleThreadExecutor().execute(new Runnable(){
                @Override
                public void run() {
                    try {
                        String googleId = AdvertisingIdUtil.getGoogleAdId(getSharedMainActivity());
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

    private void initView() {

        //info = mSession.getConsentDataInfo();

        if (!TextUtils.isEmpty(Contants.options)) {
            optionArray = Contants.options.replace("|","=").split("=");
            Log.d("option array", optionArray.length + "");
        }

        // 获取碎片管理器
        FragmentManager fm = getSupportFragmentManager();
        //fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("","")).commit();
        fm.beginTransaction().add(Contants.HOME_CONTAINER_ID,new  PrivacyStatementFragment()).commit();
    }

    public void submitConsentFormData(String value) {

        Log.d("consent form value", value);
        Call<JsonObject> call = HttpClient.submitConsentForm(this, value, mSession.getAppID());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    try {
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        JSONObject resultObject = responseObject.optJSONObject("result");
                        String consentFormID = resultObject.optString("id");
                        mSession.setConsentFormID(consentFormID);

                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(
                                    MessageUtil.logMessage(
                                            Contants.CONSENT_FORM_CODE,
                                            true, responseObject.optString("message"))
                            );
                        }

                        initPermission();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject  error = new JSONObject(String.valueOf(response.errorBody()));
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(
                                    MessageUtil.logMessage(
                                            Contants.CONSENT_FORM_CODE,
                                            false,
                                            error.optString("message")
                                    )
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                UploadLogUtil.uploadLogData(Contants.mFREContext, t.getMessage().toString());
            }
        });
    }

    public void submitFormData() {
        final String value = getValue();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap();
                map.put("value", value);
                map.put("app_id",Contants.APP_ID);

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
                Contants.consentFormID = consentFormID;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initPermission();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
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
                map.put("app_key", Contants.APP_KEY);
                map.put("android_id",androidID);

                msg = PostMethodUtils.httpPost(Contants.CANCEL_CONSENT_API, map);
                Log.e("cancel response", msg);

                /*if (Contants.onLogListener != null) {
                    Contants.onLogListener.onLogListener(
                            MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true, responseObject.optString("message"))
                    );
                }*/

                finish();
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


    public void initPermission() {
        //mSession.setSyncTime(new Date().getTime());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();

            //Toast.makeText(this, "request permissions", Toast.LENGTH_SHORT).show();
        } else {

            UploadCollectedData.formatData(this);
            getSharedMainActivity().finish();
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
            getSharedMainActivity().finish();
        }

        //Toast.makeText(this, "grant all the permissions", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermission = false;
        if (request_code == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    hasPermission = true;
                }
            }
        }

        if (hasPermission) {

        }

        //Toast.makeText(this, "onRequestPermissionsResult", Toast.LENGTH_SHORT).show();

        UploadCollectedData.formatData(this);
        getSharedMainActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
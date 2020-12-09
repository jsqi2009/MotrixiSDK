package com.motrixi.datacollection;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.fragment.PrivacyStatementFragment;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;
import com.motrixi.datacollection.utils.UploadCollectedData;

import java.util.ArrayList;
import java.util.Date;

public class DataCollectionActivity extends FragmentActivity {

    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.FOREGROUND_SERVICE};
    private int request_code = 1;
    private ArrayList<String> mPermissionList = new  ArrayList();
    private Session mSession;
    private RelativeLayout rootLayout;
    private FrameLayout frameLayout;
    private LinearLayout actionBarLayout;
    public static ConsentDetailInfo.ResultInfo info;
    public static String[] optionArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initLayout();
        setContentView(rootLayout);

        initView();
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
        mSession = new Session(Contants.mFREContext);
        //info = mSession.getConsentDataInfo();

        /*if (info != null) {
            optionArray = info.value.options.replace("|","=").split("=");
            Log.d("option array", optionArray.length + "");
        }*/

        // 获取碎片管理器
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(Contants.HOME_CONTAINER_ID, PrivacyStatementFragment.newInstance("","")).commit();
    }

    /*public void submitConsentFormData(String value) {

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
    }*/

    /*public void rejectCollect(){

        String appKey=Contants.APP_KEY;
        String androidID = Settings.System.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        Call<JsonObject> call=HttpClient.rejectCollectionData(this,appKey,androidID);
        call.enqueue(new Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject= new JSONObject(response.body().toString());

                        UploadLogUtil.uploadLogData(Contants.mFREContext,"cancel:"+responseObject.optString("message"));
                        Log.d("cancel status","success");
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(
                                    MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true, responseObject.optString("message"))
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject errorObject= new JSONObject(String.valueOf(response.errorBody()));

                        UploadLogUtil.uploadLogData(Contants.mFREContext,"cancel:"+errorObject.optString("message"));
                        Log.d("cancel status","failure");
                        if (Contants.onLogListener != null) {
                            Contants.onLogListener.onLogListener(
                                    MessageUtil.logMessage(Contants.CANCEL_COLLECT_DATA, true, errorObject.optString("message"))
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("cancel status","network failure");
                UploadLogUtil.uploadLogData(Contants.mFREContext,t.getMessage());
            }

        });
    }*/


    private void initPermission() {
        //mSession!!.agreeFlag = true
        mSession.setSyncTime(new Date().getTime());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        } else {
            UploadCollectedData.formatData(Contants.mFREContext);
            finish();
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

            /*var service: Intent = Intent(this, UploadService::class.java)
            startService(service)*/
            //upload data
            UploadCollectedData.formatData(Contants.mFREContext);
            finish();
        }
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

        UploadCollectedData.formatData(Contants.mFREContext);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
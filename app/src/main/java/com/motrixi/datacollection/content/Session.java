package com.motrixi.datacollection.content;

import android.content.Context;

import com.adobe.fre.FREContext;
import com.motrixi.datacollection.network.models.ConsentDetailInfo;

import java.util.List;


public class Session {

    private static final String FILE_NAME = ".session";

    private static final String KEY_AGREE = "agree_terms";
    private static final String KEY_OPTION1 = "consent_option1";
    private static final String KEY_OPTION2 = "consent_option2";
    private static final String KEY_OPTION3 = "consent_option3";
    private static final String KEY_OPTION4 = "consent_option4";
    private static final String KEY_OPTION5 = "consent_option5";
    private static final String KEY_OPTION6 = "consent_option6";
    private static final String KEY_VIEW_OPTION = "view_options";
    private static final String KEY_TIME_STAMP = "time_stamp";
    private static final String KEY_APP_KEY_ID = "app_key_id";
    private static final String KEY_CONSENT_FORM_ID = "consent_form_id";
    private static final String KEY_APP_KEY = "app_key";
    private static final String KEY_CONSENT_FORM_DATA = "consent_form_data";

    private static final String KEY_TERMS_CONTENT = "terms_content";
    private static final String KEY_OPTIONS = "options";
    private static final String KEY_CANCEL_BUTTON = "cancel_button_text";
    private static final String KEY_CONFIRM_BUTTON = "confirm_button_text";
    private static final String KEY_OPTION_BUTTON = "option_button_text";
    private static final String KEY_BACK_BUTTON = "back_button_text";
    private static final String KEY_TERMS_TITLE = "terms_page_title";
    private static final String KEY_LINK_TITLE = "link_page_title";
    private static final String KEY_OPTION_TITLE = "option_page_title";
    private static final String KEY_ADVERTISING_ID = "advertisingId";
    private static final String KEY_REQUEST_PERMISSION = "request_permission";

    private static final String KEY_COLLECTION_FLAG = "collecting_flag";

    private HashStorage mHashStorage;

    public Session(FREContext c) {
        this.mHashStorage = new HashStorage(c, FILE_NAME);
    }

    public Session(Context c) {
        this.mHashStorage = new HashStorage(c, FILE_NAME);
    }

   /* public void setData(Object data, Class type, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(data, type);
        this.mHashStorage.put(key, json);
    }

    public <T> T getData(Class<? extends T> type, String key) {
        Gson gson = new Gson();
        return gson.fromJson(this.mHashStorage.getString(key), type);
    }*/

    public void clear() {
        this.mHashStorage.clear();
    }



    // 清除所有与登陆相关的信息
    public void removeLoginInfo() {

    }

    public void setAppKey(String id) {
        this.mHashStorage.put(KEY_APP_KEY, id);
    }

    public String getAppKey() {
        return this.mHashStorage.getString(KEY_APP_KEY);
    }

    public void setAgreeFlag(boolean flag) {
        this.mHashStorage.put(KEY_AGREE, flag);
    }

    public boolean getAgreeFlag() {
        return this.mHashStorage.getBoolean(KEY_AGREE);
    }

    public void setSyncTime(long time) {
        this.mHashStorage.put(KEY_TIME_STAMP, time);
    }

    public long getSyncTime() {
        return this.mHashStorage.getLong(KEY_TIME_STAMP);
    }

    public void setAppID(String id) {
        this.mHashStorage.put(KEY_APP_KEY_ID, id);
    }

    public String getAppID() {
        return this.mHashStorage.getString(KEY_APP_KEY_ID);
    }

    public void setConsentFormID(String id) {
        this.mHashStorage.put(KEY_CONSENT_FORM_ID, id);
    }

    public String getConsentFormID() {
        return this.mHashStorage.getString(KEY_CONSENT_FORM_ID);
    }

    public void setTermsContent(String id) {
        this.mHashStorage.put(KEY_TERMS_CONTENT, id);
    }

    public String getTermsContent() {
        return this.mHashStorage.getString(KEY_TERMS_CONTENT);
    }

    public void setOption(String id) {
        this.mHashStorage.put(KEY_OPTIONS, id);
    }

    public String getOption() {
        return this.mHashStorage.getString(KEY_OPTIONS);
    }

    public void setCancelButton(String id) {
        this.mHashStorage.put(KEY_CANCEL_BUTTON, id);
    }

    public String getCancelButton() {
        return this.mHashStorage.getString(KEY_CANCEL_BUTTON);
    }

    public void setOptionButton(String id) {
        this.mHashStorage.put(KEY_OPTION_BUTTON, id);
    }

    public String getOptionButton() {
        return this.mHashStorage.getString(KEY_OPTION_BUTTON);
    }

    public void setConfirmButton(String id) {
        this.mHashStorage.put(KEY_CONFIRM_BUTTON, id);
    }

    public String getConfirmButton() {
        return this.mHashStorage.getString(KEY_CONFIRM_BUTTON);
    }

    public void setBackButton(String id) {
        this.mHashStorage.put(KEY_BACK_BUTTON, id);
    }

    public String getBackButton() {
        return this.mHashStorage.getString(KEY_BACK_BUTTON);
    }

    public void setTermsTitle(String id) {
        this.mHashStorage.put(KEY_TERMS_TITLE, id);
    }

    public String getTermsTitle() {
        return this.mHashStorage.getString(KEY_TERMS_TITLE);
    }

    public void setLinkTitle(String id) {
        this.mHashStorage.put(KEY_LINK_TITLE, id);
    }

    public String getLinkTitle() {
        return this.mHashStorage.getString(KEY_LINK_TITLE);
    }

    public void setOptionTitle(String id) {
        this.mHashStorage.put(KEY_OPTION_TITLE, id);
    }

    public String getOptionTitle() {
        return this.mHashStorage.getString(KEY_OPTION_TITLE);
    }

    public void setAdvertisingID(String id) {
        this.mHashStorage.put(KEY_ADVERTISING_ID, id);
    }

    public String getAdvertisingID() {
        return this.mHashStorage.getString(KEY_ADVERTISING_ID);
    }

    public void setPermissionFlag(boolean flag) {
        this.mHashStorage.put(KEY_REQUEST_PERMISSION, flag);
    }

    public boolean getPermissionFlag() {
        return this.mHashStorage.getBoolean(KEY_REQUEST_PERMISSION);
    }

    public void setIsCollecting(boolean flag) {
        this.mHashStorage.put(KEY_COLLECTION_FLAG, flag);
    }

    public boolean getIsCollecting() {
        return this.mHashStorage.getBoolean(KEY_COLLECTION_FLAG);
    }

    /*public void setConsentDataInfo(ConsentDetailInfo.ResultInfo info){
        setData(info, ConsentDetailInfo.ResultInfo.class, KEY_CONSENT_FORM_DATA);
    }

    public ConsentDetailInfo.ResultInfo getConsentDataInfo() {
        return getData(ConsentDetailInfo.ResultInfo.class, KEY_CONSENT_FORM_DATA);
    }*/




    /*var agreeFlag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_AGREE)
    set(paramString) = this.mHashStorage.put(KEY_AGREE, paramString)


    var option_1_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION1)
    set(paramString) = this.mHashStorage.put(KEY_OPTION1, paramString)

    var option_2_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION2)
    set(paramString) = this.mHashStorage.put(KEY_OPTION2, paramString)

    var option_3_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION3)
    set(paramString) = this.mHashStorage.put(KEY_OPTION3, paramString)

    var option_4_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION4)
    set(paramString) = this.mHashStorage.put(KEY_OPTION4, paramString)

    var option_5_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION5)
    set(paramString) = this.mHashStorage.put(KEY_OPTION5, paramString)

    var option_6_flag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_OPTION6)
    set(paramString) = this.mHashStorage.put(KEY_OPTION6, paramString)

    var viewOptionsFlag: Boolean
    get() = this.mHashStorage.getBoolean(KEY_VIEW_OPTION)
    set(paramString) = this.mHashStorage.put(KEY_VIEW_OPTION, paramString)

    var syncTime: Long
    get() = this.mHashStorage.getLong(KEY_TIME_STAMP)
    set(paramString) = this.mHashStorage.put(KEY_TIME_STAMP, paramString)

    var appID: String
    get() = this.mHashStorage.getString(KEY_APP_KEY_ID)
    set(paramString) = this.mHashStorage.put(KEY_APP_KEY_ID, paramString)

    var consentFormID: String
    get() = this.mHashStorage.getString(KEY_CONSENT_FORM_ID)
    set(paramString) = this.mHashStorage.put(KEY_CONSENT_FORM_ID, paramString)

    var appKey: String
    get() = this.mHashStorage.getString(KEY_APP_KEY)
    set(paramString) = this.mHashStorage.put(KEY_APP_KEY, paramString)*/



}

package com.motrixi.datacollection.content

import android.content.Context
import android.text.TextUtils

import com.google.gson.Gson
import com.motrixi.datacollection.network.models.ConsentDetailInfo


class Session(c: Context) {

    private val mHashStorage: HashStorage

    companion object {

        private val FILE_NAME = ".session"

        private val KEY_AGREE = "agree_terms"
        private val KEY_OPTION1 = "consent_option1"
        private val KEY_OPTION2 = "consent_option2"
        private val KEY_OPTION3 = "consent_option3"
        private val KEY_OPTION4 = "consent_option4"
        private val KEY_OPTION5 = "consent_option5"
        private val KEY_OPTION6 = "consent_option6"
        private val KEY_VIEW_OPTION = "view_options"
        private val KEY_TIME_STAMP = "time_stamp"
        private val KEY_APP_KEY_ID = "app_key_id"
        private val KEY_CONSENT_FORM_ID = "consent_form_id"
        private val KEY_APP_KEY = "app_key"
        private val KEY_CONSENT_FORM_DATA = "consent_form_data"


        private val KEY_TOKEN = "token"

    }

    init {
        this.mHashStorage = HashStorage(c, FILE_NAME)
    }

    fun clear() {
        this.mHashStorage.clear()
    }

    fun hasToken(): Boolean {
        return !TextUtils.isEmpty(token)
    }

    // 清除所有与登陆相关的信息
    fun removeLoginInfo() {

        this.mHashStorage.remove(KEY_TOKEN)
    }

    fun setData(data: Any, type: Class<*>, key: String) {
        val gson = Gson()
        val json = gson.toJson(data, type)
        this.mHashStorage.put(key, json)
    }

    fun <T> getData(type: Class<out T>, key: String): T {
        val gson = Gson()
        return gson.fromJson<T>(this.mHashStorage.getString(key), type)
    }

    var agreeFlag: Boolean
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
        set(paramString) = this.mHashStorage.put(KEY_APP_KEY, paramString)


    var consentDataInfo: ConsentDetailInfo.ResultInfo
        get() = this.getData(ConsentDetailInfo.ResultInfo::class.java, KEY_CONSENT_FORM_DATA)
        set(info) = this.setData(info, ConsentDetailInfo.ResultInfo::class.java, KEY_CONSENT_FORM_DATA)

    var token: String
        get() = this.mHashStorage.getString(KEY_TOKEN)
        set(paramString) = this.mHashStorage.put(KEY_TOKEN, paramString)











}

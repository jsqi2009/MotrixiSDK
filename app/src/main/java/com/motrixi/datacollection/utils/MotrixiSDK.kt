package com.motrixi.datacollection.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import com.motrixi.datacollection.DataCollectionActivity
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.listener.OnAppkeyListener
import com.motrixi.datacollection.listener.OnLogListener
import com.motrixi.datacollection.network.HttpClient
import com.motrixi.datacollection.network.ManifestMetaReader
import com.motrixi.datacollection.network.event.UploadDataResponseEvent
import com.motrixi.datacollection.network.models.LogInfo
import com.motrixi.datacollection.service.MotrixiService
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


/**
 * author : Jason
 *  date   : 2020/10/21 2:20 PM
 *  desc   :
 */
object MotrixiSDK {

    var mSession: Session? = null
    //private var onAppkeyListener: OnAppkeyListener? = null

    fun init(context: Context){

        HttpClient.init(context)
        mSession = Session(context)

        startService(context)
        //init ad id
        getAdvertisingId(context)

        fetchAppkey(context)
    }

    fun init(context: Context, appKey: String){

        HttpClient.init(context)
        mSession = Session(context)

        //mSession!!.appKey = appKey
        Contants.APP_KEY = appKey
        startService(context)
        //init ad id
        getAdvertisingId(context)

        verifyAppkey(context, appKey)
    }


    /**
     * set listener
     */
    fun setOnLogListener(listener: OnLogListener) {
        Contants.onLogListener = listener
    }

    /**
     * start the foreground service
     */
    private fun startService(context: Context) {
        val startService = Intent(context, MotrixiService::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(startService)
            UploadLogUtil.uploadLogData(context, "startForegroundService ")
        } else {
            context.startService(startService)
            UploadLogUtil.uploadLogData(context, "startService")
        }
    }

    /**
     * verify the app key
     */
    private fun fetchAppkey(context: Context) {
        var key = getAppkey(context)
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(context, "Please configure app key", Toast.LENGTH_SHORT).show()
            Log.d("app_key","null")
        } else {
            verifyAppkey(context, key)
//            verifyAppkey(context, "123")
        }
    }

    private fun getAppkey(context: Context): String? {

        try {
            val value = ManifestMetaReader.getMetaValue(context, "MOTRIXI_SDK_APPID")
            return value
        } catch (e: Exception) {
            return ""
        }
    }

    /**
     * verify the app key via API
     */
    private fun verifyAppkey(context: Context, key: String?) {

        Log.d("app_key", key!!)
        var call = HttpClient.verifyAppkey(context, key)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {
                Log.d("verify key", "failure")
            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful) {
                    Log.d("verify success", response.body().toString())
                    var responseObject:JSONObject = JSONObject(response.body().toString())
                    var resultObject:JSONObject = responseObject.optJSONObject("result")
                    var appID = resultObject.optString("id")
//                    Contants.APP_ID = appID
                    mSession!!.appID = appID
                    Log.d("app_id", appID)

                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(MessageUtil.logMessage(Contants.APP_KEY_CODE, true, responseObject.optString("message")))
                    }
                    checkIsAgree(context)
                } else {
                    var error = JSONObject(response.errorBody()!!.string())
                    Log.d("verify failure", error.optString("message"))
                    if (Contants.onLogListener != null) {
                        Contants.onLogListener!!.onLogListener(MessageUtil.logMessage(Contants.APP_KEY_CODE, false, error.optString("message")))
                    }
                }
            }
        })
    }

    /**
     * get the AdvertisingId
     */
    private fun getAdvertisingId(context: Context) {
        try {
            Executors.newSingleThreadExecutor().execute(object : Runnable{
                override fun run() {
                    val googleId: String = AdvertisingIdUtil.getGoogleAdId(context.applicationContext)!!
                    Log.d("google Id:", googleId)
                    Contants.advertisingID = googleId
                }
            })
        } catch (e: Exception) {
        }
    }

    private fun checkIsAgree(context: Context) {

        try {
            var flag = mSession!!.agreeFlag
            if (!flag) {
                val intent: Intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.setClass(context, DataCollectionActivity::class.java)
                context.startActivity(intent)
            } else {
                Log.d("is agree:", "already agree")
                //UploadCollectedData.formatData(context)
            }
        } catch (e: Exception) {
        }

    }

    /**
     * reset the consent form data
     */
    fun resetConsentForm(mContext: Context) {
        UploadLogUtil.uploadLogData(mContext, "call reset consent form API")

        val intent: Intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setClass(mContext, DataCollectionActivity::class.java)
        mContext.startActivity(intent)
    }



}
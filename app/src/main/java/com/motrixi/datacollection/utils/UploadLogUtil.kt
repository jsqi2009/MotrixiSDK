package com.motrixi.datacollection.utils

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.gson.JsonObject
import com.motrixi.datacollection.content.Contants
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.network.HttpClient
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

/**
 * author : Jason
 *  date   : 2020/11/18 1:23 PM
 *  desc   :
 */
object UploadLogUtil {

    fun uploadLogData(context: Context, value: String) {

        var value = MessageUtil.uploadLog(value)
        var appKey = Contants.APP_KEY
        var androidID = Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        var call = HttpClient.uploadLog(context, value, appKey, androidID)
        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: retrofit2.Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: retrofit2.Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }
}
package com.motrixi.datacollection.utils

import org.json.JSONObject

/**
 * author : Jason
 *  date   : 2020/11/18 12:53 PM
 *  desc   :
 */
object MessageUtil {

    fun logMessage(type: Int, success: Boolean, msg: String): String {

        try {
            var jsonObject = JSONObject()
            jsonObject.put("type", type)
            jsonObject.put("success", success)
            jsonObject.put("info", msg)

            return jsonObject.toString()
        } catch (e: Exception) {
            return ""
        }
    }

    fun uploadLog(msg: String): String {

        try {
            var jsonObject = JSONObject()
            jsonObject.put("info", msg)

//            return jsonObject.toString()
            return msg
        } catch (e: Exception) {
            return ""
        }
    }
}
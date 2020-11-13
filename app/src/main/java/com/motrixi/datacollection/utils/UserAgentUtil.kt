package com.motrixi.datacollection.utils

import android.content.Context

/**
 * author : Jason
 *  date   : 2020/10/20 6:08 PM
 *  desc   :
 */
object UserAgentUtil {

    fun getUserAgent(context: Context): String {

        try {
            //        val userAgent = WebView(context).settings.userAgentString
            val userAgent = System.getProperty("http.agent")
            return userAgent
        } catch (e: Exception) {
            return ""
        }
    }
}
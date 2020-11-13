package com.motrixi.datacollection.utils

import android.content.Context
import android.telephony.TelephonyManager
import android.text.TextUtils


/**
 * author : Jason
 *  date   : 2020/10/13 6:47 PM
 *  desc   :
 */
object SimInfoUtil {


    private fun getSimInfo(context: Context): String{
        val iPhoneManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return iPhoneManager.simOperator
    }

    fun getMCC(context: Context): String {

        val simInfo = getSimInfo(context)
        if (!TextUtils.isEmpty(simInfo)) {
            return simInfo.substring(0, 3)
        } else {
            return ""
        }

    }

    fun getMNC(context: Context): String {

        val simInfo = getSimInfo(context)
        if (!TextUtils.isEmpty(simInfo)) {
            return simInfo.substring(3 ,simInfo.length)
        } else {
            return ""
        }


    }
}
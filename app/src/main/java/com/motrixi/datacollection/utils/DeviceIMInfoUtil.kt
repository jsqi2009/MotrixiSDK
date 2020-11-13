package com.motrixi.datacollection.utils

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager


/**
 * author : Jason
 *  date   : 2020/10/13 5:45 PM
 *  desc   : get device IMEI， need READ_PHONE_STATE permission
 */
object DeviceIMInfoUtil {


    /**
     * fetch device IMEI
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getDeviceIMEI(context: Context): String {

        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var imei = telephonyManager.deviceId
            if (imei == null) {
                imei = ""
            }
            return imei
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * fetch device IMSI
     */
    @SuppressLint("MissingPermission")
    fun getDeviceIMSI(context: Context): String? {

        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            var imsi = telephonyManager.subscriberId
            if (null == imsi) {
                imsi = ""
            }
            return imsi
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}
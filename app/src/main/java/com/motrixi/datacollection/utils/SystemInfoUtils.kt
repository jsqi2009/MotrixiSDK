package com.motrixi.datacollection.utils

import android.content.Context
import android.os.Build
import android.util.Log
import java.util.*


/**
 * author : Jason
 *  date   : 2020/10/14 9:32 PM
 *  desc   :
 */
object SystemInfoUtils {

    /**
     * 获取设备宽度（px）
     *
     */
    fun getDeviceWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取设备高度（px）
     */
    fun getDeviceHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取厂商名
     */
    fun getDeviceManufacturer(): String? {
        return Build.MANUFACTURER
    }

    /**
     * 获取产品名
     */
    fun getDeviceProduct(): String? {
        return Build.PRODUCT
    }

    /**
     * 获取手机品牌
     */
    fun getDeviceBrand(): String? {
        return Build.BRAND
    }

    /**
     * 获取手机型号
     */
    fun getDeviceModel(): String? {
        return Build.MODEL
    }

    /**
     * 获取手机主板名
     */
    fun getDeviceBoard(): String? {
        return Build.BOARD
    }

    /**
     * 设备名
     */
    fun getDeviceDevice(): String? {
        return Build.DEVICE
    }

    /**
     *
     *
     * fingerprit 信息
     */
    fun getDeviceFubgerprint(): String? {
        return Build.FINGERPRINT
    }

    /**
     * 硬件名
     *
     */
    fun getDeviceHardware(): String? {
        return Build.HARDWARE
    }

    /**
     * 主机
     *
     */
    fun getDeviceHost(): String? {
        return Build.HOST
    }

    /**
     *
     * 显示ID
     */
    fun getDeviceDisplay(): String? {
        return Build.DISPLAY
    }

    /**
     * ID
     *
     */
    fun getDeviceId(): String? {
        return Build.ID
    }

    /**
     * 获取手机用户名
     *
     */
    fun getDeviceUser(): String? {
        return Build.USER
    }

    /**
     * 获取手机 硬件序列号
     */
    fun getDeviceSerial(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Build.getSerial()
        } else {
            return Build.SERIAL
        }
    }

    /**
     * 获取手机Android 系统SDK
     *
     * @return
     */
    fun getDeviceSDK(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * 获取手机Android 版本
     *
     * @return
     */
    fun getDeviceAndroidVersion(): String? {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     */
    fun getDeviceSupportLanguage(): String {
        Log.e("language", "Local:" + Locale.GERMAN)
        Log.e("language", "Local:" + Locale.ENGLISH)
        Log.e("language", "Local:" + Locale.US)
        Log.e("language", "Local:" + Locale.CHINESE)
        Log.e("language", "Local:" + Locale.TAIWAN)
        Log.e("language", "Local:" + Locale.FRANCE)
        Log.e("language", "Local:" + Locale.FRENCH)
        Log.e("language", "Local:" + Locale.GERMANY)
        Log.e("language", "Local:" + Locale.ITALIAN)
        Log.e("language", "Local:" + Locale.JAPAN)
        Log.e("language", "Local:" + Locale.JAPANESE)
        return Locale.getAvailableLocales().toString()
    }

    fun getDeviceAllInfo(context: Context): String? {
        return "" +
                "\n1. Device Width:\n\t\t" + getDeviceWidth(context) +
                "\n\n2. Device Height:\n\t\t" + getDeviceHeight(context) +
                "\n\n3. Device Model:\n\t\t" + android.os.Build.MODEL +
                "\n\n4. Manufacture:\n\t\t" + android.os.Build.MANUFACTURER +
                "\n\n5. Android Version:\n\t\t" + android.os.Build.VERSION.RELEASE +
                "\n\n6. Android SDK Version:\n\t\t" + android.os.Build.VERSION.SDK_INT +
                "\n\n7. Product:\n\t\t" + android.os.Build.PRODUCT +
                "\n\n8. ID:\n\t\t" + android.os.Build.ID +
                "\n\n9. Display ID:\n\t\t" + android.os.Build.DISPLAY +
                "\n\n10. Hardware:\n\t\t" + android.os.Build.HARDWARE +
                "\n\n11. Device Name:\n\t\t" + android.os.Build.DEVICE +
                "\n\n12. Bootloader:\n\t\t" + android.os.Build.BOOTLOADER +
                "\n\n13. Board:\n\t\t" + android.os.Build.BOARD +
                "\n\n14. CodeName:\n\t\t" + android.os.Build.VERSION.CODENAME


    }

}
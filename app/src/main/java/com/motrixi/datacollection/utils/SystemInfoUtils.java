package com.motrixi.datacollection.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.util.Freezable;
import android.os.Build;
import android.util.Log;

import com.adobe.fre.FREContext;

import java.util.Locale;

/**
 * author : Jason
 * date   : 2020/12/8 1:29 PM
 * desc   :
 */
public class SystemInfoUtils {

    /**
     * 获取设备宽度（px）
     *
     */
    public static int getDeviceWidth(FREContext context){
        return context.getActivity().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     */
    public static int getDeviceHeight(FREContext context){
        return context.getActivity().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getDeviceHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取厂商名
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取产品名
     */
    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机品牌
     */
    public static String getDeviceBrand(){
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * 获取手机主板名
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }

    /**
     * 设备名
     */
    public static String getDeviceDevice(){
        return Build.DEVICE;
    }

    /**
     *
     *
     * fingerprit 信息
     */
    public static String getDeviceFubgerprint(){
        return Build.FINGERPRINT;
    }

    /**
     * 硬件名
     *
     */
    public static String getDeviceHardware() {
        return Build.HARDWARE;
    }

    /**
     * 主机
     *
     */
    public static String getDeviceHost() {
        return Build.HOST;
    }

    /**
     *
     * 显示ID
     */
    public static String getDeviceDisplay(){
        return Build.DISPLAY;
    }

    /**
     * ID
     *
     */
    public static String getDeviceId(){
        return Build.ID;
    }

    /**
     * 获取手机用户名
     *
     */
    public static String getDeviceUser() {
        return Build.USER;
    }

    /**
     * 获取手机 硬件序列号
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceSerial(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }

    /**
     * 获取手机Android 系统SDK
     *
     * @return
     */
    public static int getDeviceSDK(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本
     *
     * @return
     */
    public static String getDeviceAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     */
    public static String getDeviceSupportLanguage(){
        Log.e("language", "Local:" + Locale.GERMAN);
        Log.e("language", "Local:" + Locale.ENGLISH);
        Log.e("language", "Local:" + Locale.US);
        Log.e("language", "Local:" + Locale.CHINESE);
        return Locale.getAvailableLocales().toString();
    }

    public static String getDeviceAllInfo(FREContext context){
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
                "\n\n14. CodeName:\n\t\t" + android.os.Build.VERSION.CODENAME;


    }

    public static String getDeviceAllInfo(Context context){
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
                "\n\n14. CodeName:\n\t\t" + android.os.Build.VERSION.CODENAME;


    }
}

package com.motrixi.datacollection.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * author : Jason
 * date   : 2020/11/10 4:45 PM
 * desc   :
 */
object NetworkUtil {
    /**
     *
     * @return true:connect  false:unconnect
     */
    fun iConnected(context: Context): Boolean {
        try {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                    if (networkCapabilities != null) {
                        return (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                    }
                } else {
                    val networkInfo = manager.activeNetworkInfo
                    return networkInfo != null && networkInfo.isConnected
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * if Wifi connect
     *
     * @return true:已连接 false:未连接
     */
    fun isWifiConnected(context: Context): Boolean {
        try {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                    if (networkCapabilities != null) {
                        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    }
                } else {
                    val networkInfo = manager.activeNetworkInfo
                    return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_WIFI
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * if Cell connect
     */
    fun isMobileData(context: Context): Boolean {
        try {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (manager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val networkCapabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                    if (networkCapabilities != null) {
                        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    }
                } else {
                    val networkInfo = manager.activeNetworkInfo
                    return networkInfo != null && networkInfo.isConnected && networkInfo.type == ConnectivityManager.TYPE_MOBILE
                }
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }
}
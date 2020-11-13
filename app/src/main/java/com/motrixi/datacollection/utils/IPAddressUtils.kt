package com.motrixi.datacollection.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.util.Log
import java.net.*


/**
 * author : Jason
 * date   : 2020/10/15 10:05 PM
 * desc   :
 */
object IPAddressUtils {

    fun getCurrentIPAddress(context: Context): String? {
        try {
            var ipAddress: String? = null
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val mobileNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (mobileNetworkInfo != null && mobileNetworkInfo!!.isConnected) {
                ipAddress = currentIpAddress
            } else if (wifiNetworkInfo!!.isConnected) {
                val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val address = wifiInfo.ipAddress
                ipAddress = intToStringIP(address)

    //            ipAddress = getOutNetIP(context)
            }
            return ipAddress
        } catch (e: Exception) {
            return ""
        }
    }

    private val currentIpAddress: String?
        get() {
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val net = en.nextElement()
                    val enumIpAddr = net.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            } catch (ex: SocketException) {
                Log.e("local IP", ex.toString())
            }
            return null
        }

    private fun intToStringIP(ip: Int): String {
        val sb = StringBuilder()
        sb.append(ip and 0xFF).append(".")
        sb.append(ip shr 8 and 0xFF).append(".")
        sb.append(ip shr 16 and 0xFF).append(".")
        sb.append(ip shr 24 and 0xFF)

        return sb.toString()
    }

}
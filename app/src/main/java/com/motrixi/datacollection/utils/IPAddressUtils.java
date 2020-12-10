package com.motrixi.datacollection.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.adobe.fre.FREContext;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * author : Jason
 * date   : 2020/12/8 3:09 PM
 * desc   :
 */
public class IPAddressUtils {

    public static String getCurrentIPAddress(FREContext context) {
        try {
            String ipAddress = "";
            NetworkInfo info = ((ConnectivityManager) context.getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

                    ipAddress = currentIpAddress();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI){
                    WifiManager wifiManager = (WifiManager) context.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                }
            }
            return ipAddress;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getCurrentIPAddress(Context context) {
        try {
            String ipAddress = "";
            NetworkInfo info = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

                    ipAddress = currentIpAddress();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI){
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                }
            }
            return ipAddress;
        } catch (Exception e) {
            return "";
        }
    }

    private static String currentIpAddress(){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

}

package com.motrixi.datacollection.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager


/**
 * author : Jason
 *  date   : 2020/10/13 5:45 PM
 *  desc   : get device IMEI
 */
object InstalledPackagesUtil {


    fun getInstalledPackageList(context: Context): List<String>? {
        val packages: MutableList<String> = ArrayList()
        try {
            val packageInfos: List<PackageInfo> = context.packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES)
            for (info in packageInfos) {

                //非系统应用
                if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM === 0) {
                    val pkg = info.packageName
                    val appName = info.applicationInfo.loadLabel(context.packageManager)
                    //packages.add(appName as String)
                    packages.add(info.packageName)   //包名
                }
                //系统应用
                else {

                }

            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return packages
    }
}
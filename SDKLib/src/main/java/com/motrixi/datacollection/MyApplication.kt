package com.motrixi.datacollection

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

/**
 * author : Jason
 *  date   : 2020/11/4 3:22 PM
 *  desc   :
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CrashReport.initCrashReport(applicationContext, "ba4c00eff4", false)
    }
}
package com.motrixi.datacollection.content

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

import java.util.Stack



class AppManager private constructor() {

    /**
     * add Activity
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * get current Activity
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * finish current Activity
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * finish Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * finish Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }


    /**
     * finish all Activity
     */
    fun finishAllActivity() {
        try {
            var i = 0
            val size = activityStack!!.size
            while (i < size) {
                if (null != activityStack!![i]) {
                    activityStack!![i].finish()
                }
                i++
            }
            activityStack!!.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * exit app
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
        }

    }

    companion object {
        private var activityStack: Stack<Activity>? = null
        private var instance: AppManager? = null

        /**
         * single
         */
        val appManager: AppManager
            get() {
                if (instance == null) {
                    instance = AppManager()
                }
                return instance!!
            }
    }
}
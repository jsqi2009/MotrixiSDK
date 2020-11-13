package com.motrixi.datacollection.content

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

import java.util.Stack

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 可以让所有的activity都继承BaseActivity然后给activity在onCreate中添加到栈中onDetroyed中移除
 *
 *
 * 添加Activity到堆栈
 * AppManager.getAppManager().addActivity(this);
 * 结束Activity&从堆栈中移除
 * AppManager.getAppManager().finishActivity(this);
 */

class AppManager private constructor() {

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
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
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }


    /**
     * 结束所有Activity
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
     * 退出应用程序
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
         * 单一实例
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
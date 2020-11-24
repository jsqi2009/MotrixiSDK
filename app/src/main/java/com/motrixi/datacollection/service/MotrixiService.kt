package com.motrixi.datacollection.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.util.Log
import com.motrixi.datacollection.content.Session
import com.motrixi.datacollection.utils.UploadCollectedData
import com.motrixi.datacollection.utils.UploadLogUtil
import org.apache.commons.lang3.concurrent.BasicThreadFactory
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * author : Jason
 *  date   : 2020/11/3 4:48 PM
 *  desc   :
 */
class MotrixiService: Service() {

    private var startUploadExecutor: ScheduledExecutorService? = null
    private val CHANNEL_ID = "motrixi_sdk_channel"
    private val CHANNEL_NAME = "motrixi_sdk_foreground_service"
    private var id = 100
    private var mSession: Session? = null
//    private val TIME_VALUE: Long = 24*60*60*1000
    private val TIME_VALUE: Long = 3*60*1000


    override fun onCreate() {
        super.onCreate()

        Log.e("oncreate","create service")
        mSession = Session(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForeground()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*var builder: Notification.Builder = Notification.Builder(this.applicationContext)
        var notification: Notification = builder.build()

        startForeground(100, notification)*/

        startUploading()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setForeground() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(channel)
        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            //.setContentTitle("收到一条重要通知")
            //.setContentText("这是重要通知")
            //.setSmallIcon(R.mipmap.ic_launcher)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .build()
        startForeground(id, notification)
        Log.e("action","startForeground service")
    }

    /**
     * start upload the data
     */
    private fun startUploading() {

        try {
            if (startUploadExecutor == null) {
                startUploadExecutor = ScheduledThreadPoolExecutor(1,
                    BasicThreadFactory.Builder().namingPattern("start-thread").daemon(true).build())
            }
            startUploadExecutor!!.scheduleAtFixedRate(object : Runnable{
                override fun run() {
                    var currentTime = Date().time
                    var lastTime = mSession!!.syncTime
                    if (lastTime.toInt() == 0) {
                        Log.d("not confirm", "have not confirm")
                        //UploadLogUtil.uploadLogData(this@MotrixiService, "have not confirm")
                        return
                    }
                    if ((currentTime - lastTime) >= TIME_VALUE) {

                        Log.d("service", "start service")
                        UploadLogUtil.uploadLogData(this@MotrixiService, "upload data via service")
                        mSession!!.syncTime = Date().time
                        UploadCollectedData.formatData(applicationContext)
                    } else {
                        Log.d("current time", currentTime.toString())
                        Log.d("last time", lastTime.toString())

                        //UploadLogUtil.uploadLogData(this@MotrixiService, "current time$currentTime")
                    }
                }

            }, 0, 10*1000, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
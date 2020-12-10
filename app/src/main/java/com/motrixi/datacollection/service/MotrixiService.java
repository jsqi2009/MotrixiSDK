package com.motrixi.datacollection.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.motrixi.datacollection.content.Contants;
import com.motrixi.datacollection.content.Session;
import com.motrixi.datacollection.utils.UploadCollectedData;
import com.motrixi.datacollection.utils.UploadLogUtil;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.motrixi.datacollection.content.Contants.lastSyncTime;

/**
 * author : Jason
 * date   : 2020/12/8 4:08 PM
 * desc   :
 */
public class MotrixiService extends Service {

    private static ScheduledExecutorService startUploadExecutor;
    private static String CHANNEL_ID = "motrixi_sdk_channel";
    private static String CHANNEL_NAME = "motrixi_sdk_foreground_service";
    private int id = 100;
    //    private val TIME_VALUE: Long = 24*60*60*1000
    private static Long TIME_VALUE = Long.valueOf(10*60*1000);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("oncreate","create service");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForeground();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startUploading();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void setForeground() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                //.setContentTitle("收到一条重要通知")
                //.setContentText("这是重要通知")
                //.setSmallIcon(R.mipmap.ic_launcher)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build();
        startForeground(id, notification);
        Log.e("action","startForeground service");
    }

    /**
     * start upload the data
     */
    private void startUploading() {

        try {
            if (startUploadExecutor == null) {
                /*startUploadExecutor = ScheduledThreadPoolExecutor(1,
                    BasicThreadFactory.Builder().namingPattern("start-thread").daemon(true).build())*/
                startUploadExecutor = new ScheduledThreadPoolExecutor(1);
            }
            startUploadExecutor.scheduleAtFixedRate(new Runnable(){
                @Override
                public void run() {
                    long currentTime = new Date().getTime();
                    long lastTime = lastSyncTime;
                    if (lastTime == 0) {
                        Log.d("not confirm", "have not confirm");
                        return;
                    }
                    if ((currentTime - lastTime) >= TIME_VALUE) {

                        Log.d("service", "start service");
                        //UploadLogUtil.uploadLogData(Contants.mFREContext, "uploading data");
                        lastSyncTime = new Date().getTime();
                        //mSession.setSyncTime(new Date().getTime());
                        if (Contants.mFREContext != null) {
                            UploadCollectedData.formatData(getApplicationContext());
                        } else {
                            UploadCollectedData.formatData(getApplicationContext());
                        }

                    } else {
                        Log.d("current time", String.valueOf(currentTime));
                        Log.d("last time", String.valueOf(lastTime));

                    }
                }

            }, 0, 10*1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
        }
    }
}

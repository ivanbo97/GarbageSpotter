package com.example.garbagespotter;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;

public class UploadingService extends Service {

    private File[] gpxFiles;
    private File[] images;
    public static final String CHANNEL_ID = "UploadingServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        Intent notificationIntent = new Intent (this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Uploading Service")
                .setContentText("File is uploading!")
                .setTicker("Processing!")
                .setContentIntent(pendingIntent)
                .build();
        startForeground(10,notification);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        gpxFiles = (File [])intent.getExtras().get("gpxFiles");
        images = (File [])intent.getExtras().get("images");
        Thread uploadingThread = new Thread (new FileUploadRunnable(gpxFiles, images,UploadingService.this,getApplicationContext()));
        uploadingThread.start();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel  = new NotificationChannel(CHANNEL_ID,"Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}

package com.science09.apple.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";

    private MediaPlayer mPlayer;

    public MyService() {
    }

    //其他对象通过bindService方法通知该Service时该方法会被调用
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        Log.d(TAG, "MyService onBind()");
        mPlayer.start();
        return null;
    }

    // 其他对象通过unbindService方法通知该Service时该方法会被调用
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "MyService onUnBind()");
        mPlayer.stop();
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() ===");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        //super.onCreate();
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wangyuan);
        mPlayer.setLooping(true);
        Log.d(TAG, "MyService onCreate()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        mPlayer.start();
        Log.d(TAG, "MyService onStart()");
    }

    @Override
    public void onDestroy() {
        mPlayer.stop();
        Log.d(TAG, "MyService onDestroy");
    }
}

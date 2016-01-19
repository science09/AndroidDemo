package com.science09.apple.servicedemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {
    private static final String TAG = "TimeService";
    private Timer timer = null;
    private SimpleDateFormat sdf = null;
    private Intent timeIntent = null;
    private Bundle bundle = null;

    public TimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "TimeService->onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "TimeService->OnCreate");
        init();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendTimeChangeBroadcast();
            }
        }, 1000, 1000);
    }

    private void init(){
        timer = new Timer();
        sdf = new SimpleDateFormat("yyyy年MM月dd日 "+"hh:mm:ss");
        timeIntent = new Intent();
        bundle = new Bundle();
    }

    private void sendTimeChangeBroadcast(){
        bundle.putString("time", getTime());
        timeIntent.putExtras(bundle);
        timeIntent.setAction(MainActivity.TIME_CHANGED_ACTION);
        sendBroadcast(timeIntent);
    }

    private String getTime(){
        return sdf.format(new Date());
    }

    @Override
    public ComponentName startService(Intent service) {
        Log.d(TAG, "TimeService->startService");
        return super.startService(service);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TimeService->onDestroy");
    }
}

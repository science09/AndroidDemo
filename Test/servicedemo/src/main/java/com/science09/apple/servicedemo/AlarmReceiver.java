package com.science09.apple.servicedemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by apple on 16/1/19.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "======== "+intent.getAction().toString());
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}

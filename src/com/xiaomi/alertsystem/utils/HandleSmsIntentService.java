package com.xiaomi.alertsystem.utils;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.xiaomi.alertsystem.data.Constants;


public class HandleSmsIntentService extends Service {

    public static final String TAG = "HandleSmsIntentService";
    public static final String StringKey = "sms";
    private SmsBroadcastReceiver mSmsBroadcastReceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        Log.d(TAG, "service created");
        super.onCreate();
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Constants.SMS_RECEIVED);
        //=================设置优先级别===================
        mIntentFilter.setPriority(2147483647);  
        registerReceiver(mSmsBroadcastReceiver, mIntentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "service destroy");
        super.onDestroy();
        if(mSmsBroadcastReceiver != null){
            unregisterReceiver(mSmsBroadcastReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

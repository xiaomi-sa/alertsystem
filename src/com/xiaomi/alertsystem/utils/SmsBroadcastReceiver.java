package com.xiaomi.alertsystem.utils;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.xiaomi.alertsystem.data.AlertManager;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.data.NotifyMeta;
import com.xiaomi.alertsystem.ui.MainActivity;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcastReceiver";
    private static final String strACT = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "sms intent received:" + intent.getAction());
        if (intent.getAction().equals(strACT)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                StringBuilder sb = new StringBuilder();
                String address = null;
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msg = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                for (SmsMessage currMsg : msg) {
                    address = currMsg.getDisplayOriginatingAddress();
                    sb.append(currMsg.getDisplayMessageBody());
                }
                Log.d(TAG, "=================================");
                Log.d(TAG, "====sms:"+sb.toString());
                Log.d(TAG, address);
                Log.d(TAG, "=================================");
                
                List<String> phoneList = SmsManager.getAllPhone();


                if (phoneList.contains(address) || AlertManager.matchAlert(sb.toString())){
                    List<String> list = AlertManager.getAlertManager().parse(
                            sb.toString());
                    AlertMeta alertMeta = AlertManager.getAlertManager().format(list);
                    if (alertMeta != null) {
                        alertMeta.mMsg = sb.toString();
                        SmsManager.insertAlertSms(alertMeta);
                        doNotifyMsg(context, alertMeta, sb.toString());
                        //只有在拦截列表中，并且分解成功了，才abort
                		int mSmsUnReadNum = SmsManager.getAlertUnReadNum();
                		CommonUtils.sendIconCountMessage(context, mSmsUnReadNum);
                        this.abortBroadcast();
                    }
                }

            }
        }
    }

    private void doNotifyMsg(Context c, AlertMeta meta, String sb) {
        if (TextUtils.equals(AlertMeta.ALERT_LEVEL_P0, meta.mAlertLevel)) {
            checkPrefence(c, AlertMeta.ALERT_LEVEL_P0, sb);
        } else if (TextUtils.equals(AlertMeta.ALERT_LEVEL_P1, meta.mAlertLevel)) {
            checkPrefence(c, AlertMeta.ALERT_LEVEL_P1, sb);
        } else if (TextUtils.equals(AlertMeta.ALERT_LEVEL_P2, meta.mAlertLevel)) {
            checkPrefence(c, AlertMeta.ALERT_LEVEL_P2, sb);
        } else if (TextUtils.equals(AlertMeta.ALERT_LEVEL_P3, meta.mAlertLevel)) {
            checkPrefence(c, AlertMeta.ALERT_LEVEL_P3, sb);
        } else {
            final Intent intent = new Intent(c, MainActivity.class);
            NotificationUtils.doNotify(intent, c, sb, null, sb, true, true, 0, 0, 0);
        }
    }
    
    private int getPageIndex(String key){
    	int index = 0;
    	
    	for(int i = 0; i< Constants.NOTIFICATION_LEVEL.size(); i++){
    		if(key!=null && key.equals(Constants.NOTIFICATION_LEVEL.get(i)))
    			index = i;
    	}
    	return index;
    }

    private void checkPrefence(final Context c, final String key,
                               final String sb) {
        NotifyMeta meta = new NotifyMeta(key, c);
        int type = meta.getValue();

        boolean sound = false;
        boolean vibrate = false;
        boolean bubble = false;

        if (type == Constants.NOTIFICATION_SOUND_VIBRATE) {
            sound = true;
            vibrate = true;
        } else if (type == Constants.NOTIFICATION_SOUND) {
            sound = true;
        } else if (type == Constants.NOTIFICATION_VIBRATE) {
            vibrate = true;
        } else if (type == Constants.NOTIFICATION_BUBBLE) {
            bubble = true;
        }
        
        //设置跳转页
        Intent intent = new Intent(c, MainActivity.class);
        Bundle bundle = new Bundle();
        int index = getPageIndex(key);
		bundle.putInt("PageIndex", index);
		intent.putExtras(bundle);
		
        if (!bubble) {
            NotificationUtils.doNotify(intent, c,
                    sb, null, sb, sound, vibrate, 0, 0, 0);
        } else {
            NotificationUtils.doNotify(intent, c, sb, null, sb, false, false, 0,
                    0, 0);
        }
    }
}

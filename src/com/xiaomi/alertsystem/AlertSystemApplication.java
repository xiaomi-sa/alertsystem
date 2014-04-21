/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.xiaomi.alertsystem.data.AlertManager;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.utils.HandleSmsIntentService;
import com.xiaomi.alertsystem.utils.SmsManager;

public class AlertSystemApplication extends Application {
    public static final String TAG = "AlertSystemApplication";
	private static AlertSystemApplication sAlertSystemApplication;

	public static AlertSystemApplication getApp() {
		return sAlertSystemApplication;
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		super.onCreate();

		sAlertSystemApplication = (AlertSystemApplication) this
				.getApplicationContext();
		SmsManager.init(getApplicationContext());
        startService();
	}

    public void startService(){
        Log.d(TAG, "start service");
        Intent i = new Intent(this, HandleSmsIntentService.class);
        startService(i);
    }

	public List<AlertMeta> parse(List<String> lines) {
		List<AlertMeta> alertMetas = new ArrayList<AlertMeta>();

		for (String str : lines) {
			List<String> sms = AlertManager.getAlertManager().parse(str);
			AlertMeta meta = AlertManager.getAlertManager().format(sms);
			alertMetas.add(meta);
		}

		return alertMetas;
	}
}

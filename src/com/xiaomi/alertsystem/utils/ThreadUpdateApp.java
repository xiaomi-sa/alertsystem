package com.xiaomi.alertsystem.utils;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;

import com.xiaomi.alertsystem.data.Constants;

public class ThreadUpdateApp extends Thread {
	private String mUrl;
	private Context mContext;
	private Handler mHandler;

	public ThreadUpdateApp(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;

	}

	public void run() {
		PackageInfo pInfo = null;
		try {
			pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String version = pInfo.versionName;
		String ret = CommonUtils.execUrl(Constants.URL_UPDATE + version);
		int stat = 0;
		String url = "";
		try {
			JSONObject json = new JSONObject(ret);
			url = json.getString("url");
			stat = json.getInt("stat");
			mUrl = url;
		} catch (JSONException e) {
			mHandler.sendEmptyMessage(Constants.MSG_VER_ERROR);
			e.printStackTrace();
			return;
		}

		if (stat == 1) {			
			mHandler.sendEmptyMessage(Constants.MSG_VER_UPDATE);
			String today = CommonUtils.getNowDate();
			String filePath = CommonUtils.getDefaultHome() + today + ".apk";
			CommonUtils.download(mUrl, filePath);
			Uri uri = Uri.fromFile(new File(filePath)); // 这里是APK路径
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		} else {
			mHandler.sendEmptyMessage(Constants.MSG_VER_NONEED);
		}
	}
}
/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.data;

import android.util.Log;

import com.xiaomi.alertsystem.AlertSystemApplication;
import com.xiaomi.alertsystem.data.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlertManager {
	public final static String TAG = "AlertManager";
	public final static char sPatternStart = '[';
	public final static char sPatternEnd = ']';
	public static AlertManager mAlertManager;

	private AlertManager() {

	}

	// 按照规则匹配
	public static boolean matchAlert(String sms) {
		if (sms.startsWith("[P") || sms.startsWith("【P"))
			return true;
		Pattern pattern = Pattern.compile(".*\\[P\\d\\].*||.*\\【P\\d\\】.*");
		Matcher matcher = pattern.matcher(sms);
		if (matcher != null && matcher.matches())
			return true;
		return false;
	}

	public static AlertManager getAlertManager() {
		if (mAlertManager == null) {
			mAlertManager = new AlertManager();
		}
		return mAlertManager;
	}

	private boolean isLevelField(String s) {
		Pattern pattern = Pattern.compile("p\\d");
		Matcher matcher = pattern.matcher(s);
		if (matcher != null && matcher.matches())
			return true;
		return false;
	}

	public List<String> parse(String msg) {
		List<String> list = new ArrayList<String>();

		// 首先尝试【】
		Pattern pattern = Pattern.compile("\\【(.*?)\\】");
		Matcher matcher = pattern.matcher(msg);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}
		// 然后尝试[]
		if (list.size() < 4) {
			list.clear();
			pattern = Pattern.compile("\\[(.*?)\\]");
			matcher = pattern.matcher(msg);
			while (matcher.find()) {
				list.add(matcher.group(1));
			}
		}

		// 长度小于4,显示ParseError
		if (list.size() < 4)
			return null;
		else
			return list;
	}

	public AlertMeta format(List<String> lst) {
		AlertMeta meta = null;
		if (lst == null || lst.size() == 0) {
			return meta;
		}

		try {
			meta = new AlertMeta();
			int levelIndex = 0;
			for (int i = 0; i < lst.size(); i++) {
				if (Constants.NOTIFICATION_LEVEL.contains(lst.get(i)
						.toUpperCase())) {
					levelIndex = i;
					break;
				}
			}
			// 如果从报警级别开始，字段不足4个，则退出
			if (lst.size() - levelIndex < 4) {
				Log.d(TAG, "sms parse  error!");
				return null;
			}
			Log.d(TAG,"sms parse  ok, level :" +  lst.get(levelIndex));
			
			//没有IP的情况，只有4个域
			if ((lst.size() - levelIndex) == 4) {
				meta.mAlertLevel = lst.get(levelIndex + 0).toUpperCase();
				meta.mFlag = lst.get(levelIndex + 1);
				meta.mMachineName = lst.get(levelIndex + 2);
				meta.mBody = lst.get(levelIndex + 3);
				return meta;
			}else{
				meta.mAlertLevel = lst.get(levelIndex + 0).toUpperCase();
				meta.mFlag = lst.get(levelIndex + 1);
				meta.mMachineName = lst.get(levelIndex + 2);
				meta.mMachineIP = lst.get(levelIndex + 3);
				meta.mBody = lst.get(levelIndex + 4);
				return meta;
			}
			

		} catch (IndexOutOfBoundsException e) {
			// TODO:return raw string
			Log.d(TAG, "invalid sms");
			return null;
		}
	}

	public boolean isIIegle(List<String> list) {
		if (Constants.NOTIFICATION_LEVEL.contains(list.get(0))) {
			return true;
		} else {
			return false;
		}
	}
}
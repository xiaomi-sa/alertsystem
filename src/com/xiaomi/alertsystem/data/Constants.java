/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.data;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
	public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	// broadcast: sms updated
	public static final String sActSmsInserted = "com.xiaomi.alertsystem.SMS_INSERTED";

	// intent: start activity thread
	public static final String sActSmsThread = "com.xiaomi.alertsystem.SMS_THREAD";
	public static final String TIELE_INTENT = "title_intent";
	public static final String KEY = "key";
	public static final String HOST = "host";
	public static final String URL_UPDATE = "http://xbox.pt.xiaomi.com/monitor/apk/alertsystem/update?ver=";
	public static final String APP_DIR = "alertsystem/";
	public static final String DEFAULT_DIR = "/sdcard/";


	// 报警方式
	public static final int NOTIFICATION_NONE = 0;
	public static final int NOTIFICATION_SOUND = 1;
	public static final int NOTIFICATION_VIBRATE = 2;
	public static final int NOTIFICATION_SOUND_VIBRATE = 3;
	public static final int NOTIFICATION_BUBBLE = 4;
	
	// 版本消息提示
	public static final int MSG_VER_ERROR = 1000;
	public static final int MSG_VER_UPDATE = 1001;
	public static final int MSG_VER_NONEED = 1002;

	public static final String[] NOTIFICATION = { "不提醒", "声音", "振动", "声音振动",
			"通知栏" };
	public static ArrayList<String> NOTIFICATION_LEVEL = new ArrayList(
			Arrays.asList("P", "P0", "P1", "P2", "P3"));

	// *******************拦截号码*********************
	public static ArrayList<String> DEFAULT_PHONE = new ArrayList(
			Arrays.asList("10657520300931689", "10690269222", "15011518472",
					"951312330315"));

	public static final String P0_SOUND_KEY = "pref_p0_notification";
	public static final String P1_SOUND_KEY = "pref_p1_notification";
	public static final String P2_SOUND_KEY = "pref_p2_notification";
	public static final String P3_SOUND_KEY = "pref_p3_notification";
	public static final String P4_SOUND_KEY = "pref_p4_notification";
	public static final String P5_SOUND_KEY = "pref_p5_notification";

	public static final String BROADCAST_TYPE = "com.xiaomi.alertsystem.broadcast_type";
	public static final String BROADCAST_SMS = "com.xiaomi.alertsystem.broadcast_sms";
	// 数据刷新方式
	public static final int DATA_REFREASH = 1;
	public static final int DATA_CHANGE = 2;
	public static final int DATA_REMOVE = 3;
	public static final int DATA_LIST_CHANGE = 4;

}
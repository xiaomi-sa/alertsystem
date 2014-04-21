/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.data;

import com.xiaomi.alertsystem.utils.CommonUtils;

import android.content.Context;

public class NotifyMeta {
			
	private String key;
	private String mPerfKey;
	private Context mContext;
	
	
	public NotifyMeta(String key, Context context) {
		super();
		this.key = key.toLowerCase();
		this.mContext = context;
		// 为了兼容老版本
		this.mPerfKey = "pref_"+ key.toLowerCase() +"_notification ";
	}

	public String getKey() {
		return key;
	}

	public String getName_cn() {
		return key.toUpperCase() + "提醒设置";
	}
	
	public String getName_en() {
		// 为了兼容老版本
		return mPerfKey;
	}
	
	public int getValue() {
		return CommonUtils.getSettingInt(mContext, mPerfKey, Constants.NOTIFICATION_VIBRATE);
	}
	
	public String getTextValue() {
		int index = getValue();
		return Constants.NOTIFICATION[index];
	}
	public void setValue(int value) {
		CommonUtils.setSettingInt(mContext, mPerfKey, value);
	}

}

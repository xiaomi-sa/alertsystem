/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(false);			
		MobclickAgent.onError(this);
		MobclickAgent.updateOnlineConfig(this);		
		
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	


}


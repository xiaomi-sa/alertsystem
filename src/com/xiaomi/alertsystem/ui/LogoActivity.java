/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;


import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

import com.xiaomi.alertsystem.R;

public class LogoActivity extends BaseActivity {

	private Handler handler;
	private ImageView mBackColor;
	private int[] colors = {R.color.xiaomiYellow,R.color.metroGreen, R.color.metroRed};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.logo);
		findViews();
		
		Random rnd = new Random();
		int color = colors[rnd.nextInt(colors.length)];
		
		/**************变换启动背景颜色****************/
		//mBackColor.setImageResource(color);

    	this.handler = new Handler() {
			public void handleMessage(Message msg) {
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case 0:
						Intent intent = new Intent(LogoActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						break;
					}
				}
				super.handleMessage(msg);
			}
		};
		
		handler.sendEmptyMessageDelayed(0, 1500);
	}


	private void findViews() {
		mBackColor = (ImageView) findViewById(R.id.backcolor);
	}
	
   
	
}

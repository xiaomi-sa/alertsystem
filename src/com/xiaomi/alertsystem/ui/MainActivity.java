/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.umeng.analytics.MobclickAgent;
import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertManager;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.utils.CommonUtils;
import com.xiaomi.alertsystem.utils.SmsManager;
import com.xiaomi.alertsystem.utils.ThreadUpdateApp;
import com.xiaomi.alertsystem.utils.SmsManager.DatabaseChangedListener;

public class MainActivity extends FragmentActivity implements
		DatabaseChangedListener {
	private static final String TAG = "MainActivity";
	private Context mContext = MainActivity.this;
	private PagerSlidingTabStrip mTabs;
	private ViewPager mPager;
	private MyPagerAdapter adapter;
	private View mSetting;
	private Handler handler;

	public static final String AB = "0123456789abcdefjghijklmnopqrstuvwxyz";
	public static List<String> level = Constants.NOTIFICATION_LEVEL;
	public static String flag = "OK";
	public static Random rnd = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.setDebugMode(false);			
		MobclickAgent.onError(this);
		MobclickAgent.updateOnlineConfig(this);	
		setContentView(R.layout.main);
	
		findViews();
		setListen();
		//new MyTask().start();
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			int index = bundle.getInt("PageIndex");
			mPager.setCurrentItem(index);
		}
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case Constants.MSG_VER_UPDATE:
						Toast.makeText(mContext, "发现新版本，版本更新中...", Toast.LENGTH_SHORT)
								.show();
						break;
					case Constants.MSG_VER_NONEED:
						break;
					case Constants.MSG_VER_ERROR:
						break;
					}
				}
				super.handleMessage(msg);
			}
		};
		
		String setkey = "updatetime";
		String today = CommonUtils.getNowDate();
		String dateStr = CommonUtils.getSettingString(mContext, setkey, "");
		if(dateStr != null && dateStr.equals(today)){
			Log.i(TAG, "今天无需更新");
		}else{
			ThreadUpdateApp t = new ThreadUpdateApp(mContext, handler);
			t.start();
			CommonUtils.setSettingString(mContext, setkey, today);
		}
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	public void findViews() {
		mSetting = (View) findViewById(R.id.setting);
		mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		mPager = (ViewPager) findViewById(R.id.pager);
	}

	private void setListen() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mTabs.setViewPager(mPager);
		mSetting.setOnClickListener(settingListener);
	}

	static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	static int mIndex = 0;
	static String mName = randomString(10);;

	static List<String> randomMsg() {
		List<String> list = new ArrayList<String>();
		list.add(level.get(rnd.nextInt(5)));
		list.add(flag);
		if (mIndex++ == 10) {
			mIndex = 0;
			mName = randomString(10);
		}
		String m = mName;
		String ip = "127.0.0.1";
		String body = m + " ok";
		list.add(m);
		list.add(ip);
		list.add(body);
		return list;
	}

	class MyTask extends Thread {

		public void run() {
			for (int i = 0; i < 1000; i++) {
				List<String> list = randomMsg();
				AlertMeta alertMeta = AlertManager.getAlertManager().format(
						list);
				alertMeta.mMsg = list.toString();
				SmsManager.insertAlertSms(alertMeta);
			}
		}
	}

	android.view.View.OnClickListener settingListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(mContext, SettingActivity.class);
			startActivity(intent);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private ArrayList<Fragment> mPages = new ArrayList<Fragment>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			Init();
		}

		private void Init() {

			for (int i = 0; i < level.size(); i++) {
				mPages.add(AlertLevelFragment.newInstance(level.get(i), mContext));
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if( level.get(position).equals("P"))
				return "所有";
			else
				return level.get(position) + "级";
		}

		@Override
		public int getCount() {
			return level.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mPages.get(position);
		}

	}

	@Override
	protected void onDestroy() {
		int mSmsUnReadNum = SmsManager.getAlertUnReadNum();
		CommonUtils.sendIconCountMessage(mContext, mSmsUnReadNum);
		super.onDestroy();
	}

	@Override
	public void notifyChanged(String tablename, AlertMeta meta) {
		// TODO Auto-generated method stub
		
	}
}

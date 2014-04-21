/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.SmsManager;

public class AlertThreadActivity extends BaseActivity {
	public static final String TAG = "AlertThreadActivity";
	public static final String TIELE_INTENT = "title_intent";
	public static final String KEY = "key";
	private ListView mListView;
	private String mLevel;
	private String mHost;
	private AlertThreadAdapter mAdapter;
	private TextView mTitleView;
	private ImageView mDeleteView;
	private Boolean mDeleteStatus;
	private List<AlertMeta> mThreadSmsList = new ArrayList<AlertMeta>();

	private BroadcastReceiver receiver = null;
	private View mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thread_activity_layout);
		
		findViews();
		setListen();

		// 获取主机所有报警
		mLevel = "P";//getIntent().getStringExtra(Constants.KEY);
		mHost = getIntent().getStringExtra(Constants.HOST);
		mTitleView.setText(mHost + "问题");
		mDeleteStatus = false;

		if (!TextUtils.isEmpty(mLevel) && !TextUtils.isEmpty(mHost)) {
			mThreadSmsList = SmsManager.queryAlertSmsByHost(
					SmsManager.sProjection, mHost, mLevel);
			mAdapter = new AlertThreadAdapter(this, mThreadSmsList, mLevel, mHost,
					deleteListener);
			mListView.setAdapter(mAdapter);
		}

		mListView.setOnItemClickListener(mItemListener);
		// 注册receiver
		//enableReceiver();
		
		//后台更新数据
		new Thread(){
			public void run(){
			for(AlertMeta sms: mThreadSmsList){
				if(sms.mStatus == 0){
					sms.mStatus = 1;
					//更新数据库
					SmsManager.updateStatus(sms);
				}
			}
			}
		}.start();
		
		//发送广播
	    Intent intent=new Intent(Constants.sActSmsInserted);
	    intent.putExtra(Constants.BROADCAST_TYPE, Constants.DATA_LIST_CHANGE);
        intent.putExtra(Constants.BROADCAST_SMS,   mHost);
		sendBroadcast(intent);	
	}
	

	private void setListen() {
		mDeleteView.setOnClickListener(trashListener);
		mBack.setOnClickListener(gobackListener);
	}


	private void findViews() {
		mListView = (ListView) findViewById(R.id.list);
		mTitleView = (TextView) findViewById(R.id.title);
		mDeleteView = (ImageView) findViewById(R.id.trash);
		mBack = findViewById(R.id.goback);
	}

	private OnClickListener gobackListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertThreadActivity.this.finish();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if(mDeleteStatus){
				mAdapter.setDelete(false);
				mAdapter.notifyDataSetChanged();
				mListView.setOnItemClickListener(mItemListener);
				mDeleteView.setImageResource(R.drawable.trash);
				mDeleteStatus = false;
				return true;
			}else{
				this.finish();
			}
				
		}
		return false;
	}


	private OnClickListener trashListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDeleteStatus == false) {
				mAdapter.setDelete(true);
				mAdapter.notifyDataSetChanged();
				mListView.setOnItemClickListener(null);
				mDeleteView.setImageResource(R.drawable.done);
				mDeleteStatus = true;
			} else {
				mAdapter.setDelete(false);
				mAdapter.notifyDataSetChanged();
				mListView.setOnItemClickListener(mItemListener);
				mDeleteView.setImageResource(R.drawable.trash);
				mDeleteStatus = false;
			}
		}
	};

	OnClickListener deleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int pos = (Integer) v.getTag();
			AlertMeta sms = mThreadSmsList.get(pos);
			// 删除数据库
			SmsManager.deleteOneSms(sms);
			// 删除列表
			mThreadSmsList.remove(pos);
			mAdapter.notifyDataSetChanged();
			//通知上层界面
		    Intent intent=new Intent(Constants.sActSmsInserted);
		    intent.putExtra(Constants.BROADCAST_TYPE, Constants.DATA_REMOVE);
	        intent.putExtra(Constants.BROADCAST_SMS, sms);
			sendBroadcast(intent);
		}
	};

	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unregisterReceiver(receiver);
	}

	OnItemClickListener mItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final Intent intent = new Intent();
			// 传递短信列表，子activity能够更新数据
			AlertContentActivity.mMetaList = mThreadSmsList;
			Bundle bundle = new Bundle();
			bundle.putInt("ID", position);
			intent.putExtras(bundle);
			intent.setClass(AlertThreadActivity.this,
					AlertContentActivity.class);
			startActivity(intent);
		}
	};

	public void enableReceiver() {
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				doReceive(intent);
			}
		};
		registerReceiver(receiver, makeReceiverFilter());
	}

	public IntentFilter makeReceiverFilter() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.sActSmsInserted);
		return filter;
	}

	public void doReceive(Intent intent) {
		mAdapter.notifyDataSetChanged();
		final String action = intent.getAction();
		Log.d(TAG, "receive broadcast:" + action);
		if (action.equals(Constants.sActSmsInserted)) {
			mAdapter.notifyDataSetChanged();
		}
	}
}

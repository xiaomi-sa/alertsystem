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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.SmsManager;

public class AlertLevelFragment extends Fragment implements OnItemClickListener {
	public static final String TAG = "AlertLevelFragment";
	public static final String TIELE_INTENT = "title_intent";
	public static final String KEY = "key";
	private ListView mListView;
	private AlertMainlAdapter mAdapter;
	private List<AlertMeta> mSms;
	private int mPageNum;
	private int mPageTotal;
	private String mLevel;
	private static Context mContext;
	public BroadcastReceiver receiver = null;


	public static AlertLevelFragment newInstance(String level, Context context) {
		AlertLevelFragment f = new AlertLevelFragment();
		mContext = context;
		Bundle b = new Bundle();
		b.putString(KEY, level);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLevel = getArguments().getString(KEY);
		Log.d(TAG, "Level:" + mLevel);

	}

	@Override
	public void onDestroy() {
		if(receiver != null)
			mContext.unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.level_activity_layout, null);

		mListView = (ListView) view.findViewById(R.id.list);

		// 初始化页面数目
		mPageTotal = SmsManager.getAlertPageNum(mLevel);
		mPageNum = 0;
		mSms = SmsManager.queryAlertSmsByPage(SmsManager.sProjection, mPageNum,
				mLevel);

		mAdapter = new AlertMainlAdapter(mContext, mSms, mLevel);
		mListView.setAdapter(mAdapter);

		// 注册receiver
		enableReceiver();
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollListener(pageScroll);
		return view;
	}

	public void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		mAdapter.notifyDataSetChanged();
	    MobclickAgent.onPageStart("AlertLevelFragment");

	}
	

	
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("AlertLevelFragment"); 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//Log.d(TAG, "click, put to intent:" + mSms.get(position));
		final Intent intent = new Intent(mContext, AlertThreadActivity.class);
		intent.putExtra(Constants.sActSmsThread, mSms.get(position));
		intent.putExtra(Constants.KEY, mLevel);
		intent.putExtra(Constants.HOST, mSms.get(position).mMachineName);
		startActivity(intent);
	}

	public AbsListView.OnScrollListener pageScroll = new OnScrollListener() {
		private int firstView = 0;
		private boolean finish = false;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (firstView > 0 && mPageNum < mPageTotal - 1) {
					mPageNum++;
					List<AlertMeta> list = SmsManager.queryAlertSmsByPage(
							SmsManager.sProjection, mPageNum, mLevel);
					mSms.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
//					if (!finish)
//						Toast.makeText(mContext, "还好，没有更多报警了",
//								Toast.LENGTH_SHORT).show();
//					finish = true;
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			firstView = firstVisibleItem;
//			System.out.println("firstVisibleItem: " + firstVisibleItem
//					+ " , visibleItemCount:" + visibleItemCount
//					+ " , totalItemCount:" + totalItemCount);

		}
	};


	public void enableReceiver() {
		receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				doReceive(intent);
			}
		};
		mContext.registerReceiver(receiver, makeReceiverFilter());
	}

	public IntentFilter makeReceiverFilter() {
		final IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.sActSmsInserted);
		return filter;
	}

	private void updateMetaList(AlertMeta meta) {
		// 遍历，并且修改
		for (AlertMeta elem : mSms) {
			if (elem.mId == meta.mId) {
				elem.mStatus = 1;
				break;
			}
		}
		//mAdapter.notifyDataSetChanged();
	}
	
	private void updateHostList(String host) {
		// 更新主机数据
		for (AlertMeta elem : mSms) {
			if (elem.mMachineName != null && elem.mMachineName.equals(host)) {
				elem.mStatus = 1;
			}
		}
		//mAdapter.notifyDataSetChanged();
	}
		
	private void removeMetaList(AlertMeta meta) {
		// 遍历，并且修改
		for (AlertMeta elem : mSms) {
			if (elem.mId == meta.mId) {
				//elem.mStatus = 1;
				mSms.remove(elem);
				break;
			}
		}
		//mAdapter.notifyDataSetChanged();
	}

	public void doReceive(Intent intent) {
		mAdapter.notifyDataSetChanged();
		final String action = intent.getAction();

		Log.d(TAG, "receive broadcast:" + action);
		if (action.equals(Constants.sActSmsInserted)) {
			int type = intent.getIntExtra(Constants.BROADCAST_TYPE, -1);
			Log.d(TAG, "broadcast action is smsinserted, notify, type:" + type);

			// 完全重新加载
			if (type == Constants.DATA_REFREASH) {
				mPageTotal = SmsManager.getAlertPageNum(mLevel);
				mPageNum = 0;
				List<AlertMeta> list = SmsManager.queryAlertSmsByPage(SmsManager.sProjection,
						mPageNum, mLevel);
				mSms.clear();
				mSms.addAll(list);
				mAdapter.notifyDataSetChanged();
			}

			// 部分数据更新
			if (type == Constants.DATA_CHANGE) {
				AlertMeta meta = (AlertMeta) intent
						.getParcelableExtra(Constants.BROADCAST_SMS);

				if(meta == null)
					return ;
				// 获取更新数据，更新后清除
				if (mLevel.equals("P")) {
					updateMetaList(meta);
				} else {
					if (meta.mAlertLevel.equals(mLevel)) 
						updateMetaList(meta);											
				}
			}
			
			// 主机数据更新
			if (type == Constants.DATA_LIST_CHANGE) {
				String host = intent.getStringExtra(Constants.BROADCAST_SMS);
				if(host == null || host.equals("") == true)
					return ;
				
				updateHostList(host);

			}
			
			// 部分数据删除
			if (type == Constants.DATA_REMOVE) {
				AlertMeta meta = (AlertMeta) intent
						.getParcelableExtra(Constants.BROADCAST_SMS);

				if(meta == null)
					return ;
				// 获取更新数据，更新后清除
				if (mLevel.equals("P")) {
					removeMetaList(meta);
				} else {
					if (meta.mAlertLevel.equals(mLevel)) 
						removeMetaList(meta);											
				}
			}
		}
	}



}

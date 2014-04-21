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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.data.NotifyMeta;
import com.xiaomi.alertsystem.utils.AsyncTaskUtils;
import com.xiaomi.alertsystem.utils.SmsManager;
import com.xiaomi.alertsystem.utils.ThreadUpdateApp;

public class SettingActivity extends BaseActivity {

	final private Context mContext = SettingActivity.this;
	private static final String TAG = "SettingActivity";
	private ListView mNotifyList;
	private List<NotifyMeta> mList = new ArrayList<NotifyMeta>();
	private SmsNotifyAdapter mNotifyAdpter;
	private View mIntercept;
	private View mAddPhone;
	//private View mRules;
	private View mDeleteRead;
	private View mDeleteAll;
	private int mSmsReadNum;
	private int mSmsTotalNum;
	private TextView mSmsReadText;
	private TextView mSmsTotalText;
	private View mBack;
	private View mVersionCheck;
	private Handler handler;
	private View mUsage;
	private View mAbout;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		initData();
		findViews();
		setListen();

		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (!Thread.currentThread().isInterrupted()) {
					switch (msg.what) {
					case Constants.MSG_VER_UPDATE:
						dismissPrepareDialog();
						Toast.makeText(mContext, "版本更新中...", Toast.LENGTH_SHORT)
								.show();
						break;
					case Constants.MSG_VER_NONEED:
						dismissPrepareDialog();
						Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT)
								.show();
						break;
					case Constants.MSG_VER_ERROR:
						dismissPrepareDialog();
						Toast.makeText(mContext, "版本更新失败", Toast.LENGTH_SHORT)
								.show();
						break;
					}
				}
				super.handleMessage(msg);
			}
		};
	}

	private void initData() {
		for (int i = 0; i < Constants.NOTIFICATION_LEVEL.size()-1; i++) {
			NotifyMeta meta = new NotifyMeta("P" + i, SettingActivity.this);
			mList.add(meta);
		}

		mSmsReadNum = SmsManager.getAlertReadNum();
		mSmsTotalNum = SmsManager.getAlertNum();
	}

	private void setListen() {
		mNotifyList.setOnItemClickListener(notifyListListener);
		mDeleteRead.setOnClickListener(mDeleteReadListener);
		mDeleteAll.setOnClickListener(mDeleteAllListener);
		mAddPhone.setOnClickListener(mAddPhoneListener);
		mIntercept.setOnClickListener(mInterceptListener);
		mAbout.setOnClickListener(mAboutListener);
		mUsage.setOnClickListener(mUsageListener);
		//mRules.setOnClickListener(mRulesListener);
		mVersionCheck.setOnClickListener(mVersionCheckListener);
		mBack.setOnClickListener(gobackListener);
	}

	private void findViews() {
		mNotifyList = (ListView) findViewById(R.id.notify_list);
		mNotifyAdpter = new SmsNotifyAdapter(SettingActivity.this, mList);
		mNotifyList.setAdapter(mNotifyAdpter);
		mIntercept = findViewById(R.id.intercept);
		mAddPhone = findViewById(R.id.addphone);
		mAbout = findViewById(R.id.about);
		mUsage = findViewById(R.id.usage);
		//mRules = findViewById(R.id.rules);
		mDeleteRead = findViewById(R.id.deleteread);
		mDeleteAll = findViewById(R.id.deleteall);
		mVersionCheck = findViewById(R.id.version_update);
		mSmsReadText = (TextView) findViewById(R.id.sms_read_text);
		mSmsTotalText = (TextView) findViewById(R.id.sms_all_text);

		mBack = findViewById(R.id.goback);
		setSmsNum();
	}

	private void setSmsNum() {
		if (mSmsReadNum > 0)
			mSmsReadText.setText(mSmsReadNum + "条");
		else
			mSmsReadText.setText("");
		if (mSmsTotalNum > 0)
			mSmsTotalText.setText("共" + mSmsTotalNum + "条");
		else
			mSmsTotalText.setText("");

	}
	
	private OnClickListener mAboutListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(mContext,
					AboutActivity.class);
			startActivity(intent);
		}
	};

	private OnClickListener mUsageListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(mContext,
					UsageActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener gobackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SettingActivity.this.finish();
		}
	};

	OnClickListener mVersionCheckListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			updateVersion();
		}
	};

	OnItemClickListener notifyListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			final int pPos = index;
			new AlertDialog.Builder(mContext)
					.setTitle("短信提醒方式")
					.setItems(
							getResources().getStringArray(R.array.sound_type),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int pos) {
									String key = "p" + pPos;
									NotifyMeta meta = new NotifyMeta(key,
											mContext);
									meta.setValue(pos);
									mNotifyAdpter.notifyDataSetChanged();
								}
							}).show();

		}
	};

	OnClickListener mDeleteReadListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			deleteHasRead();
		}
	};

	OnClickListener mDeleteAllListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			deleteAllThread();
		}
	};

	OnClickListener mAddPhoneListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			addPhoneNumber();
		}
	};

	OnClickListener mInterceptListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent intent2 = new Intent(mContext,
					PhoneNumberActivity.class);
			startActivity(intent2);
		}
	};
	
	OnClickListener mRulesListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent intent2 = new Intent(mContext,
					SmsRulesActivity.class);
			startActivity(intent2);
		}
	};
	private ProgressDialog prepareProgress;

	private void addPhoneNumber() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View v = LayoutInflater.from(this).inflate(
				R.layout.add_phone_number, null);
		
		final EditText editText = (EditText) v.findViewById(R.id.edit_content);
		builder.setView(v);

		builder.setTitle(R.string.addohone);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String phone = editText.getText().toString();
						if (TextUtils.isEmpty(phone)) {
							Toast.makeText(mContext, R.string.notnull,
									Toast.LENGTH_SHORT).show();
							return;
						}

						if (phone.length() > 20) {
							Toast.makeText(mContext, R.string.toolong,
									Toast.LENGTH_SHORT).show();
							return;
						}

						List<String> phoneList = SmsManager.getAllPhone();
						if (phoneList.contains(phone)) {
							Toast.makeText(mContext, R.string.allreadyexist,
									Toast.LENGTH_SHORT).show();
							return;
						}

						// 添加到数据库
						SmsManager.insertPhone(phone);
						Toast.makeText(mContext, R.string.phoneaddsuss,
								Toast.LENGTH_SHORT).show();

					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	private void deleteAllThread() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SmsManager.deleteAllThread();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				mSmsTotalNum = 0;
				mSmsReadNum = 0;
				setSmsNum();
				notifyChanged(null, null);
			}
		};

		AsyncTaskUtils.exe(task);
	}

	private void deleteHasRead() {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				SmsManager.deleteHasRead();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				mSmsReadText.setText("");
				mSmsTotalNum = mSmsTotalNum - mSmsReadNum;
				mSmsReadNum = 0;
				setSmsNum();
				notifyChanged(null, null);
			}

		};

		AsyncTaskUtils.exe(task);
	}

	public void notifyChanged(String tablename, AlertMeta meta) {
		// 发送通知
		Intent intent = new Intent(Constants.sActSmsInserted);
		intent.putExtra(Constants.BROADCAST_TYPE, Constants.DATA_REFREASH);
		sendBroadcast(intent);
	}

	private void updateVersion() {
		showPrepareDialog();
		ThreadUpdateApp t = new ThreadUpdateApp(mContext, handler);
		t.start();
	}

	private void showPrepareDialog() {
		prepareProgress = new ProgressDialog(this);
		prepareProgress.setMessage("版本检查中...");
		prepareProgress.setMax(100);
		prepareProgress.show();
	}

	private void dismissPrepareDialog() {
		if (prepareProgress != null && prepareProgress.isShowing()) {
			prepareProgress.dismiss();
		}
	}

}

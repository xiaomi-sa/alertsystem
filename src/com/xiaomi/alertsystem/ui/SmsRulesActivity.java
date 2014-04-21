/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.SmsManager;

public class SmsRulesActivity extends BaseActivity {
	
	private Context mContext = SmsRulesActivity.this;
	private ListView mListView;
	private List<String> mPhoneList;
	private PhoneNumberAdapter mAdapter;
	private View mBack;
	private View mAddRules;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_rules_layout);
		findViews();
		setListens();

	}

	private void findViews() {
		mListView = (ListView) findViewById(R.id.phone_list);
		mBack = findViewById(R.id.goback);
		mAddRules = findViewById(R.id.addrules);
		mPhoneList = SmsManager.getAllPhone();
	}

	private void setListens() {
		mAdapter = new PhoneNumberAdapter(mPhoneList);
		mListView.setAdapter(mAdapter);
		mBack.setOnClickListener(gobackListener);
		mAddRules.setOnClickListener(addRulesListener);
	}

	private OnClickListener gobackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			SmsRulesActivity.this.finish();
		}
	};

	private OnClickListener addRulesListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			addRulesDialog();
		}
	};

	private void addRulesDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final View v = LayoutInflater.from(this).inflate(
				R.layout.add_rules_dialog, null);
		final EditText startWithEdit = (EditText) v
				.findViewById(R.id.start_with);
		final EditText endWithEdit = (EditText) v
				.findViewById(R.id.edit_content);
		final EditText alertLevelEdit = (EditText) v
				.findViewById(R.id.alert_level);
		final EditText alertFlagEdit = (EditText) v
				.findViewById(R.id.alert_status);
		final EditText alertHostEdit = (EditText) v
				.findViewById(R.id.alert_host);
		final EditText alertIpEdit = (EditText) v.findViewById(R.id.alert_ip);
		final EditText alertContentEdit = (EditText) v
				.findViewById(R.id.alert_content);

		builder.setView(v);

		builder.setTitle(R.string.addrules);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// String phone = editText.getText().toString();
						String alertStart = startWithEdit.getText().toString();
						String alertEnd = endWithEdit.getText().toString();
						String alertLevel = alertLevelEdit.getText().toString();
						String alertFlag = alertFlagEdit.getText().toString();
						String alertHost = alertHostEdit.getText().toString();
						String alertIp = alertIpEdit.getText().toString();
						String alertContent = alertContentEdit.getText()
								.toString();

						if (TextUtils.isEmpty(alertStart)
								|| TextUtils.isEmpty(alertEnd)
								|| TextUtils.isEmpty(alertLevel)
								|| TextUtils.isEmpty(alertFlag)
								|| TextUtils.isEmpty(alertHost)
								|| TextUtils.isEmpty(alertIp)
								|| TextUtils.isEmpty(alertContent)) {
							Toast.makeText(mContext, R.string.notnull,
									Toast.LENGTH_SHORT).show();
							return ;
						}

						// if (TextUtils.isEmpty(phone)) {
						// Toast.makeText(mContext, R.string.notnull,
						// Toast.LENGTH_SHORT).show();
						// return;
						// }
						//
						// if (phone.length() > 20) {
						// Toast.makeText(mContext, R.string.toolong,
						// Toast.LENGTH_SHORT).show();
						// return;
						// }
						//
						// List<String> phoneList = SmsManager.getAllPhone();
						// if (phoneList.contains(phone)) {
						// Toast.makeText(mContext, R.string.allreadyexist,
						// Toast.LENGTH_SHORT).show();
						// return;
						// }
						//
						// // 添加到数据库
						// SmsManager.insertPhone(phone);
						// Toast.makeText(mContext, R.string.phoneaddsuss,
						// Toast.LENGTH_SHORT).show();

					}
				});
		builder.setNegativeButton(R.string.cancel, null);
		builder.show();
	}

	public class PhoneNumberAdapter extends BaseAdapter {
		List<String> mList;
		PhoneNumberAdapter mAdapter;

		public PhoneNumberAdapter(List<String> list) {
			super();
			mAdapter = this;
			mList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int pos) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.phone_list_item, null);
			}
			TextView nameTextView = (TextView) convertView
					.findViewById(R.id.phone);
			View button = (View) convertView.findViewById(R.id.delete_button);
			String phone = mList.get(position);
			nameTextView.setText(phone);
			button.setTag(position);
			if (Constants.DEFAULT_PHONE.contains(phone))
				button.setVisibility(View.INVISIBLE);
			else
				button.setVisibility(View.VISIBLE);

			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					int pos = (Integer) view.getTag();
					String phone = mPhoneList.get(pos);
					mPhoneList.remove(pos);
					SmsManager.deletePhoneNumber(phone);
					mAdapter.notifyDataSetChanged();
				}
			});

			return convertView;
		}

	}

}

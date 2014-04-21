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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.SmsManager;

public class PhoneNumberActivity extends BaseActivity {

	private ListView mListView;
	private List<String> mPhoneList;
	private PhoneNumberAdapter mAdapter;
	private View mBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_number_layout);
		mListView = (ListView) findViewById(R.id.phone_list);
		mBack = findViewById(R.id.goback);
		mPhoneList= SmsManager.getAllPhone();
		mAdapter =new PhoneNumberAdapter(mPhoneList);
		mListView.setAdapter(mAdapter);
		mBack.setOnClickListener(gobackListener);	
	}
	
	private OnClickListener gobackListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			PhoneNumberActivity.this.finish();
		}
	};

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
			TextView nameTextView = (TextView) convertView.findViewById(R.id.phone);
			View button = (View) convertView.findViewById(R.id.delete_button);
			String phone = mList.get(position);
			nameTextView.setText(phone);
			button.setTag(position);
			if(Constants.DEFAULT_PHONE.contains(phone))
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

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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.AsyncTaskUtils;
import com.xiaomi.alertsystem.utils.SmsManager;
import com.xiaomi.alertsystem.utils.SmsManager.DatabaseChangedListener;

public class AlertContentActivity extends BaseActivity implements
        DatabaseChangedListener {
    public static final String TAG = "AlertContentActivity";
    public static List<AlertMeta> mMetaList;
    private AlertMeta mSms = null;
    private int mId = 0;
	private View mBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);
    	mId = getIntent().getExtras().getInt("ID");
		mBack = findViewById(R.id.goback);
		mBack.setOnClickListener(gobackListener);


    	if(mMetaList != null){
    		mSms = mMetaList.get(mId);
	        mSms.mStatus = 1;
	        buildContentView();
	        updateStatusTask();
	
	        SmsManager.registerTableChangeListener(this);
    	}
    }


	private OnClickListener gobackListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertContentActivity.this.finish();
		}
	};
    
    private void updateStatusTask() {
        AsyncTask<Void, Void, AlertMeta> task = new AsyncTask<Void, Void, AlertMeta>() {

            @Override
            protected AlertMeta doInBackground(Void... params) {
                return SmsManager.updateStatus(mSms);
            }

            @Override
            protected void onPostExecute(AlertMeta result) {
                super.onPostExecute(result);
                notifyChanged("", result);
            }
        };
        AsyncTaskUtils.exe(task);
    }

    private void buildContentView() {
        if (mSms == null) {
            return;
        }

        TextView flagView = (TextView) findViewById(R.id.flag);
        TextView safeView = (TextView) findViewById(R.id.safetype);
        TextView hostnameView = (TextView) findViewById(R.id.hostname);
        TextView machineIPView = (TextView) findViewById(R.id.ip);
        TextView contentView = (TextView) findViewById(R.id.content);
        TextView timeView = (TextView) findViewById(R.id.time);
        TextView originalView = (TextView) findViewById(R.id.msg);


        if (mSms.isProblemMachine()) {
            flagView.setTextColor(this.getResources().getColor(R.color.class_J));
        } else {
            flagView.setTextColor(this.getResources().getColor(R.color.class_K));
        }
        flagView.setText(mSms.mFlag);
        contentView.setText(mSms.mBody);
        safeView.setText(mSms.getAlertLevel());
        hostnameView.setText(mSms.getMachineName());
        machineIPView.setText(mSms.getMachineIP());
        timeView.setText(mSms.getDateTime());
        originalView.setText(mSms.mMsg);
    }

    @Override
    //发送广播，通知第一级Activity和第二级Activity
    public void notifyChanged(String tablename, AlertMeta meta) {
        Log.d(TAG, "changed:" + meta.mMsg);
        //改变第二级activity数据，第二级全部Reload
        mMetaList.get(mId).mStatus = 1;
        
	    Intent intent=new Intent(Constants.sActSmsInserted);
	    intent.putExtra(Constants.BROADCAST_TYPE, Constants.DATA_CHANGE);
        intent.putExtra(Constants.BROADCAST_SMS, meta);
		sendBroadcast(intent);
    }
}

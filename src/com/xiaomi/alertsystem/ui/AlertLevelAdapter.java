/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;

import java.util.List;

//TODO:deprecated, will not be used
public class AlertLevelAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<AlertMeta> mList;

    public AlertLevelAdapter(Activity activity, List<AlertMeta> sms) {
        mActivity = activity;
        mList = sms;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.level_list_layout, null);
        }
        Log.d("AlertLevelAdapter", "getview:" + mList.get(position).toString());
        TextView text = (TextView) convertView.findViewById(R.id.sub_title);
        TextView body = (TextView) convertView.findViewById(R.id.body);
        text.setText(mList.get(position).mAddress);
        body.setText(mList.get(position).mBody);
        return convertView;
    }

}

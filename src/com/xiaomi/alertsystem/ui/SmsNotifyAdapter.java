/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.NotifyMeta;

import java.util.List;

//TODO:deprecated, will not be used
public class SmsNotifyAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotifyMeta> mList;

    public SmsNotifyAdapter(Context context, List<NotifyMeta> notify) {
    	mContext = context;
        mList = notify;
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
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.sms_notify_item, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.pn_notify_name);
        TextView body = (TextView) convertView.findViewById(R.id.pn_notify_text);
        text.setText(mList.get(position).getName_cn());
        body.setText(mList.get(position).getTextValue());
        return convertView;
    }

}

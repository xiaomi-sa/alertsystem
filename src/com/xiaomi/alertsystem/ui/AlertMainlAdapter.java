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

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;

public class AlertMainlAdapter extends BaseAdapter {

    private static final String TAG = "AlertMainlAdapter";
    private List<AlertMeta> mSms = null;
    private Context mContext = null;
    private String mKey = null;

    public AlertMainlAdapter(final  Context context,
                             final List<AlertMeta> sms, final String key) {
    	mContext = context;
        mSms = sms;
        mKey = key;
    }

    @Override
    public int getCount() {
        return mSms != null ? mSms.size() : 0;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.main_list_layout, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.subTitle = (TextView) convertView
                    .findViewById(R.id.sub_title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.problem = (TextView) convertView.findViewById(R.id.problem);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AlertMeta meta = mSms.get(position);
        //Log.d(TAG, meta.toString());
        if (meta.isProblemMachine()) {
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.namecard_info_not_verified));
        } else {
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(
                    R.drawable.namecard_info_verified));
        }

        holder.title.setText(meta.getMachineName());
        holder.subTitle.setText(meta.mBody);
        holder.time.setText(meta.getDateTime());
        if (TextUtils.equals(mKey, "P")) {
            holder.problem.setVisibility(View.VISIBLE);
            holder.problem.setText(meta.mAlertLevel);
        } else {
            holder.problem.setVisibility(View.GONE);
        }

        if (meta.mStatus == 0) {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.class_B));
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.status.setText(R.string.weidu);
            holder.status.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.class_D));
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.status.setText(R.string.yidu);
            holder.status.setTextColor(mContext.getResources().getColor(R.color.class_K));
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView subTitle;
        TextView time;
        TextView problem;
        TextView status;
    }

}

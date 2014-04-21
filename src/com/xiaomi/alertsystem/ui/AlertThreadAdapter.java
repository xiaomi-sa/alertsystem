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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomi.alertsystem.R;
import com.xiaomi.alertsystem.data.AlertMeta;

public class AlertThreadAdapter extends BaseAdapter {

    private static final String TAG = "AlertThreadAdapter";
    private List<AlertMeta> mSms = null;
    private Context mContext = null;
    private Boolean mDelete;
	private OnClickListener mListener = null;

    public AlertThreadAdapter(final Context c,
                              final List<AlertMeta> sms, final String key, final String host, OnClickListener l) {
    	mContext = c;
        mSms = sms;
        mDelete = false;
        mListener = l;
    }

    @Override
    public int getCount() {
        return mSms != null ? mSms.size() : 0;
    }
    
    public void setDelete(Boolean flag){
    	mDelete = flag;
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
            holder.delete = (ImageView) convertView.findViewById(R.id.delete_button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if(mDelete)
        	holder.delete.setVisibility(View.VISIBLE);
        else
        	holder.delete.setVisibility(View.GONE);
        
        if(mListener != null)
        	holder.delete.setOnClickListener(mListener);

        AlertMeta meta = mSms.get(position);
        //保存位置
        holder.delete.setTag(position);
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
        holder.problem.setVisibility(View.VISIBLE);
        holder.problem.setText(meta.mAlertLevel);

//        if (meta.mStatus == 0) {
//            holder.title.setTextColor(mContext.getResources().getColor(R.color.class_B));
//            holder.title.setTypeface(null, Typeface.BOLD);
//            holder.status.setText(R.string.weidu);
//        } else {
            holder.title.setTextColor(mContext.getResources().getColor(R.color.class_D));
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.status.setText(R.string.yidu);
//        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView title;
        TextView subTitle;
        TextView time;
        TextView problem;
        TextView status;
        ImageView delete;
    }

}

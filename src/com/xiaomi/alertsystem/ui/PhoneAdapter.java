/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class PhoneAdapter extends BaseAdapter {

    List<String> mPhones;
    Activity mActivity;

    public PhoneAdapter(final Activity activity, final List<String> strings) {
        mActivity = activity;
        mPhones = strings;
    }

    @Override
    public int getCount() {
        return mPhones == null || mPhones.size() == 0 ? 0 : mPhones.size();
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
        return null;
    }

}

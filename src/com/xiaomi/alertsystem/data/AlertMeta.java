/**
 * All rights Reserved, Designed By NoOps.me
 * Company: XIAOMI.COM
 * @author:    
 *      Xiaodong Pan <panxiaodong@xiaomi.com>
 *      Wei Lai  <laiwei@xiaomi.com>
 * @version    V1.0 
 */

package com.xiaomi.alertsystem.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertMeta extends Sms implements Parcelable {
    public static final String ALERT_LEVEL_PALL = "P";
    public static final String ALERT_LEVEL_P0 = "P0";
    public static final String ALERT_LEVEL_P1 = "P1";
    public static final String ALERT_LEVEL_P2 = "P2";
    public static final String ALERT_LEVEL_P3 = "P3";

    public static final String ALERT_FLAG_OK = "OK";
    public static final String ALERT_FLAG_ERROR = "PROBLEM";

    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");

    public String mAlertLevel;

    public String mFlag; // 报警标志

    public String mMachineName;

    public String mMachineIP;

    public String mMsg;

    public AlertMeta(final String level, final String flag,
                     final String machineName, final String machineIP, final String msg) {

        mAlertLevel = level;
        mFlag = flag;
        mMachineName = machineName;
        mMachineIP = machineIP;
        mMsg = msg;
    }

    public String toString() {
        return "(" + mId + ")" + mMsg.toString();
    }

    public AlertMeta(Parcel in) {
        super(in);
        mAlertLevel = in.readString();
        mFlag = in.readString();
        mMachineName = in.readString();
        mMachineIP = in.readString();
        mMsg = in.readString();
    }

    public AlertMeta() {

    }

    public String getAlertLevel() {
        return mAlertLevel;
    }

    public String getMachineName() {
        return mMachineName;
    }

    public String getMachineIP() {
        return mMachineIP;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public String getProblem() {
        return mBody;
    }

    public String getDateTime() {
        Date date = new Date(mTimeStamp);
        return mSimpleDateFormat.format(date);
    }

    public boolean isProblemMachine() {
        if (TextUtils.isEmpty(mFlag)) {
            return false;
        }
        return TextUtils.equals(mFlag, ALERT_FLAG_ERROR);
    }

    public boolean isHighProblem() {
        if (TextUtils.isEmpty(mAlertLevel)) {
            return false;
        }

        return TextUtils.equals(mAlertLevel, ALERT_LEVEL_P0);
    }

    @Override
    public boolean equals(Object o) {
        AlertMeta alertMeta = (AlertMeta) o;

        if (o == null) {
            return false;
        }

        return alertMeta.mId == mId;
    }

    public static final Parcelable.Creator<AlertMeta> CREATOR = new Creator<AlertMeta>() {

        @Override
        public AlertMeta[] newArray(int size) {
            return new AlertMeta[size];
        }

        @Override
        public AlertMeta createFromParcel(Parcel source) {
            return new AlertMeta(source);
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mAlertLevel);
        dest.writeString(mFlag);
        dest.writeString(mMachineName);
        dest.writeString(mMachineIP);
        dest.writeString(mMsg);
    }

}

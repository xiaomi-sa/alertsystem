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

public class Sms implements Parcelable {
	public long mId;

	public String mAddress;

	public String mPerson;

	public String mBody;

	public long mTimeStamp;

	public String mType;

	public int mStatus; // 0，未读，1，一读

	public Sms(final long id, final String address, final String person,
			final String body, final long date, final String type,
			final int status) {
		mId = id;
		mAddress = address;
		mPerson = person;
		mBody = body;
		mTimeStamp = date;
		mType = type;
		mStatus = status;
	}

	public Sms(Parcel in) {
		mId = in.readLong();
		mAddress = in.readString();
		mPerson = in.readString();
		mBody = in.readString();
		mTimeStamp = in.readLong();
		mType = in.readString();
		mStatus = in.readInt();
	}

	public Sms() {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	public final static Parcelable.Creator<Sms> CREATOR = new Creator<Sms>() {

		@Override
		public Sms[] newArray(int size) {
			return new Sms[size];
		}

		@Override
		public Sms createFromParcel(Parcel source) {
			return new Sms(source);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeString(mAddress);
		dest.writeString(mPerson);
		dest.writeString(mBody);
		dest.writeLong(mTimeStamp);
		dest.writeString(mType);
		dest.writeInt(mStatus);
	}

}

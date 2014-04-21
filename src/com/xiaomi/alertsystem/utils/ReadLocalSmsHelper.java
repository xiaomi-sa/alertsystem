package com.xiaomi.alertsystem.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xiaomi.alertsystem.data.Sms;

import java.util.ArrayList;
import java.util.List;

public class ReadLocalSmsHelper {
    public final static String SMS_URI_ALL = "content://sms/";
    public final static String SMS_URI_INBOX = "content://sms/inbox";
    public final static String SMS_URI_SEND = "content://sms/sent";
    public final static String SMS_URI_DRAFT = "content://sms/draft";
    public final static String SMS_URI_OUTBOX = "content://sms/outbox";
    public final static String SMS_URI_FAILED = "content://sms/failed";
    public final static String SMS_URI_QUEUED = "content://sms/queued";

    public static class SmsColoumn {
        public final static String id = "_id";
        public final static String address = "address";
        public final static String person = "person";
        public final static String body = "body";
        public final static String date = "date";
        public final static String type = "type";
    }

    public final static String[] sProjection = new String[]{SmsColoumn.id,
            SmsColoumn.address, SmsColoumn.person, SmsColoumn.body,
            SmsColoumn.date, SmsColoumn.type};

    public static List<Sms> querySms(final Context context, final Uri uri,
                                     final String[] projection) {
        List<Sms> sms = null;
        if (uri == null) {
            return sms;
        }

        ContentResolver contentResolver = context.getContentResolver();
        try {
            Cursor cursor = contentResolver.query(uri, projection, null, null,
                    "date desc");
            sms = parseResult(cursor);
        } catch (Exception e) {
        }
        return sms;
    }

    public static List<Sms> parseResult(Cursor cur) {
        List<Sms> sms = new ArrayList<Sms>();
        if (cur.moveToFirst()) {
            int personColumn = cur.getColumnIndex(SmsColoumn.person);
            int addressColumn = cur.getColumnIndex(SmsColoumn.address);
            int bodyColumn = cur.getColumnIndex(SmsColoumn.body);
            int typeColumn = cur.getColumnIndex(SmsColoumn.type);
            int dateColumn = cur.getColumnIndex(SmsColoumn.date);
            int idColumn = cur.getColumnIndex(SmsColoumn.id);

            do {
                Sms newSms = new Sms();
                newSms.mId = cur.getLong(idColumn);
                newSms.mAddress = cur.getString(addressColumn);
                newSms.mBody = cur.getString(bodyColumn);
                newSms.mPerson = cur.getString(personColumn);
                newSms.mTimeStamp = cur.getLong(dateColumn);
                newSms.mType = cur.getString(typeColumn);
                sms.add(newSms);
            } while (cur.moveToNext());
        }
        return sms;
    }
}

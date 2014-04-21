package com.xiaomi.alertsystem.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xiaomi.alertsystem.AlertSystemApplication;
import com.xiaomi.alertsystem.data.AlertMeta;
import com.xiaomi.alertsystem.data.Constants;
import com.xiaomi.alertsystem.utils.AlertSystemDBHelper.AlertColoumn;
import com.xiaomi.alertsystem.utils.AlertSystemDBHelper.AlertPhoneColoumn;

public class SmsManager {
    private static Context mContext;
    private static AlertSystemDBHelper mAlertSystemDBHelper;
    private static final Object sDBLOCK = new Object();
    private static final int sPageSmsNum = 20;

    public final static String TAG = "SmsManager";
    public final static String[] sProjection = new String[]{AlertColoumn.id,
            AlertColoumn.level, AlertColoumn.flag, AlertColoumn.hostname,
            AlertColoumn.hostip, AlertColoumn.body, AlertColoumn.status,
            AlertColoumn.time, AlertColoumn.msg};

    public final static String[] sProjectionPhone = new String[]{AlertPhoneColoumn.number};

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        mContext = context;
        mAlertSystemDBHelper = new AlertSystemDBHelper(context,
                AlertSystemDBHelper.DATABASE_NAME, null,
                AlertSystemDBHelper.DATABASE_VERSION);
    }

    public static AlertMeta updateStatus(final AlertMeta meta) {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        synchronized (sDBLOCK) {
            ContentValues cvs = new ContentValues();
            cvs.put(AlertColoumn.status, meta.mStatus);
            db.update(AlertSystemDBHelper.DATABASE_ALERT_TABLE, cvs,
                    AlertColoumn.time + " =?",
                    new String[]{String.valueOf(meta.mTimeStamp)});
        }

        return meta;
    }

    public static String insertPhone(String string) {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cvs = new ContentValues();
            cvs.put(AlertPhoneColoumn.number, string);
            db.insert(AlertSystemDBHelper.DATEBASE_ALERT_PHONE_TABLE, null, cvs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return string;
    }

    public static void insertAlertSms(List<AlertMeta> alertMetas) {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        long time = System.currentTimeMillis();
        db.beginTransaction();
        try {
            for (AlertMeta meta : alertMetas) {
                ContentValues cvs = new ContentValues();
                cvs.put(AlertColoumn.level, meta.mAlertLevel);
                cvs.put(AlertColoumn.flag, meta.mFlag);
                cvs.put(AlertColoumn.hostname, meta.mMachineName);
                cvs.put(AlertColoumn.hostip, meta.mMachineIP);
                cvs.put(AlertColoumn.body, meta.mBody);
                cvs.put(AlertColoumn.status, meta.mStatus);
                //TODO:use meta.time
                cvs.put(AlertColoumn.time, time);
                cvs.put(AlertColoumn.msg, meta.mMsg);
                db.insert(AlertSystemDBHelper.DATABASE_ALERT_TABLE, null, cvs);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        for (AlertMeta meta : alertMetas) {
            notifyTableChange(AlertSystemDBHelper.DATABASE_ALERT_TABLE, meta);
        }
    }

    public static void insertAlertSms(AlertMeta meta) {
        long id_ = 0;
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        meta.mTimeStamp = System.currentTimeMillis();
        meta.mStatus = 0;
        db.beginTransaction();
        try {
            ContentValues cvs = new ContentValues();
            cvs.put(AlertColoumn.level, meta.mAlertLevel);
            cvs.put(AlertColoumn.flag, meta.mFlag);
            cvs.put(AlertColoumn.hostname, meta.mMachineName);
            cvs.put(AlertColoumn.hostip, meta.mMachineIP);
            cvs.put(AlertColoumn.body, meta.mBody);
            cvs.put(AlertColoumn.status, meta.mStatus);
            cvs.put(AlertColoumn.time, meta.mTimeStamp);
            cvs.put(AlertColoumn.msg, meta.mMsg);
            id_ = db.insert(AlertSystemDBHelper.DATABASE_ALERT_TABLE, null, cvs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (id_ > -1) {
            meta.mId = id_;
            notifyTableChange(AlertSystemDBHelper.DATABASE_ALERT_TABLE, meta);
        } else {
            Log.d(TAG, "insert into alert table fail:" + meta.toString());
        }
    }
    
       
    //按主机获取短信
    public static List<AlertMeta> queryAlertSmsByHost(final String[] projection, String host, String level) {
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
        Cursor c = null;
        List<AlertMeta> alertMetas = new ArrayList<AlertMeta>();

        String condiction = "";
        if(level != null && level.equals("P") == false){
        	condiction = AlertColoumn.level + "='" + level + "' AND " + AlertColoumn.hostname  + "='" + host + "' ";

        }else{
        	condiction = AlertColoumn.hostname  + "='" + host + "' ";
        }
        
        String fields = CommonUtils.join(projection, ",");
        String sql = "SELECT  "+ fields +" FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE + 
        			 " where " + condiction + 
        			 " order by " + AlertColoumn.time + " DESC ";
        c = db.rawQuery(sql, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                	
                    AlertMeta alertMeta = new AlertMeta();
                    alertMeta.mId = c
                            .getLong(c.getColumnIndex(AlertColoumn.id));
                    alertMeta.mAlertLevel = c.getString(c
                            .getColumnIndex(AlertColoumn.level));
                    alertMeta.mFlag = c.getString(c
                            .getColumnIndex(AlertColoumn.flag));
                    alertMeta.mMachineName = c.getString(c
                            .getColumnIndex(AlertColoumn.hostname));
                    alertMeta.mMachineIP = c.getString(c
                            .getColumnIndex(AlertColoumn.hostip));
                    alertMeta.mStatus = c.getInt(c
                            .getColumnIndex(AlertColoumn.status));
                    alertMeta.mTimeStamp = c.getLong(c
                            .getColumnIndex(AlertColoumn.time));
                    alertMeta.mBody = c.getString(c
                            .getColumnIndex(AlertColoumn.body));
                    alertMeta.mMsg = c.getString(c
                            .getColumnIndex(AlertColoumn.msg));
                    alertMetas.add(alertMeta);

                } while (c.moveToNext());
            }
            c.close();
        }

        return alertMetas;
    }
        
	public static int execSqlRetInt(String sql) {
		int ret = -1;
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
		// ShowLog(sql);
		Cursor result = db.rawQuery(sql, null);
		result.moveToFirst();
		while (!result.isAfterLast()) {
			ret = result.getInt(0);
			break;

		}
		result.close();
		return ret;
	}

    //获取已读短信数
    public static int getAlertReadNum(){
    	String sql = "SELECT COUNT (*) FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE + " where status=1" ;
    	return execSqlRetInt(sql);
    }
    
    public static int getAlertUnReadNum(){
    	String sql = "SELECT COUNT (*) FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE + " where status=0" ;
    	return execSqlRetInt(sql);
    }
    
    public static int getAlertNum(){
    	String sql = "SELECT COUNT (*) FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE;
    	return execSqlRetInt(sql);
    }
    
    //获取短信页数
    public static int getAlertPageNum(String level){
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
        String condiction = "";
        if(level != null && level.equals("P") == false){
        	condiction = " where " + AlertColoumn.level + "='" + level + "' ";
        }
        String sql = "SELECT COUNT (*) FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE +  condiction;
        Cursor cursor= db.rawQuery(sql, null);
        int count = 0;
        if(null != cursor)
        if(cursor.getCount() > 0){
        	cursor.moveToFirst();    
        	count = cursor.getInt(0);
	    }
	    cursor.close();
		return count/sPageSmsNum + 1;
	}
    
    //按照分页获取短信
    public static List<AlertMeta> queryAlertSmsByPage(final String[] projection, int pageNum, String level) {
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
        Cursor c = null;
        List<AlertMeta> alertMetas = new ArrayList<AlertMeta>();

        int start = pageNum * sPageSmsNum;
        String condiction = "";
        if(level != null && level.equals("P") == false){
        	condiction = AlertColoumn.level + "='" + level + "' ";
        }
      	c = db.query(AlertSystemDBHelper.DATABASE_ALERT_TABLE, sProjection,
      			condiction , null, null, null,  AlertColoumn.time + " DESC "
                + "limit " + start + "," + sPageSmsNum);

        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    AlertMeta alertMeta = new AlertMeta();
                    alertMeta.mId = c
                            .getLong(c.getColumnIndex(AlertColoumn.id));
                    alertMeta.mAlertLevel = c.getString(c
                            .getColumnIndex(AlertColoumn.level));
                    alertMeta.mFlag = c.getString(c
                            .getColumnIndex(AlertColoumn.flag));
                    alertMeta.mMachineName = c.getString(c
                            .getColumnIndex(AlertColoumn.hostname));
                    alertMeta.mMachineIP = c.getString(c
                            .getColumnIndex(AlertColoumn.hostip));
                    alertMeta.mStatus = c.getInt(c
                            .getColumnIndex(AlertColoumn.status));
                    alertMeta.mTimeStamp = c.getLong(c
                            .getColumnIndex(AlertColoumn.time));
                    alertMeta.mBody = c.getString(c
                            .getColumnIndex(AlertColoumn.body));
                    alertMeta.mMsg = c.getString(c
                            .getColumnIndex(AlertColoumn.msg));
                    alertMetas.add(alertMeta);

                } while (c.moveToNext());
            }
            c.close();
        }

        return alertMetas;
    }

    public static List<AlertMeta> queryAlertSms(final String[] projection) {
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
        Cursor c = null;
        List<AlertMeta> alertMetas = new ArrayList<AlertMeta>();

        c = db.query(AlertSystemDBHelper.DATABASE_ALERT_TABLE, sProjection,
                null, null, null, null, AlertColoumn.time + " DESC");

        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    AlertMeta alertMeta = new AlertMeta();
                    alertMeta.mId = c
                            .getLong(c.getColumnIndex(AlertColoumn.id));
                    alertMeta.mAlertLevel = c.getString(c
                            .getColumnIndex(AlertColoumn.level));
                    alertMeta.mFlag = c.getString(c
                            .getColumnIndex(AlertColoumn.flag));
                    alertMeta.mMachineName = c.getString(c
                            .getColumnIndex(AlertColoumn.hostname));
                    alertMeta.mMachineIP = c.getString(c
                            .getColumnIndex(AlertColoumn.hostip));
                    alertMeta.mStatus = c.getInt(c
                            .getColumnIndex(AlertColoumn.status));
                    alertMeta.mTimeStamp = c.getLong(c
                            .getColumnIndex(AlertColoumn.time));
                    alertMeta.mBody = c.getString(c
                            .getColumnIndex(AlertColoumn.body));
                    alertMeta.mMsg = c.getString(c
                            .getColumnIndex(AlertColoumn.msg));
                    alertMetas.add(alertMeta);

                } while (c.moveToNext());
            }
            c.close();
        }

        return alertMetas;
    }

    public static void deletePhoneNumber(String string) {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        synchronized (sDBLOCK) {
            String[] strings = {string};
            db.delete(mAlertSystemDBHelper.DATEBASE_ALERT_PHONE_TABLE,
                    AlertPhoneColoumn.number + " = ?", strings);
        }
    }
    
    

    public static void deleteOneSms(AlertMeta sms) {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();

        synchronized (sDBLOCK) {
            String sql = "delete FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE + " where _id=" + sms.mId; 
            db.execSQL(sql);
        }
    }

    public static void deleteAllThread() {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();

        synchronized (sDBLOCK) {
            String sql = "delete FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE; 
            db.execSQL(sql);
        }
    }

    public static void deleteHasRead() {
        SQLiteDatabase db = mAlertSystemDBHelper.getWritableDatabase();
        synchronized (sDBLOCK) {
        	String sql = "delete FROM " + AlertSystemDBHelper.DATABASE_ALERT_TABLE + " where status=1" ;
            db.execSQL(sql);
        }
    }

    public static List<String> getAllPhone() {
        SQLiteDatabase db = mAlertSystemDBHelper.getReadableDatabase();
        Cursor c = null;
        List<String> list = new ArrayList<String>();
        c = db.query(mAlertSystemDBHelper.DATEBASE_ALERT_PHONE_TABLE,
        		sProjectionPhone, null, null, null, null, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    list.add(c.getString(c
                            .getColumnIndex(AlertPhoneColoumn.number)));
                } while (c.moveToNext());
            }
        }
        
        list.addAll(Constants.DEFAULT_PHONE);
        return list;
    }
    
    private static Set<DatabaseChangedListener> mListeners = new HashSet<DatabaseChangedListener>();

    public static void registerTableChangeListener(
            DatabaseChangedListener listener) {
        mListeners.add(listener);
    }

    public static void unregisterTableChangeListener(
            DatabaseChangedListener listener) {
        mListeners.remove(listener);
    }

    private static void notifyTableChange(String tableName, AlertMeta meta) {
        for (DatabaseChangedListener listener : mListeners) {
            listener.notifyChanged(tableName, meta);
        }
    }

    public static interface DatabaseChangedListener {
        public void notifyChanged(String tablename, AlertMeta meta);
    }

}

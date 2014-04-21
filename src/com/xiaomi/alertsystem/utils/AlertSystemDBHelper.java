package com.xiaomi.alertsystem.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlertSystemDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "alert.db";
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_ALERT_TABLE = "alert";
    public static final String DATEBASE_ALERT_PHONE_TABLE = "phone";

    public static final String[] ALTER_TABLE_COLUMN_DEF = {AlertColoumn.level,
            "VARCHAR(4)", AlertColoumn.flag, "VARCHAR(8)", AlertColoumn.hostname, "VARCHAR(255)",
            AlertColoumn.hostip, "VARCHAR(20)", AlertColoumn.body, "TEXT",
            AlertColoumn.time, "INTEGER", AlertColoumn.status, "INTEGER",
            AlertColoumn.msg, "TEXT"
    };

    public static final String[] ALTER_PHONE_NUMBER_DEF = {
            AlertPhoneColoumn.number, "TEXT"};
    
//    public static final String[] ALTER_RULES_DEF = {
//	    	AlertRulesColoumn.start, "TEXT",
//	    	AlertRulesColoumn.end, "TEXT",
//	    	AlertRulesColoumn.level, "INTEGER",
//	    	AlertRulesColoumn.flag, "INTEGER",
//	    	AlertRulesColoumn.hostname, "INTEGER",
//	    	AlertRulesColoumn.hostip, "INTEGER",
//	    	AlertRulesColoumn.content, "TEXT"
//        };

    public final static class AlertColoumn {
        public static final String id = "_id";
        public static final String level = "level";
        public static final String flag = "flag";
        public static final String hostname = "hostname";
        public static final String hostip = "hostip";
        public static final String body = "body";
        public static final String time = "time";
        public static final String status = "status";
        public static final String msg = "msg";
    }

    public final static class AlertPhoneColoumn {
        public static final String number = "number";
    }

//    public final static class AlertRulesColoumn {
//        public static final String start = "start";
//        public static final String end = "number";
//        public static final String level = "level";
//        public static final String flag = "number";
//        public static final String hostname = "hostname";
//        public static final String hostip = "hostip";
//        public static final String content = "content";
//    }
    
    public AlertSystemDBHelper(Context context, String name,
                               CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CommonUtils.createTable(db, DATABASE_ALERT_TABLE,
                ALTER_TABLE_COLUMN_DEF);

        CommonUtils.createTable(db, DATEBASE_ALERT_PHONE_TABLE,
                ALTER_PHONE_NUMBER_DEF);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("dbupgrade", String.valueOf(oldVersion));
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + DATABASE_ALERT_TABLE + " ADD COLUMN " + AlertColoumn.msg + " TEXT");
        }
    }
}

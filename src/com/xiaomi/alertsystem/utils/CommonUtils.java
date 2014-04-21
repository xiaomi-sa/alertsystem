package com.xiaomi.alertsystem.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.xiaomi.alertsystem.data.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

public class CommonUtils {
	private static String AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:17.0) Gecko/17.0 Firefox/17.";
	private static String defaultHome;

	public static boolean mkdir(String path) {
		File file = new File(path);
		if (file.isDirectory())
			return true;
		else {
			boolean creadok = file.mkdirs();
			if (creadok) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static String getSdcardPath() {
		String sdcardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return sdcardPath;
	}

	public static void setDefaultHome() {
		String sdcard = getSdcardPath();
		String path = sdcard + "/" + Constants.APP_DIR + "/";
		if (mkdir(path))
			defaultHome = path;
		else
			defaultHome = Constants.DEFAULT_DIR;
	}

	public static String getDefaultHome() {
		if (defaultHome == null)
			setDefaultHome();
		return defaultHome;
	}

	public static String getNowDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		return sf.format(date);
	}

	public static String readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toString();
	}

	public static String execUrl(String uri) {
		String res = "";
		try {
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", AGENT);

			res = readStream(conn.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			res = "";
		}
		return res;
	}

	public static boolean download(String uri, String filePath) {
		try {
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", AGENT);
			readStreamToFile(conn.getInputStream(), filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void readStreamToFile(InputStream inStream, String filePath)
			throws Exception {
		File file = new File(filePath + ".wei");
		RandomAccessFile outStream = new RandomAccessFile(file, "rw");
		outStream.seek(0);
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		file.renameTo(new File(filePath));
		return;
	}

	public static void createTable(final SQLiteDatabase db,
			final String tableName, final String[] columnsDefinition) {
		String queryStr = "CREATE TABLE " + tableName + "(" + BaseColumns._ID
				+ " INTEGER  PRIMARY KEY ,";
		// Add the columns now, Increase by 2
		for (int i = 0; i < (columnsDefinition.length - 1); i += 2) {
			if (i != 0) {
				queryStr += ",";
			}
			queryStr += columnsDefinition[i] + " " + columnsDefinition[i + 1];
		}

		queryStr += ");";

		db.execSQL(queryStr);
	}

	public static String getSettingString(final Context context,
			final String key, final String defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static void setSettingString(final Context context,
			final String key, final String value) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = setting.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static int getSettingInt(final Context context, final String key,
			final int defaultValue) {
		final SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void setSettingInt(final Context context, final String key,
			final int value) {
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = setting.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static String join(List<String> list, String conjunction) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : list) {
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append(item);
		}
		return sb.toString();
	}

	public static String join(String[] list, String conjunction) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String item : list) {
			if (first)
				first = false;
			else
				sb.append(conjunction);
			sb.append(item);
		}
		return sb.toString();
	}

	public static void sendIconCountMessage(Context mContext, int num) {
		Intent i = new Intent(
				"android.intent.action.APPLICATION_MESSAGE_UPDATE");
		i.putExtra("android.intent.extra.update_application_component_name",
				"com.xiaomi.alertsystem/.ui.LogoActivity");
		String s = "";
		if (num > 0) {
			if (num > 99)
				s = "99+";
			else
				s = "" + num;
			i.putExtra("android.intent.extra.update_application_message_text",
					s);
		}
		mContext.sendBroadcast(i);
	}
}

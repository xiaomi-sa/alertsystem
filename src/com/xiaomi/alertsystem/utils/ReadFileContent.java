package com.xiaomi.alertsystem.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFileContent {

    public static InputStream getAssetsInputStream(final Context context,
                                                   final String filename) {
        InputStream stream = null;
        try {
            stream = context.getAssets().open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    public static List<String> getAssetsTxtLine(final InputStream inputStream) {
        List<String> list = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                list.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}

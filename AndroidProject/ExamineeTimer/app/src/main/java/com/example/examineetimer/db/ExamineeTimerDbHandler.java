package com.example.examineetimer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExamineeTimerDbHandler {
    private final String TAG = "ExamineeTimerDbHandler";

    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;

    public ExamineeTimerDbHandler(Context context){
        mHelper = new ExamineeTimerDbHelper(context);
    }

    public long insert(String tableName, ContentValues cv){
        Log.i(TAG, "insert " + tableName);

        mDB = mHelper.getWritableDatabase();
        return mDB.insert(tableName, null, cv);
    }

    public Cursor select(String tableName){
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query(tableName, null, null, null, null, null, "id DESC");
        c.moveToFirst();

        return c;
    }

    public Cursor selectByPickerDate(String tableName, long selected) {
        mDB = mHelper.getReadableDatabase();
        long today = selected;
        long tomorrow = selected + 86400000; // + 24hours
        Date d1 = new Date(today);
        Date d2 = new Date(tomorrow);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String args[] = {sdf.format(d1), sdf.format(d2)};
        Log.i(TAG, "d1 : " + sdf.format(d1) + ", d2 : " + sdf.format(d2));
        Cursor c = mDB.query(tableName, null, "start_datetime BETWEEN ? AND ?", args, null, null, "id DESC");
        c.moveToFirst();

        return c;
    }
}

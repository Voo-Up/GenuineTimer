package com.example.examineetimer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        Cursor c = mDB.query(tableName, null, null, null, null, null, "id DESC");
        c.moveToFirst();

        return c;
    }
}

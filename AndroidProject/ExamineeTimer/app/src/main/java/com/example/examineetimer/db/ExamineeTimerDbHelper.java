package com.example.examineetimer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ExamineeTimerDbHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "examinee_timer.db";

    private static final String SQL_CREATE_STUDY_TIME =
            "CREATE TABLE IF NOT EXISTS " + StudyTimeDO.StudyTimeEntry.TABLE_NAME + "(" +
                    StudyTimeDO.StudyTimeEntry.COLUMN_NAME_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    StudyTimeDO.StudyTimeEntry.COLUMN_NAME_START_DATETIME + " DATETIME," +
                    StudyTimeDO.StudyTimeEntry.COLUMN_NAME_STUDY_SEC + " INTEGER" +
                    ");";
    private static final String SQL_DELETE_STUDY_TIME =
            "DROP TABLE IF EXISTS " + StudyTimeDO.StudyTimeEntry.TABLE_NAME;


    public ExamineeTimerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDY_TIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_STUDY_TIME);
        onCreate(db);
    }
}

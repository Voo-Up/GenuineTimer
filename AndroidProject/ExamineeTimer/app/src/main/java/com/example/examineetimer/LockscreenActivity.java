package com.example.examineetimer;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.examineetimer.db.ExamineeTimerDbHandler;
import com.example.examineetimer.db.StudyTimeDO;
import com.example.examineetimer.utils.MyUtils;
import com.ncorti.slidetoact.SlideToActView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LockscreenActivity extends Activity {
    private static final String TAG = "LockscreenActivity";

    private Timer studyTimer;
    private String mStartDateTime;
    private int mTotalStudySec;

    private TextView tvStudyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        tvStudyTime = (TextView) findViewById(R.id.tv_study_timer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mStartDateTime = getDateTime();
        Log.i(TAG, "Get Start DateTime : " + mStartDateTime);

        SlideToActView sta = (SlideToActView) findViewById(R.id.sld_unlock);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                Log.i(TAG, "onSlideComplete");
                insertStudyTimeToDb();
                finishTimer();
            }
        });
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void insertStudyTimeToDb(){
        ContentValues addRowValue = new ContentValues();
        addRowValue.put(StudyTimeDO.StudyTimeEntry.COLUMN_NAME_START_DATETIME, mStartDateTime);
        addRowValue.put(StudyTimeDO.StudyTimeEntry.COLUMN_NAME_STUDY_SEC, mTotalStudySec);
        Log.i(TAG, "Timer stop and save.");
        Log.i(TAG, "Study Time : " + tvStudyTime.getText());

        ExamineeTimerDbHandler dbHandler = new ExamineeTimerDbHandler(getApplicationContext());
        long newId = dbHandler.insert(StudyTimeDO.StudyTimeEntry.TABLE_NAME, addRowValue);
        Log.i(TAG, "New Id : " + newId);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.i(TAG,"Home Key Down");
        insertStudyTimeToDb();
        finishTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("Is on?", "Turning immersive mode mode off.");
        } else {
            Log.i("Is on?", "Turning immersive mode mode on.");
        }

        //하단바 없애려면 필수 각
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        startStudyTimer();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                insertStudyTimeToDb();

                finishTimer();

                Log.i(TAG, "onKeyDown");
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startStudyTimer() {
        studyTimer = new Timer();
        mTotalStudySec = 0;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mTotalStudySec++;
                Log.i(TAG,"Timer running..." + mTotalStudySec);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String timeString = MyUtils.convertSecToTimeFormatString(mTotalStudySec);
                        tvStudyTime.setText(timeString);
                    }
                });
            }
        };
        studyTimer.schedule(timerTask,0, 1000);
    }

    private void finishTimer() {
        studyTimer.cancel();
        finish();
    }
}

package com.example.examineetimer;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.examineetimer.db.ExamineeTimerDbHandler;
import com.example.examineetimer.db.StudyTimeDO;
import com.example.examineetimer.utils.MyUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ncorti.slidetoact.SlideToActView;
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class LockscreenActivity extends Activity {
    private static final String TAG = "LockscreenActivity";
    private AdView mAdView;

    private Timer studyTimer;
    private String mStartDateTime;
    private int mTotalStudySec;

    private TextView tvStudyTime;
    private TextView tvStudyStartTime;
    private TextView tvCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);
        initAdmob();

        tvStudyTime = (TextView)findViewById(R.id.tv_study_timer);
        tvStudyStartTime = (TextView)findViewById(R.id.tv_start_time);
        tvCurrentTime = (TextView)findViewById(R.id.tv_current_time);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        mStartDateTime = getDateTime();
        Log.i(TAG, "Get Start DateTime : " + mStartDateTime);
        tvStudyStartTime.setText(mStartDateTime);

        SlideToActView sta = (SlideToActView) findViewById(R.id.sld_unlock);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                Log.i(TAG, "onSlideComplete");
                insertStudyTimeToDb();
                finishTimer();
            }
        });

        CustomAnalogClock customAnalogClock = (CustomAnalogClock) findViewById(R.id.analog_clock);
        customAnalogClock.setAutoUpdate(true);

        startStudyTimer();
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
                        tvCurrentTime.setText(getDateTime());
                    }
                });
            }
        };
        studyTimer.schedule(timerTask,0, 1000);
    }

    private void finishTimer() {
        studyTimer.cancel();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("recent_timer", MyUtils.convertSecToTimeFormatString(mTotalStudySec));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void initAdmob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.lockScreenadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {

            @Override

            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG, "onAdLoaded");
            }
            @Override

            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d(TAG, "onAdFailedToLoad " + errorCode);

            }



            @Override

            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }



            @Override

            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }



            @Override

            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override

            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}

package com.example.examineetimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ncorti.slidetoact.SlideToActView;

import java.util.Timer;
import java.util.TimerTask;

public class LockscreenActivity extends Activity {
    private static final String TAG = "LockscreenActivity";
    private Timer studyTimer;

    private int totalStudySec;
    private TextView studyTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockscreen);

        studyTimeTextView = (TextView) findViewById(R.id.studyTimeTextView);

        startStudyTimer();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        SlideToActView sta = (SlideToActView) findViewById(R.id.sld_unlock);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                //Send Timer
                Log.i(TAG, "onSlideComplete");
                finishTimer();
            }
        });
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.i(TAG,"Home Key Down");
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
                //Log.i(TAG, "onKeyDown");
                //studyTimer.cancel();

                finishTimer();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startStudyTimer() {
        studyTimer = new Timer();
        totalStudySec = 0;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                totalStudySec++;
                Log.i(TAG,"Timer running..." + totalStudySec);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String timeString = makeTotalStudyTime(totalStudySec);
                        studyTimeTextView.setText(timeString);
                    }
                });
            }
        };
        studyTimer.schedule(timerTask,0, 1000);
    }

    private void finishTimer() {
        studyTimer.cancel();

        /*
         *TODO: save total time for history
         */

        finish();
    }

    private String makeTotalStudyTime(int totalStudySec) {
        int hours = totalStudySec / 3600;
        int minutes = (totalStudySec % 3600) / 60;
        int seconds = totalStudySec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}

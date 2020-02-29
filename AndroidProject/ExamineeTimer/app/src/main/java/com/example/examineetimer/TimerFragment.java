package com.example.examineetimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimerFragment extends Fragment {
    final private String TAG = "TimerFragment";

    public TimerFragment(){}

    final private int LOCKSCREEN_ACTIVITY_REQUEST_CODE = 0;

    TextView tvRecentStudyTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timer, container, false);

        Button btnOpenLockscreen = (Button)v.findViewById(R.id.btn_open_lockscreen);
        btnOpenLockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LockscreenActivity.class);
                startActivityForResult(intent, LOCKSCREEN_ACTIVITY_REQUEST_CODE);
            }
        });

        tvRecentStudyTime = (TextView)v.findViewById(R.id.tv_recent_study_time);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (LOCKSCREEN_ACTIVITY_REQUEST_CODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String returnValue = data.getStringExtra("recent_timer");
                    Log.i(TAG, "Return Value : " + returnValue);
                    tvRecentStudyTime.setText(returnValue);
                }
                break;
            }
        }
    }
}

package com.example.examineetimer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
    Context mContext;
    int mNumOfTabs;

    public SectionsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TimerFragment timerFragment = new TimerFragment();
                return timerFragment;
            case 1:
                DiaryFragment diaryFragment = new DiaryFragment();
                return diaryFragment;
            case 2:
                PrepareFragment prepareFragment = new PrepareFragment();
                return prepareFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }

}

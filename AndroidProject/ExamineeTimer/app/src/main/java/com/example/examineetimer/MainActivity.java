package com.example.examineetimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    final private String TAG = "MainActivity";
    private AdView mAdView;

    private ViewPager vpContainer;
    private BottomNavigationView botnavMain;
    private MenuItem prevBottomNavigationItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpContainer = (ViewPager)findViewById(R.id.container);
        botnavMain = (BottomNavigationView)findViewById(R.id.botnav_main);
        initAdmob();
        
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), 3);
        vpContainer.setAdapter(sectionsPagerAdapter);
        vpContainer.setCurrentItem(0);
        prevBottomNavigationItem = botnavMain.getMenu().getItem(0);
        botnavMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itm_timer:
                        Log.i(TAG, "Select Item 1");
                        vpContainer.setCurrentItem(0);
                        return true;
                    case R.id.itm_diary:
                        Log.i(TAG, "Select Item 2");
                        vpContainer.setCurrentItem(1);
                        return true;
                    case R.id.itm_prepare:
                        Log.i(TAG, "Select Item 3");
                        vpContainer.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        });

        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevBottomNavigationItem != null) {
                    prevBottomNavigationItem.setChecked(false);
                }
                prevBottomNavigationItem = botnavMain.getMenu().getItem(position);
                prevBottomNavigationItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void initAdmob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
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

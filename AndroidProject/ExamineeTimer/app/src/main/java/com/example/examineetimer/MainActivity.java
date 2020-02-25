package com.example.examineetimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    final private String TAG = "MainActivity";

    private ViewPager vpContainer;
    private BottomNavigationView botnavMain;
    private MenuItem prevBottomNavigationItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpContainer = (ViewPager)findViewById(R.id.container);
        botnavMain = (BottomNavigationView)findViewById(R.id.botnav_main);

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
}

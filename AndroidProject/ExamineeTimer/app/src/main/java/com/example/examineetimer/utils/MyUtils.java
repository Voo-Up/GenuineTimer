package com.example.examineetimer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyUtils {
    public static String convertSecToTimeFormatString(int totalStudySec) {
        int hours = totalStudySec / 3600;
        int minutes = (totalStudySec % 3600) / 60;
        int seconds = totalStudySec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String convertDateTimeToFormatString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}

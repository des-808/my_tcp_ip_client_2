package com.example.tcp_ip_client_2.classs;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class MessageTime {
    private static String time;
    static String pattern = "HH:mm:ss";
    @SuppressLint("SimpleDateFormat")
    private static java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);

    public static String getTime() {
        return time = dateFormat.format(new java.util.Date());
    }

}

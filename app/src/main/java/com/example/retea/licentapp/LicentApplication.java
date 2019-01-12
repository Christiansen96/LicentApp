package com.example.retea.licentapp;

import android.app.Application;

public class LicentApplication extends Application {

    private static final String TAG = "LicentApplication";

    public static LicentApplication applicationInstance;

    public static LicentApplication getInstance() {
        if (applicationInstance == null) {
            applicationInstance = new LicentApplication();
        }
        return applicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}

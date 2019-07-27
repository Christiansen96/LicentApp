package com.example.retea.licentapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sesionPref";


    private static SharedPrefManager sharedPrefManager;

    public static SharedPrefManager getInstance(Context context) {
        if (sharedPrefManager == null){
            initialize(context);
            return sharedPrefManager;
        }

        return sharedPrefManager;
    }

    private static void initialize(Context context) {

        sharedPrefManager = new SharedPrefManager(context);
    }


    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }
}

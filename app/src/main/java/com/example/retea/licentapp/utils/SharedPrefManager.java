package com.example.retea.licentapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private SharedPreferences sharedPreferences;
    private Context mContext;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sesionPref";


    public static SharedPrefManager sharedPrefManager;

    public static SharedPrefManager getInstance() {

        return sharedPrefManager;
    }

    public static void initialize(Context context) {

        sharedPrefManager = new SharedPrefManager(context);
    }


    public SharedPrefManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }
}

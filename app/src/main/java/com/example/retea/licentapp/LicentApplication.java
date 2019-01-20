package com.example.retea.licentapp;

import android.app.Application;
import android.util.Log;

import com.example.retea.licentapp.models.GeologicalPosition;

public class LicentApplication extends Application {
    private static final String TAG = "LicentApplication";

    public static LicentApplication applicationInstance;
    private static GeologicalPosition deviceGeoPos = new GeologicalPosition();
    private static String deviceAddress = "";

    public static LicentApplication getInstance(){
        if(applicationInstance == null){
            applicationInstance = new LicentApplication();
        }
        return applicationInstance;
    }

    public static void setDeviceGeologicalPosition(GeologicalPosition geoPos){
        deviceGeoPos.setLatitude(geoPos.getLatitude());
        deviceGeoPos.setLongitude(geoPos.getLongitude());
        Log.d(TAG, "setDeviceGeologicalPosition: latitude"+ deviceGeoPos.getLatitude());
        Log.d(TAG, "setDeviceGeologicalPosition: longitude"+ deviceGeoPos.getLongitude());
    }

    public static GeologicalPosition getDeviceGeologicalPosition(){
        return deviceGeoPos;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }



}

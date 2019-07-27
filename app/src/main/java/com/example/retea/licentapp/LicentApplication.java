package com.example.retea.licentapp;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;

import java.util.ArrayList;
import java.util.List;

public class LicentApplication extends Application {
    private static final String TAG = "LicentApplication";

    public static LicentApplication applicationInstance;
    private static GeologicalPosition deviceGeoPos = new GeologicalPosition();
    private static String deviceAddress = "";
    private static List<Provider> globalProviderList = new ArrayList<>();



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
        setProviders();
    }
    public void setProviders(){
        List<Service> dummyServiceList = new ArrayList<>();
        globalProviderList.add(new Provider(1,"Dentist","Stomatologie", new GeologicalPosition(44.458742, 26.131624),dummyServiceList,R.drawable.ic_home_black_24dp, Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg")));
        globalProviderList.add(new Provider(2,"Curatenie","Curatenie", new GeologicalPosition(44.458650, 26.130578),dummyServiceList,R.drawable.ic_home_black_24dp,Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg") ));
        globalProviderList.add(new Provider(3,"Masaj","Masaj", new GeologicalPosition(44.457188, 26.130584),dummyServiceList,R.drawable.ic_home_black_24dp,Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg") ));
        globalProviderList.add(new Provider(4,"Instalator","Instalator", new GeologicalPosition(44.456418, 26.128841),dummyServiceList,R.drawable.ic_home_black_24dp,Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg")));
        globalProviderList.add(new Provider(5,"Mobila","Mobila", new GeologicalPosition(44.456740, 26.139082),dummyServiceList,R.drawable.ic_home_black_24dp,Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg") ));
    }
    public static List<Provider> getProviders(){
        return globalProviderList;
    }



}

package com.example.retea.licentapp;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.interfaces.CallbackProviderDB;
import com.example.retea.licentapp.interfaces.CallbackServiceDB;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.example.retea.licentapp.networking.FirestoreFunctions;

import java.util.ArrayList;
import java.util.List;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public class LicentApplication extends Application {
    private static final String TAG = "LicentApplication";

    public static LicentApplication applicationInstance;
    private static GeologicalPosition deviceGeoPos = new GeologicalPosition();
    private static String deviceAddress = "";
    private static List<Provider> mHomeProviderList = new ArrayList<>();
    private static List<Provider> mAwayProviderList = new ArrayList<>();
    private static List<Service> mServiceList = new ArrayList<>();


    public static LicentApplication getInstance() {
        if (applicationInstance == null) {
            applicationInstance = new LicentApplication();
        }
        return applicationInstance;
    }

    public static void setDeviceGeologicalPosition(GeologicalPosition geoPos) {
        deviceGeoPos.setLatitude(geoPos.getLatitude());
        deviceGeoPos.setLongitude(geoPos.getLongitude());
        Log.d(TAG, "setDeviceGeologicalPosition: latitude" + deviceGeoPos.getLatitude());
        Log.d(TAG, "setDeviceGeologicalPosition: longitude" + deviceGeoPos.getLongitude());
    }

    public static GeologicalPosition getDeviceGeologicalPosition() {
        return deviceGeoPos;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        FirestoreFunctions.downloadServices(new CallbackServiceDB() {
            @Override
            public void onSuccess(@NonNull List<Service> serviceList) {
                Log.d(TAG, "onSuccess: download services: Got here");
                FirestoreFunctions.downloadProviders(serviceList, new CallbackProviderDB() {
                    @Override
                    public void onSuccess(@NonNull List<Provider> providerList) {
                        Log.d(TAG, "onSuccess: download providers: Got here");
                        
                        for(Provider provider : providerList){
                            if(provider.getType() == PROVIDER_TYPE_HOME){
                                mHomeProviderList.add(provider);
                                Log.d(TAG, "onSuccess: found home provider");
                            } else if(provider.getType()==PROVIDER_TYPE_AWAY){
                                mAwayProviderList.add(provider);
                                Log.d(TAG, "onSuccess: found away provider");
                            }
                        }
                    }
                });
            }
        });




    }


    public static List<Provider> getHomeProvidersList() {
        return mHomeProviderList;
    }

    public static List<Provider> getAwayProviderList() {
        return mAwayProviderList;
    }


}

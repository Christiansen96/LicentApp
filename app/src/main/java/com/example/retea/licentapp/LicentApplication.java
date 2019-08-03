package com.example.retea.licentapp;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public class LicentApplication extends Application {
    private static final String TAG = "LicentApplication";

    public static LicentApplication applicationInstance;
    private static GeologicalPosition deviceGeoPos = new GeologicalPosition();
    private static String deviceAddress = "";
    private static List<Provider> homeProviderList = new ArrayList<>();
    private static List<Provider> awayProviderList = new ArrayList<>();
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
        downloadServicesFromDB();
        downloadProvidersFromDB();
    }

    public void setProviders() {
        List<Service> dummyServiceList = new ArrayList<>();
        dummyServiceList.add(new Service("1", " Detartraj", "150.00", "2", "Detartraj profesional",
                "Pula mea s o sugi nuj bag si eu ceva mai lung",
                Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg"),"1"));


        homeProviderList.add(new Provider("1", "Dentist", "Stomatologie", new GeologicalPosition(44.458742, 26.131624), dummyServiceList, R.drawable.ic_home_black_24dp,
                Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg"), PROVIDER_TYPE_HOME));


        awayProviderList.add(new Provider("1", "Dentist", "Stomatologie", new GeologicalPosition(44.458742, 26.131624), dummyServiceList, R.drawable.ic_home_black_24dp,
                Uri.parse("https://www.familydentalcare.co.za/wp-content/uploads/2018/06/emergency-dentist-in-midrand-1024x542.jpg"), PROVIDER_TYPE_AWAY));

    }

    public static List<Provider> getHomeProvidersList() {
        return homeProviderList;
    }

    public static List<Provider> getAwayProviderList() {
        return awayProviderList;
    }

    public static void downloadServicesFromDB() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference serviceRef = firebaseFirestore.collection("services");
        mServiceList.clear();


        serviceRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String id = (String) documentSnapshot.get("id");
                            String name = (String) documentSnapshot.get("name");
                            String price = (String)documentSnapshot.get("price");
                            String duration = (String)documentSnapshot.get("duration");
                            String shortDescription = (String) documentSnapshot.get("shortDescription");
                            String longDescription = (String) documentSnapshot.get("longDescription");
                            String imageUri = (String) documentSnapshot.get("imageUri");
                            String providerId = (String) documentSnapshot.get("providerId");
                            Service service = new Service(id,name,price,duration,shortDescription,longDescription,Uri.parse(imageUri),providerId);
                            mServiceList.add(service);


                            Log.d(TAG, "onSuccess: " + service.toString());

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });




    }

    public static void downloadProvidersFromDB() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference providerRef = firebaseFirestore.collection("providers");
        homeProviderList.clear();
        awayProviderList.clear();



        providerRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {



                            String id = (String) documentSnapshot.get("id");
                            String name = (String) documentSnapshot.get("name");
                            String category = (String)documentSnapshot.get("category");
                            GeoPoint geoPoint = (GeoPoint)documentSnapshot.get("geoPoint");
                            int icon =Integer.parseInt(String.valueOf(documentSnapshot.get("icon")));
                            String imageUri = (String) documentSnapshot.get("imageUri");
                            int type =Integer.parseInt(String.valueOf(documentSnapshot.get("type")));
                            Provider provider = new Provider(id,name,category,new GeologicalPosition(geoPoint.getLatitude(),geoPoint.getLongitude()),icon,Uri.parse(imageUri),type);
                            List<Service> currentProviderServiceList = new ArrayList<>();
                            for(Service service : mServiceList){
                                if(service.getProviderId().equals(id)){
                                    currentProviderServiceList.add(service);
                                    Log.d(TAG, "onSuccess: for services FOUND ONE");
                                }
                            }
                            provider.setServiceList(currentProviderServiceList);
                            if(type == PROVIDER_TYPE_HOME){
                                homeProviderList.add(provider);
                            }else if(type == PROVIDER_TYPE_AWAY){
                                awayProviderList.add(provider);
                            }



                            Log.d(TAG, "onSuccess: " + provider.toString());

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });


    }


}

package com.example.retea.licentapp.networking;

import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.interfaces.CallbackProviderDB;
import com.example.retea.licentapp.interfaces.CallbackServiceDB;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreFunctions {
    private static final String TAG = "FirestoreFunctions";

    private ProgressDialog progressDialog;

    private static List<Provider> downloadedProviderList = new ArrayList<>();
    private static List<Service> mServiceList = new ArrayList<>();

    private static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static CollectionReference providerRef = firebaseFirestore.collection("providers");
    private static CollectionReference serviceRef = firebaseFirestore.collection("services");

    public static void downloadServices(final CallbackServiceDB callbackServiceDB) {

        serviceRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Service> services = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String id = (String) documentSnapshot.get("id");
                        String name = (String) documentSnapshot.get("name");
                        String price = (String) documentSnapshot.get("price");
                        String duration = (String) documentSnapshot.get("duration");
                        String shortDescription = (String) documentSnapshot.get("shortDescription");
                        String longDescription = (String) documentSnapshot.get("longDescription");
                        String imageUri = (String) documentSnapshot.get("imageUri");
                        String providerId = (String) documentSnapshot.get("providerId");
                        Service service = new Service(id, name, price, duration, shortDescription, longDescription, Uri.parse(imageUri), providerId);
                        services.add(service);
                        Log.d(TAG, "onSuccess: " + service.toString());
                    }
                    callbackServiceDB.onSuccess(services);
                }
            }
        });

    }

    public static void downloadProviders(final List<Service> serviceList, final CallbackProviderDB callbackProviderDB) {

        providerRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Provider> providers = new ArrayList<>();
                    List<Service> currentServices = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        currentServices.clear();
                        String id = (String) documentSnapshot.get("id");
                        String name = (String) documentSnapshot.get("name");
                        String category = (String) documentSnapshot.get("category");
                        GeoPoint geoPoint = (GeoPoint) documentSnapshot.get("geoPoint");
                        int icon = Integer.parseInt(String.valueOf(documentSnapshot.get("icon")));
                        String imageUri = (String) documentSnapshot.get("imageUri");
                        int type = Integer.parseInt(String.valueOf(documentSnapshot.get("type")));
                        Provider provider = new Provider(id, name, category, new GeologicalPosition(geoPoint.getLatitude(), geoPoint.getLongitude()), icon, Uri.parse(imageUri), type);

                        for (Service service : serviceList) {
                            if (service.getProviderId().equals(provider.getId())) {
                                currentServices.add(service);
                            }
                        }
                        provider.setServiceList(currentServices);
                        providers.add(provider);
                        Log.d(TAG, "onSuccess: " + provider.toString());
                    }
                    callbackProviderDB.onSuccess(providers);
                }
            }
        });

    }


}



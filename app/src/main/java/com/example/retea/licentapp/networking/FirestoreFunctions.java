package com.example.retea.licentapp.networking;

import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.interfaces.CallbackAppointmentDB;
import com.example.retea.licentapp.interfaces.CallbackProviderDB;
import com.example.retea.licentapp.interfaces.CallbackServiceDB;
import com.example.retea.licentapp.interfaces.CallbackUserDB;
import com.example.retea.licentapp.models.Appointment;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.example.retea.licentapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private static CollectionReference appointmentRef = firebaseFirestore.collection("appointments");
    private static CollectionReference userRef = firebaseFirestore.collection("users");
    

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


    public static void downloadAppointments(final CallbackAppointmentDB callbackAppointmentDB) {

        appointmentRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Appointment> appointments = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String customerId = (String) documentSnapshot.get("customerId");
                        if (customerId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                            String providerId = (String) documentSnapshot.get("providerId");
                            String providerName = (String) documentSnapshot.get("providerName");
                            String serviceId = (String) documentSnapshot.get("serviceId");
                            String serviceName = (String) documentSnapshot.get("serviceName");
                            int day = Integer.parseInt(String.valueOf(documentSnapshot.get("day")));
                            int month = Integer.parseInt(String.valueOf(documentSnapshot.get("month")));
                            int year = Integer.parseInt(String.valueOf(documentSnapshot.get("year")));
                            int hour = Integer.parseInt(String.valueOf(documentSnapshot.get("hour")));
                            int minute = Integer.parseInt(String.valueOf(documentSnapshot.get("minute")));
                            String notes = (String) documentSnapshot.get("notes");
                            boolean confirmed = (boolean) documentSnapshot.get("confirmed");
                            Log.d(TAG, "onComplete: found " +confirmed + "appointment");


                            Appointment appointment = new Appointment(customerId, providerId, providerName, serviceId, serviceName, day, month, year, hour, minute, notes, confirmed);
                            appointment.setId(documentSnapshot.getId());
                            appointments.add(appointment);
                            Log.d(TAG, "onSuccess: " + appointment.toString());
                        }
                    }
                    callbackAppointmentDB.onSuccess(appointments);
                }
            }
        });


    }

    public static void getUserFromDB(final CallbackUserDB callbackUserDB) {

        userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        User user = new User();
                        if(documentSnapshot.get("uid").equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            Log.d(TAG, "onComplete: FOUND A USER MATCHING ID WITH CURRENT DEVICE");
                            String name = (String) documentSnapshot.get("name");
                            String imageUri = (String) documentSnapshot.get("pictureUri");
                            String phoneNumber = (String) documentSnapshot.get("phoneNumber");
                            String address = (String) documentSnapshot.get("address");
                            String uid = (String) documentSnapshot.get("uid");

                            if(uid!=null){
                                user.setUid(uid);
                            }
                            
                            if(name!=null){
                                user.setName(name);
                            }
                            if(imageUri!=null){
                                user.setPictureUri(imageUri);
                            }
                            if(phoneNumber!=null){
                                user.setPhoneNumber(phoneNumber);
                            }
                            if(address!=null){
                                user.setAddress(address);
                            }
                            Log.d(TAG, "onSuccess: " + user.toString());
                        }
                        callbackUserDB.onSuccess(user);
                        
                    }
                }
            }
        });

    }



}



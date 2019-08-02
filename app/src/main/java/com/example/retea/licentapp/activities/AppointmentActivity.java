package com.example.retea.licentapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {
    private static final String TAG = "AppointmentActivity";

    private Button addToFirestoreButton;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        addToFirestoreButton = findViewById(R.id.addToFirestoreBtn);

        mFirestore = FirebaseFirestore.getInstance();
        addToFirestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Provider mProvider = LicentApplication.getHomeProvidersList().get(2);
//                for (Service service : mProvider.getServiceList()) {
//                    saveService(service);
//                }
                for(Provider provider : LicentApplication.getHomeProvidersList()){
                    saveProvider(provider);
                }


            }
        });
    }

    public void saveService(Service service) {

        Map<String, Object> serviceObjectMap = new HashMap<>();

        serviceObjectMap.put("id", service.getId());
        serviceObjectMap.put("name", service.getName());
        serviceObjectMap.put("duration", service.getDuration());
        serviceObjectMap.put("price", service.getPrice());
        serviceObjectMap.put("imageUri", String.valueOf(service.getImageUri()));
        serviceObjectMap.put("longDescription", service.getLongDescription());
        serviceObjectMap.put("shortDescription", service.getShortDescription());

        mFirestore.collection("services").document().set(serviceObjectMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: added service");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: couldn't add service");
                    }
                });


    }

    public void saveProvider(Provider provider) {

        Map<String, Object> providerObjectMap = new HashMap<>();

        providerObjectMap.put("id", provider.getId());
        providerObjectMap.put("name", provider.getName());
        providerObjectMap.put("category", provider.getCategory());
//        providerObjectMap.put("serviceList", provider.getServiceList());
        providerObjectMap.put("icon", provider.getIcon());
        providerObjectMap.put("imageUri", String.valueOf(provider.getImageUri()));
        providerObjectMap.put("geologicalPosition", provider.getProviderGeologicalPosition());
        providerObjectMap.put("type", provider.getType());
        mFirestore.collection("providers").document().set(providerObjectMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: added provider");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: couldn't add provider");
                    }
                });

    }
}


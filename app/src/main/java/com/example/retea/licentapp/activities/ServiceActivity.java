package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

public class ServiceActivity extends AppCompatActivity {
    private static final String TAG = "ServiceActivity";

    private TextView serviceName;
    private TextView serviceLongDescription;
    private ImageView serviceImage;
    private Button getAppointmentButton;
    private TextView servicePrice;
    private TextView serviceDuration;

    private Provider mCurrentProvider;
    private Service mCurrentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent incomingIntent = this.getIntent();
        if (incomingIntent != null) {
            checkIntentForProviderInfo(incomingIntent);
        } else {
            Toast.makeText(this, "Didn't find any intent.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentProvider == null) {
            Toast.makeText(this, "Couldn't find a matching provider.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentService == null) {
            Toast.makeText(this, "Couldn't find a matching service.", Toast.LENGTH_SHORT).show();
            finish();
        }

        serviceImage = findViewById(R.id.OpenedServiceImageId);
        serviceName = findViewById(R.id.OpenedServiceNameId);
        serviceLongDescription = findViewById(R.id.OpenedServiceLongDescriptionId);
        getAppointmentButton = findViewById(R.id.OpenedServiceGetAppointmentButtonId);
        servicePrice = findViewById(R.id.OpenedServicePriceId);
        serviceDuration = findViewById(R.id.OpenedServiceDurationId);

        Picasso.get().load(mCurrentService.getImageUri()).into(serviceImage);
        serviceName.setText(mCurrentService.getName());
        serviceLongDescription.setText(mCurrentService.getLongDescription());
        if (mCurrentService.getDuration() != 0) {
            String duration = "Duration: ~" + mCurrentService.getDuration() + "h";
            serviceDuration.setText(duration);
        } else serviceDuration.setText("");
        if (mCurrentService.getPrice() != 0) {
            String price = "Price: " + mCurrentService.getPrice() + " lei";
            servicePrice.setText(price);
        } else servicePrice.setText("");

        getAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicked appointment.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: appointment");
            }
        });


    }

    private void checkIntentForProviderInfo(Intent intent) {

        int providerId = intent.getExtras().getInt("providerId");
        Log.d(TAG, "onCreate: id " + providerId);
        if (providerId != 0) {
            for (Provider provider : LicentApplication.getProviders()) {
                if (providerId == provider.getId()) {
                    mCurrentProvider = provider;
                }
            }
        }

        int serviceId = intent.getExtras().getInt("serviceId");
        Log.d(TAG, "onCreate: service with id " + serviceId);
        if (mCurrentProvider != null) {
            if (serviceId != 0) {
                for (Service service : mCurrentProvider.getServiceList()) {
                    if (serviceId == service.getId()) {
                        mCurrentService = service;
                    }
                }
            }
        }
    }
}

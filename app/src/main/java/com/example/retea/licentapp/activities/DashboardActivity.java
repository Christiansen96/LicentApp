package com.example.retea.licentapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public class DashboardActivity extends BaseNavigationDrawer {
    private static final String TAG = "DashboardActivity";

    private TextView AddressTextView;
    private LinearLayout HomeLayout;
    private LinearLayout AwayLayout;
    private Button goToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "onCreate: entered");

        AddressTextView = findViewById(R.id.AddressTextViewId);
        HomeLayout = findViewById(R.id.HomeLayout);
        AwayLayout = findViewById(R.id.AwayLayout);
        goToLoginBtn = findViewById(R.id.goToLogin);
        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, AuthenticationMethodsActivity.class));
            }
        });

        getAddresFromLocation();

        HomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.putExtra("providersType", PROVIDER_TYPE_HOME);
                intent.putExtra("tab",1);
                startActivity(intent);
            }
        });
        AwayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                intent.putExtra("providersType", PROVIDER_TYPE_AWAY);
                intent.putExtra("tab",1);
                startActivity(intent);
            }
        });



    }

    private int backpress = 0;
    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress > 1) {
            super.onBackPressed();
            finishAffinity();

        }else{
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backpress = 0;
                }
            }, 2000);
        }
    }

    @Override
    protected int getNavigationItemId() {
        return R.id.nav_dashboard;
    }

    private void getDeviceAddress(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            Log.d(TAG, "getDeviceAddress: list size is " + addressList.size());
            String address = addressList.get(0).getAddressLine(0);
            Log.d(TAG, "getDeviceAddress: " + address);
            AddressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void getAddresFromLocation(){

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DashboardActivity.this);

        try{

            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();
                        Geocoder geocoder = new Geocoder(DashboardActivity.this, Locale.getDefault());

                        try {
                            List<Address> addressList = geocoder.getFromLocation(currentLocation.getLatitude(),
                                    currentLocation.getLongitude(), 1);
                            Log.d(TAG, "getDeviceAddress: list size is " + addressList.size());
                            String address = addressList.get(0).getAddressLine(0);
                            Log.d(TAG, "getDeviceAddress: " + address);
                            AddressTextView.setText(address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(DashboardActivity.this,"unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "getLocation: " + e.getMessage());
        }
    }


}

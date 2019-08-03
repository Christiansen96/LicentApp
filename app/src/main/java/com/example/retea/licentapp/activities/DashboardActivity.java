package com.example.retea.licentapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;

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

        getDeviceAddress();

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

        LicentApplication.downloadProviderListFromDB();


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

    private void getDeviceAddress() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
//            List<Address> addressList = geocoder.getFromLocation(LicentApplication.getInstance().getDeviceGeologicalPosition().getLatitude(),
//                    LicentApplication.getInstance().getDeviceGeologicalPosition().getLongitude(), 1);
            List<Address> addressList = geocoder.getFromLocation(44.4628556,
                    26.1310713, 1);
            Log.d(TAG, "getDeviceAddress: list size is " + addressList.size());
            String address = addressList.get(0).getAddressLine(0);
            Log.d(TAG, "getDeviceAddress: " + address);
            AddressTextView.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

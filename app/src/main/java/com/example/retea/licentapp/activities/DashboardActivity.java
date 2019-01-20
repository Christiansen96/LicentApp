package com.example.retea.licentapp.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.GeologicalPosition;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {
    private static final String TAG = "DashboardActivity";

    private TextView AddressTextView;
    private Button FavouritesButton;
    private Button NearbyButton;
    private Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Log.d(TAG, "onCreate: entered");

        AddressTextView = findViewById(R.id.AddressTextViewId);
        FavouritesButton = findViewById(R.id.FavouritesButtonId);
        NearbyButton = findViewById(R.id.NearbyButtonId);
        SearchButton = findViewById(R.id.SearchButtonId);

        getDeviceAddress();

        FavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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

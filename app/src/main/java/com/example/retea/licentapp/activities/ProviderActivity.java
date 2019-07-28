package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.retea.licentapp.R;
import com.google.android.gms.maps.model.LatLng;

public class ProviderActivity extends AppCompatActivity {
    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        Intent intent = this.getIntent();
        if (intent != null) {
            int intentId = intent.getExtras().getInt("providerId");
            Log.d(TAG, "onCreate: id " + intentId);
            LatLng intentPosition = (LatLng) intent.getExtras().get("position");
            Log.d(TAG, "onCreate: position " + intentPosition);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

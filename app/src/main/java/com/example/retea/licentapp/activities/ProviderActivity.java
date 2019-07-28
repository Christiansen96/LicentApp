package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.adapters.ServiceListAdapter;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProviderActivity extends AppCompatActivity implements ServiceListAdapter.OnItemClickListener {
    private static final String TAG = "ProviderActivity";

    private ImageView mCurrentProviderImage;
    private ImageView mCurrentProviderIcon;
    private TextView mCurrentProviderName;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView serviceListRecyclerView;

    private Provider mCurrentProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
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

        mCurrentProviderImage = findViewById(R.id.OpenedProviderImageId);
        mCurrentProviderIcon = findViewById(R.id.OpenedProviderIconId);
        mCurrentProviderName = findViewById(R.id.OpenedProviderNameId);

        Picasso.get().load(mCurrentProvider.getImageUri()).into(mCurrentProviderImage);
        mCurrentProviderIcon.setImageResource(mCurrentProvider.getIcon());
        mCurrentProviderName.setText(mCurrentProvider.getName());


        List<Service> mServiceList = mCurrentProvider.getServiceList();

        serviceListRecyclerView = findViewById(R.id.ServiceListRecyclerViewId);

        ServiceListAdapter serviceListAdapter = new ServiceListAdapter(mServiceList, this);
        mLayoutManager = new GridLayoutManager(this, 2);
        serviceListRecyclerView.setLayoutManager(mLayoutManager);
        serviceListRecyclerView.setHasFixedSize(true);

        serviceListRecyclerView.setAdapter(serviceListAdapter);
        serviceListRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void checkIntentForProviderInfo(Intent intent) {

        int intentId = intent.getExtras().getInt("providerId");
        Log.d(TAG, "onCreate: id " + intentId);
        if (intentId != 0) {
            for (Provider provider : LicentApplication.getProviders()) {
                if (intentId == provider.getId()) {
                    mCurrentProvider = provider;
                    break;
                }
            }
        }


        LatLng intentPosition = (LatLng) intent.getExtras().get("position");
        Log.d(TAG, "onCreate: position " + intentPosition);
        if (intentPosition != null) {
            for (Provider provider : LicentApplication.getProviders()) {
                if (intentPosition.latitude == provider.getProviderGeologicalPosition().getLatitude() && intentPosition.longitude == provider.getProviderGeologicalPosition().getLongitude()) {
                    mCurrentProvider = provider;
                    break;
                }
            }
        }

    }

    @Override
    public void onItemClick(Service service) {
        Toast.makeText(this, "Service " + service.getName() + " clicked. ", Toast.LENGTH_SHORT).show();

    }
}

package com.example.retea.licentapp.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.activities.ProviderActivity;
import com.example.retea.licentapp.adapters.ProviderListAdapter;
import com.example.retea.licentapp.models.ClusterMarker;
import com.example.retea.licentapp.models.GeologicalPosition;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.utils.MyClusterManagerRenderer;
import com.example.retea.licentapp.utils.ViewWeightAnimationWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public class NearbyFragment extends Fragment implements OnMapReadyCallback,
        ProviderListAdapter.OnItemClickListener,
        View.OnClickListener,
        GoogleMap.OnInfoWindowClickListener {

    private static final String TAG = "NearbyFragment";
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;


    private MapView mMapView;
    private RelativeLayout mMapContainer;
    private RecyclerView providerListRecyclerView;

    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private GeologicalPosition mGeologicalPosition;
    private RecyclerView.LayoutManager layoutManager;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers;
    private int mMapLayoutState = 0;
    private int mProvidersType;
    private List<Provider> mProviderList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_tab_fragment, container, false);
        mMapView = view.findViewById(R.id.fragment_embedded_map_view_mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapContainer = view.findViewById(R.id.map_container);
        view.findViewById(R.id.btn_full_screen_map).setOnClickListener(this);
        if (getArguments() != null) {
            Log.d(TAG, "onCreateView: getArguments not null");
            mProvidersType = getArguments().getInt("providersType");
            Log.d(TAG, "onCreateView: " + mProvidersType);
        }
        if (mProvidersType == PROVIDER_TYPE_HOME){
            mProviderList = LicentApplication.getHomeProvidersList();
        }else if(mProvidersType == PROVIDER_TYPE_AWAY){
            mProviderList = LicentApplication.getAwayProviderList();
        }


        providerListRecyclerView = view.findViewById(R.id.ProviderListRecyclerView);
        providerListRecyclerView.setHasFixedSize(true);

        ProviderListAdapter providerListAdapter = new ProviderListAdapter(mProviderList, this);
        layoutManager = new LinearLayoutManager(this.getContext());
        providerListRecyclerView.setLayoutManager(layoutManager);

        providerListRecyclerView.setAdapter(providerListAdapter);
        providerListRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    private void addMapMarkers() {

        if (mGoogleMap != null) {

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(Objects.requireNonNull(getActivity()).getApplicationContext(), mGoogleMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for (Provider provider : mProviderList) {

                Log.d(TAG, "addMapMarkers: location: " + provider.getProviderGeologicalPosition().toString());
                try {
                    String snippet;
                    snippet = getProviderAddress(provider.getProviderGeologicalPosition());


                    int providerIcon = R.drawable.powered_by_google_light; // set the default avatar
                    try {
                        providerIcon = provider.getIcon();
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "addMapMarkers: no avatar for " + provider.getName() + ", setting default.");
                    }
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(provider.getProviderGeologicalPosition().getLatitude(), provider.getProviderGeologicalPosition().getLongitude()),
                            provider.getName(),
                            snippet,
                            providerIcon,
                            provider

                    );

                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);

                } catch (NullPointerException e) {
                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                }

            }
            mClusterManager.cluster();

            setCameraView(LicentApplication.getDeviceGeologicalPosition());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;

        addMapMarkers();
        mGoogleMap.setOnInfoWindowClickListener(this);

    }

    private void setCameraView(GeologicalPosition geologicalPosition) {

        double bottomBoundary = geologicalPosition.getLatitude() - .005;
        double leftBoundary = geologicalPosition.getLongitude() - .005;
        double topBoundary = geologicalPosition.getLatitude() + .005;
        double rightBoundary = geologicalPosition.getLongitude() + .005;
        mMapBoundary = new LatLngBounds(new LatLng(bottomBoundary, leftBoundary), new LatLng(topBoundary, rightBoundary));

        Log.d(TAG, "moveCamera: moving camera to: lat:" + geologicalPosition.getLatitude() + ", lng: " + geologicalPosition.getLongitude());
        LatLng latLng = new LatLng(geologicalPosition.getLatitude(), geologicalPosition.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));

    }


    @Override
    public void onItemClick(Provider provider) {
        Intent intent = new Intent(this.getContext(), ProviderActivity.class);
        intent.putExtra("providerId", provider.getId());
        intent.putExtra("providersType",provider.getType());
        startActivity(intent);
        Log.d(TAG, "onItemClick: " + provider.toString());
    }

    private void expandMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                50,
                100);
        mapAnimation.setDuration(300);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(providerListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                50,
                0);
        recyclerAnimation.setDuration(300);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    private void contractMapAnimation() {
        ViewWeightAnimationWrapper mapAnimationWrapper = new ViewWeightAnimationWrapper(mMapContainer);
        ObjectAnimator mapAnimation = ObjectAnimator.ofFloat(mapAnimationWrapper,
                "weight",
                100,
                50);
        mapAnimation.setDuration(300);

        ViewWeightAnimationWrapper recyclerAnimationWrapper = new ViewWeightAnimationWrapper(providerListRecyclerView);
        ObjectAnimator recyclerAnimation = ObjectAnimator.ofFloat(recyclerAnimationWrapper,
                "weight",
                0,
                50);
        recyclerAnimation.setDuration(300);

        recyclerAnimation.start();
        mapAnimation.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_full_screen_map) {
            if (mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED) {
                mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                expandMapAnimation();
            } else if (mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED) {
                mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                contractMapAnimation();
            }
        }
    }

    private String getProviderAddress(GeologicalPosition geologicalPosition) {

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
//            List<Address> addressList = geocoder.getFromLocation(LicentApplication.getInstance().getDeviceGeologicalPosition().getLatitude(),
//                    LicentApplication.getInstance().getDeviceGeologicalPosition().getLongitude(), 1);
            List<Address> addressList = geocoder.getFromLocation(geologicalPosition.getLatitude(),
                    geologicalPosition.getLongitude(), 1);
            String address = addressList.get(0).getAddressLine(0);
            Log.d(TAG, "getProviderAddress: " + address);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Pe undeva pe aici";
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this.getContext(), ProviderActivity.class);
        intent.putExtra("position", marker.getPosition());
        startActivity(intent);
    }
}

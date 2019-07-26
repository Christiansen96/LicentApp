package com.example.retea.licentapp.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.List;

public class NearbyFragment extends Fragment implements OnMapReadyCallback, ProviderListAdapter.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "NearbyFragment";
    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int  MAP_LAYOUT_STATE_EXPANDED = 1;


    protected MapView mMapView;
    private RelativeLayout mMapContainer;
    private RecyclerView providerListRecyclerView;

    private GoogleMap mGoogleMap;
    private LatLngBounds mMapBoundary;
    private GeologicalPosition mGeologicalPosition;
    private RecyclerView.LayoutManager layoutManager;
    private ClusterManager mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers;
    private int mMapLayoutState = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_tab_fragment, container, false);
        mMapView = view.findViewById(R.id.fragment_embedded_map_view_mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapContainer = view.findViewById(R.id.map_container);
        view.findViewById(R.id.btn_full_screen_map).setOnClickListener(this);

        List<Provider> providers = LicentApplication.getProviders();

        providerListRecyclerView = (RecyclerView) view.findViewById(R.id.ProviderListRecyclerView);
        providerListRecyclerView.setHasFixedSize(true);

        ProviderListAdapter providerListAdapter = new ProviderListAdapter(providers, this);
        layoutManager = new LinearLayoutManager(this.getContext());
        providerListRecyclerView.setLayoutManager(layoutManager);

        providerListRecyclerView.setAdapter(providerListAdapter);
        providerListRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return view;
    }

    private void addMapMarkers() {

        if (mGoogleMap != null) {

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<ClusterMarker>(getActivity().getApplicationContext(), mGoogleMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new MyClusterManagerRenderer(
                        getActivity(),
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for (Provider provider : LicentApplication.getProviders()) {

                Log.d(TAG, "addMapMarkers: location: " + provider.getProviderGeologicalPosition().toString());
                try {
                    String snippet = "";
                    if (provider.getCategory().equals("Stomatologie")) {
                        snippet = "Stomatologie";
                    } else if (provider.getCategory().equals("Curatenie")) {
                        snippet = "Curatenie";
                    } else if (provider.getCategory().equals("Masaj")) {
                        snippet = "Masaj";
                    } else if (provider.getCategory().equals("Instalator")) {
                        snippet = "Instalator";
                    } else if (provider.getCategory().equals("Mobila")) {
                        snippet = "Mobila";
                    }


                    int providerImage = R.drawable.powered_by_google_light; // set the default avatar
                    try {
                        providerImage = provider.getImage();
                    } catch (NumberFormatException e) {
                        Log.d(TAG, "addMapMarkers: no avatar for " + provider.getName() + ", setting default.");
                    }
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(provider.getProviderGeologicalPosition().getLatitude(), provider.getProviderGeologicalPosition().getLongitude()),
                            provider.getName(),
                            snippet,
                            providerImage,
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;

        addMapMarkers();


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
        startActivity(intent);
        Log.d(TAG, "onItemClick: " + provider.toString());
    }

    private void expandMapAnimation(){
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

    private void contractMapAnimation(){
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
        switch (view.getId()){
            case R.id.btn_full_screen_map:{

                if(mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED){
                    mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
                    expandMapAnimation();
                }
                else if(mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED){
                    mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
                    contractMapAnimation();
                }
                break;
            }

        }
    }
}

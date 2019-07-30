package com.example.retea.licentapp.activities;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.retea.licentapp.R;
import com.example.retea.licentapp.adapters.MainFragmentAdapter;
import com.example.retea.licentapp.fragments.FavouritesFragment;
import com.example.retea.licentapp.fragments.NearbyFragment;
import com.example.retea.licentapp.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainFragmentAdapter mMainFragmentAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting");

        mMainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(1).select();

    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavouritesFragment(), "Favourites");
        adapter.addFragment(new NearbyFragment(), "Nearby");
        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);

    }
}
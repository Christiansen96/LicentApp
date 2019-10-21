package com.example.retea.licentapp.activities;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.retea.licentapp.R;
import com.example.retea.licentapp.adapters.MainFragmentAdapter;
import com.example.retea.licentapp.fragments.FavouritesFragment;
import com.example.retea.licentapp.fragments.NearbyFragment;
import com.example.retea.licentapp.fragments.SearchFragment;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;


public class MainActivity extends AppCompatActivity {
    ;

    private static final String TAG = "MainActivity";

    private MainFragmentAdapter mMainFragmentAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private int mProvidersType;
    private int mTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting");
        mTabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);
        Intent incomingIntent = this.getIntent();
        if (incomingIntent != null){
            checkIncomingIntent(incomingIntent);
        } else {
            Toast.makeText(this, "Didn't find any intent.", Toast.LENGTH_SHORT).show();
            finish();
        }
        mMainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());

        Log.d(TAG, "onCreate: tab is " +mTab);
        mTabLayout.getTabAt(mTab);
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTab);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(new FavouritesFragment(), "Favourites");
        Fragment nearbyFragment = new NearbyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("providersType", mProvidersType);
        nearbyFragment.setArguments(bundle);
        adapter.addFragment(nearbyFragment, "Nearby");
        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);

    }

    private void checkIncomingIntent(Intent intent) {
        Log.d(TAG, "checkIncomingIntent: looking for search boolean from intent" );
        mTab = intent.getExtras().getInt("tab");
        mProvidersType = intent.getExtras().getInt("providersType");
        Log.d(TAG, "checkIncomingIntent: "+ mProvidersType);

    }
}
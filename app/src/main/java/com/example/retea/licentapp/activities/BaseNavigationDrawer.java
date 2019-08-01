package com.example.retea.licentapp.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.retea.licentapp.R;
import com.google.android.material.navigation.NavigationView;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public abstract class BaseNavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "BaseNavigationDrawer";

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FrameLayout container;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_drawer_activity);

        container = findViewById(R.id.main_container);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (container != null) {
            container.addView(view, params);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (container != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, container, false);
            container.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (container != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(view, lp);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            drawerLayout.closeDrawer(GravityCompat.START, false);
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        drawerLayout.closeDrawer(GravityCompat.START);

        final int mItemId = menuItem.getItemId();
        if (mItemId == getNavigationItemId()) {
            return true;
        }
        onMenuItemClick(mItemId);
        return true;
    }

    public void onMenuItemClick(int item) {
        switch (item) {
            case R.id.nav_dashboard:
                Log.d(TAG, "onMenuItemClick:  open dashboard activity");
                Intent dashboardActivityIntent = new Intent(this,DashboardActivity.class);
                startActivity(dashboardActivityIntent);

                break;
            case R.id.nav_profile:
                Log.d(TAG, "onMenuItemClick:  open profile activity");
                Intent myProfileActivityIntent = new Intent(this,MyProfileActivity.class);
                startActivity(myProfileActivityIntent);

                break;
            case R.id.nav_home_providers:
                Log.d(TAG, "onMenuItemClick:  open home providers activity");
                Intent homeProvidersActivityIntent = new Intent(this,MainActivity.class);
                homeProvidersActivityIntent.putExtra("providersType",PROVIDER_TYPE_HOME);
                homeProvidersActivityIntent.putExtra("tab",1);
                startActivity(homeProvidersActivityIntent);

                break;
            case R.id.nav_away_providers:
                Log.d(TAG, "onMenuItemClick:  open away providers activity");
                Intent awayProvidersActivityIntent = new Intent(this,MainActivity.class);
                awayProvidersActivityIntent.putExtra("providersType",PROVIDER_TYPE_AWAY);
                awayProvidersActivityIntent.putExtra("tab",1);
                startActivity(awayProvidersActivityIntent);

                break;
            case R.id.nav_search:
                Log.d(TAG, "onMenuItemClick:  open main activity with search fragment focused");
                Intent searchActivityIntent = new Intent(this,MainActivity.class);
                searchActivityIntent.putExtra("tab",2);
                searchActivityIntent.putExtra("providersType",PROVIDER_TYPE_AWAY);
                startActivity(searchActivityIntent);

                break;

        }
    }
    protected abstract int getNavigationItemId();
}

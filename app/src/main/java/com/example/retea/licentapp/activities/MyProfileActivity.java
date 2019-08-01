package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.retea.licentapp.R;

public class MyProfileActivity extends BaseNavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    @Override
    protected int getNavigationItemId() {
        return R.id.nav_profile;
    }
}

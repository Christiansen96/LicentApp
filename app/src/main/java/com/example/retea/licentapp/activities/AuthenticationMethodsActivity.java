package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.retea.licentapp.R;

public class AuthenticationMethodsActivity extends AppCompatActivity {

    Button emailAndPassButton, facebookButton, googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_methods);

        emailAndPassButton = findViewById(R.id.EmailAndPasswordLoginButton);
        facebookButton = findViewById(R.id.FacebookLoginButton);
        googleButton = findViewById(R.id.GoogleLoginButton);

        emailAndPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthenticationMethodsActivity.this, EmailAndPasswordAuthenticationActivity.class));
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthenticationMethodsActivity.this, FacebookAuthenticationActivity.class));
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AuthenticationMethodsActivity.this, GoogleAuthenticationActivity.class));
            }
        });
    }
}

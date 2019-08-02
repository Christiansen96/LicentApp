package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.retea.licentapp.R;

public class AppointmentActivity extends AppCompatActivity {

    private Button addToFirestoreButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        addToFirestoreButton = findViewById(R.id.addToFirestoreBtn);








    }
}

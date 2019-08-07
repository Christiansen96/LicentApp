package com.example.retea.licentapp.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import com.example.retea.licentapp.R;
import com.google.firebase.firestore.FirebaseFirestore;


public class AppointmentActivity extends BaseNavigationDrawer {
    private static final String TAG = "AppointmentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
    }

    @Override
    protected int getNavigationItemId() {
        return 5;
    }
}


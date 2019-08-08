package com.example.retea.licentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.Appointment;

import java.util.ArrayList;
import java.util.List;


public class AppointmentActivity extends BaseNavigationDrawer {
    private static final String TAG = "AppointmentActivity";

    private List<Appointment> mAppList = new ArrayList<>();
    private Appointment currentAppointment;

    TextView middleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        middleText = findViewById(R.id.confirmedTextViewId);
        mAppList.addAll(LicentApplication.getAppointmentList());
        Intent incomingIntent = this.getIntent();
        if (incomingIntent != null) {
            checkIncomingIntent(incomingIntent);
        }

        if(currentAppointment.isConfirmed()){
            middleText.setText("APPOINTMENT IS CONFIRMED, BE THERE ON TIME!");
        }else{
            middleText.setText("APPOINTMENT AWAITING CONFIRMATION, STAY ON YOUR TOES!");
        }




    }

    private void checkIncomingIntent(Intent intent) {

        String appointmentId = (String) intent.getExtras().get("appointmentId");
        if(appointmentId!=null){
            for(Appointment appointment : mAppList){
                if(appointment.getId().equals(appointmentId)){
                    currentAppointment = appointment;
                    Log.d(TAG, "checkIncomingIntent: FOUND APPOINTMENT MATCHING ID");
                }else{
                    Log.d(TAG, "checkIncomingIntent: COULDN'T FIND APPOINTMENT MATCHING ID");
                    finish();
                }
            }
        }

    }

    @Override
    protected int getNavigationItemId() {
        return 5;
    }
}


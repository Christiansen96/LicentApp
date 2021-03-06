package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.fragments.TextInputDialog;
import com.example.retea.licentapp.models.Appointment;
import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_AWAY;
import static com.example.retea.licentapp.utils.Constants.PROVIDER_TYPE_HOME;

public class ServiceActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, TextInputDialog.TextInputDialogListener {
    private static final String TAG = "ServiceActivity";

    private TextView serviceName;
    private TextView serviceLongDescription;
    private ImageView serviceImage;
    private Button getAppointmentButton;
    private TextView servicePrice;
    private TextView serviceDuration;
    private TextView shownDate;
    private TextView shownTime;
    private Button chooseTimeButton;
    private Button chooseDateButton;
    private Button addNoteButton;

    private Provider mCurrentProvider;
    private Service mCurrentService;
    private int mProviderType;

    private Integer mAppointmentDay;
    private Integer mAppointmentMonth;
    private Integer mAppointmentYear;
    private Integer mAppointmentHour;
    private Integer mAppointmentMinute;
    private String mNote = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Intent incomingIntent = this.getIntent();
        if (incomingIntent != null) {
            checkIntentForProviderInfo(incomingIntent);
        } else {
            Toast.makeText(this, "Didn't find any intent.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentProvider == null) {
            Toast.makeText(this, "Couldn't find a matching provider.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (mCurrentService == null) {
            Toast.makeText(this, "Couldn't find a matching service.", Toast.LENGTH_SHORT).show();
            finish();
        }

        serviceImage = findViewById(R.id.OpenedServiceImageId);
        serviceName = findViewById(R.id.OpenedServiceNameId);
        serviceLongDescription = findViewById(R.id.OpenedServiceLongDescriptionId);
        getAppointmentButton = findViewById(R.id.OpenedServiceGetAppointmentButtonId);
        servicePrice = findViewById(R.id.OpenedServicePriceId);
        serviceDuration = findViewById(R.id.OpenedServiceDurationId);
        chooseDateButton = findViewById(R.id.ChooseDateId);
        chooseTimeButton = findViewById(R.id.ChooseTimeId);
        shownDate = findViewById(R.id.ShownDateId);
        shownTime = findViewById(R.id.ShownTimeId);
        addNoteButton = findViewById(R.id.addNoteButton);
        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTextDialog();
            }
        });

        Picasso.get().load(mCurrentService.getImageUri()).into(serviceImage);
        serviceName.setText(mCurrentService.getName());
        serviceLongDescription.setText(mCurrentService.getLongDescription());
        if (!mCurrentService.getDuration().equals("0")) {
            String duration = "Duration: ~" + mCurrentService.getDuration() + "h";
            serviceDuration.setText(duration);
        } else serviceDuration.setText("");
        if (!mCurrentService.getPrice().equals("0")) {
            String price = "Price: " + mCurrentService.getPrice() + " lei";
            servicePrice.setText(price);
        } else servicePrice.setText("");
        getAppointmentButton.setVisibility(View.GONE);


        getAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CollectionReference appointmentRef = FirebaseFirestore.getInstance().collection("appointments");
                appointmentRef.add(new Appointment(FirebaseAuth.getInstance().getCurrentUser().getUid(), mCurrentProvider.getId(), mCurrentProvider.getName(),
                        mCurrentService.getId(), mCurrentService.getName(), mAppointmentDay, mAppointmentMonth, mAppointmentYear, mAppointmentHour, mAppointmentMinute, mNote, false));

                Intent intent = new Intent(ServiceActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });


    }

    public void openTextDialog() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.show(getSupportFragmentManager(), "TextInputDialog");

    }

    private void checkIntentForProviderInfo(Intent intent) {

        mProviderType = intent.getExtras().getInt("providersType");

        String providerId = (String) intent.getExtras().get("providerId");
        Log.d(TAG, "onCreate: id " + providerId);

        if (mProviderType == PROVIDER_TYPE_HOME) {
            if (!providerId.equals("0")) {
                for (Provider provider : LicentApplication.getHomeProvidersList()) {
                    if (providerId.equals(provider.getId())) {
                        mCurrentProvider = provider;
                    }
                }
            }

        } else if (mProviderType == PROVIDER_TYPE_AWAY) {
            if (!providerId.equals("0")) {
                for (Provider provider : LicentApplication.getAwayProviderList()) {
                    if (providerId.equals(provider.getId())) {
                        mCurrentProvider = provider;
                    }
                }
            }

        }

        String serviceId = (String) intent.getExtras().get("serviceId");
        Log.d(TAG, "onCreate: service with id " + serviceId);
        if (mCurrentProvider != null) {
            if (!serviceId.equals("0")) {
                for (Service service : mCurrentProvider.getServiceList()) {
                    if (serviceId.equals(service.getId())) {
                        mCurrentService = service;
                    }
                }
            }
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                false
        );
        timePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {


        String date = dayOfMonth + "/" + month + "/" + year;
        shownDate.setText(date);
        mAppointmentDay = dayOfMonth;
        mAppointmentMonth = month;
        mAppointmentYear = year;

        if (mAppointmentHour != null && mAppointmentMinute != null) {

            getAppointmentButton.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String lMinute = String.valueOf(minute);
        if (minute < 10) {
            lMinute = "0" + minute;
        }
        String lHour = String.valueOf(hour);
        if (hour < 10) {
            lHour = "0" + hour;
        }
        String time = lHour + ":" + lMinute + " PM";
        shownTime.setText(time);

        mAppointmentHour = hour;
        mAppointmentMinute = minute;

        if (mAppointmentDay != null && mAppointmentMonth != null && mAppointmentYear != null) {

            getAppointmentButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void applyText(String note) {
        mNote = note;


    }
}

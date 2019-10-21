package com.example.retea.licentapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retea.licentapp.R;
import com.example.retea.licentapp.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private TextInputEditText textInputName;
    private TextInputEditText textInputPhone;
    private TextView addressText;

    private Button setNameButton;
    private Button setPhoneButton;
    private Button importPictureButton;
    private Button importLocationButton;
    private Button saveButton;

    private String mName = "";
    private String mAddress;
    private String mPhoneNumber = "";
    private String mPictureUri;

    int SELECT_FILE = 0;
    int PERMISSION_REQUEST = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textInputName = findViewById(R.id.SettingsInputNameId);
        textInputPhone = findViewById(R.id.SettingsInputPhoneId);
        addressText = findViewById(R.id.SettingsAddresTextId);
        setNameButton = findViewById(R.id.SettingsNameButtonId);
        setPhoneButton = findViewById(R.id.SettingsPhoneButtonId);
        importPictureButton = findViewById(R.id.SettingsPictureButtonId);
        importLocationButton = findViewById(R.id.SettingsAddressButtonId);
        saveButton = findViewById(R.id.SettingsSaveButtonId);

        textInputName.setHint("Insert name");
        textInputPhone.setHint("Insert phone number");

        importPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    verifyPermissions();
            }
        });

        importLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsActivity.this, "API CHANGED, NO MORE IMPORT", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateForm()) {
                    return;
                }
                User user =  new User();
                user.setName(textInputName.getText().toString());
                user.setPhoneNumber(textInputPhone.getText().toString());
                user.setAddress(mAddress);
                user.setPictureUri(mPictureUri);
                user.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

                CollectionReference appointmentRef = FirebaseFirestore.getInstance().collection("users");
                appointmentRef.add(user);
            }
        });

        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = textInputName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    textInputName.setError("Required.");

                }else {
                    textInputName.setError(null);
                    mName = name;
                }
            }
        });
        setPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = textInputPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    textInputPhone.setError("Required.");

                }else {
                    textInputPhone.setError(null);
                    mPhoneNumber = phone;
                }
            }
        });

        getAddresFromLocation();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startPickImageIntent() {
        Intent gallery = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, SELECT_FILE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();

    }

    private void getAddresFromLocation(){

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(SettingsActivity.this);

        try{

            Task location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location");
                        Location currentLocation = (Location) task.getResult();
                        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());

                        try {
                            List<Address> addressList = geocoder.getFromLocation(currentLocation.getLatitude(),
                                    currentLocation.getLongitude(), 1);
                            Log.d(TAG, "getDeviceAddress: list size is " + addressList.size());
                            String address = addressList.get(0).getAddressLine(0);
                            Log.d(TAG, "getDeviceAddress: " + address);
                            addressText.setText(address);
                            mAddress = address;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(SettingsActivity.this,"unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }catch (SecurityException e){
            Log.e(TAG, "getLocation: " + e.getMessage());
        }
    }

    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: ");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){

            startPickImageIntent();

        }else{
            ActivityCompat.requestPermissions(SettingsActivity.this,permissions,PERMISSION_REQUEST);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        if (mName.equals("")) {
            textInputName.setError("Required.");
            valid = false;
        } else {
            textInputName.setError(null);
        }

        if (mPhoneNumber.equals("")) {
            textInputPhone.setError("Required.");
            valid = false;
        } else {
            textInputPhone.setError(null);
        }
        if(mPictureUri==null){
            valid = false;
            Toast.makeText(this, "Choose a picture!", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK){
            mPictureUri = data.getData().toString();
        }
    }

}




package com.example.retea.licentapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.retea.licentapp.LicentApplication;
import com.example.retea.licentapp.R;
import com.example.retea.licentapp.adapters.AppointmentListAdapter;
import com.example.retea.licentapp.models.Appointment;
import com.example.retea.licentapp.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends BaseNavigationDrawer implements AppointmentListAdapter.OnItemClickListener {
    private static final String TAG = "MyProfileActivity";

    ImageView mProfilePicture;
    TextView mProfileName;
    TextView mProfileEmail;
    RecyclerView mAwaitingAppointmentsRecyclerView;
    RecyclerView.LayoutManager topLayoutManager;
    RecyclerView.LayoutManager bottomLayoutManager;
    RecyclerView mOngoingAppointmentsRecyclerView;
    Button settingsButton;

    List<Appointment> appointmentListConfirmed = new ArrayList<>();
    List<Appointment> appointmentListNotConfirmed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mProfileName = findViewById(R.id.ProfileNameId);
        mProfilePicture = findViewById(R.id.ProfilePictureId);
        mProfileEmail = findViewById(R.id.ProfileMailId);
        settingsButton = findViewById(R.id.GoToSettingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfileActivity.this, SettingsActivity.class));
            }
        });
        for (Appointment appointment : LicentApplication.getAppointmentList()) {
            if (appointment.isConfirmed()) {
                appointmentListConfirmed.add(appointment);
            } else appointmentListNotConfirmed.add(appointment);
        }

        if(LicentApplication.getCurrentUser()!=null){
            Log.d(TAG, "onCreate: USER NOT NULL");

            if (LicentApplication.getCurrentUser().getPictureUri() != null) {
                loadImageWithPicasso(Uri.parse(LicentApplication.getCurrentUser().getPictureUri()));
            }
            if (LicentApplication.getCurrentUser().getName() != null) {
                mProfileName.setText(LicentApplication.getCurrentUser().getName());
            }
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                mProfileEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }

        }




        mAwaitingAppointmentsRecyclerView = findViewById(R.id.RecyclerViewAwaitingAppointments);
        mOngoingAppointmentsRecyclerView = findViewById(R.id.RecyclerViewOngoingAppointments);

        mAwaitingAppointmentsRecyclerView.setHasFixedSize(true);

        AppointmentListAdapter appointmentListAwaitingAdapter = new AppointmentListAdapter(appointmentListNotConfirmed, this);
        topLayoutManager = new LinearLayoutManager(this);
        mAwaitingAppointmentsRecyclerView.setLayoutManager(topLayoutManager);

        mAwaitingAppointmentsRecyclerView.setAdapter(appointmentListAwaitingAdapter);
        mAwaitingAppointmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mOngoingAppointmentsRecyclerView.setHasFixedSize(true);

        AppointmentListAdapter appointmentListOngoingAdapter = new AppointmentListAdapter(appointmentListConfirmed, this);
        bottomLayoutManager = new LinearLayoutManager(this);
        mOngoingAppointmentsRecyclerView.setLayoutManager(bottomLayoutManager);

        mOngoingAppointmentsRecyclerView.setAdapter(appointmentListOngoingAdapter);
        mOngoingAppointmentsRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    protected int getNavigationItemId() {
        return R.id.nav_profile;
    }

    @Override
    public void onItemClick(Appointment appointment) {

        Intent intent = new Intent(this, AppointmentActivity.class);
        intent.putExtra("appointmentId", appointment.getId());
        startActivity(intent);

    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }


        @Override
        public String key() {
            return "circle";
        }
    }

    private void loadImageWithPicasso(Uri image) {
        Picasso.get().load(image).into(mProfilePicture);
        Picasso.get().load(image).resize(500, 500).centerCrop().rotate(90).transform(new CircleTransform()).into(mProfilePicture);
    }
}

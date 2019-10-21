package com.example.retea.licentapp.interfaces;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.models.Appointment;

import java.util.List;

public interface CallbackAppointmentDB {

    void onSuccess(@NonNull List<Appointment> appointmentList);
}

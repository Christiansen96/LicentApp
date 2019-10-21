package com.example.retea.licentapp.interfaces;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.models.User;

public interface CallbackUserDB {

    void onSuccess(@NonNull User user);
}

package com.example.retea.licentapp.interfaces;

import androidx.annotation.NonNull;

import com.example.retea.licentapp.models.Provider;
import com.example.retea.licentapp.models.Service;

import java.util.List;

public interface CallbackProviderDB {

    void onSuccess(@NonNull List<Provider> providerList);
}

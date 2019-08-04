package com.example.retea.licentapp.models;

public class User {
    private static final String TAG = "User";


    private static User userInstance;

    public static User getInstance() {
        if (userInstance == null) {
            userInstance = new User();
            return userInstance;
        }
        return userInstance;
    }



    private User() {

    }


}

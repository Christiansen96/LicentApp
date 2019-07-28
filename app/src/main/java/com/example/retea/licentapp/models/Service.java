package com.example.retea.licentapp.models;

import android.net.Uri;

public class Service {

    private int id;
    private String name;
    private double price;
    private int duration;
    private Uri imageUri;
    private String shortDescription;
    private String longDescription;

    public Service(int id, String name, double price, int duration, String shortDescription, String longDescription) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;

    }

    public Service(int id, String name, double price, int duration, String shortDescription, String longDescription, Uri imageUri) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.imageUri = imageUri;


    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}

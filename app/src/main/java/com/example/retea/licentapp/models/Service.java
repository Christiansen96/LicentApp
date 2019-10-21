package com.example.retea.licentapp.models;

import android.net.Uri;

public class Service {

    private String id;
    private String name;
    private String price;
    private String duration;
    private Uri imageUri;
    private String shortDescription;
    private String longDescription;
    private String providerId;

    public Service(String id, String name, String price, String duration, String shortDescription, String longDescription) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;

    }

    public Service(String id, String name, String price, String duration, String shortDescription, String longDescription, Uri imageUri, String providerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.imageUri = imageUri;
        this.providerId = providerId;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String toString() {
        return id + " " + name + " " + price + " " + duration + " " + shortDescription + " " + longDescription;
    }
}

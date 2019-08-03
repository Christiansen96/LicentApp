package com.example.retea.licentapp.models;

import android.net.Uri;
import android.provider.MediaStore;

import java.util.List;

public class Provider {

    private String id;
    private String name;
    private String category;
    private GeologicalPosition geologicalPosition;
    private List<Service> serviceList;
    private int icon;
    private Uri imageUri;
    private int type;

    public Provider(String id, String name, String category, GeologicalPosition geologicalPosition, List<Service> serviceList, int icon, Uri uri, int type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.geologicalPosition = geologicalPosition;
        this.serviceList = serviceList;
        this.icon = icon;
        imageUri = uri;
        this.type = type;
    }

    public Provider(String id, String name, String category, GeologicalPosition geologicalPosition, int icon, Uri uri, int type) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.geologicalPosition = geologicalPosition;
        this.icon = icon;
        imageUri = uri;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public GeologicalPosition getProviderGeologicalPosition() {
        return geologicalPosition;
    }

    public void setProviderGeologicalPosition(GeologicalPosition location) {
        this.geologicalPosition = location;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri uri) {
        imageUri = uri;
    }

    public String toString() {
        return name + " " + category + " " + geologicalPosition.getLatitude() + " " + geologicalPosition.getLongitude();
    }
}


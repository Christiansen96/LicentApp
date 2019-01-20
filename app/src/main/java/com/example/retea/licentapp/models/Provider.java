package com.example.retea.licentapp.models;

import android.provider.MediaStore;

import java.util.List;

public class Provider {

    private int id;
    private String name;
    private String category;
    private GeologicalPosition geologicalPosition;
    private List<Service> serviceList;
    private int image;

    public Provider(int id, String name,String category, GeologicalPosition geologicalPosition, List<Service> serviceList, int image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.geologicalPosition = geologicalPosition;
        this.serviceList = serviceList;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

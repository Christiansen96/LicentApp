package com.example.retea.licentapp.models;

import android.provider.MediaStore;

import java.util.List;

public class Provider {

    private int id;
    private String name;
    private GeologicalPosition location;
    private List<Service> serviceList;
    private byte[] image;

    public Provider(int id, String name, GeologicalPosition location, List<Service> serviceList, byte[] image) {
        this.id = id;
        this.name = name;
        this.location = location;
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

    public GeologicalPosition getLocation() {
        return location;
    }

    public void setLocation(GeologicalPosition location) {
        this.location = location;
    }

    public List<Service> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

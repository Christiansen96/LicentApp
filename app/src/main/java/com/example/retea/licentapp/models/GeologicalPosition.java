package com.example.retea.licentapp.models;

public class GeologicalPosition {

    private double longitude;
    private double latitude;

    public GeologicalPosition(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public GeologicalPosition() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}

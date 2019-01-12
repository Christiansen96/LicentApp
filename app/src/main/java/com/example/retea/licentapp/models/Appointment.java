package com.example.retea.licentapp.models;

import java.util.Date;

public class Appointment {

    private int id;
    private int customerId;
    private int providerId;
    public int serviceId;
    private Date begin;
    private Date end;
    public String notes;

    public Appointment(int id, int customerId, int providerId, int serviceId, Date begin, Date end) {
        this.id = id;
        this.customerId = customerId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.begin = begin;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

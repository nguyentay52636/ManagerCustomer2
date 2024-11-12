package com.example.baitapquatrinh2.Models;

public class Customer {
    private String phoneNumber;
    private int currentPoint;
    private String creationDate;
    private String lastUpdatedDate;
    private String note;

    public Customer(String phoneNumber, int currentPoint, String creationDate, String lastUpdatedDate, String note) {
        this.phoneNumber = phoneNumber;
        this.currentPoint = currentPoint;
        this.creationDate = creationDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.note = note;
    }



    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getCurrentPoint() { return currentPoint; }
    public void setCurrentPoint(int currentPoint) { this.currentPoint = currentPoint; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    public String getLastUpdatedDate() { return lastUpdatedDate; }
    public void setLastUpdatedDate(String lastUpdatedDate) { this.lastUpdatedDate = lastUpdatedDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}

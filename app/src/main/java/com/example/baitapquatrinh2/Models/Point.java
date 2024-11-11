package com.example.baitapquatrinh2.Models;

public class Point {
    private int addedPoint;
    private int usedPoint;
    private String updateDate;

    // Constructor
    public Point(int addedPoint, int usedPoint, String updateDate) {
        this.addedPoint = addedPoint;
        this.usedPoint = usedPoint;
        this.updateDate = updateDate;
    }

    // Getters và Setters
    public int getAddedPoint() { return addedPoint; }
    public void setAddedPoint(int addedPoint) { this.addedPoint = addedPoint; }

    public int getUsedPoint() { return usedPoint; }
    public void setUsedPoint(int usedPoint) { this.usedPoint = usedPoint; }

    public String getUpdateDate() { return updateDate; }
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
}

package org.unibl.etf.mr.assetledger.model;

import java.time.LocalDateTime;

public class Asset {
    private String name;
    private String description;
    private long barcode;
    private double price;
    private LocalDateTime creationDate;
    private String employeeName;
    private String location;
    double locationLatitude;
    double locationLongitude;
    private String imagePath;

    public Asset(String name, String description, long barcode, double price, LocalDateTime creationDate, String employeeName, String location, double locationLatitude, double locationLongitude, String imagePath) {
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.price = price;
        this.creationDate = creationDate;
        this.employeeName = employeeName;
        this.location = location;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getBarcode() {
        return barcode;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getLocation() {
        return location;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public String getImagePath() {
        return imagePath;
    }
}

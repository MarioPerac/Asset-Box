package org.unibl.etf.mr.assetbox.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.io.Serializable;

@Entity(tableName = Constants.TABLE_NAME_ASSETS)
public class Asset implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "barcode")
    private long barcode;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "creation_date")
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    @ColumnInfo(name = "employee_name")
    private String employeeName;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    public Asset(long id, String name, String description, long barcode, double price, LocalDateTime creationDate, String employeeName, String location, String imagePath) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barcode = barcode;
        this.price = price;
        this.creationDate = creationDate;
        this.employeeName = employeeName;
        this.location = location;
        this.imagePath = imagePath;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

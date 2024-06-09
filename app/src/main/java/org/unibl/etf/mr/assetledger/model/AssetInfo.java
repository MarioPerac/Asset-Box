package org.unibl.etf.mr.assetledger.model;

import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import org.unibl.etf.mr.assetledger.util.LocalDateTimeConverter;

import java.time.LocalDateTime;

public class AssetInfo {


    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name = "image_path")
    private String imagePath;
    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "creation_date")
    @TypeConverters(LocalDateTimeConverter.class)
    private LocalDateTime creationDate;

    @ColumnInfo(name = "employee_name")
    private String employeeName;
    @ColumnInfo(name = "location")
    private String location;

    public AssetInfo(long id, String imagePath, String name, LocalDateTime creationDate, String employeeName, String location) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.creationDate = creationDate;
        this.employeeName = employeeName;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

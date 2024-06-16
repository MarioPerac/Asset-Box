package org.unibl.etf.mr.assetledger.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.unibl.etf.mr.assetledger.util.Constants;

@Entity(tableName = Constants.TABLE_NAME_ITEMS)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "asset_id")
    private long assetId;

    @ColumnInfo(name = "current_employee_name")
    private String currentEmployeeName;
    @ColumnInfo(name = "new_employee_name")
    private String newEmployeeName;
    @ColumnInfo(name = "current_location")
    private String currentLocation;
    @ColumnInfo(name = "new_location")
    private String newLocation;

    public Item(long assetId, String currentEmployeeName, String newEmployeeName, String currentLocation, String newLocation) {
        this.assetId = assetId;
        this.currentEmployeeName = currentEmployeeName;
        this.newEmployeeName = newEmployeeName;
        this.currentLocation = currentLocation;
        this.newLocation = newLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(long assetId) {
        this.assetId = assetId;
    }

    public String getCurrentEmployeeName() {
        return currentEmployeeName;
    }

    public void setCurrentEmployeeName(String currentEmployeeName) {
        this.currentEmployeeName = currentEmployeeName;
    }

    public String getNewEmployeeName() {
        return newEmployeeName;
    }

    public void setNewEmployeeName(String newEmployeeName) {
        this.newEmployeeName = newEmployeeName;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newLocation) {
        this.newLocation = newLocation;
    }
}

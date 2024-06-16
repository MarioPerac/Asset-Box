package org.unibl.etf.mr.assetledger.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.unibl.etf.mr.assetledger.util.Constants;

@Entity(
        tableName = Constants.TABLE_NAME_ITEMS,
        foreignKeys = @ForeignKey(
                entity = CensusList.class,
                parentColumns = "id",
                childColumns = "census_id",
                onDelete = ForeignKey.CASCADE
        )
)
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

    @ColumnInfo(name = "census_id")
    private long censusId;

    public Item(long id, long assetId, String currentEmployeeName, String newEmployeeName, String currentLocation, String newLocation, long censusId) {
        this.id = id;
        this.assetId = assetId;
        this.currentEmployeeName = currentEmployeeName;
        this.newEmployeeName = newEmployeeName;
        this.currentLocation = currentLocation;
        this.newLocation = newLocation;
        this.censusId = censusId;
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

    public long getCensusId() {
        return censusId;
    }

    public void setCensusId(long censusId) {
        this.censusId = censusId;
    }
}

package org.unibl.etf.mr.assetbox.assetsdb.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.AssetInfo;
import org.unibl.etf.mr.assetbox.util.Constants;

import java.util.List;

@Dao
public interface AssetDAO {


    @Insert
    long insert(Asset asset);

    @Update
    void update(Asset asset);

    @Update
    void update(Asset... assets);

    @Delete
    void delete(Asset asset);


    @Delete
    void deleteAssets(Asset... assets);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_ASSETS)
    List<Asset> getAll();

    @Query("SELECT id, image_path, name, creation_date, employee_name, location FROM " + Constants.TABLE_NAME_ASSETS)
    List<AssetInfo> getAllAssetInfo();

    @Query("SELECT id, image_path, name, creation_date, employee_name, location FROM " + Constants.TABLE_NAME_ASSETS + " WHERE id = :id")
    AssetInfo getAssetInfo(long id);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_ASSETS + " WHERE id = :id")
    Asset getById(long id);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_ASSETS + " WHERE barcode = :barcode")
    Asset getByBarcode(long barcode);

    @Query("DELETE FROM " + Constants.TABLE_NAME_ASSETS + " WHERE employee_name=:name")
    void deleteAllEmployeeAssets(String name);

    @Query("DELETE FROM " + Constants.TABLE_NAME_ASSETS + " WHERE location=:name")
    void deleteAllAssetsByLocation(String name);


    @Query("SELECT image_path FROM " + Constants.TABLE_NAME_ASSETS + " WHERE location=:name")
    List<String> getAllAssetsImagePathsByLocation(String name);

    @Query("SELECT image_path FROM " + Constants.TABLE_NAME_ASSETS + " WHERE employee_name=:name")
    List<String> getAllEmployeeAssetsImagePaths(String name);
    
}

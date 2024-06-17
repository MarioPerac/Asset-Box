package org.unibl.etf.mr.assetledger.assetsdb.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.AssetInfo;
import org.unibl.etf.mr.assetledger.util.Constants;

import java.time.LocalDateTime;
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
}

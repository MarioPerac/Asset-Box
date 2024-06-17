package org.unibl.etf.mr.assetledger.assetsdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.util.Constants;

import java.util.List;
import java.util.concurrent.CompletionService;

@Dao
public interface ItemDAO {
    @Insert
    long insert(Item item);

    @Insert
    void insertAll(Item... items);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_ITEMS)
    List<Item> getAllItems();

    @Query("SELECT * FROM " + Constants.TABLE_NAME_ITEMS + " WHERE census_id=:id")
    List<Item> getAllCensusListItems(long id);
}

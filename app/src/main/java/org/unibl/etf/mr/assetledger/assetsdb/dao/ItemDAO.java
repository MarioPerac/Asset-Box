package org.unibl.etf.mr.assetledger.assetsdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.mr.assetledger.model.Item;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    long insert(Item item);

    @Query("SELECT * FROM items")
    List<Item> getAllItems();
}

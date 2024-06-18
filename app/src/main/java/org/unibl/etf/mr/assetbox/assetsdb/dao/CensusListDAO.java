package org.unibl.etf.mr.assetbox.assetsdb.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.unibl.etf.mr.assetbox.model.CensusList;
import org.unibl.etf.mr.assetbox.util.Constants;

import java.util.List;

@Dao
public interface CensusListDAO {
    @Insert
    long insert(CensusList censusList);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_CENSUS_LISTS + " WHERE id = :id")
    CensusList getById(long id);

    @Query("SELECT * FROM " + Constants.TABLE_NAME_CENSUS_LISTS)
    List<CensusList> getAll();
}

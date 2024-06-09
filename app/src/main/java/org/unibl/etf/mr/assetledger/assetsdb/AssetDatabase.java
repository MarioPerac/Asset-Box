package org.unibl.etf.mr.assetledger.assetsdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.util.Constants;
import org.unibl.etf.mr.assetledger.util.LocalDateTimeConverter;

@Database(entities = { Asset.class }, version = 1)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AssetDatabase extends RoomDatabase {
    public abstract AssetDAO getAssetDAO();
    private static AssetDatabase INSTANCE;

    public static AssetDatabase getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = buildDatabaseInstance(context);
        }
        return INSTANCE;
    }

    private static AssetDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
               AssetDatabase.class,
                Constants.DB_NAME).build();
    }

    public  void cleanUp() {
        INSTANCE = null;
    }
}

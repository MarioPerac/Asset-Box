package org.unibl.etf.mr.assetledger.assetsdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.unibl.etf.mr.assetledger.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetledger.assetsdb.dao.ItemDAO;
import org.unibl.etf.mr.assetledger.model.Asset;
import org.unibl.etf.mr.assetledger.model.Item;
import org.unibl.etf.mr.assetledger.util.Constants;
import org.unibl.etf.mr.assetledger.util.LocalDateTimeConverter;

@Database(entities = {Asset.class, Item.class}, version = 2)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AssetDatabase extends RoomDatabase {

    public abstract AssetDAO getAssetDAO();

    public abstract ItemDAO getItemDAO();

    private static AssetDatabase INSTANCE;

    public static synchronized AssetDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabaseInstance(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private static AssetDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                        AssetDatabase.class,
                        Constants.DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public void cleanUp() {
        INSTANCE = null;
    }


    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME_ITEMS + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + "asset_id INTEGER NOT NULL, "
                    + "current_employee_name TEXT, "
                    + "new_employee_name TEXT, "
                    + "current_location TEXT, "
                    + "new_location TEXT)");
        }
    };
}


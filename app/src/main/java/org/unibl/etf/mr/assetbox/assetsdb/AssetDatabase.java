package org.unibl.etf.mr.assetbox.assetsdb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.unibl.etf.mr.assetbox.assetsdb.dao.AssetDAO;
import org.unibl.etf.mr.assetbox.assetsdb.dao.CensusListDAO;
import org.unibl.etf.mr.assetbox.assetsdb.dao.ItemDAO;
import org.unibl.etf.mr.assetbox.model.Asset;
import org.unibl.etf.mr.assetbox.model.CensusList;
import org.unibl.etf.mr.assetbox.model.Item;
import org.unibl.etf.mr.assetbox.util.Constants;
import org.unibl.etf.mr.assetbox.util.LocalDateTimeConverter;

@Database(entities = {Asset.class, Item.class, CensusList.class}, version = 4)
@TypeConverters(LocalDateTimeConverter.class)
public abstract class AssetDatabase extends RoomDatabase {

    public abstract AssetDAO getAssetDAO();

    public abstract ItemDAO getItemDAO();

    public abstract CensusListDAO getCensuslistDAO();

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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Step 1: Add the `census_id` column to the `items` table
            database.execSQL("ALTER TABLE `items` ADD COLUMN `census_id` INTEGER NOT NULL DEFAULT 0");

            // Step 2: Create the new `items_new` table with the correct foreign key constraint
            database.execSQL("CREATE TABLE IF NOT EXISTS `items_new` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`asset_id` INTEGER NOT NULL, " +
                    "`current_employee_name` TEXT, " +
                    "`new_employee_name` TEXT, " +
                    "`current_location` TEXT, " +
                    "`new_location` TEXT, " +
                    "`census_id` INTEGER NOT NULL DEFAULT 0, " +
                    "FOREIGN KEY(`census_id`) REFERENCES `census_lists`(`id`) ON DELETE CASCADE)");

            // Step 3: Copy data from the old `items` table to the new `items_new` table
            database.execSQL("INSERT INTO `items_new` (" +
                    "`id`, `asset_id`, `current_employee_name`, `new_employee_name`, `current_location`, `new_location`, `census_id`) " +
                    "SELECT `id`, `asset_id`, `current_employee_name`, `new_employee_name`, `current_location`, `new_location`, `census_id` " +
                    "FROM `items`");

            // Step 4: Drop the old `items` table
            database.execSQL("DROP TABLE `items`");

            // Step 5: Rename the new `items_new` table to `items`
            database.execSQL("ALTER TABLE `items_new` RENAME TO `items`");

            // Step 6: Create the `census_lists` table
            database.execSQL("CREATE TABLE IF NOT EXISTS `census_lists` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`name` TEXT, " +
                    "`creation_date` TEXT)");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS assets_new (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "description TEXT," +
                    "barcode INTEGER," +
                    "price REAL," +
                    "creation_date TEXT," +
                    "employee_name TEXT," +
                    "location TEXT," +
                    "image_path TEXT)");

            database.execSQL("INSERT INTO assets_new (id, name, description, barcode, price, creation_date, employee_name, location, image_path) " +
                    "SELECT id, name, description, barcode, price, creation_date, employee_name, location, image_path FROM assets");

            database.execSQL("DROP TABLE assets");

            database.execSQL("ALTER TABLE assets_new RENAME TO assets");
        }
    };
}


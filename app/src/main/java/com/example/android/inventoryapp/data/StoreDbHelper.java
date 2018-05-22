package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

public class StoreDbHelper extends SQLiteOpenHelper {

    // Tag for the log messages
    private static final String LOG_TAG = "Create SQL table: ";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "catalog.db";

    /**
     * @param context to use to open or create the database
     */
    public StoreDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STORE_TABLE = "CREATE TABLE " + StoreEntry.TABLE_NAME + " (" +
                StoreEntry.COLUMN_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StoreEntry.COLUMN_PRODUCT_TITLE + " TEXT NOT NULL, " +
                StoreEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                StoreEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                StoreEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                StoreEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                StoreEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL, " +
                StoreEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_STORE_TABLE);

        // Log the table creation
        Log.v(LOG_TAG, SQL_CREATE_STORE_TABLE);

    }

    /**
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME);
        onCreate(db);

    }
}

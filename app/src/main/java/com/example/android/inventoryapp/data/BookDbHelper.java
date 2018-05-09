package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventoryapp.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "catalog.db";
    private static final String LOG_TAG = "Create SQL table: ";

    /**
     * @param context to use to open or create the database
     */
    public BookDbHelper(Context context) {
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

        final String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + " (" +
                BookEntry.COLUMN_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                BookEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                BookEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                BookEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL" + ");";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);

        // Log the table creation
        Log.v(LOG_TAG, SQL_CREATE_BOOKS_TABLE);

    }

    /**
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BookEntry.TABLE_NAME);
        onCreate(db);

    }
}

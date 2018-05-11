package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BookContract.BookEntry;
import com.example.android.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    // Initializing dbHelper
    private BookDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiation the subClass of the SQLiteOpenHelper
        dbHelper = new BookDbHelper(this);
    }

    /**
     * This method callback helper methods (insertDummyData() and displayDatabaseInfo())
     * when activity start. The purpose is to insert dummy data and display that data
     * within the TextView (displayView)
     */
    @Override
    protected void onStart() {
        super.onStart();
        insertDummyData();
        displayDatabaseInfo();
    }

    /**
     * Helper method to display the SQL information's table (books).
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database and read it.
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // get a string array of the database's column name
        String[] projection = {
                BookEntry.COLUMN_KEY,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_TITLE,
                BookEntry.COLUMN_PRICE,
                BookEntry.COLUMN_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_PHONE
        };

        // Create a Cursor object with all the table's content
        Cursor cursor = database.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Get the view
        TextView displayView = findViewById(R.id.display_table);
        String header = getResources().getString(R.string.table_header_view, cursor.getCount());

        try {
            // Create a header in the textView
            displayView.setText(header);
            displayView.append(BookEntry.COLUMN_KEY + " - " +
                    BookEntry.COLUMN_PRODUCT_NAME + " - " +
                    BookEntry.COLUMN_PRODUCT_TITLE + " - " +
                    BookEntry.COLUMN_PRICE + " - " +
                    BookEntry.COLUMN_QUANTITY + " - " +
                    BookEntry.COLUMN_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_KEY);
            int productColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int titleColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the return rows from idColumnIndex.
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                // Display the values from each column
                displayView.append("\n" + currentId + " - " +
                currentProduct + " - " +
                currentTitle + " - " +
                currentPrice + "€" + " - " +
                currentQuantity + " - " +
                currentSupplier + " - " +
                currentPhone);
            }

        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert dummy data into the SQL table (books)
     */
    private void insertDummyData() {
        // Gets the database in write mode
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // delete rows so no more than 10 records are displayed
        database.delete(BookEntry.TABLE_NAME, null, null);

        // Create a ContentValue object
        ContentValues values = new ContentValues();

        // Loop 10 time to insert dummy data in the table (books).
        for(int i = 0; i<10; i++) {
            values.put(BookEntry.COLUMN_PRODUCT_NAME, "Harry Potter");
            values.put(BookEntry.COLUMN_PRODUCT_TITLE, "Philosopher's Stone");
            values.put(BookEntry.COLUMN_PRICE, 10);
            values.put(BookEntry.COLUMN_QUANTITY, 1);
            values.put(BookEntry.COLUMN_SUPPLIER_NAME, "FNAC");
            values.put(BookEntry.COLUMN_SUPPLIER_PHONE, "02123456");
            // Insert a new row into the database
            long newRowId = database.insert(BookEntry.TABLE_NAME, null, values);
            Log.v(LOG_TAG, "New Row Id is: " + newRowId);
        }
    }

}

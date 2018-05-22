package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryapp.data.StoreContract.StoreEntry;
import com.example.android.inventoryapp.data.StoreDbHelper;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    // Initializing dbHelper
    private StoreDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(i);
            }
        });

        // Instantiation the subClass of the SQLiteOpenHelper
        dbHelper = new StoreDbHelper(this);
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
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which the items is displayed.
     * @return You must return true for the menu to be displayed;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                //TODO;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to display the SQL information's table (books).
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database and read it.
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // get a string array of the database's column name
        String[] projection = {
                StoreEntry.COLUMN_KEY,
                StoreEntry.COLUMN_PRODUCT_NAME,
                StoreEntry.COLUMN_PRODUCT_TITLE,
                StoreEntry.COLUMN_PRICE,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_PHONE,
                StoreEntry.COLUMN_SUPPLIER_EMAIL};

        // Create a Cursor object with all the table's content
        Cursor cursor = database.query(
                StoreEntry.TABLE_NAME,
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
            displayView.append(StoreEntry.COLUMN_KEY + " - " +
                    StoreEntry.COLUMN_PRODUCT_NAME + " - " +
                    StoreEntry.COLUMN_PRODUCT_TITLE + " - " +
                    StoreEntry.COLUMN_PRICE + " - " +
                    StoreEntry.COLUMN_QUANTITY + " - " +
                    StoreEntry.COLUMN_SUPPLIER_NAME + " - " +
                    StoreEntry.COLUMN_SUPPLIER_PHONE +
                    StoreEntry.COLUMN_SUPPLIER_EMAIL +"\n");

            // Index of each column
            int idColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_KEY);
            int productColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
            int titleColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_TITLE);
            int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE);
            int emailColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_EMAIL);

            // Iterate through all the return rows from idColumnIndex.
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);
                String currentEmail = cursor.getString(emailColumnIndex);

                // Display the values from each column
                displayView.append("\n" + currentId + " - " +
                currentProduct + " - " +
                currentTitle + " - " +
                currentPrice + "€" + " - " +
                currentQuantity + " - " +
                currentSupplier + " - " +
                currentPhone + " - " +
                currentEmail);
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
        database.delete(StoreEntry.TABLE_NAME, null, null);

        // Create a ContentValue object
        ContentValues values = new ContentValues();

        // Loop 10 time to insert dummy data in the table (books).
        for(int i = 0; i<10; i++) {
            values.put(StoreEntry.COLUMN_PRODUCT_NAME, "Harry Potter");
            values.put(StoreEntry.COLUMN_PRODUCT_TITLE, "Philosopher's Stone");
            values.put(StoreEntry.COLUMN_PRICE, 10);
            values.put(StoreEntry.COLUMN_QUANTITY, 1);
            values.put(StoreEntry.COLUMN_SUPPLIER_NAME, "FNAC");
            values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, "02123456");
            values.put(StoreEntry.COLUMN_SUPPLIER_EMAIL, "adresse@domain.com");
            // Insert a new row into the database
            long newRowId = database.insert(StoreEntry.TABLE_NAME, null, values);
            Log.v(LOG_TAG, "New Row Id is: " + newRowId);
        }
    }

}

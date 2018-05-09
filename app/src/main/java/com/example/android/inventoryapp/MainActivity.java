package com.example.android.inventoryapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.BookContract.BookEntry;
import com.example.android.inventoryapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    // Initializing dbHelper
    private BookDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiation the subClass of the SQLiteOpenHelper
        dbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        // Create and/or open a database and read it.
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        // get a string array of the database's column name
        String[] projection = {
                BookEntry.COLUMN_KEY,
                BookEntry.COLUMN_PRODUCT_NAME,
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
        String header = getResources().getString(R.string.header_view);
        String books = getResources().getString(R.string.books);

        try {
            // Create a header in the textView
            displayView.setText(header + " " + cursor.getCount() + " " + books);
            displayView.append(BookEntry.COLUMN_KEY + " - " +
                    BookEntry.COLUMN_PRODUCT_NAME + " - " +
                    BookEntry.COLUMN_PRICE + " - " +
                    BookEntry.COLUMN_QUANTITY + " - " +
                    BookEntry.COLUMN_SUPPLIER_NAME + " - " +
                    BookEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Index of each column
            int idColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_KEY);
            int productColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the return rows from idColumnIndex.
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);

                // Display the values from each column
                displayView.append("\n" + currentId + "- " +
                currentProduct + " - " +
                currentPrice + " - " +
                currentQuantity + " - " +
                currentSupplier + " - " +
                currentPhone);
            }

        } finally {
            cursor.close();
        }
    }
}

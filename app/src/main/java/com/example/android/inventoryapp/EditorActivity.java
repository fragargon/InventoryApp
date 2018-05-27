package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

public class EditorActivity extends AppCompatActivity{

    /** Identifier the store loader id. */
    private static final int EXISTING_STORE_LOADER = 0;

    /** Content Uri for the existing store */
    private Uri currentStoreUri;

    /** Initialize various EditText field. */
    private EditText productName;
    private EditText productPrice;
    private EditText productQuantity;
    private EditText supplierName;
    private EditText supplierEmail;
    private EditText supplierPhone;
    private ImageButton btnAddItem;
    private ImageButton btnDelItem;
    private ImageButton btnSendMail;
    private ImageButton btnCallPhone;
    private Spinner productSpinner;

    /**
     * Type of product selected, the possible value are:
     * 0 for informatics (default), 1 for computer, 2 for desktop, 3 for laptop
     * 4 for hardware, 5 for software, 6 for cellphone, 7 for smartphone
     */
    private int selectedProduct = StoreEntry.PRODUCT_DEFAULT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();

        setupSpinner();

    }

    /**
     * Method to cast the views to display the variable's content.
     */
    private void initView() {

        // Find relevant View to edit the text.
        productName = findViewById(R.id.edit_product_name);
        productPrice = findViewById(R.id.edit_product_price);
        productQuantity = findViewById(R.id.edit_product_quantity);
        supplierName = findViewById(R.id.edit_supplier_name);
        supplierEmail = findViewById(R.id.edit_supplier_mail);
        supplierPhone = findViewById(R.id.edit_supplier_phone);

        // Find relevant ImageButton to trigger an intent.
        btnAddItem = findViewById(R.id.edit_btn_add);
        btnDelItem = findViewById(R.id.edit_btn_del);
        btnSendMail = findViewById(R.id.edit_btn_send_mail);
        btnCallPhone = findViewById(R.id.edit_btn_call_phone);

        // Find the dropdown spinner
        productSpinner = findViewById(R.id.product_spinner);

    }

    /**
     * Create a spinner dropdown object.
     */
    private void setupSpinner() {
        // Create an adapter for spinner - use the default layout.
        ArrayAdapter productSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.product_type, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view 1 item per line.
        productSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner.
        productSpinner.setAdapter(productSpinnerAdapter);

        // Set a listener int the item clicked spinner.
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)) {
                    switch (position) {
                        case 1: selectedProduct = StoreEntry.PRODUCT_COMPUTER;
                        break;
                        case 2: selectedProduct = StoreEntry.PRODUCT_DESKTOP;
                        break;
                        case 3: selectedProduct = StoreEntry.PRODUCT_LAPTOP;
                        break;
                        case 4: selectedProduct = StoreEntry.PRODUCT_HARDWARE;
                        break;
                        case 5: selectedProduct = StoreEntry.PRODUCT_SOFTWARE;
                        break;
                        case 6: selectedProduct = StoreEntry.PRODUCT_CELLPHONE;
                        break;
                        case 7: selectedProduct = StoreEntry.PRODUCT_SMARTPHONE;
                        break;
                        default: selectedProduct = StoreEntry.PRODUCT_DEFAULT;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedProduct = 0; // Informatics
            }
        });
    }

    private void saveProduct() {
        // Read from the inputFields.
        String nameProduct = productName.getText().toString().trim();
        String priceString = productPrice.getText().toString().trim();
        String quantityString = productQuantity.getText().toString().trim();
        String nameSupplier = supplierName.getText().toString().trim();
        String email = supplierEmail.getText().toString().trim();
        String phone = supplierPhone.getText().toString().trim();

        // parse priceString into an float dataType..
        float price = 0;
        if(!TextUtils.isEmpty(priceString)) {
            price = Float.parseFloat(priceString);
        }

        // parse quantityString into an int dataType..
        int quantity = 0;
        if(!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }

        // Create a ContentValue object where columns name are the key.
        ContentValues values = new ContentValues();
        values.put(StoreEntry.COLUMN_PRODUCT_TITLE, selectedProduct);
        values.put(StoreEntry.COLUMN_PRODUCT_NAME, nameProduct);
        values.put(StoreEntry.COLUMN_PRICE, price);
        values.put(StoreEntry.COLUMN_QUANTITY, quantity);
        values.put(StoreEntry.COLUMN_SUPPLIER_NAME, nameSupplier);
        values.put(StoreEntry.COLUMN_SUPPLIER_EMAIL, email);
        values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, phone);

        // Pass the URI in the database
        Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
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
            case R.id.action_save:
                // Save pet into database
                saveProduct();
                // Exit Activity
                finish();
                return true;
            case R.id.action_detele:
                // TODO
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

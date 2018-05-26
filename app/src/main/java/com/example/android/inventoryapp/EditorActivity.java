package com.example.android.inventoryapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class EditorActivity extends AppCompatActivity{

    // Identifier the store loader id.
    private static final int EXISTING_STORE_LOADER = 0;

    // Content Uri for the existing store
    private Uri currentStoreUri;

    // Initialize various EditText field.
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();

        setupSpinner();

    }

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
                // TODO
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
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
                // TODO
                return true;
            case R.id.action_detele:
                // TODO
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

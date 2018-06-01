package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

import java.util.Locale;

public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks <Cursor> {

    /** TAG for the log messages. */
    private static final String LOG_TAG = EditorActivity.class.getSimpleName();

    /** Identifier the store loader id. */
    private static final int EXISTING_STORE_LOADER = 0;

    /** Content Uri for the existing store */
    private Uri currentProductUri;

    /** Initialize various Views field. */
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

    /**  Initialize various variable*/
    String nameProduct;
    String priceString;
    String quantityString;
    String nameSupplier;
    String email;
    String phone;

    /**
     * Type of product selected, the possible value are:
     * 0 for informatics (default), 1 for computer, 2 for desktop, 3 for laptop
     * 4 for hardware, 5 for software, 6 for cellphone, 7 for smartphone
     */
    private int selectedProduct = StoreEntry.PRODUCT_DEFAULT;

    /** Boolean flag that keeps track of whether the product has been edited (true) or not (false) */
    private boolean productHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the productHasChanged boolean to true.
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            productHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Get the intent that launch this activity
        Intent intent = getIntent();
        currentProductUri = intent.getData();

        // Check the content URI, if it is null then means that
        // creating a new product
        if(currentProductUri == null) {
            setTitle(getString(R.string.editor_insert_mode));

        } else {
            // Otherwise this is an existing product.
            setTitle(getString(R.string.editor_edit_mode));

            // Kick off the loader callback.
            getLoaderManager().initLoader(EXISTING_STORE_LOADER, null, this);
        }

        /* See method {@link initView()} */
        initView();

        /* See method {@link hasBeenTouched()} */
        hasBeenTouched();

        /* See method {@link setupSpinner()} */
        setupSpinner();

        /*
          OnclickListener on the button to increase the product quantity.
         */
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock = productQuantity.getText().toString().trim();
                if(!TextUtils.isEmpty(stock)) {
                    int addStock = Integer.parseInt(stock);
                    productQuantity.setText(String.valueOf(addStock + 1));
                } else {
                    Toast.makeText(EditorActivity.this, getString(R.string.error_empty_product_price),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
          OnclickListener on the button to decrease the product quantity.
         */
        btnDelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock = productQuantity.getText().toString().trim();
                int delStock = Integer.parseInt(stock);
                if(delStock >= 1) {
                    productQuantity.setText(String.valueOf(delStock - 1));
                } else {
                    Toast.makeText(EditorActivity.this, getString(R.string.error_empty_product_quantity_zero),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*
          OnclickListener on the button to send an order to the supplier.
         */
        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = supplierEmail.getText().toString();
                String name = supplierName.getText().toString();
                String subject = getString(R.string.header_message, name);
                composeEmail(address, subject);
            }
        });

        /*
          OnclickListener on the button to dial supplier's phone number.
         */
        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber(supplierPhone.getText().toString());
            }
        });

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
     * Method to setup an onTouchListener on all the variable contents.
     * Now we can define if the user has touched or modified the views.
     */
    private void hasBeenTouched() {
        productName.setOnTouchListener(onTouchListener);
        productPrice.setOnTouchListener(onTouchListener);
        productQuantity.setOnTouchListener(onTouchListener);
        supplierName.setOnTouchListener(onTouchListener);
        supplierEmail.setOnTouchListener(onTouchListener);
        supplierPhone.setOnTouchListener(onTouchListener);
        btnAddItem.setOnTouchListener(onTouchListener);
        btnDelItem.setOnTouchListener(onTouchListener);
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
                        case 1: selectedProduct = StoreEntry.PRODUCT_COMPUTER; // Computer
                        break;
                        case 2: selectedProduct = StoreEntry.PRODUCT_DESKTOP; // Desktop
                        break;
                        case 3: selectedProduct = StoreEntry.PRODUCT_LAPTOP; // Laptop
                        break;
                        case 4: selectedProduct = StoreEntry.PRODUCT_HARDWARE; // Hardware
                        break;
                        case 5: selectedProduct = StoreEntry.PRODUCT_SOFTWARE; // Software
                        break;
                        case 6: selectedProduct = StoreEntry.PRODUCT_CELLPHONE; // Cellphone
                        break;
                        case 7: selectedProduct = StoreEntry.PRODUCT_SMARTPHONE; // Smartphone
                        break;
                        default: selectedProduct = StoreEntry.PRODUCT_DEFAULT; // Informatics
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
        /* Initialize various variable*/
        nameProduct = productName.getText().toString().trim();
        priceString = productPrice.getText().toString().trim();
        quantityString = productQuantity.getText().toString().trim();
        nameSupplier = supplierName.getText().toString().trim();
        email = supplierEmail.getText().toString().trim();
        phone = supplierPhone.getText().toString().trim();

        // Check if this is supposed to be a new product
        // and check if all the fields in the editor are blank
        if(currentProductUri == null && TextUtils.isEmpty(nameProduct) &&
                TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) &&
                TextUtils.isEmpty(nameSupplier) && TextUtils.isEmpty(email) &&
                TextUtils.isEmpty(phone)) {
            // Since no fields were modified, return early.
            return;
        }

        // Create a ContentValue object where columns name are the key.
        ContentValues values = new ContentValues();

        // set the spinner product title id.
        values.put(StoreEntry.COLUMN_PRODUCT_TITLE, selectedProduct);

        // Check if the fields product name is fulfill by the user.
        if(!TextUtils.isEmpty(nameProduct)) {
            values.put(StoreEntry.COLUMN_PRODUCT_NAME, nameProduct);
        } else {
            // if blank display an error.
            productName.requestFocus();
            productName.setError(getString(R.string.error_empty_product_name));
            return;
        }

        // Check if the fields product price is fulfill by the user.
        // parse priceString into an float dataType..
        float price;
        if(!TextUtils.isEmpty(priceString)) {
            price = Float.parseFloat(priceString);
            values.put(StoreEntry.COLUMN_PRICE, price);
        } else {
            // if blank display an error.
            productPrice.requestFocus();
            productPrice.setError(getString(R.string.error_empty_product_price));
            return;
        }

        // Check if the fields product quantity is fulfill by the user.
        // parse quantityString into an int dataType..
        int quantity;
        if(!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
            values.put(StoreEntry.COLUMN_QUANTITY, quantity);
        } else {
            // if blank display an error.
            productQuantity.requestFocus();
            productQuantity.setError(getString(R.string.error_empty_product_quantity));
            return;
        }

        // Check if the fields supplier name is fulfill by the user.
        if(!TextUtils.isEmpty(nameSupplier)) {
            values.put(StoreEntry.COLUMN_SUPPLIER_NAME, nameSupplier);
        } else {
            // if blank display an error.
            supplierName.requestFocus();
            supplierName.setError(getString(R.string.error_empty_supplier_name));
            return;
        }

        // Check if the fields supplier email is fulfill by the user.
        if(!TextUtils.isEmpty(email)) {
            values.put(StoreEntry.COLUMN_SUPPLIER_EMAIL, email);
        } else {
            // if blank display an error.
            supplierEmail.requestFocus();
            supplierEmail.setError(getString(R.string.error_empty_supplier_email));
            return;
        }

        // Check if the fields supplier email is fulfill by the user.
        if(!TextUtils.isEmpty(phone)) {
            values.put(StoreEntry.COLUMN_SUPPLIER_PHONE, phone);
        } else {
            // if blank display an error.
            supplierPhone.requestFocus();
            supplierPhone.setError(getString(R.string.error_empty_supplier_phone));
            return;
        }

        // Determine if this is a new or existing pet by checking if mCurrentPetUri is null or not.
        if(currentProductUri == null) {
            // This is a new product, so insert a new product into the provider,
            // Pass the URI in the database
            Uri newUri = getContentResolver().insert(StoreEntry.CONTENT_URI, values);

            // Show a text message if the insertion is successful or not.
            if(newUri == null) {
                // if the rowId is -1, insertion failed.
                Toast.makeText(this, getString(R.string.insertion_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // insertion is successful.
                Toast.makeText(this, getString(R.string.insertion_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            // Otherwise this is an EXISTING product, so update the pet with content URI: currentProductUri.
            int rowsUpdated = getContentResolver().update(currentProductUri, values, null, null);

            // Show a text message if the update is successful or not.
            if(rowsUpdated == 0) {
                // if no row is affected.
                Toast.makeText(this, getString(R.string.update_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // update row is successful.
                Toast.makeText(this, getString(R.string.update_successful),
                        Toast.LENGTH_SHORT).show();
            }

            // Exit Activity
            finish();
        }
    }

    /**
     * Helper method to check that all fields are fulfill
     */
    private void checkAllField() {
        // Check if this is supposed to be a new product
        // and check if all the fields in the editor are fulfill
        if(currentProductUri == null &&
                TextUtils.isEmpty(nameProduct) ||
                TextUtils.isEmpty(priceString) ||
                TextUtils.isEmpty(quantityString) ||
                TextUtils.isEmpty(nameSupplier) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone)) {
            // Since no fields were modified, popup a message..
            Toast.makeText(this, getString(R.string.error_fulfill_all_the_field)
                    , Toast.LENGTH_SHORT).show();;
        } else {
            //exit the activity
            finish();
        }
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
     * Prepare the Screen's standard options menu to be displayed.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if(currentProductUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_detele);
            menuItem.setVisible(false);
        }
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
                checkAllField();
                return true;
            case R.id.action_detele:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
                // Respond to a click on the UP button in the appBar.
            case android.R.id.home:
                /// If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if(!productHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                DialogInterface.OnClickListener discardButtonListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                // Show a dialog that notifies the user has unsaved changes.
                showUnsavedDialog(discardButtonListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Editor shows all product attribute, define a projection that contains
        // all the columns from the database catalog.
        String[] projection = {
                StoreEntry.COLUMN_KEY,
                StoreEntry.COLUMN_PRODUCT_TITLE,
                StoreEntry.COLUMN_PRODUCT_NAME,
                StoreEntry.COLUMN_PRICE,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_SUPPLIER_NAME,
                StoreEntry.COLUMN_SUPPLIER_EMAIL,
                StoreEntry.COLUMN_SUPPLIER_PHONE};
        // This loader will execute the content provider's query method.
        return new CursorLoader(this,   // Parent activity context.
                currentProductUri,              // Query the content URI for the current product.
                projection,                     // Columns to include in the resulting cursor.
                null,                  // No selection clause.
                null,               // No selection args.
                null);                 // Default sor order
    }

    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {
            // Find the columns of product attributes that we're interested in.
            int titleColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_PRODUCT_TITLE);
            int nameColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_PRICE);
            int quantityColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE);
            int emailColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_EMAIL);
            int phoneColumnIndex = data.getColumnIndex(StoreEntry.COLUMN_SUPPLIER_PHONE);

            // Extract out the value from the Cursor for the given column index
            int title = data.getInt(titleColumnIndex);
            String name = data.getString(nameColumnIndex);
            float price = data.getFloat(priceColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            String supplier = data.getString(supplierColumnIndex);
            String email = data.getString(emailColumnIndex);
            String phone = data.getString(phoneColumnIndex);

            // Update the views on the screen with the values from the database.
            productName.setText(name);
            productPrice.setText(String.format("%.2f", price));
            productQuantity.setText(String.format(Integer.toString(quantity), Locale.getDefault()));
            supplierName.setText(supplier);
            supplierEmail.setText(email);
            supplierPhone.setText(phone);

            // Title is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown option.
            switch (title) {
                case StoreEntry.PRODUCT_COMPUTER:
                    productSpinner.setSelection(1);
                    break;
                case StoreEntry.PRODUCT_DESKTOP:
                    productSpinner.setSelection(2);
                    break;
                case StoreEntry.PRODUCT_LAPTOP:
                    productSpinner.setSelection(3);
                    break;
                case StoreEntry.PRODUCT_HARDWARE:
                    productSpinner.setSelection(4);
                    break;
                case StoreEntry.PRODUCT_SOFTWARE:
                    productSpinner.setSelection(5);
                    break;
                case StoreEntry.PRODUCT_CELLPHONE:
                    productSpinner.setSelection(6);
                    break;
                case StoreEntry.PRODUCT_SMARTPHONE:
                    productSpinner.setSelection(7);
                    break;
                default: productSpinner.setSelection(0);

            }

        }

    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        productSpinner.setSelection(0);
        productName.setText("");
        productPrice.setText("");
        productQuantity.setText("");
        supplierName.setText("");
        supplierEmail.setText("");
        supplierPhone.setText("");

    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if(!productHasChanged) {
            super.onBackPressed();
            return;
        }
        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        DialogInterface.OnClickListener discardButton =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        // Show dialog that there are unsaved changes.
        showUnsavedDialog(discardButton);
    }

    /**
     * Method to build an alertDialog interface to handle up button action and behaviour
     * that the user has changed or not the edit mode in the activity.
     * @param discardButton is the action if user select the positive button.
     */
    private void showUnsavedDialog(DialogInterface.OnClickListener discardButton) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.alert_dialog_unsaved_message));
        builder.setPositiveButton(getString(R.string.alert_dialog_discard), discardButton);
        builder.setNegativeButton(getString(R.string.alert_dialog_keep_editing), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the alertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.alert_dialog_confirm_delete_product));
        builder.setPositiveButton(getString(R.string.alert_dialog_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the delete button.
                deletedProduct();
            }
        });
        builder.setNegativeButton(getString(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked the cancel button.
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show alertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Helper method to delete a single product rows from the database catalog.
     */
    private void deletedProduct() {
        // Only perform the delete if this is an existing product.
        if(currentProductUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            int rowsDeleted = getContentResolver().delete(currentProductUri, null, null);
            Log.v(LOG_TAG, rowsDeleted + getString(R.string.log_message_row_delete));

            // Show a toast message if deleting is successful or not.
            if(rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.delete_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    }

    /**
     * helper method handle a dialer intent whic be trigger
     * with a click listener button.
     * @param phoneNumber is the supplier's phone number
     */
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

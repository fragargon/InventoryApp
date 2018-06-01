package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.adapter.StoreCursorAdapter;
import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // TAG for the log messages.
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // Identifier the loader id.
    private static final int STORE_LOADER = 0;

    // Initialize various class
    StoreCursorAdapter cursorAdapter;
    Intent intent;

    @SuppressWarnings("deprecation")
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

        // Find the ListView which will be populated with the item's catalog.
        ListView listView = findViewById(R.id.list_view);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
        // Setup an Adapter to create a list item for each rows of catalog database.
        cursorAdapter = new StoreCursorAdapter(this, null);
        listView.setAdapter(cursorAdapter);

        // Setup an item click listener.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity.class}
                intent = new Intent(MainActivity.this, EditorActivity.class);
                // Form the content URI that represents the selected product in the list.
                Uri currentProductUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent.
                intent.setData(currentProductUri);
                // Launch the {@link EditorActivity} to display details of the selected product.
                startActivity(intent);
            }
        });

        // Kick off the loader callback.
        getLoaderManager().initLoader(STORE_LOADER, null, this);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which the items is displayed.
     * @return You must return true for the menu to be displayed;
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
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
        // User clicked on a menu option in the app bar.
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                // Respond to a click on the "Delete all entries" menu option
                showDialogDeleteConfirmation();
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
        // Define a projection that specifies which column the database
        // will be displayed after this query.
        String[] projection = {
                StoreEntry.COLUMN_KEY,
                StoreEntry.COLUMN_PRODUCT_TITLE,
                StoreEntry.COLUMN_PRODUCT_NAME,
                StoreEntry.COLUMN_QUANTITY,
                StoreEntry.COLUMN_PRICE};

        // This Loader will execute the ContentProvider's query method on a background thread.
        return new CursorLoader(this,   // parent activity context
                StoreEntry.CONTENT_URI,         // provider content uri to query
                projection,                     // columns to include in the resulting cursor
                null,                   // no selection clause
                null,               // no selection args
                null);                  // default sort order
    }

    /**
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link #StoreCursorAdapter} with this new cursor.
        cursorAdapter.swapCursor(data);

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
        cursorAdapter.swapCursor(null);

    }

    private void showDialogDeleteConfirmation() {
        // Create an AlertDialog.Builder and set the message.
        // Confirmation for the positive buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_confirm_delete_all);
        builder.setPositiveButton(R.string.alert_dialog_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Delete" button, so delete the table catalog.
                deletedAllProduct();
            }
        });
        // Confirmation negative buttons on the dialog.
        builder.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletedAllProduct() {
        int rowsDeleted = getContentResolver().delete(StoreEntry.CONTENT_URI, null, null);
        Log.v(LOG_TAG, rowsDeleted + getString(R.string.log_message_all_delete));
    }
}

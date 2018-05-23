package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

/**
 * {@link ContentProvider} for InventoryApp.
 */
public class StoreProvider extends ContentProvider {

    // Tag for the log messages
    private static final String LOG_TAG = StoreProvider.class.getSimpleName();

    // Uri matcher code for the content uri for the catalog table.
    private static final int CATALOG = 100;

    // Uri matcher code for the content uri for the a single item in the catalog's table.
    private static final int CATALOG_ID = 101;

    /**
     * UriMatcher object to match a content URI to a correspond code.
     * The input passed into the constructor represents the code to return
     * for the root URI.
     */
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is call the first time anything is called from this class.
    static {
        // The content URI of the form: "content://com.example.android.inventoryapp/catalog"
        // will map the integer code {@link #CATALOG}. This URI is used to provide access
        // to multiple rows of the table.
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_CATALOG, CATALOG);
        // The content URI of the form: "content://com.example.android.inventoryapp/catalog/#"
        // will map the integer code {link #CATALOG_ID}. This URI is used to provide access
        // to a single rows of the table ("/#") is a wildcard.
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_CATALOG + "/#", CATALOG_ID);
    }

    // Initialize a StoreDbHelper object.
    private StoreDbHelper dbHelper;

    /**
     * Implement this to initialize your content provider on startup.
     * This method is called for all registered content providers on the
     * application main thread at application launch time.
     *
     * @return true if the provider was successfully loaded, false otherwise
     */
    @Override
    public boolean onCreate() {
        // Instantiate a StoreDbHelper object.
        dbHelper = new StoreDbHelper(getContext());
        return true;
    }

    /**
     * Implement this to handle query requests from clients.
     *
     * @param uri           The URI to query. This will be the full URI sent by the client;
     *                      if the client is requesting a specific record, the URI will end in a record number
     *                      that the implementation should parse and add to a WHERE or HAVING clause, specifying
     *                      that _id value.
     * @param projection    The list of columns to put into the cursor. If
     *                      {@code null} all columns are included.
     * @param selection     A selection criteria to apply when filtering rows.
     *                      If {@code null} then all rows are included.
     * @param selectionArgs You may include ?s in selection, which will be replaced by
     *                      the values from selectionArgs, in order that they appear in the selection.
     *                      The values will be bound as Strings.
     * @param sortOrder     How the rows in the cursor should be sorted.
     *                      If {@code null} then the provider is free to define the sort order.
     * @return a Cursor or {@code null}.
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Create and/or open a database to read from it.
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // This Cursor will hold the result of the query.
        Cursor cursor;

        // Figure out if the UriMatcher can match the Uri to a specific code.
        final int match = uriMatcher.match(uri);
        switch (match) {
            case CATALOG:
                // For the CATALOG code, query the catalog table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = db.query(StoreEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case CATALOG_ID:
                // For the CATALOG_ID code, extract out the ID from the URI.
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = StoreEntry.COLUMN_KEY + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // This will perform a query on the catalog table where the _id equals an int to
                // return a Cursor containing that row of the table.
                cursor = db.query(StoreEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        // Set notification URI on the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor.
        return cursor;
    }

    /**
     * Implement this to handle requests for the MIME type of the data at the
     * given URI.
     *
     * @param uri the URI to query.
     * @return a MIME type string, or {@code null} if there is no type.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case CATALOG:
                return StoreEntry.CONTENT_LIST_TYPE;

            case CATALOG_ID:
                return StoreEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + "with match " + match);
        }
    }

    /**
     * Implement this to handle requests to insert a new row.
     *
     * @param uri    The content:// URI of the insertion request. This must not be {@code null}.
     * @param values A set of column_name/value pairs to add to the database.
     *               This must not be {@code null}.
     * @return The URI for the newly inserted item.
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case CATALOG:
                //TODO
        }
        return null;
    }

    /**
     * Implement this to handle requests to delete one or more rows.
     * The implementation should apply the selection clause when performing
     * deletion, allowing the operation to affect multiple rows in a directory.
     *
     * @param uri           The full URI to query, including a row ID (if a specific record is requested).
     * @param selection     An optional restriction to apply to rows when deleting.
     * @param selectionArgs
     * @return The number of rows affected.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * Implement this to handle requests to update one or more rows.
     * The implementation should update all rows matching the selection
     * to set the columns according to the provided values map.
     *
     * @param uri           The URI to query. This can potentially have a record ID if this
     *                      is an update request for a specific record.
     * @param values        A set of column_name/value pairs to update in the database.
     *                      This must not be {@code null}.
     * @param selection     An optional filter to match rows to update.
     * @param selectionArgs
     * @return the number of rows affected.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

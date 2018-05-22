package com.example.android.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Api Contract for the Inventory App
 */
public final class StoreContract {

    /**
     * Create a private constructor because no one should ever create a {@link StoreContract} object.
     */
    private StoreContract() {}

    /**
     * The "Content authority" is a name for the entire content provider.
     * The content authority is the package name for the app.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * (content://com.example.android.inventoryapp/catalog)
     * Is a valid path for looking at data's catalog.
     */
    public static final String PATH_CATALOG = "catalog";

    /**
     * Inner class that defines constant values for the catalog database table.
     * Each entry in the table represents a single store's item.
     */
    public static final class StoreEntry implements BaseColumns {

        /**
         * The content URI to access the catalog data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATALOG);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of item's catalog.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATALOG;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item's catalog.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATALOG;

        /**
         * Name of the database table for the inventoryApp.
         */
        public final static String TABLE_NAME = "catalog";

        /**
         * Unique ID number for the catalog (only fir use by the database table.
         *
         * type: INTEGER
         */
        public final static String COLUMN_KEY = BaseColumns._ID;

        /**
         * product name item in the store
         *
         * type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product";

        /**
         * title of the product in the store
         *
         * type: TEXT
         */
        public final static String COLUMN_PRODUCT_TITLE = "title";

        /**
         * price of the product in the store
         *
         * type: TEXT
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * product quantity in the store
         *
         * type: TEXT
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * product's supplier name
         *
         * type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "name";

        /**
         * product's supplier phone
         *
         * type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE = "phone";

        /**
         * product's supplier email
         *
         * type: TEXT
         */
        public final static String COLUMN_SUPPLIER_EMAIL = "email";
    }
}

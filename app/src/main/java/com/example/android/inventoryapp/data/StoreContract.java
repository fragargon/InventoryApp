package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class StoreContract {

    /** Create a private constructor because no one should ever create a {@link StoreContract} object. */
    private StoreContract() {}

    public static final class StoreEntry implements BaseColumns {

        // Table name
        public final static String TABLE_NAME = "catalog";

        // Column name
        public final static String COLUMN_KEY = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product";
        public final static String COLUMN_PRODUCT_TITLE = "title";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "name";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";
        public final static String COLUMN_SUPPLIER_EMAIL = "email";
    }
}

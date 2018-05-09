package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    /** Create a private constructor because no one should ever create a {@link BookContract} object. */
    private BookContract() {}

    public static final class BookEntry implements BaseColumns {

        // Table name
        public final static String TABLE_NAME = "books";

        // Column name
        public final static String COLUMN_KEY = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "product";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "name";
        public final static String COLUMN_SUPPLIER_PHONE = "phone";
    }
}

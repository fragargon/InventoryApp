package com.example.android.inventoryapp.adapter;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.StoreContract.StoreEntry;

import java.text.NumberFormat;

public class StoreCursorAdapter extends CursorAdapter{

    private static final String LOG_TAG = StoreCursorAdapter.class.getSimpleName();

    /**
     * Constructor that always enables auto-requery.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @deprecated This option is discouraged, as it results in Cursor queries
     * being performed on the application's UI thread and thus can cause poor
     * responsiveness or even Application Not Responding errors.  As an alternative,
     * use {@link LoaderManager} with a {@link CursorLoader}.
     */
    public StoreCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template.
        TextView tvProductTitle = view.findViewById(R.id.list_product_title);
        TextView tvProductName = view.findViewById(R.id.list_product_name);
        TextView tvProductPrice = view.findViewById(R.id.list_product_price);
        TextView tvProductQuantity = view.findViewById(R.id.list_product_quantity);
        ImageButton ibSoldItem = view.findViewById(R.id.list_btn);

        // Extract properties from cursor.
        int titleColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_TITLE);
        int nameColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.COLUMN_QUANTITY);
        final int selectedProductId = cursor.getColumnIndex(StoreEntry.COLUMN_KEY);

        // Read the catalog attribute from the cursor for the current item.
        int titleValue = cursor.getInt(titleColumnIndex);
        // Convert the value from the database (from 0 to 7) to a string.
        // for the product type..
        final String[] title = context.getResources().getStringArray(R.array.product_type);
        // Extract the data from the cursor
        final String name = cursor.getString(nameColumnIndex);
        final float price = cursor.getFloat(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        final int productId = cursor.getInt(selectedProductId);

        // Populate fields with extracted properties.
        // Set text for product title
        tvProductTitle.setText(title[titleValue]);
        // Set the text for the product name
        tvProductName.setText(name);
        // Set the price with the correct currency.
        NumberFormat currentCurrency = NumberFormat.getCurrencyInstance();
        tvProductPrice.setText(currentCurrency.format(price));
        // Set the number for the quantity.
        tvProductQuantity.setText(String.valueOf(quantity));

        // Set the listener to decrement the stock.
        ibSoldItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity > 0) {
                    int newQuantity = quantity - 1;
                    Uri quantityUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, productId);
                    ContentValues values = new ContentValues();
                    values.put(StoreEntry.COLUMN_QUANTITY, newQuantity);
                    int rowUpdated = context.getContentResolver().update(quantityUri,
                            values, null, null);
                    if(!(rowUpdated > 0)) {
                        Toast.makeText(context, context.getString(R.string.product_sold_error),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.product_sold),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.product_sold_stock_zero),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

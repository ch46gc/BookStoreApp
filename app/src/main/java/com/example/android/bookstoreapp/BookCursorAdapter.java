package com.example.android.bookstoreapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CursorAdapter;
import com.example.android.bookstoreapp.data.BookContract.BookEntry;
/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of book data as its data source. This adapter knows
 * how to create list items for each row of book data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {
    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
        /**
         * Makes a new blank list item view. No data is set (or bound) to the views yet.
         *
         * @param context app context
         * @param cursor  The cursor from which to get the data. The cursor is already
         *                moved to the correct position.
         * @param parent  The parent to which the new view is attached to
         * @return the newly created list item view.
         */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }
            /**
             * This method binds the book data (in the current row pointed to by cursor) to the given
             * list item layout. For example, the name for the current book can be set on the name TextView
             * in the list item layout.
             *
             * @param view    Existing view, returned earlier by newView() method
             * @param context app context
             * @param cursor  The cursor from which to get the data. The cursor is already moved to the
             *                correct row.
             */
            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Find individual views that we want to modify in the list item layout
                TextView nameTextView = view.findViewById(R.id.name);
                TextView priceTextView = view.findViewById(R.id.price);
                TextView quantityTextView = view.findViewById(R.id.quantity);
                TextView inventoryTextView = view.findViewById(R.id.inventory);
                // Find the columns of book attributes that we're interested in
                int productNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
                int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
                int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
                int inventoryColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_INVENTORY);


                // Read the book attributes from the Cursor for the current book

                String bookName = cursor.getString(productNameColumnIndex);
                String bookPrice = cursor.getString(priceColumnIndex);
                String bookQuantity = cursor.getString(quantityColumnIndex);
                String bookInventory = cursor.getString(inventoryColumnIndex);

                /*Increase quantity by unit of 1*/
                if(TextUtils.isEmpty(bookInventory)){
                    bookInventory = context.getString(R.string.unknown_inventory);
                }
                // Update the TextViews with the attributes for the current book
                nameTextView.setText(bookName);
                priceTextView.setText(bookPrice);
                quantityTextView.setText(bookQuantity);
                inventoryTextView.setText(bookInventory);

            }
        }


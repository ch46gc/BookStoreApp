package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;
import com.example.android.bookstoreapp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    /**
     * Database helper that will provide access to the database
     */
    private BookDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);

            }
        });
        mDbHelper = new BookDbHelper(this);

        displayDatabaseInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_INVENTORY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_PHONE_NUMBER
        };

        Cursor cursor = db.query(
                BookEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);
        TextView displayView = findViewById(R.id.text_view_book);
        try {
            // Create a header in the Text View that will look ike this:
            //
            // The books table contains<number of rows in a Cursor> books.
            //_id - product name - price - quantity - inventory - supplier name - phone number
            displayView.setText("The books table contains: " + cursor.getCount() + " books.\n\n");
            displayView.append(BookEntry._ID + " - " +
                    BookEntry.COLUMN_BOOK_NAME +
                    "\n" + BookEntry.COLUMN_BOOK_PRICE +
                    "\n" + BookEntry.COLUMN_BOOK_QUANTITY +
                    "\n" + BookEntry.COLUMN_BOOK_INVENTORY +
                    "\n" + BookEntry.COLUMN_BOOK_SUPPLIER_NAME +
                    "\n" + BookEntry.COLUMN_BOOK_PHONE_NUMBER +
                    "\n");
            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int inventoryColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_INVENTORY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int phoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PHONE_NUMBER);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentInventory = cursor.getString(inventoryColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                int currentPhoneNumber = cursor.getInt(phoneNumberColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        +currentPrice + " - " +
                        +currentQuantity + " - " +
                        "\n" + currentInventory + " - " +
                        "\n" + currentSupplierName + " - " +
                        "\n" + currentPhoneNumber));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    public void insertData() {
        //Will get the database in the write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //Create a ContentValues object where the column names are the keys,
        //and book's attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, getString(R.string.product_name));
        values.put(BookEntry.COLUMN_BOOK_PRICE, "10");
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, "1");
        values.put(BookEntry.COLUMN_BOOK_INVENTORY, BookEntry.INVENTORY_ARCHITECTURE);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, getString(R.string.supplier_name));
        values.put(BookEntry.COLUMN_BOOK_PHONE_NUMBER, getString(R.string.supplier_phone));
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New row ID " + newRowId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}






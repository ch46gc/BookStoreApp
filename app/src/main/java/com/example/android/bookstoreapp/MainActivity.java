package com.example.android.bookstoreapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
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

import com.example.android.bookstoreapp.data.BookContract.BookEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for book loader data
     */
    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);

            }
        });


        // Find the ListView which will populate the book data
        ListView bookListView = findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);
        //Setup an Adapter to create a list of items for each row of book data in the Cursor.
        //There is no book data yet(until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);
        //Setup item clickListener
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create new intent to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                //Form the content URI that represents the specific book that was clicked on,
                //by appending the"id"(passed as input to this method) onto the
                //@link BookEntry#CONTENT_URI}.
                //For example, the URI would be "content:com.example.android.bookstoreapp/books/2"
                //if the book with ID 2 was clicked on.

                Uri currentBookUri = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                //Set the URI on the data field of the intent
                intent.setData(currentBookUri);
                //Launch the {@link EditorActivity} to display the data for the current book.
                startActivity(intent);

            }
            });

        //Kick off the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void insertData() {
        //Create a ContentValues object where the column names are the keys,
        //and book's attributes are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, getString(R.string.product_name));
        values.put(BookEntry.COLUMN_BOOK_PRICE, getString(R.string.book_price));
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, getString(R.string.quantity));
        values.put(BookEntry.COLUMN_BOOK_INVENTORY, BookEntry.INVENTORY_ARCHITECTURE);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, getString(R.string.supplier_name));
        values.put(BookEntry.COLUMN_BOOK_PHONE_NUMBER, getString(R.string.supplier_phone));

        // Insert a new row for Android SQLite Essentials into the provider using the ContentResolver.
        // Use the {@link BookEntry#CONTENT_URI} to indicate that we want to insert
        // into the books database table.
        // Receive the new content URI that will allow us to access data in the future.
        Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
        Log.v("MainActivity", "New book has been saved " + newUri);
    }
    private void deleteAllBooks() {
        int rowsDeleted = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + "rows deleted from book database");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();

                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define  a projection that specifies the columns from the table we care about.
        String[]projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_INVENTORY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_PHONE_NUMBER};
        //this loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  //Parent activity context
                BookEntry.CONTENT_URI, //Parent content URI to query
                projection,           // Columns to include in the resulting Cursor
                null,                 // No selection clause
                null,                 // No selection arguments
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link BookCursorAdapter} with this new cursor containing updated book data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}



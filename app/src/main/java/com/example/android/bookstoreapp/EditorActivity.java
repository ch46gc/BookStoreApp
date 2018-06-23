package com.example.android.bookstoreapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;
import com.example.android.bookstoreapp.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity {
    /**
     * EditText field to enter the book's name
     */
    private EditText mProductNameEditText;
    /**
     * EditText field to enter the book's quantity
     */
    private EditText mQuantityEditText;
    /**
     * EditText field to enter the book's price
     */
    private EditText mPriceEditText;
    /**
     * EditText field to enter the supplier's name
     */
    private EditText mSupplierNameEditText;
    /**
     * EditText field to enter the supplier's phone number
     */
    private EditText mPhoneNumberEditText;
    /**
     * EditText field to enter the book's inventory
     */
    private Spinner mInventorySpinner;
    /**
     * Category of the book. The possible valid values are in the BookContract.java file:
     * {@link BookEntry#INVENTORY_ARCHITECTURE},{@link BookEntry#INVENTORY_ART},
     * {@link BookEntry#INVENTORY_CHILDREN_FICTION},{@link BookEntry#INVENTORY_COMPUTERS},
     * {@link BookEntry#INVENTORY_EDUCATION},{@link BookEntry#INVENTORY_LITERARY_COLLECTIONS},
     * {@link BookEntry#INVENTORY_MEDICAL},{@link BookEntry#INVENTORY_LAW},
     * {@link BookEntry#INVENTORY_PHILOSOPHY},{@link BookEntry#INVENTORY_SCIENCE},
     * {@link BookEntry#INVENTORY_PSYCHOLOGY},{@link BookEntry#INVENTORY_MUSIC},
     * {@link BookEntry#INVENTORY_MATHEMATICS},{@link BookEntry#INVENTORY_UNKNOWN}.
     */
    private int mInventory = BookEntry.INVENTORY_UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        // Find all relevant views that we will need to read user input from
        mProductNameEditText = findViewById(R.id.edit_product_name);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mPriceEditText = findViewById(R.id.edit_price);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mPhoneNumberEditText = findViewById(R.id.edit_phone_number);
        mInventorySpinner = findViewById(R.id.spinner);
        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the category of the book.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter inventorySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_inventory_options, android.R.layout.simple_spinner_item);
        // Specify dropdown layout style - simple list view with 1 item per line
        inventorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mInventorySpinner.setAdapter(inventorySpinnerAdapter);
        // Set the integer mSelected to the constant values
        mInventorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_unknown))) {
                        mInventory = BookEntry.INVENTORY_UNKNOWN; // Unknown
                    } else if (selection.equals(getString(R.string.category_architecture))) {
                        mInventory = BookEntry.INVENTORY_ARCHITECTURE; // Architecture Category
                    } else if (selection.equals(getString(R.string.category_art)))
                        mInventory = BookEntry.INVENTORY_ART;  // Art Category
                } else if (selection.equals(getString(R.string.category_children_fiction))) {
                    mInventory = BookEntry.INVENTORY_CHILDREN_FICTION; // Children's fiction category
                } else if (selection.equals(getString(R.string.category_computers))) {
                    mInventory = BookEntry.INVENTORY_COMPUTERS;// Computer Category
                } else if (selection.equals(getString(R.string.category_education))) {
                    mInventory = BookEntry.INVENTORY_EDUCATION; // Education Category
                } else if (selection.equals(getString(R.string.category_literary_collections))) {
                    mInventory = BookEntry.INVENTORY_LITERARY_COLLECTIONS; // Literary collections category
                } else if (selection.equals(getString(R.string.category_medical))) {
                    mInventory = BookEntry.INVENTORY_MEDICAL; // Medical category
                } else if (selection.equals(getString(R.string.category_law))) {
                    mInventory = BookEntry.INVENTORY_LAW; // Law category
                } else if (selection.equals(getString(R.string.category_philosophy))) {
                    mInventory = BookEntry.INVENTORY_PHILOSOPHY; // Philosophy category
                } else if (selection.equals(getString(R.string.category_science))) {
                    mInventory = BookEntry.INVENTORY_SCIENCE; // Science category
                } else if (selection.equals(getString(R.string.category_psychology))) {
                    mInventory = BookEntry.INVENTORY_PSYCHOLOGY;// Psychology category
                } else if (selection.equals(getString(R.string.category_music))) {
                    mInventory = BookEntry.INVENTORY_MUSIC; // Music category
                } else {
                    mInventory = BookEntry.INVENTORY_MATHEMATICS; // Mathematics category
                    // Psychology category

                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mInventory = BookEntry.INVENTORY_UNKNOWN; //Unknown
            }
        });
    }

    private void insertData() {
        String productNameString = mProductNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        double price = Double.valueOf(priceString);


        BookDbHelper mDbHelper = new BookDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, productNameString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, price);
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        values.put(BookEntry.COLUMN_BOOK_INVENTORY, mInventory);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_PHONE_NUMBER, phoneNumber);
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving book", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Book saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Shows the user clicked on an option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Save pet to database
                insertData();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (MainActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}











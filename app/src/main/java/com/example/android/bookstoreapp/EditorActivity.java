package com.example.android.bookstoreapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.bookstoreapp.data.BookContract.BookEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Identifier for the book data loader
     */
    private static final int EXISTING_BOOK_LOADER = 0;
    /**
     * Content URI for the existing book ( null if its's a new book)
     */
    private Uri mCurrentBookUri;
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
     * {@link BookEntry#INVENTORY_MATHEMATICS}, or {@link BookEntry#INVENTORY_UNKNOWN}.
     */
    private int mInventory = BookEntry.INVENTORY_UNKNOWN;
    /**
     * int for quantity check
     */
    private int givenQuantity;
    ImageButton mAddition;
    ImageButton mSubtraction;
    private boolean mBookHasChanged = false;
    //OnTouchListener that will listen for any users touches on a View, this will imply that they
    //are modifying the view, and change the mBookHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mBookHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //The intent that was used to launch the activity,
        //in order to figure out if we are creating a new book or editing and existing one.
        Intent intent = getIntent();
        mCurrentBookUri = intent.getData();
        //If the intent Doesn't contain a book URI, then we know that we are creating a new book.
        if (mCurrentBookUri == null) {
            //This is a new book, so change the app bar to say "Add a book"
            setTitle(getString(R.string.editor_activity_title_new_book));
            //Invalidate the options menu, so the "Delete" menu option can be hidden.
            //(It doesn't make sense to delete a book that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            //Otherwise this is an existing book, so change app bar to say " Edit Book"
            setTitle(getString(R.string.editor_activity_title_edit_book));
            //Initialize a loader to read the book data from database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        mProductNameEditText = findViewById(R.id.edit_product_name);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mPriceEditText = findViewById(R.id.edit_price);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mPhoneNumberEditText = findViewById(R.id.edit_phone_number);
        mInventorySpinner = findViewById(R.id.spinner);
        /* Button for increasing quantity */
        mAddition = findViewById(R.id.add_button);
        /* Button for decreasing quantity  */
        mSubtraction = findViewById(R.id.minus_button);
        mProductNameEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mPhoneNumberEditText.setOnTouchListener(mTouchListener);
        mInventorySpinner.setOnTouchListener(mTouchListener);
        mAddition.setOnTouchListener(mTouchListener);
        mSubtraction.setOnTouchListener(mTouchListener);
        //increase quantity
        mAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, R.string.quantity_required, Toast.LENGTH_SHORT).show();
                    } else {
                    givenQuantity = Integer.parseInt(quantity);
                    mQuantityEditText.setText(String.valueOf(givenQuantity + 1));
                }

            }
        });
        //decrease quantity
        mSubtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = mQuantityEditText.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    Toast.makeText(EditorActivity.this, R.string.quantity_required, Toast.LENGTH_SHORT).show();
                    } else {
                    givenQuantity = Integer.parseInt(quantity);
                    //To validate if quantity is greater than 0
                    if ((givenQuantity - 1) >= 0) {
                        mQuantityEditText.setText(String.valueOf(givenQuantity - 1));
                    } else {
                        Toast.makeText(EditorActivity.this, R.string.quantity_warning, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        /* Button for phone call  */
        ImageButton mPhone = findViewById(R.id.call);
        //button for phone call
        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneNumber = mPhoneNumberEditText.getText().toString().trim();
                phoneOrder(PhoneNumber);

            }
        });
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
                    if (selection.equals(getString(R.string.category_architecture))) {
                        mInventory = BookEntry.INVENTORY_ARCHITECTURE; // Architecture
                    } else if (selection.equals(getString(R.string.category_art))) {
                        mInventory = BookEntry.INVENTORY_ART; //  Art Category
                    } else if (selection.equals(getString(R.string.category_children_fiction))) {
                        mInventory = BookEntry.INVENTORY_CHILDREN_FICTION;  // Children's fiction Category
                    } else if (selection.equals(getString(R.string.category_computers))) {
                        mInventory = BookEntry.INVENTORY_COMPUTERS; // Computer category
                    } else if (selection.equals(getString(R.string.category_education))) {
                        mInventory = BookEntry.INVENTORY_EDUCATION;// Education  Category
                    } else if (selection.equals(getString(R.string.category_literary_collections))) {
                        mInventory = BookEntry.INVENTORY_LITERARY_COLLECTIONS; // Literary collections Category
                    } else if (selection.equals(getString(R.string.category_medical))) {
                        mInventory = BookEntry.INVENTORY_MEDICAL; //  Medical category
                    } else if (selection.equals(getString(R.string.category_law))) {
                        mInventory = BookEntry.INVENTORY_LAW; // Law category
                    } else if (selection.equals(getString(R.string.category_philosophy))) {
                        mInventory = BookEntry.INVENTORY_PHILOSOPHY; // Philosophy category
                    } else if (selection.equals(getString(R.string.category_science))) {
                        mInventory = BookEntry.INVENTORY_SCIENCE; // Science category
                    } else if (selection.equals(getString(R.string.category_psychology))) {
                        mInventory = BookEntry.INVENTORY_PSYCHOLOGY; //  Psychology category
                    } else if (selection.equals(getString(R.string.category_music))) {
                        mInventory = BookEntry.INVENTORY_MUSIC;// Music category
                    } else if (selection.equals(getString(R.string.category_Mathematics))) {
                        mInventory = BookEntry.INVENTORY_MATHEMATICS; // Mathematics category
                    } else {
                        mInventory = BookEntry.INVENTORY_UNKNOWN; // Unknown

                    }

                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mInventory = BookEntry.INVENTORY_UNKNOWN; //Unknown
            }
        });
    }

    /**
     * Get user input from editor and save book into database.
     */
    private void saveData() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productNameString = mProductNameEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        // Check if
        // this is supposed to be a new book
        // and check if all the fields in the editor are blank
        if (mCurrentBookUri == null && TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(quantityString)
                || TextUtils.isEmpty(priceString) || TextUtils.isEmpty(supplierNameString) ||
                TextUtils.isEmpty(supplierNameString) || TextUtils.isEmpty(phoneNumber)
                || mInventory == BookEntry.INVENTORY_UNKNOWN) {
            Toast.makeText(this, getString(R.string.field_required),
                    Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(productNameString)) {
                mProductNameEditText.setError(getString(R.string.name_required));
                return;
            }
            else if (TextUtils.isEmpty(priceString)) {
                mPriceEditText.setError(getString(R.string.price_required));
                return;
            }
           else if(TextUtils.isEmpty(quantityString)) {
            mQuantityEditText.setError(getString(R.string.quantity_required_edit));
            return;
            }
            else if(TextUtils.isEmpty(supplierNameString)) {
            mSupplierNameEditText.setError(getString(R.string.supplier_name_required));
            return;
            }
            else if(TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberEditText.setError(getString(R.string.phone_number_required));
            //No fields were modified, we return early without creating a new book.
            //No Need to create ContentValues or do any ContentProvider operations.
            return;
        }
        // Create a ContentValues object where column names are the keys,
        // and book attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_NAME, productNameString);
        values.put(BookEntry.COLUMN_BOOK_PRICE, priceString);
        values.put(BookEntry.COLUMN_BOOK_INVENTORY, mInventory);
        values.put(BookEntry.COLUMN_BOOK_SUPPLIER_NAME, supplierNameString);
        values.put(BookEntry.COLUMN_BOOK_PHONE_NUMBER, phoneNumber);
        int quantity = 0;
        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(BookEntry.COLUMN_BOOK_QUANTITY, quantity);
        //This will check a new or existing book with the the mCurrentBookUri is null or not
        if (mCurrentBookUri == null) {
            Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);
            if (newUri == null) {
                //If the new content URI is null, then there is an error wth inserting
                Toast.makeText(this, getString(R.string.editor_insert_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                //Otherwise, the insertion was successful and we can display a toast
                Toast.makeText(this, getString(R.string.editor_insert_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING book, so update the book with content URI: mCurrentBookUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentBookUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentBookUri, values, null, null);
            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                //If no rows are affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                //Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new book, hide the "Delete" menu item.
        if (mCurrentBookUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Shows the user clicked on an option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Save book to database
                saveData();
                //Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the book hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!mBookHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }


                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the book hasn't changed, continue with handling back button press
        if (!mBookHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();

                    }
                };
        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Since the editor shows all the pet attributes, define a projection that contains
        //all columns from the book table
        String[] projection = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_NAME,
                BookEntry.COLUMN_BOOK_PRICE,
                BookEntry.COLUMN_BOOK_QUANTITY,
                BookEntry.COLUMN_BOOK_INVENTORY,
                BookEntry.COLUMN_BOOK_SUPPLIER_NAME,
                BookEntry.COLUMN_BOOK_PHONE_NUMBER};
        //This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  //Parent activity context
                mCurrentBookUri,               //Query the content URI for the current book
                projection,                   //Columns to include in the resulting Cursor
                null,                  //No selection clause
                null,              // No selection arguments
                null);                 //Default sort order

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_QUANTITY);
            int inventoryColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_INVENTORY);
            int supplierNameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_SUPPLIER_NAME);
            int phoneNumberColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PHONE_NUMBER);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            int inventory = cursor.getInt(inventoryColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String phoneNumber = cursor.getString(phoneNumberColumnIndex);
            // Update the views on the screen with the values from the database
            mProductNameEditText.setText(name);
            mPriceEditText.setText(String.format("$%s", price));
            mQuantityEditText.setText(quantity);
            mSupplierNameEditText.setText(supplierName);
            mPhoneNumberEditText.setText(phoneNumber);
            // Inventory is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options .
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (inventory) {
                case BookEntry.INVENTORY_ARCHITECTURE:
                    mInventorySpinner.setSelection(1);
                    break;
                case BookEntry.INVENTORY_ART:
                    mInventorySpinner.setSelection(2);
                    break;
                case BookEntry.INVENTORY_CHILDREN_FICTION:
                    mInventorySpinner.setSelection(3);
                    break;
                case BookEntry.INVENTORY_COMPUTERS:
                    mInventorySpinner.setSelection(4);
                    break;
                case BookEntry.INVENTORY_EDUCATION:
                    mInventorySpinner.setSelection(5);
                    break;
                case BookEntry.INVENTORY_LITERARY_COLLECTIONS:
                    mInventorySpinner.setSelection(6);
                    break;
                case BookEntry.INVENTORY_MEDICAL:
                    mInventorySpinner.setSelection(7);
                    break;
                case BookEntry.INVENTORY_LAW:
                    mInventorySpinner.setSelection(8);
                    break;
                case BookEntry.INVENTORY_PHILOSOPHY:
                    mInventorySpinner.setSelection(9);
                    break;
                case BookEntry.INVENTORY_SCIENCE:
                    mInventorySpinner.setSelection(10);
                    break;
                case BookEntry.INVENTORY_PSYCHOLOGY:
                    mInventorySpinner.setSelection(11);
                    break;
                case BookEntry.INVENTORY_MUSIC:
                    mInventorySpinner.setSelection(12);
                    break;
                case BookEntry.INVENTORY_MATHEMATICS:
                    mInventorySpinner.setSelection(13);
                    break;
                default:
                    mInventorySpinner.setSelection(0);
                    break;
            }

        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
        mSupplierNameEditText.setText("");
        mPhoneNumberEditText.setText("");
        mInventorySpinner.setSelection(0); // Select "Unknown"
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners

        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_message);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_message);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the book.
                deleteBook();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing book.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteBook() {
        // Only perform the delete if this is an existing book.
        if (mCurrentBookUri != null) {
            // Call the ContentResolver to delete the book at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentBookUri
            // content URI already identifies the book that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentBookUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_book_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_book_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
    /**
     * Call book's supplier
     *
     * @param phoneNumber - supplier's phone number
     */
    private void phoneOrder(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
















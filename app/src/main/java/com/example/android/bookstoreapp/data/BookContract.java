package com.example.android.bookstoreapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class BookContract {
    // This will prevent someone from accidentally instantiating the contract class,
    //giving it an empty constructor.
    private BookContract() {
        }
    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.android.bookstoreapp";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.bookstoreapp/books/ is a valid path for
     * looking at book data. content://com.example.android.bookstoreapp/shipment/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "shipment".
     */
    public static final String PATH_BOOKS = "books";
    /**
     * Inner class that defines constant values for books database table.
     * Each entry in the table will represent a single book.
     */
    public static final class BookEntry implements BaseColumns {
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of books.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        /**
         * The MIME type of the {@link #CONTENT_URI} for a single book.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        /**
         * The content URI to access the book data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKS);
        /**
         * Name of database table for books
         */
        public final static String TABLE_NAME = "books";
        /**
         * Unique ID number for the book (only for use in the database table.)
         * <p>
         * Type:INTEGER
         */
        public final static String _ID = BaseColumns._ID;
        /**
         * Name of the book.
         * <p>
         * Type:TEXT
         */
        public final static String COLUMN_BOOK_NAME = "productName";
        /**
         * Product price of the book.
         * <p>
         * Type:INTEGER
         */
        public final static String COLUMN_BOOK_PRICE = "price";
        /**
         * Quantity of books.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_BOOK_QUANTITY = "quantity";
        /**
         * Inventory of books
         * Type:Text
         */
        public static final String COLUMN_BOOK_INVENTORY = "inventory";
        /**
         * Supplier name of the book.
         * <p>
         * Type:TEXT
         */

        public final static String COLUMN_BOOK_SUPPLIER_NAME = "supplierName";
        /**
         * Phone number for the supplier.
         * <p>
         * Type:INTEGER
         */
        public static final String COLUMN_BOOK_PHONE_NUMBER = "phoneNumber";
        /**
         * Possible values for the inventory of the book
         */
        public static final int INVENTORY_UNKNOWN = 0;
        public static final int INVENTORY_ARCHITECTURE = 1;
        public static final int INVENTORY_ART = 2;
        public static final int INVENTORY_CHILDREN_FICTION = 3;
        public static final int INVENTORY_COMPUTERS = 4;
        public static final int INVENTORY_EDUCATION = 5;
        public static final int INVENTORY_LITERARY_COLLECTIONS = 6;
        public static final int INVENTORY_MEDICAL = 7;
        public static final int INVENTORY_LAW = 8;
        public static final int INVENTORY_PHILOSOPHY = 9;
        public static final int INVENTORY_SCIENCE = 10;
        public static final int INVENTORY_PSYCHOLOGY = 11;
        public static final int INVENTORY_MUSIC = 12;
        public static final int INVENTORY_MATHEMATICS = 13;
        /**
         * @param inventory for the book
         * @return whether or not the given inventory is {@link #INVENTORY_UNKNOWN},
         * {@link #INVENTORY_ARCHITECTURE}, {@link #INVENTORY_ART},
         * {@link #INVENTORY_CHILDREN_FICTION}, {@link #INVENTORY_COMPUTERS},
         * {@link #INVENTORY_EDUCATION}, {@link #INVENTORY_LITERARY_COLLECTIONS},
         * {@link #INVENTORY_MEDICAL}, {@link #INVENTORY_LAW}, {@link #INVENTORY_PHILOSOPHY},
         * {@link #INVENTORY_SCIENCE}, {@link #INVENTORY_SCIENCE}, {@link #INVENTORY_PSYCHOLOGY},
         * {@link #INVENTORY_MUSIC}, {@link #INVENTORY_MATHEMATICS}
         */
        public static boolean isValidInventory(int inventory) {
            if (inventory == INVENTORY_UNKNOWN || inventory == INVENTORY_ARCHITECTURE || inventory == INVENTORY_ART
                    || inventory == INVENTORY_CHILDREN_FICTION || inventory == INVENTORY_COMPUTERS ||
                    inventory == INVENTORY_EDUCATION || inventory == INVENTORY_LITERARY_COLLECTIONS ||
                    inventory == INVENTORY_MEDICAL || inventory == INVENTORY_LAW || inventory ==
                    INVENTORY_PHILOSOPHY || inventory == INVENTORY_SCIENCE || inventory == INVENTORY_PSYCHOLOGY
                    || inventory == INVENTORY_MUSIC || inventory == INVENTORY_MATHEMATICS) {
                return true;
            } else {
                return false;
            }
        }
    }
}




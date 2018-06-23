package com.example.android.bookstoreapp.data;

import android.provider.BaseColumns;

public final class BookContract {
    // This will prevent someone from accidentally instantiating the contract class,
    //giving it an empty constructor.
    private BookContract() {
    }

    /**
     * Inner class that defines constant values for books database table.
     * Each entry in the table will represent a single book.
     */
    public static final class BookEntry implements BaseColumns {
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
         * Phone number for the supplier.
         * <p>
         * Type:INTEGER
         */
        public static final String COLUMN_BOOK_PHONE_NUMBER = "phoneNumber";

    }
}





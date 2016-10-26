package com.mikeivan.apps.scatternotes.data;

import android.provider.BaseColumns;

/**
 * Created by Mivan on 9/13/2016.
 *
 * This is a contract for the beer db. These are the names
 * of the columns to be used.
 */
public final class BeerContract {

    private BeerContract() {
    }

    public static final class BeerTableEntry implements BaseColumns {

        /**
         * Name of database table for beers
         */
        public final static String TABLE_NAME = "beer";


        /**
         *  Unique ID number for the beer (only for use in the database table).
         *  Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         *  Name of the company that produces the beer
         *  Type: TEXT
         */
        public final static String COLUMN_BEER_COMPANY = "company";

        /**
         *  Name of the product of the beer.
         *  Type: TEXT
         */
        public final static String COLUMN_BEER_PRODUCT = "product";

        /**
         *  User's rating of the beer.
         *  The only possible values are: RATING_ZERO, RATING_ONE, RATING_TWO,
         *                                RATING_THREE, RATING_FOUR, and RATING_FIVE
         *  Type: INTEGER
         */
        public final static String COLUMN_BEER_RATING = "rating";

        /**
         *  Notes on the specific beer.
         *  Type: TEXT
         */
        public final static String COLUMN_BEER_NOTES = "notes";

        /**
         *  Date of the entry of the beer.
         *  Type: TEXT
         */
        public final static String COLUMN_BEER_DATE = "date";

        /**
         *  Possible values for the rating of the beer.
         */
        public static final int RATING_ZERO = 0;
        public static final int RATING_ONE = 1;
        public static final int RATING_TWO = 2;
        public static final int RATING_THREE = 3;
        public static final int RATING_FOUR = 4;
        public static final int RATING_FIVE = 5;
    }

}



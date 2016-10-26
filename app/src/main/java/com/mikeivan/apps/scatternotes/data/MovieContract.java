package com.mikeivan.apps.scatternotes.data;

import android.provider.BaseColumns;

/**
 * Created by Mivan on 9/19/2016.
 *
 * This is a contract for the movie db. These are the names
 * of the columns to be used.
 */
public class MovieContract {

    private MovieContract() {
    }

    public static final class MovieTableEntry implements BaseColumns {

        /**
         * Name of database table for movies
         */
        public final static String TABLE_NAME = "movie";


        /**
         *  Unique ID number for the movie (only for use in the database table).
         *  Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         *  Title of the movie.
         *  Type: TEXT
         */
        public final static String COLUMN_MOVIE_TITLE = "title";

        /**
         *  Year of the movie.
         *  Type: INTEGER
         */
        public final static String COLUMN_MOVIE_YEAR = "year";

        /**
         *  User's rating of the movie.
         *  The only possible values are: RATING_ZERO, RATING_ONE, RATING_TWO,
         *                                RATING_THREE, RATING_FOUR, and RATING_FIVE
         *  Type: INTEGER
         */
        public final static String COLUMN_MOVIE_RATING = "rating";

        /**
         *  Notes on the movie.
         *  Type: TEXT
         */
        public final static String COLUMN_MOVIE_NOTES = "notes";

        /**
         *  Date of the entry of the movie.
         *  Type: TEXT
         */
        public final static String COLUMN_MOVIE_DATE = "date";

        /**
         *  Possible values for the rating of the movie.
         */
        public static final int RATING_ZERO = 0;
        public static final int RATING_ONE = 1;
        public static final int RATING_TWO = 2;
        public static final int RATING_THREE = 3;
        public static final int RATING_FOUR = 4;
        public static final int RATING_FIVE = 5;
    }

}

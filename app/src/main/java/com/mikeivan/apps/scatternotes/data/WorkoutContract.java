package com.mikeivan.apps.scatternotes.data;

import android.provider.BaseColumns;

/**
 * Created by Mivan on 9/13/2016.
 *
 * This is a contract for the workout db. These are the names
 * of the columns to be used.
 */
public final class WorkoutContract {

    private WorkoutContract() {
    }

    public static final class WorkoutTableEntry implements BaseColumns {

        /**
         * Name of database table for workouts
         */
        public final static String TABLE_NAME = "workout";


        /**
         *  Unique ID number for the workout (only for use in the database table).
         *  Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         *  Date of the entry of the workout.
         *  Type: TEXT
         */
        public final static String COLUMN_GYM_DATE = "date";


    }

}



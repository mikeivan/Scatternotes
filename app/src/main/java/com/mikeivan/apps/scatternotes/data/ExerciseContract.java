package com.mikeivan.apps.scatternotes.data;

import android.provider.BaseColumns;


/**
 * Created by Mivan on 9/19/2016.
 *
 * This is a contract for the exercise db. These are the names
 * of the columns to be used.
 */
public class ExerciseContract {

    private ExerciseContract() {
    }

    public static final class ExerciseTableEntry implements BaseColumns {

        /**
         * Name of database table for exercises
         */
        public final static String TABLE_NAME = "exercise";

        /**
         *  Unique ID number for the exercise (only for use in the database table).
         *  Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         *  _id of the parent in the workout table. All exercises are children of one
         *  parent in the workout table. Each row in the workout table has zero or more
         *  exercise children
         *  Type: INTEGER
         */
        public final static String COLUMN_WORKOUT_PARENT = "parent_id";

        /**
         *  Description of the exercise done.
         *  Type: TEXT
         */
        public final static String COLUMN_WORKOUT_EXERCISE = "exercise";

        /**
         *  Date of the entry of the exercise.
         *  Type: TEXT
         */
        public final static String COLUMN_WORKOUT_DATE = "date";


    }

}

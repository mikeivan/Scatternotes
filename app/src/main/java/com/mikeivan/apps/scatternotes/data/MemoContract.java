package com.mikeivan.apps.scatternotes.data;

import android.provider.BaseColumns;

/**
 * Created by Mivan on 9/25/2016.
 *
 * This is a contract for the memo db. These are the names
 * of the columns to be used.
 */

public class MemoContract {

    private MemoContract() {
    }

    public static final class MemoTableEntry implements BaseColumns {

        /**
         * Name of database table for memos
         */
        public final static String TABLE_NAME = "memo";

        /**
         *  Unique ID number for the memo (only for use in the database table).
         *  Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         *  Title of the memo.
         *  Type: TEXT
         */
        public final static String COLUMN_MEMO_TITLE = "title";

        /**
         *  Notes of the memo.
         *  Type: TEXT
         */
        public final static String COLUMN_MEMO_NOTES = "notes";

        /**
         *  Date of the entry of the memo.
         *  Type: TEXT
         */
        public final static String COLUMN_MEMO_DATE = "date";


    }

}
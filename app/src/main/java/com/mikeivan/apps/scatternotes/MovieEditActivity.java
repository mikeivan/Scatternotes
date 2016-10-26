package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.mikeivan.apps.scatternotes.data.MovieContract;
import com.mikeivan.apps.scatternotes.data.MovieContract.MovieTableEntry;
import com.mikeivan.apps.scatternotes.data.MovieDbHelper;

/*
 * This activity is used to update or delete a memo row.
 */
public class MovieEditActivity extends AppCompatActivity {


    // EditText field to enter the movie's title
    private EditText mTitleEditText;


    // EditText field to enter the movie's year
    private EditText mYearEditText;


    // EditText field to enter the movie's notes
    private EditText mNotesEditText;

    // Spinner to enter the beer's rating
    private Spinner mRatingSpinner;

    // This keeps track of what rating was selected as it is selected
    // default value is 0
    private int mRating = MovieTableEntry.RATING_ZERO;

    /*
     * This function updates a movie in the movie database
     * date is unaltered - still date of creation
     */
    private boolean updateMovie() {
        // Retrieve information in EditTexts
        String titleString = mTitleEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();
        int yearInt;

        if(titleString.equals("")) {
            Toast.makeText(getBaseContext(), "Title cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Parsing an int from a String can throw an exception
        try {
            yearInt = Integer.parseInt(mYearEditText.getText().toString().trim());
        } catch(NumberFormatException e) {
            yearInt = 2016;
        }

        // Create database interaction objects
        MovieDbHelper mDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, titleString);
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, yearInt);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, mRating);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, notesString);

        // Create the where clause, so we update only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = MovieTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(MovieTableEntry._ID, -1) };

        // Update the row, returning the number of rows updated (should be 1 or 0)
        long newRowId = db.update(MovieTableEntry.TABLE_NAME, values, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Movie saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
           //Toast.makeText(getApplicationContext(), "Error with saving movie", Toast.LENGTH_SHORT).show();
            Log.v("MovieEditActivity", "Error with updating memo");
            return false;
        }
    }

    /*
     * This function deletes a movie row from the movie database
     */
    private void deleteMovie() {
        // Create database interaction objects
        MovieDbHelper mDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create the where clause, so we delete only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = MovieTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(MovieTableEntry._ID, -1) };

        // delete the row, returning the number of rows deleted (should be 1 or 0)
        long newRowId = db.delete(MovieTableEntry.TABLE_NAME, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Movie deleted: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with deleting movie", Toast.LENGTH_SHORT).show();
            Log.v("MovieEditActivity", "Error with deleting memo");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_edit);


        // Setup FAB to update movie
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateMovie()) {
                    Intent i = new Intent(MovieEditActivity.this, MovieViewActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        // Setup FAB to delete movie
        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMovie();
                Intent i = new Intent(MovieEditActivity.this, MovieViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mTitleEditText = (EditText) findViewById(R.id.edit_movie_title);
        mYearEditText = (EditText) findViewById(R.id.edit_movie_year);
        mNotesEditText = (EditText) findViewById(R.id.edit_movie_notes);
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);

        // Populate EditTexts with actual values from the db, this should make editing easier
        mTitleEditText.setText(getIntent().getStringExtra(MovieTableEntry.COLUMN_MOVIE_TITLE));
        mYearEditText.setText(""+getIntent().getIntExtra(MovieTableEntry.COLUMN_MOVIE_YEAR, -1));
        mNotesEditText.setText(getIntent().getStringExtra(MovieTableEntry.COLUMN_MOVIE_NOTES));

        setupSpinner();

        // Set spinner to rating from db
        mRatingSpinner.setSelection(getIntent().getIntExtra(MovieTableEntry.COLUMN_MOVIE_RATING, 0));
    }

    // Code borrowed and adapted from Udacity course: "Android Basics: Data Storage"
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter ratingSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_rating_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ratingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mRatingSpinner.setAdapter(ratingSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mRatingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.rating_one))) {
                        mRating = MovieTableEntry.RATING_ONE;
                    } else if (selection.equals(getString(R.string.rating_two))) {
                        mRating = MovieTableEntry.RATING_TWO;
                    } else if (selection.equals(getString(R.string.rating_three))) {
                        mRating = MovieTableEntry.RATING_THREE;
                    } else if (selection.equals(getString(R.string.rating_four))) {
                        mRating = MovieTableEntry.RATING_FOUR;
                    } else if (selection.equals(getString(R.string.rating_five))) {
                        mRating = MovieTableEntry.RATING_FIVE;
                    } else {
                        mRating = MovieTableEntry.RATING_ZERO;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRating = 0; // Zero
            }
        });
    }
}

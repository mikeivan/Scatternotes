package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.mikeivan.apps.scatternotes.data.MovieContract;
import com.mikeivan.apps.scatternotes.data.MovieContract.MovieTableEntry;

/*
 * This activity is used to search within the memo database.
 * There are search fields for all enter-able fields from the MemoAddActivity.
 * There is also a search field to search all other fields.
 *
 * The search query string is sent via intent to the MemoSearchResultsActivity,
 * which will do the actual query.
 */
public class MovieSearchActivity extends AppCompatActivity {

    // Setup EditTexts for searching
    private Spinner mRatingSpinner;
    private EditText mTitleEditText;
    private EditText mYearEditText;
    private EditText mNotesEditText;
    private EditText mSearchAllEditText;

    // This keeps track of what rating was selected as it is selected
    // default value is 0
    private int mRating = MovieContract.MovieTableEntry.RATING_ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search);

        // Setup FAB for searching movie title
        FloatingActionButton fab_search_title = (FloatingActionButton) findViewById(R.id.fab_search_title);
        fab_search_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieSearchActivity.this, MovieSearchResultsActivity.class);
                i.putExtra("query", MovieTableEntry.COLUMN_MOVIE_TITLE + " LIKE \"%" + mTitleEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching movie year
        FloatingActionButton fab_search_year = (FloatingActionButton) findViewById(R.id.fab_search_year);
        fab_search_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieSearchActivity.this, MovieSearchResultsActivity.class);
                i.putExtra("query", MovieTableEntry.COLUMN_MOVIE_YEAR + " LIKE \"%" + mYearEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching movie rating
        FloatingActionButton fab_search_rating = (FloatingActionButton) findViewById(R.id.fab_search_rating);
        fab_search_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieSearchActivity.this, MovieSearchResultsActivity.class);
                i.putExtra("query", MovieTableEntry.COLUMN_MOVIE_RATING + " = " + mRating);
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching movie notes
        FloatingActionButton fab_search_notes = (FloatingActionButton) findViewById(R.id.fab_search_notes);
        fab_search_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieSearchActivity.this, MovieSearchResultsActivity.class);
                i.putExtra("query", MovieTableEntry.COLUMN_MOVIE_NOTES + " LIKE \"%" + mNotesEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching all above fields
        FloatingActionButton fab_search_all = (FloatingActionButton) findViewById(R.id.fab_search_all);
        fab_search_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MovieSearchActivity.this, MovieSearchResultsActivity.class);
                i.putExtra("query", MovieTableEntry.COLUMN_MOVIE_TITLE + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + MovieTableEntry.COLUMN_MOVIE_YEAR + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + MovieTableEntry.COLUMN_MOVIE_RATING + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + MovieTableEntry.COLUMN_MOVIE_NOTES + " LIKE \"%" +  mSearchAllEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mTitleEditText = (EditText) findViewById(R.id.search_movie_title);
        mYearEditText = (EditText) findViewById(R.id.search_movie_year);
        mNotesEditText = (EditText) findViewById(R.id.search_movie_notes);
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);
        mSearchAllEditText = (EditText) findViewById(R.id.search_movie_all);

        setupSpinner();
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
                        mRating = MovieTableEntry.RATING_ZERO;
                    } else if (selection.equals(getString(R.string.rating_two))) {
                        mRating = MovieTableEntry.RATING_ONE;
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

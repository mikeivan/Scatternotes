package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.mikeivan.apps.scatternotes.data.MovieContract;
import com.mikeivan.apps.scatternotes.data.MovieContract.MovieTableEntry;
import com.mikeivan.apps.scatternotes.data.MovieDbHelper;

/*
 * This activity shows an advanced view of an individual movie
 */
public class MovieViewAdvActivity extends AppCompatActivity {

    // Gives us access to movie db
    private MovieDbHelper mMovieDbHelper;

    // Variables for all fields of movie row
    int currentID;
    String currentTitle;
    int currentYear;
    int currentRating;
    String currentNotes;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list_item_advanced);

        int target = getIntent().getIntExtra("MoviePosition", -1);

        mMovieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] project = {
                MovieTableEntry._ID,
                MovieTableEntry.COLUMN_MOVIE_TITLE,
                MovieTableEntry.COLUMN_MOVIE_YEAR,
                MovieTableEntry.COLUMN_MOVIE_RATING,
                MovieTableEntry.COLUMN_MOVIE_NOTES,
                MovieTableEntry.COLUMN_MOVIE_DATE
        };

        String whereClause = MovieTableEntry._ID + "=?";
        String[] selectionArgs = { "" + target };

        Cursor cursor = db.query(MovieTableEntry.TABLE_NAME,
                project,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MovieTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_TITLE);
            int yearColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_YEAR);
            int ratingColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_RATING);
            int notesColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_DATE);

            //Toast.makeText(getBaseContext(), "Query count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();

            // Only set TextViews if something is found
            if (cursor.moveToNext() && cursor.getCount() > 0) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                currentTitle = cursor.getString(titleColumnIndex);
                currentYear = cursor.getInt(yearColumnIndex);
                currentRating = cursor.getInt(ratingColumnIndex);
                currentNotes = cursor.getString(notesColumnIndex);
                currentDate = cursor.getString(dateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView

                TextView titleTextView = (TextView) findViewById(R.id.movie_text_view);
                titleTextView.setText(currentTitle);

                TextView yearTextView = (TextView) findViewById(R.id.year_text_view);
                yearTextView.setText(""+currentYear);

                TextView ratingTextView = (TextView) findViewById(R.id.rating_text_view);
                ratingTextView.setText(""+currentRating);

                TextView notesTextView = (TextView) findViewById(R.id.notes_text_view);
                notesTextView.setText(currentNotes);

                TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
                dateTextView.setText(currentDate);

                // Only setup FAB if we found something - FAB to edit movie
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MovieViewAdvActivity.this, MovieEditActivity.class);
                        i.putExtra(MovieTableEntry._ID, currentID);
                        i.putExtra(MovieTableEntry.COLUMN_MOVIE_TITLE, currentTitle);
                        i.putExtra(MovieTableEntry.COLUMN_MOVIE_YEAR, currentYear);
                        i.putExtra(MovieTableEntry.COLUMN_MOVIE_RATING, currentRating);
                        i.putExtra(MovieTableEntry.COLUMN_MOVIE_NOTES, currentNotes);
                        i.putExtra(MovieTableEntry.COLUMN_MOVIE_DATE, currentDate);
                        startActivity(i);
                        finish();
                    }
                });

            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }
}
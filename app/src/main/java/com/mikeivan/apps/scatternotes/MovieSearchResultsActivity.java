package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mikeivan.apps.scatternotes.data.MovieContract;
import com.mikeivan.apps.scatternotes.data.MovieContract.MovieTableEntry;
import com.mikeivan.apps.scatternotes.data.MovieDbHelper;

import java.util.ArrayList;

/*
 * This activity queries the movie db and displays the results
 */
public class MovieSearchResultsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_search_results);


        setResults();
    }

    /*
     * This function performs the query and sets the results
     */
    private void setResults() {
        // Retrieve the query string from intent
        String target = getIntent().getStringExtra("query");

        // Create database interaction objects
        MovieDbHelper mMovieDbHelper = new MovieDbHelper(this);
        ArrayList<MovieEntry> movies = new ArrayList<MovieEntry>();

        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] project = {
                MovieTableEntry._ID,
                MovieTableEntry.COLUMN_MOVIE_TITLE,
                MovieTableEntry.COLUMN_MOVIE_YEAR,
                MovieTableEntry.COLUMN_MOVIE_RATING,
                MovieTableEntry.COLUMN_MOVIE_NOTES,
                MovieTableEntry.COLUMN_MOVIE_DATE
        };

        // Setup a cursor for the results of the query
        Cursor cursor = db.query(MovieTableEntry.TABLE_NAME,
                project,                               // The columns to return
                target,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_TITLE);
            int yearColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_YEAR);
            int ratingColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_RATING);
            int notesColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(MovieTableEntry.COLUMN_MOVIE_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentYear = cursor.getString(yearColumnIndex);
                int currentRating = cursor.getInt(ratingColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                movies.add(new MovieEntry(currentID, currentTitle, currentDate));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

        // Create a MovieAdapter for our movie array
        MovieAdapter adapter = new MovieAdapter(this, movies);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);

        // Create a clicklistener so when an entry is clicked we are taken to
        // the advanced view of that item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MovieEntry currentMovie = (MovieEntry) parent.getItemAtPosition(position);

                //Toast.makeText(getBaseContext(),"Clicked: " + position + " movie: " + currentMovie.getMovie(),Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MovieSearchResultsActivity.this, MovieViewAdvActivity.class);
                i.putExtra("MoviePosition", currentMovie.getId());
                startActivity(i);
                finish();
            }
        });

    }
}

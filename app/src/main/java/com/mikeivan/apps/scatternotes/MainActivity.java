package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.MemoContract.MemoTableEntry;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;
import com.mikeivan.apps.scatternotes.data.BeerDbHelper;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;
import com.mikeivan.apps.scatternotes.data.WorkoutContract.WorkoutTableEntry;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;
import com.mikeivan.apps.scatternotes.data.MovieContract.MovieTableEntry;
import com.mikeivan.apps.scatternotes.data.MovieDbHelper;
import com.mikeivan.apps.scatternotes.data.ExerciseContract.ExerciseTableEntry;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

import java.io.File;

/*
 * This is the main activity for Scatternotes.
 * You can add, view, search or export from this screen.
 */
public class MainActivity extends AppCompatActivity {

    // db accessors
    private MemoDbHelper mMemoDbHelper;
    private BeerDbHelper mBeerDbHelper;
    private MovieDbHelper mMovieDbHelper;
    private WorkoutDbHelper mWorkoutDbHelper;
    private ExerciseDbHelper mExerciseDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup TextViews
        TextView add_cat = (TextView) findViewById(R.id.add);
        TextView view_cat = (TextView) findViewById(R.id.view);
        TextView search_cat = (TextView) findViewById(R.id.search);
        TextView export_cat = (TextView) findViewById(R.id.export);

        // Set a click listener on add view
        add_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addIntent);
            }
        });

        // Set a click listener on view View
        view_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the phrases View is clicked on.
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(viewIntent);
            }
        });

        // Set a click listener on search View
        search_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family View is clicked on.
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });


        // Set a click listener on export View
        export_cat.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the colors View is clicked on.
            @Override
            public void onClick(View view) {
                Intent exportIntent = new Intent(MainActivity.this, ExportActivity.class);
                startActivity(exportIntent);

            }
        });

        // Initialize db accessors
        mMemoDbHelper = new MemoDbHelper(this);
        mBeerDbHelper = new BeerDbHelper(this);
        mWorkoutDbHelper = new WorkoutDbHelper(this);
        mMovieDbHelper = new MovieDbHelper(this);
        mExerciseDbHelper = new ExerciseDbHelper(this);

    }

    /*
     * This function is used to insert dummy data into the databases.
     */
    private void insertData() {
        Toast.makeText(getBaseContext(), "Inserting dummy data", Toast.LENGTH_SHORT).show();
        insertMemoData();
        insertBeerData();
        insertGymData();
        insertMovieData();
    }

    /*
     * This function deletes the databases and reinitializes them.
     */
    private void deleteData() {
        // Delete memo database
        SQLiteDatabase db = mMemoDbHelper.getWritableDatabase();
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MemoTableEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        mMemoDbHelper.onCreate(db);

        // Delete beer database
        db = mBeerDbHelper.getWritableDatabase();
        SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + BeerTableEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        mBeerDbHelper.onCreate(db);

        // Delete workout database
        db = mWorkoutDbHelper.getWritableDatabase();
        SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + WorkoutTableEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        mWorkoutDbHelper.onCreate(db);

        // Delete exercise database
        db = mExerciseDbHelper.getWritableDatabase();
        SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ExerciseTableEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        mExerciseDbHelper.onCreate(db);

        // Delete movie database
        db = mMovieDbHelper.getWritableDatabase();
        SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MovieTableEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        mMovieDbHelper.onCreate(db);


    }

    /*
     * This function deletes all temporary files in the cache directory.
     */
    private void deleteCache() {
        File cacheDir = getBaseContext().getCacheDir();

        //Toast.makeText(getBaseContext(), "Cache dir: " + cacheDir.getAbsoluteFile(), Toast.LENGTH_SHORT).show();

        File[] files = cacheDir.listFiles();

        if (files != null) {
            for (File file : files)
                file.delete();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteData();
                return true;
            // Respond to a click on the "Delete all Cached Files" menu option
            case R.id.action_delete_cache:
                deleteCache();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * This function inserts dummy data into the memo database.
     */
    private void insertMemoData() {
        SQLiteDatabase db = mMemoDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MemoTableEntry.COLUMN_MEMO_TITLE, "Shopping List");
        values.put(MemoTableEntry.COLUMN_MEMO_NOTES, "Apples, Oranges, Bananas");
        values.put(MemoTableEntry.COLUMN_MEMO_DATE, "06-10-2016");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MemoTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New memo row id: " + newRowId);

        values = new ContentValues();
        values.put(MemoTableEntry.COLUMN_MEMO_TITLE, "TODO");
        values.put(MemoTableEntry.COLUMN_MEMO_NOTES, "Laundry");
        values.put(MemoTableEntry.COLUMN_MEMO_DATE, "06-12-2016");

        newRowId = db.insert(MemoTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New memo row id: " + newRowId);

        values = new ContentValues();
        values.put(MemoTableEntry.COLUMN_MEMO_TITLE, "Remember anniversary");
        values.put(MemoTableEntry.COLUMN_MEMO_NOTES, "");
        values.put(MemoTableEntry.COLUMN_MEMO_DATE, "06-14-2016");

        newRowId = db.insert(MemoTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New memo row id: " + newRowId);

        values = new ContentValues();
        values.put(MemoTableEntry.COLUMN_MEMO_TITLE, "Call Mom");
        values.put(MemoTableEntry.COLUMN_MEMO_NOTES, "It's her birthday");
        values.put(MemoTableEntry.COLUMN_MEMO_DATE, "06-16-2016");

        newRowId = db.insert(MemoTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New memo row id: " + newRowId);

    }

    /*
     * This function inserts dummy data into the beer database.
     */
    private void insertBeerData() {
        SQLiteDatabase db = mBeerDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, "Blue Moon");
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, "Belgian White");
        values.put(BeerTableEntry.COLUMN_BEER_RATING, BeerTableEntry.RATING_FIVE);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, "Best with a slice of orange!");
        values.put(BeerTableEntry.COLUMN_BEER_DATE, "09-10-2016");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New beer row id: " + newRowId);

        values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, "Miller");
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, "High Life");
        values.put(BeerTableEntry.COLUMN_BEER_RATING, BeerTableEntry.RATING_FOUR);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, "Champagne of Beers");
        values.put(BeerTableEntry.COLUMN_BEER_DATE, "09-12-2016");

        newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New beer row id: " + newRowId);

        values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, "Coors");
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, "Coors Lite");
        values.put(BeerTableEntry.COLUMN_BEER_RATING, BeerTableEntry.RATING_THREE);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, "C minus");
        values.put(BeerTableEntry.COLUMN_BEER_DATE, "09-14-2016");

        newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New beer row id: " + newRowId);

        values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, "Diageo");
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, "Guiness");
        values.put(BeerTableEntry.COLUMN_BEER_RATING, BeerTableEntry.RATING_FOUR);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, "Motor oil");
        values.put(BeerTableEntry.COLUMN_BEER_DATE, "09-16-2016");

        newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New beer row id: " + newRowId);

        values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, "AB");
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, "Stella Artois");
        values.put(BeerTableEntry.COLUMN_BEER_RATING, BeerTableEntry.RATING_FOUR);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, "STELLA!");
        values.put(BeerTableEntry.COLUMN_BEER_DATE, "09-18-2016");

        newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New beer row id: " + newRowId);
    }

    /*
     * This function inserts dummy data into the workout database
     * and exercise database.
     */
    private void insertGymData() {
        SQLiteDatabase db = mWorkoutDbHelper.getWritableDatabase();
        SQLiteDatabase db2 = mExerciseDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkoutTableEntry.COLUMN_GYM_DATE, "07-10-2016");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WorkoutTableEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New gym row id: " + newRowId);
        values = new ContentValues();
        values.put(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT, newRowId);
        values.put(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, "Bench - 3x5 - 145lbs");
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_DATE, "07-10-2016");
        // Insert exercise associated to new workout row
        newRowId = db2.insert(ExerciseTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New workout row id: " + newRowId);


        values = new ContentValues();
        values.put(WorkoutTableEntry.COLUMN_GYM_DATE, "07-12-2016");

        newRowId = db.insert(WorkoutTableEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New gym row id: " + newRowId);
        values = new ContentValues();
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_PARENT, newRowId);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, "Squat - 3x5 - 185lbs");
        values.put(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE, "07-12-2016");
        newRowId = db2.insert(ExerciseTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New workout row id: " + newRowId);


        values = new ContentValues();
        values.put(WorkoutTableEntry.COLUMN_GYM_DATE, "07-14-2016");

        newRowId = db.insert(WorkoutTableEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New gym row id: " + newRowId);
        values = new ContentValues();
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_PARENT, newRowId);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, "Deadlift - 1x5 - 225lbs");
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_DATE, "07-14-2016");
        newRowId = db2.insert(ExerciseTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New workout row id: " + newRowId);


        values = new ContentValues();
        values.put(WorkoutTableEntry.COLUMN_GYM_DATE, "07-16-2016");

        newRowId = db.insert(WorkoutTableEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New gym row id: " + newRowId);
        values = new ContentValues();
        values.put(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT, newRowId);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, "Bike ride - 16 miles");
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_DATE, "07-16-2016");
        newRowId = db2.insert(ExerciseContract.ExerciseTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New workout row id: " + newRowId);


        values = new ContentValues();
        values.put(WorkoutTableEntry.COLUMN_GYM_DATE, "07-18-2016");

        newRowId = db.insert(WorkoutTableEntry.TABLE_NAME, null, values);
        Log.v("MainActivity", "New gym row id: " + newRowId);

        values = new ContentValues();
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_PARENT, newRowId);
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE, "Jog - 2 miles");
        values.put(ExerciseTableEntry.COLUMN_WORKOUT_DATE, "07-18-2016");
        newRowId = db2.insert(ExerciseContract.ExerciseTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New workout row id: " + newRowId);
    }

    /*
     * This function inserts dummy data into the movie database.
     */
    private void insertMovieData() {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, "Jaws");
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, 1975);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, MovieTableEntry.RATING_FIVE);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, "Great White!");
        values.put(MovieTableEntry.COLUMN_MOVIE_DATE, "08-10-2016");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(MovieTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New movie row id: " + newRowId);

        values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, "Back to the Future");
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, 1985);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, MovieTableEntry.RATING_FOUR);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, "Great Scott!");
        values.put(MovieTableEntry.COLUMN_MOVIE_DATE, "08-12-2016");

        newRowId = db.insert(MovieTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New movie row id: " + newRowId);

        values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, "The Matrix");
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, 1999);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, MovieTableEntry.RATING_THREE);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, "WHOA!");
        values.put(MovieTableEntry.COLUMN_MOVIE_DATE, "08-14-2016");

        newRowId = db.insert(MovieTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New movie row id: " + newRowId);

        values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, "The Terminator");
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, 1984);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, MovieTableEntry.RATING_FOUR);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, "I'll be back!");
        values.put(MovieTableEntry.COLUMN_MOVIE_DATE, "08-16-2016");

        newRowId = db.insert(MovieTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New movie row id: " + newRowId);

        values = new ContentValues();
        values.put(MovieTableEntry.COLUMN_MOVIE_TITLE, "Toy Story");
        values.put(MovieTableEntry.COLUMN_MOVIE_YEAR, 1995);
        values.put(MovieTableEntry.COLUMN_MOVIE_RATING, MovieTableEntry.RATING_FOUR);
        values.put(MovieTableEntry.COLUMN_MOVIE_NOTES, "BUZZ!");
        values.put(MovieTableEntry.COLUMN_MOVIE_DATE, "08-18-2016");

        newRowId = db.insert(MovieTableEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "New movie row id: " + newRowId);
    }
}

package com.mikeivan.apps.scatternotes;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.BeerContract;
import com.mikeivan.apps.scatternotes.data.BeerDbHelper;
import com.mikeivan.apps.scatternotes.data.ExerciseContract;
import com.mikeivan.apps.scatternotes.data.WorkoutContract;
import com.mikeivan.apps.scatternotes.data.WorkoutDbHelper;
import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoDbHelper;
import com.mikeivan.apps.scatternotes.data.MovieContract;
import com.mikeivan.apps.scatternotes.data.MovieDbHelper;
import com.mikeivan.apps.scatternotes.data.ExerciseDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * This activity is used to export database data.
 *
 * Currently you can email csv file(s) of the databases
 */
public class ExportActivity extends AppCompatActivity {

    // File variable for each db
    File memoDB;
    File beerDB;
    File workoutDB;
    File exerciseDB;
    File movieDB;

    // Names for the db files
    private final String memoDbName = "memo.csv";
    private final String beerDbName = "beer.csv";
    private final String gymDbName = "workout.csv";
    private final String exerciseDbName = "exercise.csv";
    private final String movieDbName = "movie.csv";

    // CheckBoxes to know which files to export
    CheckBox memoCheckBox;
    CheckBox beerCheckBox;
    CheckBox gymCheckBox;
    CheckBox exerciseCheckBox;
    CheckBox movieCheckBox;
    CheckBox allCheckBox;

    // Used to access dbs
    private MemoDbHelper mMemoDbHelper;
    private BeerDbHelper mBeerDbHelper;
    private WorkoutDbHelper mWorkoutDbHelper;
    private ExerciseDbHelper mExerciseDbHelper;
    private MovieDbHelper mMovieDbHelper;

    private FileWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        // Setup FAB to initiate email intent and file creation
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND_MULTIPLE);
                i.putExtra(Intent.EXTRA_SUBJECT, "Export Scatternotes DB(s) as CSV");
                i.setType("message/rfc822");

                ArrayList<Uri> uris = new ArrayList<Uri>();

                //check if checkboxes are clicked, if yes, create files and attach to email
                memoCheckBox = (CheckBox) findViewById(R.id.checkbox_memo);
                beerCheckBox = (CheckBox) findViewById(R.id.checkbox_beer);
                gymCheckBox = (CheckBox) findViewById(R.id.checkbox_workout);
                exerciseCheckBox = (CheckBox) findViewById(R.id.checkbox_exercise);
                movieCheckBox = (CheckBox) findViewById(R.id.checkbox_movie);
                allCheckBox = (CheckBox) findViewById(R.id.checkbox_all);

                if(memoCheckBox.isChecked() || allCheckBox.isChecked()) {
                    createMemos();
                    Uri memoUri = FileProvider.getUriForFile(getBaseContext(),"com.mikeivan.scatternotes.fileprovider", memoDB);
                    uris.add(memoUri);
                }

                if(beerCheckBox.isChecked() || allCheckBox.isChecked()) {
                    createBeers();
                    Uri beerUri = FileProvider.getUriForFile(getBaseContext(),"com.mikeivan.scatternotes.fileprovider", beerDB);
                    uris.add(beerUri);
                }

                if(gymCheckBox.isChecked() || allCheckBox.isChecked()) {
                    createWorkouts();
                    Uri gymUri = FileProvider.getUriForFile(getBaseContext(),"com.mikeivan.scatternotes.fileprovider", workoutDB);
                    uris.add(gymUri);
                }

                if(exerciseCheckBox.isChecked() || allCheckBox.isChecked()) {
                    createExercises();
                    Uri exerciseUri = FileProvider.getUriForFile(getBaseContext(),"com.mikeivan.scatternotes.fileprovider", exerciseDB);
                    uris.add(exerciseUri);
                }

                if(movieCheckBox.isChecked() || allCheckBox.isChecked()) {
                    createMovies();
                    Uri movieUri = FileProvider.getUriForFile(getBaseContext(),"com.mikeivan.scatternotes.fileprovider", movieDB);
                    uris.add(movieUri);
                }

                i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);


                try {
                    startActivity(Intent.createChooser(i , "Email:").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                catch(ActivityNotFoundException e) {
                    Toast.makeText(getBaseContext(),
                            "Sorry No email Application was found",
                            Toast.LENGTH_SHORT).show();
                }

                finish();

            }
        });

        mMemoDbHelper = new MemoDbHelper(this);
        mBeerDbHelper = new BeerDbHelper(this);
        mWorkoutDbHelper = new WorkoutDbHelper(this);  
        mExerciseDbHelper = new ExerciseDbHelper(this);  
        mMovieDbHelper = new MovieDbHelper(this);
    }

    /*
     * This function creates a temporary file of the memo db
     */
    private void createMemos() {

        // Create file from app's cache directory
        memoDB = new File(getCacheDir(), memoDbName);
        
        SQLiteDatabase db = mMemoDbHelper.getReadableDatabase();

        String[] project = {
                MemoContract.MemoTableEntry._ID,
                MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE,
                MemoContract.MemoTableEntry.COLUMN_MEMO_DATE,
                MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES
        };
        
        Cursor cursor = db.query(MemoContract.MemoTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        
        // If previous cache memo db file exists, delete it
        try {
            if(memoDB.exists()) {
                memoDB.delete();
                FileOutputStream fos = openFileOutput(memoDbName, Context.MODE_WORLD_WRITEABLE);
                fos.close();
                //Get reference to the file
                memoDB = new File(getBaseContext().getCacheDir(), memoDbName);
            }

            writer = new FileWriter(memoDB);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_DATE);
            int notesColumnIndex = cursor.getColumnIndex(MemoContract.MemoTableEntry.COLUMN_MEMO_NOTES);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);
                
                // Append the values from each column of the current row to the file
                writer.append("" + currentID + "," + currentTitle + "," + currentNotes + "," + currentDate + "\n");
            }

            writer.close();

        } catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp memo file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    /*
     * This function creates a temporary file of the beer db
     */
    private void createBeers() {

        // Create file from app's cache directory
        beerDB = new File(getCacheDir(), beerDbName);
        
        SQLiteDatabase db = mBeerDbHelper.getReadableDatabase();

        String[] project = {
                BeerContract.BeerTableEntry._ID,
                BeerContract.BeerTableEntry.COLUMN_BEER_COMPANY,
                BeerContract.BeerTableEntry.COLUMN_BEER_PRODUCT,
                BeerContract.BeerTableEntry.COLUMN_BEER_RATING,
                BeerContract.BeerTableEntry.COLUMN_BEER_NOTES,
                BeerContract.BeerTableEntry.COLUMN_BEER_DATE
        };
        
        Cursor cursor = db.query(BeerContract.BeerTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // If previous cache beer db file exists, delete it
        try {
            if(beerDB.exists()) {
                beerDB.delete();
                FileOutputStream fos = openFileOutput(beerDbName, Context.MODE_WORLD_WRITEABLE);
                fos.close();
                //Get reference to the file
                beerDB = new File(getCacheDir(), beerDbName);
            }

            writer = new FileWriter(beerDB);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry._ID);
            int companyColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_COMPANY);
            int productColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_PRODUCT);
            int ratingColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_RATING);
            int dateColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_DATE);
            int notesColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_NOTES);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentCompany = cursor.getString(companyColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                int currentRating = cursor.getInt(ratingColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                String currentNotes = cursor.getString(notesColumnIndex);

                // Append the values from each column of the current row to the file
                writer.append("" + currentID + "," + currentCompany + "," + currentProduct + "," +
                              currentRating + "," + currentNotes + "," + currentDate + "\n");
            }

            writer.close();

        } catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp beer file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    /*
     * This function creates a temporary file of the workout db
     */
    private void createWorkouts() {

        // Create file from app's cache directory
        workoutDB = new File(getCacheDir(), gymDbName);

        SQLiteDatabase db = mWorkoutDbHelper.getReadableDatabase();

        String[] project = {
                WorkoutContract.WorkoutTableEntry._ID,
                WorkoutContract.WorkoutTableEntry.COLUMN_GYM_DATE
        };

        Cursor cursor = db.query(WorkoutContract.WorkoutTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // If previous cache workout db file exists, delete it
        try {
            if(workoutDB.exists()) {
                workoutDB.delete();
                FileOutputStream fos = openFileOutput(gymDbName, Context.MODE_WORLD_WRITEABLE);
                fos.close();
                //Get reference to the file
                workoutDB = new File(getBaseContext().getCacheDir(), gymDbName);
            }

            writer = new FileWriter(workoutDB);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(WorkoutContract.WorkoutTableEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(WorkoutContract.WorkoutTableEntry.COLUMN_GYM_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Append the values from each column of the current row to the file
                writer.append("" + currentID + "," + currentDate + "\n");
            }

            writer.close();

        } catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp workout file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    /*
     * This function creates a temporary file of the exercise db
     */
    private void createExercises() {

        // Create file from app's cache directory
        exerciseDB = new File(getCacheDir(), exerciseDbName);

        SQLiteDatabase db = mExerciseDbHelper.getReadableDatabase();

        String[] project = {
                ExerciseContract.ExerciseTableEntry._ID,
                ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT,
                ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE,
                ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE
        };

        Cursor cursor = db.query(ExerciseContract.ExerciseTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // If previous cache exercise db file exists, delete it
        try {
            if(exerciseDB.exists()) {
                exerciseDB.delete();
                FileOutputStream fos = openFileOutput(exerciseDbName, Context.MODE_WORLD_WRITEABLE);
                fos.close();
                //Get reference to the file
                exerciseDB = new File(getBaseContext().getCacheDir(), exerciseDbName);
            }


            writer = new FileWriter(exerciseDB);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry._ID);
            int parentColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_PARENT);
            int exerciseColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_EXERCISE);
            int dateColumnIndex = cursor.getColumnIndex(ExerciseContract.ExerciseTableEntry.COLUMN_WORKOUT_DATE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                int currentParent = cursor.getInt(parentColumnIndex);
                String currentExercise = cursor.getString(exerciseColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);

                // Append the values from each column of the current row to the file
                writer.append("" + currentID + "," + currentParent + "," + currentExercise + "," + currentDate + "\n");
            }

            writer.close();

        } catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp exercise file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }

    /*
     * This function creates a temporary file of the movie db
     */
    private void createMovies() {

        // Create file from app's cache directory
        movieDB = new File(getCacheDir(), movieDbName);

        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();

        String[] project = {
                MovieContract.MovieTableEntry._ID,
                MovieContract.MovieTableEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieTableEntry.COLUMN_MOVIE_YEAR,
                MovieContract.MovieTableEntry.COLUMN_MOVIE_RATING,
                MovieContract.MovieTableEntry.COLUMN_MOVIE_NOTES,
                MovieContract.MovieTableEntry.COLUMN_MOVIE_DATE
        };

        Cursor cursor = db.query(MovieContract.MovieTableEntry.TABLE_NAME,
                project,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        // If previous cache movie db file exists, delete it
        try {
            if(movieDB.exists()) {
                movieDB.delete();
                FileOutputStream fos = openFileOutput(movieDbName, Context.MODE_WORLD_WRITEABLE);
                fos.close();
                //Get reference to the file
                movieDB = new File(getCacheDir(), movieDbName);
            }

            writer = new FileWriter(movieDB);

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry.COLUMN_MOVIE_TITLE);
            int yearColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry.COLUMN_MOVIE_YEAR);
            int ratingColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry.COLUMN_MOVIE_RATING);
            int notesColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry.COLUMN_MOVIE_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(MovieContract.MovieTableEntry.COLUMN_MOVIE_DATE);

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

                // Appendthe values from each column of the current row to the file
                writer.append("" + currentID + "," + currentTitle + "," + currentYear + "," +
                        currentRating + "," + currentNotes + "," + currentDate + "\n");
            }

            writer.close();

        } catch(IOException e){
            Toast.makeText(getBaseContext(), "Unable create temp movie file. Check logcat for stackTrace", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }

    }
}

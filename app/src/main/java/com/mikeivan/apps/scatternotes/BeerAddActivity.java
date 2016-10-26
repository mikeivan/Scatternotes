package com.mikeivan.apps.scatternotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.BeerContract;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;
import com.mikeivan.apps.scatternotes.data.BeerDbHelper;

/*
 * This activity adds a beer into the database
 */
public class BeerAddActivity extends AppCompatActivity {

    // EditText field to enter the beer's company
    private EditText mCompanyEditText;

    // EditText field to enter the beer's product
    private EditText mProductEditText;

    // EditText field to enter the beer's notes
    private EditText mNotesEditText;

    // Spinner to enter the beer's rating
    private Spinner mRatingSpinner;

    // This keeps track of what rating was selected as it is selected
    // default value is 0
    private int mRating = BeerContract.BeerTableEntry.RATING_ZERO;

    /*
     * This function adds a new beer into the beer database
     * date used is the date of creation.
     */
    private boolean insertBeer() {
        // Retrieve information in EditTexts
        String companyString = mCompanyEditText.getText().toString().trim();
        String productString = mProductEditText.getText().toString().trim();
        String notesString = mNotesEditText.getText().toString().trim();

        if(productString.equals("")) {
            Toast.makeText(getBaseContext(), "Product cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Create database interaction objects
        BeerDbHelper mDbHelper = new BeerDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Deprecated class, but using for API 15 compatibility
        Time now = new Time();
        now.setToNow();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, companyString);
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, productString);
        values.put(BeerTableEntry.COLUMN_BEER_RATING, mRating);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, notesString);
        values.put(BeerTableEntry.COLUMN_BEER_DATE, now.format("%m-%d-%Y"));

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(BeerTableEntry.TABLE_NAME, null, values);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Beer saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with saving beer", Toast.LENGTH_SHORT).show();
            Log.v("BeerAddActivity", "Error with saving beer");
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_add);

        // Setup FAB to save beer into database
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(insertBeer()) {
                    // After the beer has been added, show Beer View
                    Intent i = new Intent(BeerAddActivity.this, BeerViewActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        // Setup EditTexts
        mCompanyEditText = (EditText) findViewById(R.id.edit_beer_company);
        mProductEditText = (EditText) findViewById(R.id.edit_beer_product);
        mNotesEditText = (EditText) findViewById(R.id.edit_beer_notes);
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);

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
                        mRating = BeerTableEntry.RATING_ONE;
                    } else if (selection.equals(getString(R.string.rating_two))) {
                        mRating = BeerTableEntry.RATING_TWO;
                    } else if (selection.equals(getString(R.string.rating_three))) {
                        mRating = BeerTableEntry.RATING_THREE;
                    } else if (selection.equals(getString(R.string.rating_four))) {
                        mRating = BeerTableEntry.RATING_FOUR;
                    } else if (selection.equals(getString(R.string.rating_five))) {
                        mRating = BeerTableEntry.RATING_FIVE;
                    } else {
                        mRating = BeerTableEntry.RATING_ZERO;
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

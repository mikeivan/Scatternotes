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

import static com.mikeivan.apps.scatternotes.R.id.fab;

/*
 * This activity is used to update or delete a beer row.
 */
public class BeerEditActivity extends AppCompatActivity {

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
     * This function updates a beer in the beer database
     * date is unaltered - still date of creation
     */
    private boolean updateBeer() {
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

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(BeerTableEntry.COLUMN_BEER_COMPANY, companyString);
        values.put(BeerTableEntry.COLUMN_BEER_PRODUCT, productString);
        values.put(BeerTableEntry.COLUMN_BEER_RATING, mRating);
        values.put(BeerTableEntry.COLUMN_BEER_NOTES, notesString);

        // Create the where clause, so we update only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = BeerTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(BeerTableEntry._ID, -1) };

        // Update the row, returning the primary key value of the new row
        long newRowId = db.update(BeerTableEntry.TABLE_NAME, values, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Beer saved with id: " + newRowId, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
           //Toast.makeText(getApplicationContext(), "Error with saving beer", Toast.LENGTH_SHORT).show();
            Log.v("BeerEditActivity", "Error with updating beer");
            return false;
        }

    }

    /*
     * This function deletes a beer row from the beer database
     */
    private void deleteBeer() {
        // Create database interaction objects
        BeerDbHelper mDbHelper = new BeerDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create the where clause, so we delete only the specified row
        // This row id is sent from the previous activity via an intent
        String whereClause = BeerTableEntry._ID + "=?";
        String[] selectionArgs = { "" + getIntent().getIntExtra(BeerTableEntry._ID, -1) };

        // delete the row, returning the number of rows deleted (should be 1 or 0)
        long newRowId = db.delete(BeerTableEntry.TABLE_NAME, whereClause, selectionArgs);

        // This is for debugging, can be commented out and/or changed to logging
        if(newRowId > 0) {
            //Toast.makeText(getApplicationContext(), "Beer deleted: " + newRowId, Toast.LENGTH_SHORT).show();
        }
        else {
            //Toast.makeText(getApplicationContext(), "Error with deleting beer", Toast.LENGTH_SHORT).show();
            Log.v("BeerEditActivity", "Error with deleting beer");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_edit);


        // Setup FAB to update beer row
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateBeer()) {
                    Intent i = new Intent(BeerEditActivity.this, BeerViewActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        // Setup FAB to delete beer row
        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBeer();
                Intent i = new Intent(BeerEditActivity.this, BeerViewActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mCompanyEditText = (EditText) findViewById(R.id.edit_beer_company);
        mProductEditText = (EditText) findViewById(R.id.edit_beer_product);
        mNotesEditText = (EditText) findViewById(R.id.edit_beer_notes);
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);

        // Populate EditTexts with actual values from the db, this should make editing easier
        mCompanyEditText.setText(getIntent().getStringExtra(BeerTableEntry.COLUMN_BEER_COMPANY));
        mProductEditText.setText(getIntent().getStringExtra(BeerTableEntry.COLUMN_BEER_PRODUCT));
        mNotesEditText.setText(getIntent().getStringExtra(BeerTableEntry.COLUMN_BEER_NOTES));

        setupSpinner();

        // Set spinner to rating from db
        mRatingSpinner.setSelection(getIntent().getIntExtra(BeerTableEntry.COLUMN_BEER_RATING, 0));
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


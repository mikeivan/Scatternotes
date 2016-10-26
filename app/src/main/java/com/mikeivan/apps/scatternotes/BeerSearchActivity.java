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

import com.mikeivan.apps.scatternotes.data.BeerContract;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;

/*
 * This activity is used to search within the beer database.
 * There are search fields for all enter-able fields from the BeerAddActivity.
 * There is also a search field to search all other fields.
 *
 * The search query string is sent via intent to the BeerSearchResultsActivity,
 * which will do the actual query.
 */
public class BeerSearchActivity extends AppCompatActivity {

    // Setup EditTexts for searching
    private Spinner mRatingSpinner;
    private EditText mCompanyEditText;
    private EditText mProductEditText;
    private EditText mNotesEditText;
    private EditText mSearchAllEditText;

    // This keeps track of what rating was selected as it is selected
    // default value is 0
    private int mRating = BeerContract.BeerTableEntry.RATING_ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_search);

        // Setup FAB for searching beer company
        FloatingActionButton fab_search_company = (FloatingActionButton) findViewById(R.id.fab_search_company);
        fab_search_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeerSearchActivity.this, BeerSearchResultsActivity.class);
                i.putExtra("query", BeerTableEntry.COLUMN_BEER_COMPANY + " LIKE \"%" + mCompanyEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching beer product
        FloatingActionButton fab_search_product = (FloatingActionButton) findViewById(R.id.fab_search_product);
        fab_search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeerSearchActivity.this, BeerSearchResultsActivity.class);
                i.putExtra("query", BeerTableEntry.COLUMN_BEER_PRODUCT + " LIKE \"%" + mProductEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching beer rating, currently searches for all entries with exact same rating
        FloatingActionButton fab_search_rating = (FloatingActionButton) findViewById(R.id.fab_search_rating);
        fab_search_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeerSearchActivity.this, BeerSearchResultsActivity.class);
                i.putExtra("query", BeerTableEntry.COLUMN_BEER_RATING + " = " + mRating);
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching beer notes
        FloatingActionButton fab_search_notes = (FloatingActionButton) findViewById(R.id.fab_search_notes);
        fab_search_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeerSearchActivity.this, BeerSearchResultsActivity.class);
                i.putExtra("query", BeerTableEntry.COLUMN_BEER_NOTES + " LIKE \"%" + mNotesEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching all above fields
        FloatingActionButton fab_search_all = (FloatingActionButton) findViewById(R.id.fab_search_all);
        fab_search_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BeerSearchActivity.this, BeerSearchResultsActivity.class);
                i.putExtra("query", BeerTableEntry.COLUMN_BEER_COMPANY + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + BeerTableEntry.COLUMN_BEER_PRODUCT + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + BeerTableEntry.COLUMN_BEER_RATING + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                        + BeerTableEntry.COLUMN_BEER_NOTES + " LIKE \"%" +  mSearchAllEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mCompanyEditText = (EditText) findViewById(R.id.search_beer_company);
        mProductEditText = (EditText) findViewById(R.id.search_beer_product);
        mNotesEditText = (EditText) findViewById(R.id.search_beer_notes);
        mRatingSpinner = (Spinner) findViewById(R.id.spinner_rating);
        mSearchAllEditText = (EditText) findViewById(R.id.search_beer_all);

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
                        mRating = BeerContract.BeerTableEntry.RATING_ZERO;
                    } else if (selection.equals(getString(R.string.rating_two))) {
                        mRating = BeerContract.BeerTableEntry.RATING_ONE;
                    } else if (selection.equals(getString(R.string.rating_three))) {
                        mRating = BeerContract.BeerTableEntry.RATING_THREE;
                    } else if (selection.equals(getString(R.string.rating_four))) {
                        mRating = BeerContract.BeerTableEntry.RATING_FOUR;
                    } else if (selection.equals(getString(R.string.rating_five))) {
                        mRating = BeerContract.BeerTableEntry.RATING_FIVE;
                    } else {
                        mRating = BeerContract.BeerTableEntry.RATING_ZERO;
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

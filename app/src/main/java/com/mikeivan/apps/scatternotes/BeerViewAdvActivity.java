package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.GregorianCalendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikeivan.apps.scatternotes.data.BeerContract;
import com.mikeivan.apps.scatternotes.data.BeerContract.BeerTableEntry;
import com.mikeivan.apps.scatternotes.data.BeerDbHelper;

/*
 * This activity shows an advanced view of an individual beer
 */
public class BeerViewAdvActivity extends AppCompatActivity {

    // Gives us access to beer db
    private BeerDbHelper mBeerDbHelper;

    // Variables for all fields of beer row
    int currentID;
    String currentCompany;
    String currentProduct;
    int currentRating;
    String currentNotes;
    String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beer_list_item_advanced);

        int target = getIntent().getIntExtra("BeerPosition", -1);

        mBeerDbHelper = new BeerDbHelper(this);
        SQLiteDatabase db = mBeerDbHelper.getReadableDatabase();

        String[] project = {
                BeerTableEntry._ID,
                BeerTableEntry.COLUMN_BEER_COMPANY,
                BeerTableEntry.COLUMN_BEER_PRODUCT,
                BeerTableEntry.COLUMN_BEER_RATING,
                BeerTableEntry.COLUMN_BEER_NOTES,
                BeerTableEntry.COLUMN_BEER_DATE
        };

        String whereClause = BeerTableEntry._ID + "=?";
        String[] selectionArgs = { "" + target };

        Cursor cursor = db.query(BeerContract.BeerTableEntry.TABLE_NAME,
                project,                               // The columns to return
                whereClause,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );


        try {
            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry._ID);
            int companyColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_COMPANY);
            int productColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_PRODUCT);
            int ratingColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_RATING);
            int notesColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_NOTES);
            int dateColumnIndex = cursor.getColumnIndex(BeerContract.BeerTableEntry.COLUMN_BEER_DATE);

            //Toast.makeText(getBaseContext(), "Query count: "+cursor.getCount(), Toast.LENGTH_SHORT).show();

            // Only set TextViews if something is found
            if (cursor.moveToNext() && cursor.getCount() > 0) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                currentCompany = cursor.getString(companyColumnIndex);
                currentProduct = cursor.getString(productColumnIndex);
                currentRating = cursor.getInt(ratingColumnIndex);
                currentNotes = cursor.getString(notesColumnIndex);
                currentDate = cursor.getString(dateColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView

                TextView companyTextView = (TextView) findViewById(R.id.company_text_view);
                companyTextView.setText(currentCompany);

                TextView productTextView = (TextView) findViewById(R.id.product_text_view);
                productTextView.setText(currentProduct);

                TextView ratingTextView = (TextView) findViewById(R.id.rating_text_view);
                ratingTextView.setText(""+currentRating);

                TextView notesTextView = (TextView) findViewById(R.id.notes_text_view);
                notesTextView.setText(currentNotes);

                TextView dateTextView = (TextView) findViewById(R.id.date_text_view);
                dateTextView.setText(currentDate);

                //Switch rating to display images instead of numbers (eventually)

                // Only setup FAB if we found something - FAB to edit beer
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(BeerViewAdvActivity.this, BeerEditActivity.class);
                        i.putExtra(BeerTableEntry._ID, currentID);
                        i.putExtra(BeerTableEntry.COLUMN_BEER_COMPANY, currentCompany);
                        i.putExtra(BeerTableEntry.COLUMN_BEER_PRODUCT, currentProduct);
                        i.putExtra(BeerTableEntry.COLUMN_BEER_RATING, currentRating);
                        i.putExtra(BeerTableEntry.COLUMN_BEER_NOTES, currentNotes);
                        i.putExtra(BeerTableEntry.COLUMN_BEER_DATE, currentDate);
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

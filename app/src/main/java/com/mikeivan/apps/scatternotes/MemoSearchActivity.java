package com.mikeivan.apps.scatternotes;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.mikeivan.apps.scatternotes.data.MemoContract;
import com.mikeivan.apps.scatternotes.data.MemoContract.MemoTableEntry;

/*
 * This activity is used to search within the memo database.
 * There are search fields for all enter-able fields from the MemoAddActivity.
 * There is also a search field to search all other fields.
 *
 * The search query string is sent via intent to the MemoSearchResultsActivity,
 * which will do the actual query.
 */
public class MemoSearchActivity extends AppCompatActivity {

    // Setup EditTexts for searching
    private EditText mTitleEditText;
    private EditText mNotesEditText;
    private EditText mSearchAllEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_search);

        // Setup FAB for searching memo title
        FloatingActionButton fab_search_title = (FloatingActionButton) findViewById(R.id.fab_search_title);
        fab_search_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoSearchActivity.this, MemoSearchResultsActivity.class);
                i.putExtra("query", MemoTableEntry.COLUMN_MEMO_TITLE + " LIKE \"%" + mTitleEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching memo notes
        FloatingActionButton fab_search_notes = (FloatingActionButton) findViewById(R.id.fab_search_notes);
        fab_search_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoSearchActivity.this, MemoSearchResultsActivity.class);
                i.putExtra("query", MemoTableEntry.COLUMN_MEMO_NOTES + " LIKE \"%" + mNotesEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup FAB for searching all above fields
        FloatingActionButton fab_search_all = (FloatingActionButton) findViewById(R.id.fab_search_all);
        fab_search_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemoSearchActivity.this, MemoSearchResultsActivity.class);
                i.putExtra("query", MemoTableEntry.COLUMN_MEMO_TITLE + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\" OR "
                + MemoTableEntry.COLUMN_MEMO_NOTES + " LIKE \"%" + mSearchAllEditText.getText().toString().trim() + "%\"");
                startActivity(i);
                finish();
            }
        });

        // Setup EditTexts
        mTitleEditText = (EditText) findViewById(R.id.search_memo_title);
        mNotesEditText = (EditText) findViewById(R.id.search_memo_notes);
        mSearchAllEditText = (EditText) findViewById(R.id.search_memo_all);

    }
}

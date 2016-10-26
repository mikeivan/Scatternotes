package com.mikeivan.apps.scatternotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mivan on 9/25/2016.
 *
 * This class extends ArrayAdapter to be used with MemoEntry.
 * This enables use of ScrollView to display database data
 */

public class MemoAdapter extends ArrayAdapter<MemoEntry> {

    // Constructor, which calls super, passing in 0 for default resource file,
    // which is defined later in getView
    public MemoAdapter(Activity context, ArrayList<MemoEntry> memos) {
        super(context, 0, memos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.memo_list_item, parent, false);
        }

        // Get the MemoEntry object located at this position in the list
        MemoEntry currentMemo = getItem(position);

        // Find the TextView in the memo_list_item.xml layout with the memo title
        // and set it to the memo title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentMemo.getTitle());

        // Find the TextView in the memo_list_item.xml layout with the memo date
        // and set it to the memo date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentMemo.getDate());

        return listItemView;
    }

}

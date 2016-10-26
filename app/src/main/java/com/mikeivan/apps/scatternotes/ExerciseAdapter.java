package com.mikeivan.apps.scatternotes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mivan on 9/21/2016.
 *
 * This class extends ArrayAdapter to be used with ExerciseEntry.
 * This enables use of ScrollView to display database data
 */

public class ExerciseAdapter extends ArrayAdapter<ExerciseEntry> {

    // Constructor, which calls super, passing in 0 for default resource file,
    // which is defined later in getView
    public ExerciseAdapter(Activity context, ArrayList<ExerciseEntry> workouts) {
        super(context, 0, workouts);
    }

    // Override getView to display according to exercise_list_item.xml
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.exercise_list_item, parent, false);
        }

        // Get the ExerciseEntry object located at this position in the list
        ExerciseEntry currentWorkout = getItem(position);

        // Find the TextView in the exercise_list_item.xml layout with the exercise
        TextView exerciseTextView = (TextView) listItemView.findViewById(R.id.exercise_text_view);
        exerciseTextView.setText(currentWorkout.getExercise());

        return listItemView;
    }

}
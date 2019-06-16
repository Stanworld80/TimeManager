package com.pasqualiselle.timemanager.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pasqualiselle.timemanager.R;

/**
 * {@link TimeManagerCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class TimeManagerCursorAdapter extends CursorAdapter {


    /**
     * @param context     The context
     * @param c           The cursor from which to get the data
     * @param autoRequery I dont know what is this parameter, it needs study
     */
    public TimeManagerCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
    }


    /**
     * This method binds the activity data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current activity can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView activityNameTxtView = view.findViewById(R.id.activityNameTextView);
        TextView durationTxtView = view.findViewById(R.id.durationTextView);

        //Extract properties from the cursor
        String activityName = cursor.getString(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivitiesDuration.COLUMN_DURATION)) / 1000;

        //populated fields with extracted properties
        activityNameTxtView.setText(activityName);
        durationTxtView.setText(duration + " seconds");
    }
}

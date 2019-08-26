package com.pasqualiselle.timemanager.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

public class ActivitiesCursorAdapter extends CursorAdapter {


    /**
     * @param context     The context
     * @param c           The cursor from which to get the data
     * @param autoRequery I dont know what is this parameter, it needs study
     */
    public ActivitiesCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView theTextView = view.findViewById(android.R.id.text1);
        final String activityName = cursor.getString(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));

        theTextView.setText(activityName);
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        String value  = cursor.getString(cursor.getColumnIndexOrThrow(TimeManagerContract.ActivityEntry.COLUMN_ACTIVITY_NAME));
        return value;
    }
}

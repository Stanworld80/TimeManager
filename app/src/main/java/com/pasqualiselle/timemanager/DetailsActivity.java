package com.pasqualiselle.timemanager;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.pasqualiselle.timemanager.adapters.DetailsCursorAdapter;
import com.pasqualiselle.timemanager.data.TimeManagerContract;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // getting datas
        Intent localIntent = getIntent();
        String activityName = localIntent.getStringExtra("activityName");
        int activityId = localIntent.getIntExtra("activity_id", 0);

        // settings views
        TextView titleView = findViewById(R.id.activityTitle);
        titleView.setText(activityName);
        displayInstancesList(activityId);

        //TODO : here should go a alertdialog on the button to clear data
    }


    public void displayInstancesList(int activityId)
    {
        String[] projection =
                {
                        TimeManagerContract.InstanceEntry._ID,
                        TimeManagerContract.InstanceEntry.COLUMN_START_TIME,
                        TimeManagerContract.InstanceEntry.COLUMN_END_TIME
                };
        String whereSelection = TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID + "=?";
        String[] whereParams = { String.valueOf(activityId)};
        Cursor cursor = getContentResolver().query(
                TimeManagerContract.InstanceEntry.CONTENT_URI,
                projection,
                whereSelection,
                whereParams,
                null);

        //Find ListView to populate with the activity name data
        ListView detailsListItemsView = findViewById(R.id.listViewItems);

        //Setup cursor adapter using cursor from_last step
        DetailsCursorAdapter theDetailsCursorAdapter = new DetailsCursorAdapter(this,cursor,false);

        //Attach cursor adapter to the Listview
        detailsListItemsView.setAdapter(theDetailsCursorAdapter);

    }
}

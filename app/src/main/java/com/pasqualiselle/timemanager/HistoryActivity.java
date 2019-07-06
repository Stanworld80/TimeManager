package com.pasqualiselle.timemanager;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.pasqualiselle.timemanager.data.TimeManagerContract;
import com.pasqualiselle.timemanager.adapters.TimeManagerCursorAdapter;

public class HistoryActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the ACTIVITIES database.
     */

    private void displayDatabaseInfo() {

        Cursor cursor = getContentResolver().query(
                TimeManagerContract.ActivitiesDuration.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Find ListView to populate with the activity name data
        ListView timeManagerListItemsView = findViewById(R.id.listViewItems);

        //Setup cursor adapter using cursor from_last step
        TimeManagerCursorAdapter timeManagerCursorAdapter = new TimeManagerCursorAdapter(this,cursor,false);

        //Attach cursor adapter to the Listview
        timeManagerListItemsView.setAdapter(timeManagerCursorAdapter);



    }

}

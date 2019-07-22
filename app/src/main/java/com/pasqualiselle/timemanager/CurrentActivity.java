package com.pasqualiselle.timemanager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.pasqualiselle.timemanager.data.TimeManagerContract;

public class CurrentActivity extends AppCompatActivity {

    TextView mActivityNameTextView;

    private SharedPreferences mSharedPreferences;

    private String mCurrentActivityName;
    private Long mCurrentActivityId;


    private Chronometer mChronometer;
    private boolean running;
    private long startTime;
    private long mStartDateTime;
    private long mEndDateTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_cours);

        setTitle("Current Activity");
        mSharedPreferences = getSharedPreferences(MainActivity.PREF_TIMEMANAGER_KEY,MODE_PRIVATE);

        mActivityNameTextView = findViewById(R.id.text_activity_name);

        mCurrentActivityName = mSharedPreferences.getString(MainActivity.PREF_KEY_CURRENT_ACTIVITY_NAME,"hey");
        mCurrentActivityId = mSharedPreferences.getLong(MainActivity.PREF_KEY_CURRENT_ACTIVITY_ID,0);

        mActivityNameTextView.setText(mCurrentActivityName);

        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat(" %s");

        if(!running){
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            running = true;
            startTime = SystemClock.elapsedRealtime();
            mStartDateTime = System.currentTimeMillis();
        }

    }


    public Chronometer getDuration(){

        return mChronometer;
    }

    public void terminateChronometer(View view) {

        mChronometer.stop();
        mEndDateTime = mStartDateTime + (SystemClock.elapsedRealtime() - startTime);

        ContentValues values = new ContentValues();
        values.put(TimeManagerContract.InstanceEntry.COLUMN_ACTIVITY_ID, mCurrentActivityId);
        values.put(TimeManagerContract.InstanceEntry.COLUMN_START_TIME, mStartDateTime);
        values.put(TimeManagerContract.InstanceEntry.COLUMN_END_TIME, mEndDateTime);

        //Insert a new instance into TimeManagerProvider, returning the content URI for the new instance

        Uri newUri;
        try {
            newUri = getContentResolver().insert(TimeManagerContract.InstanceEntry.CONTENT_URI, values);
        } catch (IllegalArgumentException e) {

            Log.e("MainActivity", "while trying to insert instance with values : " + e.getMessage());
            newUri = null;
        }
        //Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {

            //If the new content URI is null, then there was an error with insertion
            Toast.makeText(this, getString(R.string.editor_insert_instance_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            //Otherwise, the insertion was successful and we can display a toast
            Toast.makeText(this, getString(R.string.editor_insert_instance_successful),
                    Toast.LENGTH_SHORT).show();
            Log.d("CurrentActivity" , "Instance saved for activity "+ mCurrentActivityName
                    +"("+mCurrentActivityId +")"
                    +" : start at "+mStartDateTime+" - end at : "+mEndDateTime );
        }

        finish();
    }
}
package com.pasqualiselle.timemanager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pasqualiselle.timemanager.data.TimeManagerContract;

import java.text.SimpleDateFormat;

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
    private Thread mRingerThread;
    private Switch mRingSwitcher;
    private MediaPlayer mediaPlayerRinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_cours);

        setTitle("Current Activity");
        mSharedPreferences = getSharedPreferences(MainActivity.PREF_TIMEMANAGER_KEY, MODE_PRIVATE);

        mActivityNameTextView = findViewById(R.id.text_activity_name);

        mCurrentActivityName = mSharedPreferences.getString(MainActivity.PREF_KEY_CURRENT_ACTIVITY_NAME, "hey");
        mCurrentActivityId = mSharedPreferences.getLong(MainActivity.PREF_KEY_CURRENT_ACTIVITY_ID, 0);

        mActivityNameTextView.setText(mCurrentActivityName);

        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat(" %s");

        if (!running) {
            mChronometer.setBase(SystemClock.elapsedRealtime());
            mChronometer.start();
            running = true;
            startTime = SystemClock.elapsedRealtime();
            mStartDateTime = System.currentTimeMillis();
        }
        setRinger();

        TextView editTimer = findViewById(R.id.editTimer);

        editTimer.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        mRingSwitcher.setChecked(false);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mRingSwitcher.setChecked(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );
         mediaPlayerRinger = MediaPlayer.create(getApplication(), R.raw.ring);
        setRingerSwitch();
    }
    public void onDestroy()
    {
        super.onDestroy();
        mRingSwitcher.setChecked(false);
    }

    public void setRingerSwitch() {
        mRingSwitcher = findViewById(R.id.switch1);
        mRingSwitcher.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            setRinger();
                            mRingerThread.start();
                        }
                        else
                            mRingerThread.interrupt();
                    }
                }
        );
    }

    public void terminateChronometer(View view) {

        mRingSwitcher.setChecked(false);
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
            Log.d("CurrentActivity", "Instance saved for activity " + mCurrentActivityName
                    + "(" + mCurrentActivityId + ")"
                    + " : start at " + mStartDateTime + " - end at : " + mEndDateTime);
        }

        finish();
    }

    public void setRinger() {
        mRingerThread = new Thread() {
            @Override
            public void run() {
                try {
                    Switch timerSwitch = findViewById(R.id.switch1);
                    TextView timerTextView = findViewById(R.id.editTimer);
                    while (!isInterrupted()) {
                        int timerValue = Integer.valueOf(timerTextView.getText().toString());
                        if (timerSwitch.isChecked() && timerValue >= 1) {
                            sleep(60000 * timerValue);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    mediaPlayerRinger.start();
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    mediaPlayerRinger.stop();
                }
            }
        };
    }

}
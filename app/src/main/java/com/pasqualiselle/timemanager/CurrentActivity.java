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
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pasqualiselle.timemanager.data.TimeManagerContract;

public class CurrentActivity extends AppCompatActivity {

    TextView mActivityNameTextView;

    private SharedPreferences mSharedPreferences;

    private String mCurrentActivityName;
    private Long mCurrentActivityId;


    private Chronometer mChronometer;
    private boolean running = false;
    private long startTime;
    private long mStartDateTime;
    private long mEndDateTime;
    private Thread mRingerThread;
    private Switch mRingSwitcher;
    private MediaPlayer mediaPlayerRinger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TIMEMANAGER", this.getClass() + " : onCreate called.");
        setContentView(R.layout.activity_en_cours);

        setTitle("Current Activity");
        mSharedPreferences = getSharedPreferences(MainActivity.PREF_TIMEMANAGER_KEY, MODE_PRIVATE);

        mActivityNameTextView = findViewById(R.id.text_activity_name);

        mCurrentActivityName = mSharedPreferences.getString(MainActivity.PREF_KEY_CURRENT_ACTIVITY_NAME, "hey");
        mCurrentActivityId = mSharedPreferences.getLong(MainActivity.PREF_KEY_CURRENT_ACTIVITY_ID, 0);

        mActivityNameTextView.setText(mCurrentActivityName);

        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat(" %s");

        running = mSharedPreferences.getBoolean(MainActivity.PREF_KEY_CURRENT_RUNNING, false);
        if (!running) {
            startTime = SystemClock.elapsedRealtime();
            mStartDateTime = System.currentTimeMillis();
            running = true;
        } else {
            startTime = mSharedPreferences.getLong(MainActivity.PREF_KEY_CURRENT_STARTTIME, 0);
            mStartDateTime = mSharedPreferences.getLong(MainActivity.PREF_KEY_CURRENT_MSTARTDATETIME, 0);
        }
        mChronometer.setBase(startTime);
        mChronometer.start();


        TextView editTimer = findViewById(R.id.editTimer);

        setRingerSwitch();
        boolean ringerState = mSharedPreferences.getBoolean(MainActivity.PREF_KEY_CURRENT_RINGERSTATE, false);
        Log.d("TestRINGER", "ringerState:"+ringerState);
        mRingSwitcher.setChecked(ringerState);

        setRingerThread();

        editTimer.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.d("TestRINGER:", "beforeTextChanged  ");
                        mRingSwitcher.setChecked(false);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Log.d("TestRINGER:", "onTextChanged  ");
                        mRingSwitcher.setChecked(false);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                }
        );

        mediaPlayerRinger = MediaPlayer.create(getApplication(), R.raw.ring);

    }


    public void onDestroy() {

        Log.d("TIMEMANAGER", this.getClass() + " : onDestroy called.");
        mSharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_CURRENT_RUNNING, running).apply();
        if (running) {
            mSharedPreferences.edit().putLong(MainActivity.PREF_KEY_CURRENT_STARTTIME, startTime).apply();
            mSharedPreferences.edit().putLong(MainActivity.PREF_KEY_CURRENT_MSTARTDATETIME, mStartDateTime).apply();
            mSharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_CURRENT_RINGERSTATE, mRingSwitcher.isChecked()).apply();
        }
        mRingSwitcher.setChecked(false);
        super.onDestroy();
    }

    public void setRingerSwitch() {
        mRingSwitcher = findViewById(R.id.switch1);
        mRingSwitcher.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Log.d("TestRINGER: onchange", "onCheckedChanged: to "+isChecked);
                            setRingerThread();
                            mRingerThread.start();
                        } else {
                            Log.d("TestRINGER: onchange", "onCheckedChanged: to " + isChecked);
                            mRingerThread.interrupt();
                        }

                    }
                }
        );

    }

    public void terminateChronometer(View view) {
        running = false;
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


    public void setRingerThread() {
        mRingerThread = new Thread() {
            private void offRinger() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayerRinger.stop();
                        mRingSwitcher.setChecked(false);
                    }
                });
            }

            @Override
            public void run() {

                try {
                    Switch timerSwitch = findViewById(R.id.switch1);
                    TextView timerTextView = findViewById(R.id.editTimer);
                    while (!isInterrupted()) {
                        String timertxt = timerTextView.getText().toString();

                        if (timertxt.matches("\\d+")) { // check if match only digit, and at least one
                            int timerValue = Integer.valueOf(timertxt);
                            if (timerSwitch.isChecked() && timerValue >= 1) {
                                sleep(60000 * timerValue);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaPlayerRinger.start();
                                    }
                                });
                            }
                        } else
                            offRinger();
                    }
                } catch (InterruptedException e) {
                    offRinger();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TIMEMANAGER", this.getClass() + " : onResume called.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("TIMEMANAGER", this.getClass() + " : onRestart called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TIMEMANAGER ", this.getClass() + " : onPause called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TIMEMANAGER", this.getClass() + " : onStop called.");
    }


}
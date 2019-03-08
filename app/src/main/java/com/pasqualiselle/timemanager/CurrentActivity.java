package com.pasqualiselle.timemanager;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class CurrentActivity extends AppCompatActivity {

    TextView mActivityNameTextView;

    private SharedPreferences mSharedPreferences;

    private String mActivityName1;

    private Chronometer mChronometer;
    private long pauseOffset;
    private boolean running;
    private String mDuration;
    private TextView mTextViewDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_cours);

        mSharedPreferences = getSharedPreferences(MainActivity.PREF_KEY_ACTIVITY_NAMES,MODE_PRIVATE);

        mActivityNameTextView = findViewById(R.id.text_activity_name);

        mActivityName1 = mSharedPreferences.getString(MainActivity.PREF_KEY_ACTIVITY_NAME_NUMBER1,"hey");
        mActivityNameTextView.setText(mActivityName1);

        mChronometer = findViewById(R.id.chronometer);
        mChronometer.setFormat("Time %s");
mTextViewDuration = findViewById(R.id.textViewDuration);
        mTextViewDuration.setText("Duration ");
    }

    public void startChronometer(View v){

        if(!running){
            mChronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            mChronometer.start();
            running = true;
        }

    }

    public Chronometer getDuration(){

        return mChronometer;
    }

    public void pauseChronometer(View v){

        if(running){
            mChronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
            running = false;
        }


    }

    public void resetChronometer(View v){

         mChronometer.setBase(SystemClock.elapsedRealtime());
         pauseOffset = 0;
    }
}

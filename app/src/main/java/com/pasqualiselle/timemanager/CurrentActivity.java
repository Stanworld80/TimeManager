package com.pasqualiselle.timemanager;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CurrentActivity extends AppCompatActivity {

    TextView mActivityNameTextView;

    private SharedPreferences mSharedPreferences;

    private String mActivityName1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_cours);

        mSharedPreferences = getSharedPreferences(MainActivity.PREF_KEY_ACTIVITY_NAMES,MODE_PRIVATE);

        mActivityNameTextView = findViewById(R.id.text_activity_name);

        mActivityName1 = mSharedPreferences.getString(MainActivity.PREF_KEY_ACTIVITY_NAME_NUMBER1,"hey");
        mActivityNameTextView.setText(mActivityName1);


    }
}

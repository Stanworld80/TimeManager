package com.pasqualiselle.timemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout ll = findViewById(R.id.scrollHistoricLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 120; i++) {
            TextView aHistoricLineTextView = new TextView(this);
            aHistoricLineTextView.setText( "test"+":"+i );
            ll.addView(aHistoricLineTextView, lp);
        }


    }
}

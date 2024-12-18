package com.example.tomato;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        TextView recordTextView = findViewById(R.id.recordTextView);
        String duration = getIntent().getStringExtra("duration");
        String endTime = getIntent().getStringExtra("endTime");

        if (duration != null && endTime != null) {
            String record = duration + "\n結束時間: " + endTime;
            recordTextView.setText(record);
        } else {
            recordTextView.setText("No records found.");
        }
    }
}
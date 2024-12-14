package com.example.tomato;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        TextView recordTextView = findViewById(R.id.recordTextView);
        SharedPreferences sharedPreferences = getSharedPreferences("FocusRecords", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        StringBuilder records = new StringBuilder("Focus Time Records:\n");
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            records.append(entry.getValue().toString()).append("\n");
        }
        recordTextView.setText(records.toString());
    }

}
package com.example.tomato;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {
    private TextView recordTextView;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordTextView = findViewById(R.id.recordTextView);
        CalendarView calendarView = findViewById(R.id.calendarView);
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                displayRecordsForDate(selectedDate);
            }
        });

        // Display records for the current date initially
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        displayRecordsForDate(currentDate);
    }

    private void displayRecordsForDate(String date) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, null, DatabaseHelper.COLUMN_DATE + "=?", new String[]{date}, null, null, null);
        StringBuilder records = new StringBuilder();

        while (cursor.moveToNext()) {
            int hours = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_HOURS));
            int minutes = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_MINUTES));
            int seconds = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_SECONDS));
            records.append("日期: ").append(date)
                    .append(", 專注時長: ").append(hours).append("h ")
                    .append(minutes).append("m ").append(seconds).append("s\n");
        }

        cursor.close();
        recordTextView.setText(records.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
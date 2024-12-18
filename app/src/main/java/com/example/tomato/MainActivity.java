package com.example.tomato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private Button startPauseButton;
    private Button resetButton;
    private Button recordButton;

    private Timer timer;
    private int totalSeconds;
    private int remainingSeconds;
    private boolean isRunning = false;
    private TimerMode currentMode = TimerMode.POMODORO;

    enum TimerMode {
        POMODORO, SHORT_BREAK, LONG_BREAK
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("FocusRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        timerTextView = findViewById(R.id.timerTextView);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetButton = findViewById(R.id.resetButton);
        recordButton = findViewById(R.id.recordButton);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        updateTimerDisplay();

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startTimer() {
        if (!isRunning) {
            int hours = hourPicker.getValue();
            int minutes = minutePicker.getValue();
            int seconds = secondPicker.getValue();
            totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
            remainingSeconds = totalSeconds;

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (remainingSeconds > 0) {
                                remainingSeconds--;
                                updateTimerDisplay();
                            } else {
                                completeTimer();
                            }
                        }
                    });
                }
            }, 0, 1000);

            isRunning = true;
            startPauseButton.setText("暫停");
        }
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isRunning = false;
        startPauseButton.setText("開始");
    }

    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        remainingSeconds = totalSeconds;
        isRunning = false;
        updateTimerDisplay();
        startPauseButton.setText("開始");
    }

    private void completeTimer() {
        pauseTimer();
        saveCompletionTime();
        switch (currentMode) {
            case POMODORO:
                currentMode = TimerMode.SHORT_BREAK;
                remainingSeconds = 5 * 60; // Short break 5 minutes
                break;
            case SHORT_BREAK:
                currentMode = TimerMode.POMODORO;
                remainingSeconds = 25 * 60; // Back to 25 minutes work
                break;
        }
        updateTimerDisplay();
    }

    private void saveCompletionTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("FocusRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        editor.putString(String.valueOf(System.currentTimeMillis()), currentTime);
        editor.apply();
    }

    private void updateTimerDisplay() {
        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        int seconds = remainingSeconds % 60;

        hourPicker.setValue(hours);
        minutePicker.setValue(minutes);
        secondPicker.setValue(seconds);

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeString);

    }
}
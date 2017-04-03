package com.example.ryan.gamenight;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class TimerActivity extends AppCompatActivity {

    private Button btStart;

    private NumberPicker npHours;
    private NumberPicker npMinutes;
    private NumberPicker npSeconds;

    private int hours;
    private int minutes;
    private int seconds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Set up the number pickers
        npHours = (NumberPicker)findViewById(R.id.timer_np_hours);
        npMinutes = (NumberPicker)findViewById(R.id.timer_np_minutes);
        npSeconds = (NumberPicker)findViewById(R.id.timer_np_seconds);

        npHours.setMinValue(0);
        npMinutes.setMinValue(0);
        npSeconds.setMinValue(0);

        npHours.setMaxValue(99);
        npMinutes.setMaxValue(59);
        npSeconds.setMaxValue(59);

        // Set the Start button listener
        // TODO: Finish implementation
        btStart = (Button)findViewById(R.id.timer_bt_start);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hours = npHours.getValue();
                minutes = npMinutes.getValue();
                seconds = npSeconds.getValue();

                runnable.run();
            }
        });
    }

    Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(seconds >= 0){
                seconds--;
                npSeconds.setValue(seconds);
                mHandler.postDelayed(this, 1000);
            }
        }
    };
}
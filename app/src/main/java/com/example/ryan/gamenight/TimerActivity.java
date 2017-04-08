package com.example.ryan.gamenight;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

public class TimerActivity extends AppCompatActivity {

    private Button btStart;
    private Button btReset;

    private NumberPicker npHours;
    private NumberPicker npMinutes;
    private NumberPicker npSeconds;

    private ImageButton ibHourglass;

    private int hours;
    private int minutes;
    private int seconds;

    ObjectAnimator animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // Get the number pickers
        npHours = (NumberPicker)findViewById(R.id.timer_np_hours);
        npMinutes = (NumberPicker)findViewById(R.id.timer_np_minutes);
        npSeconds = (NumberPicker)findViewById(R.id.timer_np_seconds);

        // Set the number pickers' min and max values
        npHours.setMinValue(0);
        npMinutes.setMinValue(0);
        npSeconds.setMinValue(0);

        npHours.setMaxValue(99);
        npMinutes.setMaxValue(59);
        npSeconds.setMaxValue(59);

        // Get the image button and start button
        // TODO: Finish implementation - alert/notification
        ibHourglass = (ImageButton)findViewById(R.id.timer_ib_hourglass);
        btStart = (Button)findViewById(R.id.timer_bt_start);

        // Set the startTimer listener for image button and start button
        ibHourglass.setOnClickListener(startTimer);
        btStart.setOnClickListener(startTimer);

        // Get the reset button and disable it
        btReset = (Button)findViewById(R.id.timer_bt_reset);
        btReset.setOnClickListener(resetTimer);
    }

    // OnClickListener to start the timer
    View.OnClickListener startTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Get the values on the Number Pickers
            hours = npHours.getValue();
            minutes = npMinutes.getValue();
            seconds = npSeconds.getValue();

            // Disable the Number Pickers
            npHours.setEnabled(false);
            npMinutes.setEnabled(false);
            npSeconds.setEnabled(false);

            // Disable the reset button
            btReset.setEnabled(false);

            // Start the rotation animation
            startRotation();

            // Change the start button to a stop button
            btStart.setText(R.string.stop);
            btStart.setOnClickListener(stopTimer);

            // Change the hourglass to a stop button
            ibHourglass.setOnClickListener(stopTimer);

            // Start a new thread, if no time is left don't delay the call
            if (hours == 0 && minutes == 0 && seconds == 0){
                handler.post(runnable);
            } else {
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    // OnClickListener to start the timer
    View.OnClickListener stopTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            stopRotation();

            // Change the stop button to a start button
            btStart.setText(R.string.start);
            btStart.setOnClickListener(startTimer);

            // Change the hourglass to a start button
            ibHourglass.setOnClickListener(startTimer);

            // Enable the reset button
            btReset.setEnabled(true);

            // Pause the thread
            handler.removeCallbacks(runnable);
        }
    };

    // OnClickListener to reset the timer
    View.OnClickListener resetTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Set the Number Pickers to 0
            npHours.setValue(0);
            npMinutes.setValue(0);
            npSeconds.setValue(0);

            // Enable the Number Pickers
            npHours.setEnabled(true);
            npMinutes.setEnabled(true);
            npSeconds.setEnabled(true);
        }
    };

    // Create the thread to handle the timer countdown
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(seconds > 0){
                seconds--;
                npSeconds.setValue(seconds);
                handler.postDelayed(this, 1000);
            } else if (minutes > 0) {
                minutes--;
                seconds = 59;
                npMinutes.setValue(minutes);
                npSeconds.setValue(seconds);
                handler.postDelayed(this, 1000);
            } else if (hours > 0) {
                hours--;
                minutes = 59;
                seconds = 59;
                npHours.setValue(hours);
                npMinutes.setValue(minutes);
                npSeconds.setValue(seconds);
                handler.postDelayed(this, 1000);
            } else {
                // When the timer is finished, click the start and reset button
                btStart.callOnClick();
                btReset.callOnClick();
            }
        }
    };

    // Start the image button rotation
    public void startRotation(){
        animation = ObjectAnimator.ofFloat(ibHourglass, "rotation", 0f, 180f);
        animation.setDuration(2000);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    // Stop the image button rotation
    public void stopRotation(){
        animation.end();
    }
}
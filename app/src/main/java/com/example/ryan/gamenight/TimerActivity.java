package com.example.ryan.gamenight;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;

public class TimerActivity extends AppCompatActivity {

    private static final String TAG = "TimerActivity";

    static final public String UI_UPDATE = "com.example.ryan.gamenight.UI_UPDATE";

    private Button btControl;
    private Button btReset;

    private NumberPicker npHours;
    private NumberPicker npMinutes;
    private NumberPicker npSeconds;

    private ImageButton ibHourglass;

    ObjectAnimator animation;

    BroadcastReceiver broadcastReceiver;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        context = this;

        // Setup the broadcast listener
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO: update ui with broadcast data
                Bundle uiData = intent.getExtras();
                int s = uiData.getInt("seconds");
                int m = uiData.getInt("minutes");
                int h = uiData.getInt("hours");
                Log.d(TAG, String.valueOf(h) + ":" + String.valueOf(m) + ":" + String.valueOf(s));
                updateUi(s, m, h);
            }
        };

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

        // Get views for every button
        // TODO: Finish implementation - alert/notification
        ibHourglass = (ImageButton)findViewById(R.id.timer_ib_hourglass);
        btControl = (Button)findViewById(R.id.timer_bt_start);
        btReset = (Button)findViewById(R.id.timer_bt_reset);

        // Setup the animation
        animation = ObjectAnimator.ofFloat(ibHourglass, "rotation", 0f, 180f);
        animation.setDuration(2000);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Activate the broadcast manager
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), new IntentFilter(UI_UPDATE));

        // TODO: Check if the service is running
        // If the timer is running, start in the appropriate state
        if (isTimerRunning(TimerService.class)) {

            // Put the ui into the stop state
            stopUiState();
        } else {

            // Put the ui into the start state
            startUiState();

            // Enable the Number Pickers
            npHours.setEnabled(true);
            npMinutes.setEnabled(true);
            npSeconds.setEnabled(true);
        }
    }

    @Override
    protected void onStop() {

        // Free the broadcast manager
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    // OnClickListener to start the timer
    View.OnClickListener startTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Get the values on the Number Pickers
            int hours = npHours.getValue();
            int minutes = npMinutes.getValue();
            int seconds = npSeconds.getValue();

            // Set ui to stop state
            stopUiState();

            // Create a bundle with relevant data
            Bundle timerServiceBundle = new Bundle();
            timerServiceBundle.putInt("hours", hours);
            timerServiceBundle.putInt("minutes", minutes);
            timerServiceBundle.putInt("seconds", seconds);

            // Start the timer service with bundled data
            Intent timerServiceIntent = new Intent(context, TimerService.class);
            timerServiceIntent.putExtras(timerServiceBundle);
            context.startService(timerServiceIntent);
        }
    };

    // OnClickListener to start the timer
    View.OnClickListener stopTimer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Set ui to start state
            startUiState();

            // TODO: tell the service to hault
            Intent stopTimerService = new Intent(context, TimerService.class);
            stopService(stopTimerService);

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

    // Set the ui to the start state (i.e. timer is paused)
    private void startUiState(){

        // Set control button state
        btControl.setText(R.string.start);
        btControl.setOnClickListener(startTimer);

        // Set reset button state
        btReset.setOnClickListener(resetTimer);
        btReset.setEnabled(true);

        // Set hourglass
        ibHourglass.setOnClickListener(startTimer);
        stopRotation();
    }

    // Set the ui to the stop state (i.e. timer is running)
    private void stopUiState(){

        // Set control button state
        btControl.setText(R.string.stop);
        btControl.setOnClickListener(stopTimer);

        // Set reset button state
        btReset.setOnClickListener(resetTimer);
        btReset.setEnabled(false);

        // Set hourglass
        ibHourglass.setOnClickListener(stopTimer);
        startRotation();

        // Disable the Number Pickers
        npHours.setEnabled(false);
        npMinutes.setEnabled(false);
        npSeconds.setEnabled(false);
    }

    // Update the ui
    private void updateUi(int seconds, int minutes, int hours){
        npHours.setValue(hours);
        npMinutes.setValue(minutes);
        npSeconds.setValue(seconds);

        // When the timer hits zero, set ui to start state and reset number pickers
        if (seconds == 0 && minutes == 0 && hours == 0) {
            startUiState();
            btReset.callOnClick();
        }
    }

    // Start the image button rotation
    private void startRotation(){
        animation.start();
    }

    // Stop the image button rotation
    private void stopRotation(){
        animation.end();
    }

    // Check if the timer service is running
    private boolean isTimerRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
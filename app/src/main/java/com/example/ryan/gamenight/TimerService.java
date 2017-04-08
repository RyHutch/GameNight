package com.example.ryan.gamenight;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class TimerService extends Service {

    static final public String UI_UPDATE = "com.example.ryan.gamenight.UI_UPDATE";

    private int hours;
    private int minutes;
    private int seconds;

    LocalBroadcastManager localBroadcastManager;

    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get Bundle data
        Bundle bundle = intent.getExtras();
        hours = bundle.getInt("hours");
        minutes = bundle.getInt("minutes");
        seconds = bundle.getInt("seconds");

        // Start a new thread, if no time is left don't delay the call to end itself
        if (hours == 0 && minutes == 0 && seconds == 0){
            handler.post(runnable);
        } else {
            handler.postDelayed(runnable, 1000);
        }

        // Return service restart behavior
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // Make sure to kill the thread! VERY IMPORTANT!!!
        handler.removeCallbacks(runnable);

        super.onDestroy();
    }

    // Create the thread to handle the timer countdown
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(seconds > 0){
                seconds--;
                sendTimeData(seconds, minutes, hours);
                handler.postDelayed(this, 1000);
            } else if (minutes > 0) {
                minutes--;
                seconds = 59;
                sendTimeData(seconds, minutes, hours);
                handler.postDelayed(this, 1000);
            } else if (hours > 0) {
                hours--;
                minutes = 59;
                seconds = 59;
                sendTimeData(seconds, minutes, hours);
                handler.postDelayed(this, 1000);
            } else {
                // Timer is finished its service
                sendTimeData(seconds, minutes, hours);
                stopSelf();
            }
        }
    };

    // Broadcast the time data to the TimerActivity so it can update the UI
    private void sendTimeData(int seconds, int minutes, int hours){

        // Add the time info to a bundle
        Bundle uiBundle = new Bundle();
        uiBundle.putInt("seconds", seconds);
        uiBundle.putInt("minutes", minutes);
        uiBundle.putInt("hours", hours);

        // Make an intent and bundle data
        Intent uiIntent = new Intent(UI_UPDATE);
        uiIntent.putExtras(uiBundle);
        localBroadcastManager.sendBroadcast(uiIntent);
    }
}
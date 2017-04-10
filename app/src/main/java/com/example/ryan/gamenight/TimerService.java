package com.example.ryan.gamenight;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

public class TimerService extends Service {

    static final public String UI_UPDATE = "com.example.ryan.gamenight.UI_UPDATE";

    static final public int NOTIFICATION_ID = 42;

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
        ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Get Bundle data
        Bundle bundle = intent.getExtras();
        hours = bundle.getInt("hours");
        minutes = bundle.getInt("minutes");
        seconds = bundle.getInt("seconds");

        // Start a new thread, if no time is left don't delay the call to end itself
        if (hours == 0 && minutes == 0 && seconds == 0) {
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
            if (seconds > 0) {
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

                // Create a notification explaining that the timer has finished
                addNotification();

                // Timer can finish its service
                sendTimeData(seconds, minutes, hours);
                stopSelf();
            }
        }
    };

    // Build a notification to explain that the timer has finished
    private void addNotification() {

        // Set the content of the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_timer_black_24dp)
                        .setContentTitle("Timer Expired")
                        .setContentText("The timer set in Game Night has expired.")
                        .setAutoCancel(true);

        // If the notification is clicked, reroute the user to the TimerActivity
        Intent notificationIntent = new Intent(this, TimerActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Check with the notification manager, and add a notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    // Broadcast the time data to the TimerActivity so it can update the UI
    private void sendTimeData(int seconds, int minutes, int hours) {

        // Add the time info to a bundle
        Bundle uiBundle = new Bundle();
        uiBundle.putInt("seconds", seconds);
        uiBundle.putInt("minutes", minutes);
        uiBundle.putInt("hours", hours);

        // Make an intent, bundle data, and broadcast
        Intent uiIntent = new Intent(UI_UPDATE);
        uiIntent.putExtras(uiBundle);
        localBroadcastManager.sendBroadcast(uiIntent);
    }
}
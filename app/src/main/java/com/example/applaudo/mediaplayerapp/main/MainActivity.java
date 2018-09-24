package com.example.applaudo.mediaplayerapp.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.applaudo.mediaplayerapp.R;
import com.example.applaudo.mediaplayerapp.constants.Constants;
import com.example.applaudo.mediaplayerapp.receivers.Actions;
import com.example.applaudo.mediaplayerapp.receivers.PlayerReceiver;
import com.example.applaudo.mediaplayerapp.services.PlayerService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PlayerReceiver.OnPlayPausePressed {

    private ImageButton mPlayStop, mInfo,mMute;
    private Boolean mIsPlaying = false;
    private Boolean mIsMuted = false;

    //Notifications
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotificationPlayerManager;

    //Receiver
    private PlayerReceiver mPlayerReceiver = new PlayerReceiver(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the filters and actions
        IntentFilter playerFilter = new IntentFilter();
        playerFilter.addAction(Actions.ACTION_CUSTOM_PAUSE);
        playerFilter.addAction(Actions.ACTION_CUSTOM_PLAY);
        playerFilter.addAction(Actions.ACTION_CUSTOM_MUTE);
        playerFilter.addAction(Actions.ACTION_CUSTOM_UNMUTE);

        //Registering the local receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayerReceiver, playerFilter);

        mPlayStop = findViewById(R.id.btnPlayStop);
        mInfo = findViewById(R.id.btnInfo);
        mMute = findViewById(R.id.btnMute);

        mPlayStop.setOnClickListener(this);
        mInfo.setOnClickListener(this);
        mMute.setOnClickListener(this);

        //Create the notification channel
        createNotificationChannel();

    }

    @Override
    public void onClick(View v) {

        Intent intentMediaAction = new Intent(getApplicationContext(), PlayerService.class);

        switch (v.getId()) {
            case R.id.btnPlayStop:
                if (!mIsPlaying) {
                    //Sends the action to perform to the service (PLAY)
                    intentMediaAction.putExtra(Constants.PLAY_PAUSE, "PLAY");
                    startService(intentMediaAction);

                } else {
                    //Sends the action to perform to the service (PAUSE)
                    intentMediaAction.putExtra(Constants.PLAY_PAUSE, "PAUSE");
                    startService(intentMediaAction);
                }
                break;

            case R.id.btnInfo:
                //Starts the info activity
                Intent intent = new Intent(getApplicationContext(), ActivityRadioDetails.class);
                startActivity(intent);
                break;

            case R.id.btnMute:
                if (!mIsMuted) {
                    //Sends the action to perform to the service (MUTE)
                    intentMediaAction.putExtra(Constants.PLAY_PAUSE, "MUTE");
                    startService(intentMediaAction);
                    break;
                } else {
                    //Sends the action to perform to the service (UNMUTE)
                    intentMediaAction.putExtra(Constants.PLAY_PAUSE, "UNMUTE");
                    startService(intentMediaAction);
                    break;
                }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //This is where it sends the notification
        NotificationCompat.Builder playerBuilder = getNotificationBuilder();
        mNotificationPlayerManager.notify(NOTIFICATION_ID, playerBuilder.build());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Unregister receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayerReceiver);

        //Stops the service when the activity is destroyed
        stopService(new Intent(getApplicationContext(), PlayerService.class));

        //Closes the notification when the activity is destroyed
        mNotificationPlayerManager.cancel(NOTIFICATION_ID);


    }

    //region Notifications

    private NotificationCompat.Builder getNotificationBuilder() {
        //This is the intent the notification will use to launch the activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent to run predefined code
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, 0);


        //Intent for the play button
        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.putExtra(Constants.PLAY_PAUSE, "PLAY");
        playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the play button
        //This is were it is sent to the Service
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        //Intent for the pause button
        Intent pauseIntent = new Intent(this, PlayerService.class);
        pauseIntent.putExtra(Constants.PLAY_PAUSE, "PAUSE");
        pauseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the pause button
        PendingIntent pausePendingIntent = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for the mute button
        Intent muteIntent = new Intent(this, PlayerService.class);
        muteIntent.putExtra(Constants.PLAY_PAUSE, "MUTE");
        muteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the mute button
        PendingIntent mutePendingIntent = PendingIntent.getService(this, 2, muteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent for the unmute button
        Intent unmuteIntent = new Intent(this, PlayerService.class);
        unmuteIntent.putExtra(Constants.PLAY_PAUSE, "UNMUTE");
        unmuteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the mute button
        PendingIntent unmutePendingIntent = PendingIntent.getService(this, 3, unmuteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create a new notification
        NotificationCompat.Builder playerBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Live Radio") //Set the title
                .setContentText("You're listening to: http://us5.internet-radio.com:8110/listen.pls&t=.m3u") //Set the description
                .setSmallIcon(R.drawable.ic_note) //Set the icon
                .setContentIntent(notificationPendingIntent) //Sets the content intent (pending intent) for this notification
                .setAutoCancel(false) //Sets the notification not to close when the user taps on it
                //This is for backwards compatibility (Android 7.1 or lower)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL); //Set the sound, vibration, LED-color pattern for the notification

        //Adding the action buttons
        playerBuilder.addAction(R.mipmap.ic_play, "Play", playPendingIntent);
        playerBuilder.addAction(R.mipmap.ic_pause, "Pause", pausePendingIntent);
        playerBuilder.addAction(0,"Mute",mutePendingIntent);
        playerBuilder.addAction(0,"Un-mute",unmutePendingIntent);

        return playerBuilder;
    }


    public void createNotificationChannel() {
        mNotificationPlayerManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Checks if the android version is above 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Creating the notification channel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Player notification", NotificationManager.IMPORTANCE_HIGH);

            //Setting its configuration
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from player");

            //Creates the notification channel and passes it onto the manager
            mNotificationPlayerManager.createNotificationChannel(notificationChannel);

        }


    }

    //endregion


    //region Implementation of the interface
    @Override
    public void onButtonPressed(String action) {
        if (action.equals(Actions.ACTION_CUSTOM_PLAY)) {
            //Sets the icon to play
            mPlayStop.setImageResource(R.drawable.radio_pause);
            mIsPlaying = true;
        } else if (action.equals(Actions.ACTION_CUSTOM_PAUSE)) {
            //Sets the icon to pause
            mPlayStop.setImageResource(R.drawable.radio_play);
            mIsPlaying = false;
        } else if (action.equals(Actions.ACTION_CUSTOM_MUTE)) {
            mMute.setImageResource(R.drawable.ic_unmute);
            mIsMuted = true;
        } else {
            mMute.setImageResource(R.drawable.ic_mute);
            mIsMuted=false;
        }
    }

    //endregion

}

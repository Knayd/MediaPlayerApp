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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mPlayStop, mInfo;
    private Boolean mIsPlaying =false;

    //Notifications
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotificationPlayerManager;

    //Receiver
    private PlayerReceiver mPlayerReceiver = new PlayerReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the filters and actions
        IntentFilter playerFilter = new IntentFilter();
        playerFilter.addAction(Actions.ACTION_CUSTOM_PAUSE);
        playerFilter.addAction(Actions.ACTION_CUSTOM_PLAY);

        //Registering the local receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayerReceiver, playerFilter);

        mPlayStop = findViewById(R.id.btnPlayStop);
        mInfo = findViewById(R.id.btnInfo);

        mPlayStop.setOnClickListener(this);
        mInfo.setOnClickListener(this);

        //Create the notification channel
        createNotificationChannel();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayStop:

                Intent intentPlayPause = new Intent(getApplicationContext(), PlayerService.class);

                if (!mIsPlaying) {
                    //Sets the icon to play and sends the action to perform to the service
                    mPlayStop.setImageResource(R.mipmap.ic_pause);
                    Toast.makeText(this, "Play!", Toast.LENGTH_SHORT).show();
                    mIsPlaying=true;

                    intentPlayPause.putExtra(Constants.PLAY_PAUSE,"PLAY");
                    startService(intentPlayPause);

                    /*//Broadcast==========
                    //Creates a new intent with the custom broadcast
                    Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PLAY);
                    //Sends the broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);*/


                } else {
                    //Sets the icon to pause and sends the action to perform to the service
                    mPlayStop.setImageResource(R.mipmap.ic_play);
                    mIsPlaying=false;

                    intentPlayPause.putExtra(Constants.PLAY_PAUSE,"PAUSE");
                    startService(intentPlayPause);

                    /*//Creates a new intent with the custom broadcast
                    Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PAUSE);
                    //Sends the broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);*/
                }
                break;


            case R.id.btnInfo:
                //Starts the info activity
                Intent intent = new Intent(getApplicationContext(),ActivityRadioDetails.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //This is where it sends the notification
        NotificationCompat.Builder playerBuilder = getNotificationBuilder();
        mNotificationPlayerManager.notify(NOTIFICATION_ID,playerBuilder.build());

        Toast.makeText(this, "Stopped!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Unregister receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayerReceiver);

        //Stops the service when the activity is destroyed
        stopService(new Intent(getApplicationContext(), PlayerService.class));

    }

    //region Notifications

    private NotificationCompat.Builder getNotificationBuilder(){
        //This is the intent the notification will use to launch the activity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent to run predefined code
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent, 0);


        //Intent for the play button
        Intent playIntent = new Intent(this,PlayerService.class);
        playIntent.putExtra(Constants.PLAY_PAUSE,"PLAY");
        playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the play button
        //This is were it is sent to the Service
        PendingIntent playPendingIntent = PendingIntent.getService(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        //Intent for the pause button
        Intent pauseIntent = new Intent(this, PlayerService.class);
        pauseIntent.putExtra(Constants.PLAY_PAUSE,"PAUSE");
        pauseIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Pending intent for the pause button
        PendingIntent pausePendingIntent = PendingIntent.getService(this,1,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Create a new notification
        NotificationCompat.Builder playerBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("Radio") //Set the title
                .setContentText("You're listening to: http://us5.internet-radio.com:8110/listen.pls&t=.m3u") //Set the description
                .setSmallIcon(R.drawable.note) //Set the icon
                .setContentIntent(notificationPendingIntent) //Sets the content intent (pending intent) for this notification
                .setAutoCancel(false) //Sets the notifcation not to close when the user taps on it
                //This is for backwards compatibility (Anroid 7.1 or lower)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL); //Set the sound, vibration, LED-color pattern for the notification

        //Adding the action buttons
        playerBuilder.addAction(0,"Play",playPendingIntent);
        playerBuilder.addAction(0,"Pause",pausePendingIntent);

        return playerBuilder;
    }


    public void createNotificationChannel() {
        mNotificationPlayerManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Checks if the android version is above 26
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
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


}

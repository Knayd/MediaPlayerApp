package com.example.applaudo.mediaplayerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.applaudo.mediaplayerapp.receivers.Actions;
import com.example.applaudo.mediaplayerapp.services.PlayerService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mPlayStop, mInfo;
    private Boolean mIsPlaying =false;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayStop:
                if (!mIsPlaying) {
                    //Plays the music and sets the icon
                    mPlayStop.setImageResource(R.mipmap.ic_pause);
                    Toast.makeText(this, "Play!", Toast.LENGTH_SHORT).show();
                    mIsPlaying=true;


                    //Creates a new intent with the custom broadcast
                    Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PLAY);
                    //Sends the broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);


                } else {
                    //Pauses the music and sets the icon
                    mPlayStop.setImageResource(R.mipmap.ic_play);
                    mIsPlaying=false;

                    //Creates a new intent with the custom broadcast
                    Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PAUSE);
                    //Sends the broadcast
                    LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);

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

        Toast.makeText(this, "Stopped!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Unregister receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayerReceiver);
    }


    //The receiver
    public class PlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String intentAction = intent.getAction();

            //This is where the logic of playing/pause goes
            if (intentAction != null) {
                String toastMessage = "Unkown intent action";

                switch (intentAction){
                    case Actions.ACTION_CUSTOM_PLAY:
                        toastMessage = "Receiver - Play";
                        //Service start
                        startService(new Intent(getApplicationContext(), PlayerService.class));
                        break;
                    case Actions.ACTION_CUSTOM_PAUSE:
                        toastMessage = "Receiver - Pause";
                        stopService(new Intent(getApplicationContext(), PlayerService.class));
                        break;
                }

                Toast.makeText(context,toastMessage, Toast.LENGTH_SHORT).show();
            }



        }
    }


}

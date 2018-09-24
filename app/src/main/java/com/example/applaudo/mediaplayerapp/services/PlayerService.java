package com.example.applaudo.mediaplayerapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.applaudo.mediaplayerapp.constants.Constants;
import com.example.applaudo.mediaplayerapp.receivers.Actions;

import java.io.IOException;

public class PlayerService extends Service {
    //This is what makes the player play/stop
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Sets the media player
        String url = "http://us5.internet-radio.com:8110/listen.pls&t=.m3u";
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Gets the action sent by the button
        Bundle actionToPerform = intent.getExtras();
        String theAction;
        if (actionToPerform != null) {
            theAction = actionToPerform.getString(Constants.PLAY_PAUSE);

            if (theAction.equals("PLAY")) {
                mediaPlayer.start();

                //Creates a new intent with the custom broadcast
                Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PLAY);
                //Sends the broadcast
                LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);

            } else if (theAction.equals("PAUSE")) {
                mediaPlayer.pause();

                //Creates a new intent with the custom broadcast
                Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_PAUSE);
                //Sends the broadcast
                LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);
            } else if (theAction.equals("MUTE")) {
                mediaPlayer.setVolume(0, 0);

                //Creates a new intent with the custom broadcast
                Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_MUTE);
                //Sends the broadcast
                LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);
            } else {
                mediaPlayer.setVolume(1,1);

                //Creates a new intent with the custom broadcast
                Intent customPlayerBroadcastReceiver = new Intent(Actions.ACTION_CUSTOM_UNMUTE);
                //Sends the broadcast
                LocalBroadcastManager.getInstance(this).sendBroadcast(customPlayerBroadcastReceiver);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mediaPlayer.stop();
    }
}

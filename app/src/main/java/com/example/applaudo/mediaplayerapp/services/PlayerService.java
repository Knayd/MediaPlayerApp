package com.example.applaudo.mediaplayerapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.applaudo.mediaplayerapp.constants.Constants;

import java.io.IOException;
import java.nio.BufferUnderflowException;

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
                Toast.makeText(this, "onStartCommand Play", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "onStartCommand Pause", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
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

package com.example.applaudo.mediaplayerapp.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Sets the media player and makes it play
        String url = "http://us5.internet-radio.com:8110/listen.pls&t=.m3u";
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mediaPlayer.pause();
    }
}

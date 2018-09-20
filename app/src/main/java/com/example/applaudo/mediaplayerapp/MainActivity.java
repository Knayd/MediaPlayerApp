package com.example.applaudo.mediaplayerapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton mPlayStop, mInfo;
    private Boolean mIsPlaying =false;
    //Creating the media player
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPlayStop = findViewById(R.id.btnPlayStop);
        mInfo = findViewById(R.id.btnInfo);


        String url = "http://us5.internet-radio.com:8110/listen.pls&t=.m3u";
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    mediaPlayer.start();
                } else {
                    //Pauses the music and sets the icon
                    mediaPlayer.pause();
                    mPlayStop.setImageResource(R.mipmap.ic_play);
                    mIsPlaying=false;
                    Toast.makeText(this, "Pause!", Toast.LENGTH_SHORT).show();
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
        //Stops the music when changing the activity
//        mediaPlayer.pause();
//        mPlayStop.setImageResource(R.mipmap.ic_play);
//        mIsPlaying=false;
        Toast.makeText(this, "Stopped!", Toast.LENGTH_SHORT).show();
    }
}

package com.example.applaudo.mediaplayerapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class PlayerReceiver extends BroadcastReceiver {

    private OnPlayPausePressed mCallback;

    //This is to change the button icon in the Main Activity
    public interface OnPlayPausePressed {
        void onButtonPressed(String action);
    }

    //Android Studio doesn't like if there's no default constructor
    public PlayerReceiver() {
    }

    public PlayerReceiver(OnPlayPausePressed mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        //This is where it register the actions to change the buttons
        if (intentAction != null) {

            switch (intentAction) {
                case Actions.ACTION_CUSTOM_PLAY:
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_PLAY);
                    break;
                case Actions.ACTION_CUSTOM_PAUSE:
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_PAUSE);
                    break;
                case Actions.ACTION_CUSTOM_MUTE:
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_MUTE);
                    break;
                case Actions.ACTION_CUSTOM_UNMUTE:
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_UNMUTE);
                    break;
            }

        }


    }
}

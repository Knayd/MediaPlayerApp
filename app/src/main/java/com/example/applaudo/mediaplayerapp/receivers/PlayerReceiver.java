package com.example.applaudo.mediaplayerapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.applaudo.mediaplayerapp.main.MainActivity;

//The receiver
public class PlayerReceiver extends BroadcastReceiver {

    private OnPlayPausePressed mCallback;

    //This is to change the button icon in the Main Activity
    public interface OnPlayPausePressed{
        void onButtonPressed(String action);
    }



    public PlayerReceiver(OnPlayPausePressed mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();

        //This is where the logic of playing/pause goes
        if (intentAction != null) {
            String toastMessage = "Unkown intent action";

            switch (intentAction){
                case Actions.ACTION_CUSTOM_PLAY:
              //      toastMessage = "Receiver - Play";
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_PLAY);
                    //Service start
                    break;
                case Actions.ACTION_CUSTOM_PAUSE:
           //         toastMessage = "Receiver - Pause";
                    //Sends the action to the MainActivity
                    mCallback.onButtonPressed(Actions.ACTION_CUSTOM_PAUSE);
                    break;
            }

           // Toast.makeText(context,toastMessage, Toast.LENGTH_SHORT).show();
        }


    }
}

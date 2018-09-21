package com.example.applaudo.mediaplayerapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

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
                    break;
                case Actions.ACTION_CUSTOM_PAUSE:
                    toastMessage = "Receiver - Pause";
                    break;
            }

            Toast.makeText(context,toastMessage, Toast.LENGTH_SHORT).show();
        }


    }
}

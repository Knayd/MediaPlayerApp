package com.example.applaudo.mediaplayerapp.receivers;

import com.example.applaudo.mediaplayerapp.BuildConfig;

public class Actions {
    //The custom actions for the broadcast receiver
    public static final String ACTION_CUSTOM_PLAY = BuildConfig.APPLICATION_ID + "ACTION_CUSTOM_PLAY";
    public static final String ACTION_CUSTOM_PAUSE = BuildConfig.APPLICATION_ID + "ACTION_CUSTOM_PAUSE";
    public static final String ACTION_CUSTOM_MUTE = BuildConfig.APPLICATION_ID + "ACTION_CUSTOM_MUTE";
    public static final String ACTION_CUSTOM_UNMUTE = BuildConfig.APPLICATION_ID + "ACTION_CUSTOM_UNMUTE";

}
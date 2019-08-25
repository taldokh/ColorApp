package com.example.user.colorapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaPlayer player;
        int num = wichColorName();
        player = MediaPlayer.create(this, num);
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public int wichColorName(){ // returns the resulted color audio file number in order to play it

        int result = 0;


        switch (MainActivity.wellComeTxtv.getText().toString()){

            case "BLACK":
                result = R.raw.black;
                break;
            case "PURPLE":
                result = R.raw.purple;
                break;
            case "LIGHT PURPLE":
                result = R.raw.light_purple;
                break;
            case "DARK PURPLE":
                result = R.raw.dark_purple;
                break;
            case "YELLOW":
                result = R.raw.yellow;
                break;
            case "LIGHT YELLOW":
                result = R.raw.light_yellow;
                break;
            case "DARK YELLOW":
                result = R.raw.dark_yellow;
                break;
            case "RED":
                result = R.raw.red;
                break;
            case "LIGHT RED":
                result = R.raw.light_red;
                break;
            case "DARK RED":
                result = R.raw.dark_red;
                break;
            case "WHITE":
                result = R.raw.white;
                break;
            case "GREEN":
                result = R.raw.green;
                break;
            case "LIGHT GREEN":
                result = R.raw.light_green;
                break;
            case "DARK GREEN":
                result = R.raw.dark_green;
                break;
            case "BROWN":
                result = R.raw.brown;
                break;
            case "BLUE":
                result = R.raw.blue;
                break;
            case "LIGHT BLUE":
                result = R.raw.light_blue;
                break;
            case "DARK BLUE":
                result = R.raw.dark_blue;
                break;
            case "PINK":
                result = R.raw.pink;
                break;
            case "LIGHT PINK":
                result = R.raw.light_pink;
                break;
            case "DARK PINK":
                result = R.raw.dark_pink;
                break;
            case "ORANGE":
                result = R.raw.orange;
                break;
            case "LIGHT ORANGE":
                result = R.raw.light_orange;
                break;
            case "DARK ORANGE":
                result = R.raw.dark_orange;
                break;
        }
        return result;
    }

}

package com.example.upmood.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.upmood.Service.MusicService;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("action_music",0);

        Intent intentService = new Intent(context, MusicService.class);
        intentService.putExtra("action_service",action);
        context.startService(intentService);
    }
}

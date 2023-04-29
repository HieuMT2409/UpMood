package com.example.upmood.Service;

import static com.example.upmood.Service.MusicApplication.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.Activity.DanhsachbaihatActivity;
import com.example.upmood.R;
import com.example.upmood.model.Songs;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    @SuppressLint("RemoteViewLayout")
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if(intent != null){
            if (bundle != null) {
                Songs song = (Songs) bundle.get("song");

                if(song != null){
                    startMusic(song);
                    sendNotification(song);
                }
            }
        }

        return START_STICKY;
    }

    private void startMusic(Songs song) {
        try {
            String url = song.getLinkSong();
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(url);
            }
            mediaPlayer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNotification(Songs song) {
        Intent intent = new Intent(this, DanhsachbaihatActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeFile(song.getImage());
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_music_note_24)
                .setSubText("Music APK UPMOOD")
                .setContentTitle(song.getSinger())
                .setContentText(song.getNameSong())
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .setSound(null)
                .addAction(R.drawable.pre_icon, "Previous", null)
                .addAction(R.drawable.pause_icon, "Pause", null)
                .addAction(R.drawable.next_icon, "Next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .build();

        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
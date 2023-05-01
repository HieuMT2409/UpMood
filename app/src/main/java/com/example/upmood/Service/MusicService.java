package com.example.upmood.Service;

import static com.example.upmood.R.layout.activity_danhsachbaihat;
import static com.example.upmood.Service.MusicApplication.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.media.session.MediaButtonReceiver;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.Activity.DanhsachbaihatActivity;
import com.example.upmood.Activity.MainActivity;
import com.example.upmood.Broadcast.MusicReceiver;
import com.example.upmood.Fragment.HomeFragment;
import com.example.upmood.R;
import com.example.upmood.model.Songs;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private DanhsachbaihatActivity danhsachbaihatActivity;
    private boolean isPlay;
    private boolean isMusic;
    private Songs mSong;

    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    @SuppressLint("RemoteViewLayout")
    @Override
    public void onCreate() {
        super.onCreate();

        RemoteViews views = new RemoteViews(getPackageName(),R.layout.layout_notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if (intent != null) {
            if (bundle != null) {
                Songs song = (Songs) bundle.get("song");
                if (song != null) {
                    try {
                        mSong = song;
                        PlayMusic(song);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.execute(song);
                }
            }
        }

        int actionMusic = intent.getIntExtra("action_service",0);
        try {
            handleActionMusic(actionMusic);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return START_STICKY;
    }

    private class NotificationTask extends AsyncTask<Songs, Void, Void> {
        @Override
        protected Void doInBackground(Songs... songs) {
            Songs song = songs[0];
            try {
                sendNotification(song);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }

    private void PlayMusic(Songs songs) throws IOException {
        String url = songs.getLinkSong();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.start();

        isMusic = true;
        isPlay = true;
    }

    private void handleActionMusic(int action) throws ExecutionException, InterruptedException {
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
        }
    }

    private void resumeMusic() throws ExecutionException, InterruptedException {
        isPlay = true;
        mediaPlayer.start();
        sendNotification(mSong);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isMusic == false || isPlay == false) {
                    return;
                }
                handler.postDelayed(this, 100);
            }
        };
        handler.postDelayed(runnable, 100);

    }

    private void pauseMusic() throws ExecutionException, InterruptedException {
        if(isMusic == false  && isPlay == false){
            return;
        }
        mediaPlayer.pause();
        isPlay = false;
        sendNotification(mSong);
    }
    private void sendNotification(Songs song) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(this, DanhsachbaihatActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // xu ly hinh anh de them vao trong notification
        Bitmap bitmap = Glide.with(getApplicationContext())
                .asBitmap()
                .load(song.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.avatar_default)
                        .error(R.drawable.avatar_default))
                .submit()
                .get();

        //cai dat
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.layout_notification);
        views.setImageViewResource(R.id.imgPlay_Pause,R.drawable.pause_icon);
        views.setTextViewText(R.id.tvSongName,song.getNameSong());
        views.setTextViewText(R.id.tvSinger,song.getSinger());
        views.setImageViewBitmap(R.id.largeIcon,bitmap);

        //xu ly su kien click tren notify
        if(isPlay){
            views.setOnClickPendingIntent(R.id.imgPlay_Pause,getPendingIntent(this,ACTION_PAUSE));
            views.setImageViewResource(R.id.imgPlay_Pause,R.drawable.pause_icon);
        }else{
            views.setOnClickPendingIntent(R.id.imgPlay_Pause,getPendingIntent(this,ACTION_RESUME));
            views.setImageViewResource(R.id.imgPlay_Pause,R.drawable.play_icon);
        }

        //hien thi thong bao
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_music_note_24)
                .setSubText("Music APK UPMOOD")
                .setContentIntent(pendingIntent)
                .setCustomContentView(views)
                .setSound(null)
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu cấp quyền
            ActivityCompat.requestPermissions(danhsachbaihatActivity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
            return;
        }

        managerCompat.notify(1, notification);
    }

    private PendingIntent getPendingIntent(Context context,int action) {
        Intent intent = new Intent(this, MusicReceiver.class);
        intent.putExtra("action_music",action);

        return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent.FLAG_UPDATE_CURRENT);
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
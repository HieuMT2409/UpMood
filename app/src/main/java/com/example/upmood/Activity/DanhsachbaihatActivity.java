package com.example.upmood.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.R;
import com.example.upmood.Service.MusicService;
import com.example.upmood.model.Music;
import com.example.upmood.model.Songs;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DanhsachbaihatActivity extends AppCompatActivity {

    private Songs songs;
    private ImageView bg_blur_img,themeMusic,btnBack,btnPreMusic,btnPlayMusic,btnNextMusic,btnPlaylist;
    private MediaPlayer mediaPlayer;
    private TextView nameSong,tvTimeStart,tvTimeEnd,tvscriptSong;
    private SeekBar seekBar;
    private CircleLineVisualizer circleVisualizer;

    private boolean isPlay;
    private boolean isMusic;
    private int timeMax;
    private int timeMusicStop=0;
    private float rotation = 0f;

    private ArrayList<String> arrScriptSong = new ArrayList<>();

    private int scriptLocation = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachbaihat);


        isPlay = false;
        isMusic = false;

        // Kiểm tra quyền truy cập âm thanh
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO }, 0);
        }


        //anh xa view
        bg_blur_img = findViewById(R.id.bg_blur_img);
        themeMusic = findViewById(R.id.themeMusic);
        nameSong = findViewById(R.id.nameSong);
        btnPreMusic = findViewById(R.id.btnPreMusic);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnNextMusic = findViewById(R.id.btnNextMusic);
        seekBar = findViewById(R.id.seekBar);
        tvTimeStart = findViewById(R.id.tvTimeStart);
        tvTimeEnd = findViewById(R.id.tvTimeEnd);
        tvscriptSong = findViewById(R.id.tvscriptSong);
        btnBack = findViewById(R.id.btnBack);
        circleVisualizer = findViewById(R.id.circleVisualizer);
        btnPlaylist = findViewById(R.id.btnPlaylist);

        //set thoi gian phat nhac
        tvTimeStart.setText(getTimeString(0));

        //Load data
        try {
            DataIntent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // xu ly su kien onClick
        setClick();

    }

    private String getTimeString(int i) {
        int seconds = i / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void DataIntent() throws IOException {
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("BaiHat")){
                songs = (Songs) intent.getSerializableExtra("BaiHat");

                nameSong.setText(songs.getNameSong().toUpperCase());

                Glide.with(this)
                        .load(songs.getImage())
                        .error(R.drawable.avatar_default)
                        .into(themeMusic);

                Glide.with(this)
                        .load(songs.getImage())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                        .into(bg_blur_img);
            }else{
                if(intent.hasExtra("Playlist")){
                    songs = (Songs) intent.getSerializableExtra("Playlist");

                    nameSong.setText(songs.getNameSong().toUpperCase());

                    Glide.with(this)
                            .load(songs.getImage())
                            .error(R.drawable.avatar_default)
                            .into(themeMusic);

                    Glide.with(this)
                            .load(songs.getImage())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                            .into(bg_blur_img);
                }
            }
        }
    }

    private void PlayMusic(Songs songs) throws IOException {
        String url = songs.getLinkSong();
        rotation = 0f;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();

        // xu ly lay time bai hat khi chay
        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBar.setMin(0);
            }

            // update seekbar progress every 100ms
            final Handler mHandler = new Handler();
            Runnable mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isMusic == false || isPlay == false) {
                        return;
                    }
                    timeMax = mediaPlayer.getDuration();
                    seekBar.setMax(timeMax);
                    circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
                    tvTimeEnd.setText(getTimeString(timeMax));
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tvTimeStart.setText(getTimeString(mediaPlayer.getCurrentPosition()));

                    mHandler.postDelayed(this, 100);

                    rotation = rotation + 0.5f;
                    if(rotation == 360f){
                        rotation = 0f;
                    }
                    themeMusic.setRotation(rotation);

                }
            };
            mHandler.postDelayed(mRunnable, 100);
        });
        // set khi hoan thanh bai hat thi ve 0
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                StopMusic();
                seekBar.setProgress(0);
                tvTimeStart.setText(getTimeString(0));
                rotation = 0f;
            }
        });

        btnPlayMusic.setImageResource(R.drawable.pause_icon);
        isMusic = true;
        isPlay = true;
    }

    private void setClick(){

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DanhsachbaihatActivity.this,PlaylistActivity.class);
                startActivity(intent);
            }
        });
        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMusic == false){
                    try {
                        PlayMusic(songs);
//                        StartService();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    if(isPlay == true){
                        StopMusic();
                    }else{
                        ContinueMusic();
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                timeMusicStop = seekBar.getProgress();
                mediaPlayer.pause();
                mediaPlayer.seekTo(timeMusicStop);
                mediaPlayer.start();
                btnPlayMusic.setImageResource(R.drawable.pause_icon);
            }
        });
    }

    private void ContinueMusic() {
        isPlay = true;
        btnPlayMusic.setImageResource(R.drawable.pause_icon);
        mediaPlayer.seekTo(timeMusicStop);
        mediaPlayer.start();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isMusic == false || isPlay == false) {
                    return;
                }
                timeMax = mediaPlayer.getDuration();
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
                tvTimeStart.setText(getTimeString(mediaPlayer.getCurrentPosition()));

                handler.postDelayed(this, 100);

                rotation = rotation + 0.5f;
                if(rotation == 360f){
                    rotation = 0f;
                }
                themeMusic.setRotation(rotation);

            }
        };
        handler.postDelayed(runnable, 100);
    }

    private void StopMusic() {
        isPlay = false;
        btnPlayMusic.setImageResource(R.drawable.play_icon);
        mediaPlayer.pause();
        timeMusicStop = mediaPlayer.getCurrentPosition();

    }

    private void StartService() {
        Intent intent = new Intent(DanhsachbaihatActivity.this, MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("song",songs);
        intent.putExtras(bundle);
        startService(intent);
    }
}
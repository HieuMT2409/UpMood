package com.example.upmood.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.R;
import com.example.upmood.Service.MusicService;
import com.example.upmood.model.MediaPlayerSingleton;
import com.example.upmood.model.Songs;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DanhsachbaihatActivity extends AppCompatActivity {

    private Songs songs;
    private List<Songs> songsList;
    private ImageView bg_blur_img,themeMusic,btnBack,btnPreMusic,btnPlayMusic,btnNextMusic,btnPlaylist,btnHeart,btnShuffle;
    private MediaPlayerSingleton mediaPlayerSingleton;
    private MediaPlayer mediaPlayer;
    private TextView nameSong,tvTimeStart,tvTimeEnd,tvscriptSong;
    private SeekBar seekBar;
    private CircleLineVisualizer circleVisualizer;

    private RelativeLayout playView;

    private boolean isPlay;
    private boolean isMusic;
    private int timeMax;
    private int timeMusicStop = 0;
    private int currentSongIndex = 0;
    private float rotation = 0f;

    private ArrayList<String> arrScriptSong = new ArrayList<>();

    private int scriptLocation = 0;
    private boolean repeatMode = false;
    private boolean checkHeart = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachbaihat);


        isPlay = false;
        isMusic = false;

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
        btnHeart = findViewById(R.id.btnHeart);
        playView = findViewById(R.id.playView);
        btnShuffle = findViewById(R.id.btnShuffle);


        //set thoi gian phat nhac
        tvTimeStart.setText(getTimeString(0));

        //Load data
        try {
            DataIntent();
            PlayMusic(songs.getLinkSong());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // xu ly su kien onClick
        setClick(songs);

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
                Bundle bundle = intent.getExtras();
                songs = (Songs) bundle.getSerializable("BaiHat");
                songsList = (List<Songs>) bundle.getSerializable("listBaiHat");

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

    public void PlayMusic(String linkSong) throws IOException {
        rotation = 0f;

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayerSingleton = MediaPlayerSingleton.getInstance();
        mediaPlayer = mediaPlayerSingleton.getMediaPlayer();
        mediaPlayer.setDataSource(linkSong);
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
                    tvTimeEnd.setText(getTimeString(timeMax));
                    circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
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
                NextMusic();
            }
        });

        btnPlayMusic.setImageResource(R.drawable.pause_icon);
        isMusic = true;
        isPlay = true;
    }

    private void NextMusic(){
        currentSongIndex++;
        if(currentSongIndex >= songsList.size()){
            currentSongIndex = 0;
        }

        try {
            //lay du lieu cap nhat o playlist
            songs = songsList.get(currentSongIndex);
            setClick(songs);

            //cap nhat du lieu
            nameSong.setText(songs.getNameSong().toUpperCase());

            Glide.with(DanhsachbaihatActivity.this)
                    .load(songs.getImage())
                    .error(R.drawable.avatar_default)
                    .into(themeMusic);

            Glide.with(DanhsachbaihatActivity.this)
                    .load(songs.getImage())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                    .into(bg_blur_img);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.getLinkSong());
            mediaPlayer.prepare();

            circleVisualizer.release();
            if(circleVisualizer == null){
                circleVisualizer = findViewById(R.id.circleVisualizer);
            }
            circleVisualizer.setColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());

            mediaPlayer.start();

        } catch (IOException e) {

        }
    }

    private void PreviousMusic(){
        currentSongIndex-=1;
        if(currentSongIndex >= songsList.size()){
            currentSongIndex = 0;
        }
        if(currentSongIndex == 0){
            currentSongIndex = songsList.size();
        }

        try {
            //lay du lieu cap nhat o playlist
            songs = songsList.get(currentSongIndex);
            setClick(songs);

            //cap nhat du lieu
            nameSong.setText(songs.getNameSong().toUpperCase());

            Glide.with(DanhsachbaihatActivity.this)
                    .load(songs.getImage())
                    .error(R.drawable.avatar_default)
                    .into(themeMusic);

            Glide.with(DanhsachbaihatActivity.this)
                    .load(songs.getImage())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                    .into(bg_blur_img);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.getLinkSong());
            mediaPlayer.prepare();

            circleVisualizer.release();
            if(circleVisualizer == null){
                circleVisualizer = findViewById(R.id.circleVisualizer);
            }
            circleVisualizer.setColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());

            mediaPlayer.start();


        } catch (IOException e) {

        }
    }

    private void setClick(Songs song){

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(!repeatMode){
                    repeatMode = true;
                    btnShuffle.setImageResource(R.drawable.baseline_repeat_true_24);
                }else{
                    repeatMode = false;
                    btnShuffle.setImageResource(R.drawable.baseline_repeat_24);
                }
            }
        });

        btnNextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songsList.size() > 0){
                    if(currentSongIndex < (songsList.size())){

                        if(repeatMode == true){
                            if(currentSongIndex == 0){
                                currentSongIndex = songsList.size();
                            }
                            currentSongIndex -= 1;
                        }
                        NextMusic();
                    }
                }
                btnNextMusic.setClickable(false);
                btnPreMusic.setClickable(false);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnNextMusic.setClickable(true);
                        btnPreMusic.setClickable(true);
                    }
                },5000);
            }
        });

        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkHeart){
                    checkHeart = true;
                    btnHeart.setImageResource(R.drawable.baseline_favorite_24);
                }else{
                    checkHeart = false;
                    btnHeart.setImageResource(R.drawable.baseline_favorite_border_24);

                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playView.setVisibility(View.GONE);
            }
        });
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Playlist(song);
            }
        });
        btnPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMusic == false){
                    //StartService();
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

    private void Playlist(Songs song) {
        Intent intent = new Intent(DanhsachbaihatActivity.this,PlaylistActivity.class);
        intent.putExtra("music",song);
        startActivity(intent);
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
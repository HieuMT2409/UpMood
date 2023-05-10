package com.example.upmood.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.R;
import com.example.upmood.Service.MusicService;
import com.example.upmood.model.Chill;
import com.example.upmood.model.MediaPlayerSingleton;
import com.example.upmood.model.TopTrending;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ChillActivity extends AppCompatActivity {

    private Chill songs;
    private List<Chill> songsList;
    private ImageView bg_blur_img,themeMusic,btnPreMusic,btnPlayMusic,btnNextMusic,btnPlaylist,btnHeart,btnShuffle,btnBack;
    private MediaPlayerSingleton mediaPlayerSingleton;
    private MediaPlayer mediaPlayer;
    private TextView nameSong,tvTimeStart,tvTimeEnd;
    private SeekBar seekBar;
    private CircleLineVisualizer circleVisualizer;

    private boolean isPlay;
    private boolean isMusic;
    private int timeMax;
    private int timeMusicStop = 0;
    private int currentSongIndex = 0;
    private float rotation = 0f;
    private boolean isRandom = false;
    private boolean checkHeart = false;
    private boolean checkPlaylist = false;
    private String linkSong;

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
        circleVisualizer = findViewById(R.id.circleVisualizer);
        btnPlaylist = findViewById(R.id.btnPlaylist);
        btnHeart = findViewById(R.id.btnHeart);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnBack = findViewById(R.id.btnBack);


        //set thoi gian phat nhac
        tvTimeStart.setText(getTimeString(0));

        //Load data
        try {
            DataIntent();
            if(checkPlaylist == false){
                PlayMusic(songs.getLinkSong());
            }
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
            if(intent.hasExtra("Chill")){
                Bundle bundle = intent.getExtras();
                songs = (Chill) bundle.getSerializable("Chill");
                songsList = (List<Chill>) bundle.getSerializable("listBaiHat");

                nameSong.setText(songs.getNameSong().toUpperCase());

                Glide.with(this)
                        .load(songs.getImage())
                        .error(R.drawable.avatar_default)
                        .into(themeMusic);

                Glide.with(this)
                        .load(songs.getImage())
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                        .into(bg_blur_img);
            }else if(intent.hasExtra("Playlist")){
                    checkPlaylist = true;
                    Bundle bundle = intent.getExtras();
                    songs = (Chill) bundle.getSerializable("Playlist");
                    songsList = (List<Chill>) bundle.getSerializable("listBaiHat");

                    nameSong.setText(songs.getNameSong().toUpperCase());

                    Glide.with(this)
                            .load(songs.getImage())
                            .error(R.drawable.avatar_default)
                            .into(themeMusic);

                    Glide.with(this)
                            .load(songs.getImage())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                            .into(bg_blur_img);
                    PlayMusicList(songs.getLinkSong());
            }
        }
    }

    private void PlayMusicList(String linkSong) throws IOException {
        mediaPlayerSingleton = MediaPlayerSingleton.getInstance();
        mediaPlayer = mediaPlayerSingleton.getMediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(linkSong);
        mediaPlayer.prepare();
        mediaPlayer.start();
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
//                    circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(currentSongIndex < (songsList.size())) {

                    if (isRandom == true) {
                        Random random = new Random();
                        int index = random.nextInt(songsList.size());
                        if (currentSongIndex == index) {
                            currentSongIndex = index - 1;
                        }
                        currentSongIndex = index;
                    }
                    currentSongIndex++;
                    if (currentSongIndex >= songsList.size()) {
                        currentSongIndex = 0;
                    }
                    try {
                        //lay du lieu cap nhat o playlist
                        songs = songsList.get(currentSongIndex);
                        setClick(songs);

                        //cap nhat du lieu
                        nameSong.setText(songs.getNameSong().toUpperCase());

                        Glide.with(ChillActivity.this)
                                .load(songs.getImage())
                                .error(R.drawable.avatar_default)
                                .into(themeMusic);

                        Glide.with(ChillActivity.this)
                                .load(songs.getImage())
                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 30)))
                                .into(bg_blur_img);

                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(songs.getLinkSong());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (IOException e) {

                    }
                }
            }
        });
        btnPlayMusic.setImageResource(R.drawable.pause_icon);
        isMusic = true;
        isPlay = true;
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
//                    circleVisualizer.setAudioSessionId(mediaPlayer.getAudioSessionId());
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
                if(currentSongIndex < (songsList.size())){

                    if(isRandom == true){
                        Random random = new Random();
                        int index = random.nextInt(songsList.size());
                        if(currentSongIndex == index){
                            currentSongIndex = index - 1;
                        }
                        currentSongIndex = index;
                    }

                    NextMusic();
                }
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

            Glide.with(ChillActivity.this)
                    .load(songs.getImage())
                    .error(R.drawable.avatar_default)
                    .into(themeMusic);

            Glide.with(ChillActivity.this)
                    .load(songs.getImage())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                    .into(bg_blur_img);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.getLinkSong());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {

        }
    }

    private void PreviousMusic(){

        if(currentSongIndex >= songsList.size()){
            currentSongIndex = 0;
        }
        if(currentSongIndex < songsList.size()){
            currentSongIndex-=1;
            //lay du lieu cap nhat o playlist
            songs = songsList.get(currentSongIndex);
            setClick(songs);
        }
        if(currentSongIndex < 0){
            currentSongIndex = songsList.size() - 1 ;
        }

        try {

            //cap nhat du lieu
            nameSong.setText(songs.getNameSong().toUpperCase());

            Glide.with(ChillActivity.this)
                    .load(songs.getImage())
                    .error(R.drawable.avatar_default)
                    .into(themeMusic);

            Glide.with(ChillActivity.this)
                    .load(songs.getImage())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25,30)))
                    .into(bg_blur_img);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(songs.getLinkSong());
            mediaPlayer.prepare();
            mediaPlayer.start();


        } catch (IOException e) {

        }
    }

    private void setClick(Chill song){

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if(isRandom == false){
                    isRandom = true;
                    btnShuffle.setImageResource(R.drawable.baseline_shuffle_on_24);
                }else{
                    isRandom = false;
                    btnShuffle.setImageResource(R.drawable.baseline_shuffle_true_24);
                }
            }
        });

        btnNextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songsList.size() > 0){
                    if(currentSongIndex < (songsList.size())){

                        if(isRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songsList.size());
                            if(currentSongIndex == index){
                                currentSongIndex = index - 1;
                            }
                            currentSongIndex = index;
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

        btnPreMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(songsList.size() > 0){
                    if(currentSongIndex < (songsList.size())){

                        if(isRandom == true){
                            Random random = new Random();
                            int index = random.nextInt(songsList.size());
                            if(currentSongIndex == index){
                                currentSongIndex = index - 1;
                            }
                            currentSongIndex = index;
                        }

                        PreviousMusic();
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
                String linkSong = songs.getLinkSong();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,123);
                    }else{
                        StartDownload(linkSong);
                    }
                }else{
                    StartDownload(linkSong);
                }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StartDownload(linkSong);
            }else{
                Toast.makeText(this, "Permission Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartDownload(String linkSong) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(linkSong));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Downloading Music ...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if(downloadManager != null){
            downloadManager.enqueue(request);
        }
    }

    private void Playlist(Chill song) {
        Intent intent = new Intent(ChillActivity.this,PlaylistChillActivity.class);
        intent.putExtra("music",song);
        startActivity(intent);
        finish();
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
        Intent intent = new Intent(ChillActivity.this, MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("song",songs);
        intent.putExtras(bundle);
        startService(intent);
    }
}
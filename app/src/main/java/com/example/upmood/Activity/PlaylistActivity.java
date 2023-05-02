package com.example.upmood.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.upmood.Adapter.PlaylistAdapter;
import com.example.upmood.Adapter.SongsAdapter;
import com.example.upmood.OnItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.MediaPlayerSingleton;
import com.example.upmood.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView rcPlaylist;
    private ImageView backMusic;
    private PlaylistAdapter playlistAdapter;
    private List<Songs> songsList;
    private LinearLayout ControlWrapper;
    private Songs songs;
    private MediaPlayerSingleton mediaPlayerSingleton;
    private MediaPlayer mediaPlayer;
    private int timeMusicStop=0;

    private boolean isPlaying = true;

    private ImageView iconPlaylist,btnPrePlaylist,btnPlay_Pause,btnNextPlaylist;
    private TextView songNamePlaylist,singerPlaylist;

    DanhsachbaihatActivity danhsachbaihatActivity;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //anh xa view
        rcPlaylist = findViewById(R.id.rcPlaylist);
        backMusic = findViewById(R.id.backMusic);
        ControlWrapper = findViewById(R.id.ControlWrapper);
        iconPlaylist = findViewById(R.id.iconPlaylist);
        btnPrePlaylist = findViewById(R.id.btnPrePlaylist);
        btnPlay_Pause = findViewById(R.id.btnPlay_Pause);
        btnNextPlaylist = findViewById(R.id.btnNextPlaylist);
        songNamePlaylist = findViewById(R.id.songNamePlaylist);
        singerPlaylist = findViewById(R.id.singerPlaylist);

        danhsachbaihatActivity = new DanhsachbaihatActivity();

        //xu ly recycle view
        songsList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(this,songsList);
        rcPlaylist.setLayoutManager(new LinearLayoutManager(this));
        rcPlaylist.setAdapter(playlistAdapter);

        try {
            DataIntent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getListSongsFromFireBase();

        //su kien choi nhac
        setClick();

        //bat su kien click item trong recycle view
        playlistAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song) {
                mediaPlayerSingleton = MediaPlayerSingleton.getInstance();
                mediaPlayer = mediaPlayerSingleton.getMediaPlayer();
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }

                try {
                    danhsachbaihatActivity.PlayMusic(song);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                Intent intent = new Intent(PlaylistActivity.this, DanhsachbaihatActivity.class);
//                intent.putExtra("Playlist",song);
//                PlaylistActivity.this.startActivity(intent);
            }
        });

        backMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlaylistActivity.this, "Quay ve trang phat nhac", Toast.LENGTH_SHORT).show();
            }
        });

        ControlWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlaylistActivity.this,DanhsachbaihatActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setClick() {
        //choi nhac
        btnPlay_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay_Pause.setImageResource(R.drawable.play_icon);
                    timeMusicStop = mediaPlayer.getCurrentPosition();
                }else{
                    btnPlay_Pause.setImageResource(R.drawable.pause_icon);
                    mediaPlayer.start();
                }
            }
        });

    }

    private void DataIntent() throws IOException {
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("music")){
                songs = (Songs) intent.getSerializableExtra("music");
                mediaPlayer = (MediaPlayer) intent.getSerializableExtra("media_player");

                songNamePlaylist.setText(songs.getNameSong().toUpperCase());
                singerPlaylist.setText(songs.getSinger());

                Glide.with(this)
                        .load(songs.getImage())
                        .error(R.drawable.avatar_default)
                        .into(iconPlaylist);
            }
        }
    }


    private void getListSongsFromFireBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Songs");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Songs song = snap.getValue(Songs.class);
                    songsList.add(song);
                }

                playlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "FAILED !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

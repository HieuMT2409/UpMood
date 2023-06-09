package com.example.upmood.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.upmood.Adapter.PlaylistSearchAdapter;
import com.example.upmood.Adapter.PlaylistTrendingAdapter;
import com.example.upmood.Interface.OnSearchItemClickListener;
import com.example.upmood.Interface.OnTopItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.MediaPlayerSingleton;
import com.example.upmood.model.Search;
import com.example.upmood.model.TopTrending;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaylistSearchActivity extends AppCompatActivity {

    private RecyclerView rcPlaylist;
    private PlaylistSearchAdapter playlistAdapter;
    private List<Search> songsList;
    private LinearLayout ControlWrapper;
    private Search songs;
    private MediaPlayerSingleton mediaPlayerSingleton;
    private MediaPlayer mediaPlayer;
    private RelativeLayout backgroundPlaylist;
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
        ControlWrapper = findViewById(R.id.ControlWrapper);
        iconPlaylist = findViewById(R.id.iconPlaylist);
        btnPrePlaylist = findViewById(R.id.btnPrePlaylist);
        btnPlay_Pause = findViewById(R.id.btnPlay_Pause);
        btnNextPlaylist = findViewById(R.id.btnNextPlaylist);
        songNamePlaylist = findViewById(R.id.songNamePlaylist);
        singerPlaylist = findViewById(R.id.singerPlaylist);
        backgroundPlaylist = findViewById(R.id.backgroundPlaylist);

        danhsachbaihatActivity = new DanhsachbaihatActivity();

        //xu ly recycle view
        songsList = new ArrayList<>();
        playlistAdapter = new PlaylistSearchAdapter(this,songsList);
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
        playlistAdapter.setOnItemClickListener(new OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(Search song, List<Search> searchList) {
                Intent intent = new Intent(PlaylistSearchActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Playlist",song);
                bundle.putSerializable("listBaiHat", (Serializable) searchList);
                intent.putExtras(bundle);
                PlaylistSearchActivity.this.startActivity(intent);
            }
        });

        ControlWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PlaylistSearchActivity.this, TopTrendingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Playlist",songs);
        bundle.putSerializable("listBaiHat", (Serializable) songsList);
        intent.putExtras(bundle);
        PlaylistSearchActivity.this.startActivity(intent);
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
                songs = (Search) intent.getSerializableExtra("music");
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
        DatabaseReference myRef = database.getReference("TopTrending");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Search song = snap.getValue(Search.class);
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

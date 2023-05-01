package com.example.upmood.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upmood.Adapter.PlaylistAdapter;
import com.example.upmood.Adapter.SongsAdapter;
import com.example.upmood.OnItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Songs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView rcPlaylist;
    private ImageView backMusic;
    private PlaylistAdapter playlistAdapter;
    private List<Songs> songsList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        //anh xa view
        rcPlaylist = findViewById(R.id.rcPlaylist);
        backMusic = findViewById(R.id.backMusic);

        //xu ly recycle view
        songsList = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(this,songsList);
        rcPlaylist.setLayoutManager(new LinearLayoutManager(this));
        rcPlaylist.setAdapter(playlistAdapter);

        getListSongsFromFireBase();

        //bat su kien click item trong recycle view
        playlistAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song) {
                Intent intent = new Intent(PlaylistActivity.this, DanhsachbaihatActivity.class);
                intent.putExtra("Playlist",song);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PlaylistActivity.this.startActivity(intent);
            }
        });

        backMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlaylistActivity.this, "Quay ve trang phat nhac", Toast.LENGTH_SHORT).show();
            }
        });

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

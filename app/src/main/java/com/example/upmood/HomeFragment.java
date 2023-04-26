package com.example.upmood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.upmood.model.Songs;
import com.example.upmood.model.SongsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recycleSongs;
    private LinearLayoutManager layoutManager;
    private SongsAdapter songsAdapter;
    private List<Songs> songsList;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //cài đặt cho danh sách hiển thị ngang
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recycleSongs = view.findViewById(R.id.recycleSongs);
        recycleSongs.setLayoutManager(layoutManager);

        //set adapter cho recycle view song
        songsList = new ArrayList<>();
        songsAdapter = new SongsAdapter(songsList);
        recycleSongs.setAdapter(songsAdapter);

        getListSongsFromFireBase();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
    }

    private void getListSongsFromFireBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Songs");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Songs song = snap.getValue(Songs.class);
                    songsList.add(song);
                }

                songsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "FAILED !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
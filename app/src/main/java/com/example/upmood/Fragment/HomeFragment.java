package com.example.upmood.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.upmood.Activity.DanhsachbaihatActivity;
import com.example.upmood.Activity.TopTrendingActivity;
import com.example.upmood.Adapter.TopTrendingAdapter;
import com.example.upmood.OnItemClickListener;
import com.example.upmood.OnTopItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Songs;
import com.example.upmood.Adapter.SongsAdapter;
import com.example.upmood.model.TopTrending;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recycleSongNew,recycleSongMaybe,recycleSongCute;
    private LinearLayoutManager layoutManagerSongNew,layoutManagerSongMaybe,layoutManagerSongCute;
    private SongsAdapter songsAdapter;
    private TopTrendingAdapter trendingAdapter;

    private List<Songs> songsList;
    private List<TopTrending> trendingList;
    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //cài đặt cho danh sách hiển thị ngang
        layoutManagerSongNew = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerSongMaybe = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerSongCute = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        //layout bai hat thinh hanh
        recycleSongNew = view.findViewById(R.id.recycleSongNew);
        recycleSongNew.setLayoutManager(layoutManagerSongNew);

        //layout bai hat co the muon nghe
        recycleSongMaybe = view.findViewById(R.id.recycleSongMaybe);
        recycleSongMaybe.setLayoutManager(layoutManagerSongMaybe);

        //layout bai hat cute
        recycleSongCute = view.findViewById(R.id.recycleSongCute);
        recycleSongCute.setLayoutManager(layoutManagerSongCute);

        //set adapter cho recycle view song
        songsList = new ArrayList<>();
        trendingList = new ArrayList<>();
        songsAdapter = new SongsAdapter(getContext(),songsList);
        trendingAdapter = new TopTrendingAdapter(getContext(),trendingList);

        //set bai hat cho cac recycle view
        recycleSongNew.setAdapter(trendingAdapter);
        recycleSongMaybe.setAdapter(songsAdapter);
        recycleSongCute.setAdapter(songsAdapter);

        getListSongsFromFireBase();

        //bat su kien click item trong recycle view
        songsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song) {
                Intent intent = new Intent(getActivity(), DanhsachbaihatActivity.class);
                intent.putExtra("BaiHat",song);
                getActivity().startActivity(intent);
            }
        });
        trendingAdapter.setOnItemClickListener(new OnTopItemClickListener() {
            @Override
            public void onTopItemClick(TopTrending song) {
                Intent intent = new Intent(getActivity(), TopTrendingActivity.class);
                intent.putExtra("Trending",song);
                getActivity().startActivity(intent);
            }
        });

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
        DatabaseReference topTrend = database.getReference("TopTrending");

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

        topTrend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    TopTrending topTrending = snap.getValue(TopTrending.class);
                    trendingList.add(topTrending);
                }

                trendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "FAILED !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
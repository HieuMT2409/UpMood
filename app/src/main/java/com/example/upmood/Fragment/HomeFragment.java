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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.upmood.Activity.ChillActivity;
import com.example.upmood.Activity.DanhsachbaihatActivity;
import com.example.upmood.Activity.TopTrendingActivity;
import com.example.upmood.Adapter.ChillAdapter;
import com.example.upmood.Adapter.SearchAdapter;
import com.example.upmood.Adapter.TopTrendingAdapter;
import com.example.upmood.Interface.OnChillItemClickListener;
import com.example.upmood.Interface.OnItemClickListener;
import com.example.upmood.Interface.OnTopItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Chill;
import com.example.upmood.model.Search;
import com.example.upmood.model.Songs;
import com.example.upmood.Adapter.SongsAdapter;
import com.example.upmood.model.TopTrending;
import com.example.upmood.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private RecyclerView recycleSongNew,recycleSongMaybe,recycleSongCute;
    private LinearLayoutManager layoutManagerSongNew,layoutManagerSongMaybe,layoutManagerSongCute;
    private SongsAdapter songsAdapter,trendingAdapter,chillAdapter;

    private List<Songs> songsList,trendingList,chillList;
    private RelativeLayout playView;
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

        //layout bai hat chill
        recycleSongCute = view.findViewById(R.id.recycleSongCute);
        recycleSongCute.setLayoutManager(layoutManagerSongCute);

        //set adapter cho recycle view song
        songsList = new ArrayList<>();
        trendingList = new ArrayList<>();
        chillList = new ArrayList<>();

        songsAdapter = new SongsAdapter(getContext(),songsList);
        trendingAdapter = new SongsAdapter(getContext(),trendingList);
        chillAdapter = new SongsAdapter(getContext(),chillList);

        //set bai hat cho cac recycle view
        recycleSongNew.setAdapter(trendingAdapter);
        recycleSongMaybe.setAdapter(songsAdapter);
        recycleSongCute.setAdapter(chillAdapter);

        getListSongsFromFireBase();

        playView = view.findViewById(R.id.playView);

        //bat su kien click item trong recycle view
        songsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song, List<Songs> songsList) {
                Intent intent = new Intent(getActivity(), DanhsachbaihatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BaiHat",song);
                bundle.putSerializable("listBaiHat", (Serializable) songsList);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        trendingAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song, List<Songs> songsList) {
                Intent intent = new Intent(getActivity(), DanhsachbaihatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BaiHat",song);
                bundle.putSerializable("listBaiHat", (Serializable) songsList);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        chillAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Songs song, List<Songs> songsList) {
                Intent intent = new Intent(getActivity(), DanhsachbaihatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BaiHat",song);
                bundle.putSerializable("listBaiHat", (Serializable) songsList);
                intent.putExtras(bundle);
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
        DatabaseReference myRef = database.getReference("Search");
        DatabaseReference topTrend = database.getReference("Search");
        DatabaseReference chill = database.getReference("Search");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Songs song = snap.getValue(Songs.class);
                    songsList.add(song);
                }
                Collections.shuffle(songsList);
                songsList.addAll(songsList);
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
                    Songs topTrending = snap.getValue(Songs.class);
                    trendingList.add(topTrending);
                }
                Collections.shuffle(trendingList);
                trendingList.addAll(trendingList);
                trendingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "FAILED !!!", Toast.LENGTH_SHORT).show();
            }
        });
        chill.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()){
                    Songs chillSong = snap.getValue(Songs.class);
                    chillList.add(chillSong);
                }
                Collections.shuffle(chillList);
                chillList.addAll(chillList);
                chillAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "FAILED !!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
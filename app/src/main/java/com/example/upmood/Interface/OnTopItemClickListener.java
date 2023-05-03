package com.example.upmood.Interface;

import com.example.upmood.model.TopTrending;

import java.util.List;

public interface OnTopItemClickListener {
    void onTopItemClick(TopTrending song, List<TopTrending> topTrendingList);
}

package com.example.upmood.Interface;

import com.example.upmood.model.Chill;
import com.example.upmood.model.Search;

import java.util.List;

public interface OnSearchItemClickListener {
    void onSearchItemClick(Search song, List<Search> songsList);
}

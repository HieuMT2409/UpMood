package com.example.upmood.Interface;

import com.example.upmood.model.Songs;

import java.util.List;

public interface OnItemClickListener {
    void onItemClick(Songs song, List<Songs> songsList);
}

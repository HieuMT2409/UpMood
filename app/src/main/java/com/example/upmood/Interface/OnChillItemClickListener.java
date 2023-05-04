package com.example.upmood.Interface;

import com.example.upmood.model.Chill;
import com.example.upmood.model.Songs;

import java.util.List;

public interface OnChillItemClickListener {
    void onChillItemClick(Chill song, List<Chill> songsList);
}

package com.example.upmood.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.upmood.R;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder>{
    private List<Songs> songsList;

    public SongsAdapter(List<Songs> songsList) {
        this.songsList = songsList;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_item,parent,false);

        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        Songs song = songsList.get(position);
        if(song == null){
            return;
        }

        //còn phần hiển thị hình ảnh cho bài hát
        holder.tvTitleSong.setText(song.getNameSong());
        holder.tvSinger.setText(song.getSinger());

    }

    @Override
    public int getItemCount() {
        if (songsList != null){
            return songsList.size();
        }
        return 0;
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgSong;
        private TextView tvTitleSong,tvSinger;

        public SongsViewHolder(@NonNull View itemView) {
            super(itemView);

            //ánh xạ view
            imgSong = itemView.findViewById(R.id.imgSong);
            tvTitleSong = itemView.findViewById(R.id.tvTitleSong);
            tvSinger = itemView.findViewById(R.id.tvSinger);
        }
    }
}

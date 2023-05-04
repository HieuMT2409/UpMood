package com.example.upmood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.upmood.Interface.OnItemClickListener;
import com.example.upmood.Interface.OnTopItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Songs;
import com.example.upmood.model.TopTrending;

import java.util.List;

public class PlaylistTrendingAdapter extends RecyclerView.Adapter<PlaylistTrendingAdapter.SongsViewHolder>{
    private List<TopTrending> songsList;
    private Context mContext;
    private OnTopItemClickListener mListener;
    private int lastPosition = -1;

    public PlaylistTrendingAdapter(Context context, List<TopTrending> songsList) {
        mContext = context;
        this.songsList = songsList;
    }

    //khoi tao ham onclick dde su dung click vao item trong recycle view
    public void setOnItemClickListener(OnTopItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_music,parent,false);

        return new SongsViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TopTrending song = songsList.get(position);
        if(song == null){
            return;
        }

        //còn phần hiển thị hình ảnh cho bài hát
        holder.tvSongNameP.setText(song.getNameSong());
        holder.tvSingerP.setText(song.getSinger());
        Glide.with(mContext)
                .load(song.getImage())
                .error(R.drawable.anhsedonem)
                .into(holder.imgSongP);

        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if (songsList != null){
            return songsList.size();
        }
        return 0;
    }

    public class SongsViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgSongP;
        private TextView tvSongNameP,tvSingerP;

        public SongsViewHolder(@NonNull View itemView, final OnTopItemClickListener listener) {
            super(itemView);

            //ánh xạ view
            imgSongP = itemView.findViewById(R.id.imgSongP);
            tvSongNameP = itemView.findViewById(R.id.tvSongNameP);
            tvSingerP = itemView.findViewById(R.id.tvSingerP);

            //bắt sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            TopTrending song = songsList.get(position);
                            listener.onTopItemClick(song,songsList);
                        }
                    }
                }
            });
        }
    }
}

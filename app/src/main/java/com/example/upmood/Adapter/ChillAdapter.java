package com.example.upmood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.upmood.Interface.OnChillItemClickListener;
import com.example.upmood.Interface.OnTopItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Chill;
import com.example.upmood.model.TopTrending;

import java.util.List;

public class ChillAdapter extends RecyclerView.Adapter<ChillAdapter.SongsViewHolder>{
    private List<Chill> songsList;
    private Context mContext;
    private OnChillItemClickListener mListener;

    public ChillAdapter(Context context, List<Chill> songsList) {
        mContext = context;
        this.songsList = songsList;
    }

    //khoi tao ham onclick dde su dung click vao item trong recycle view
    public void setOnItemClickListener(OnChillItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_item,parent,false);

        return new SongsViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        Chill song = songsList.get(position);
        if(song == null){
            return;
        }

        //còn phần hiển thị hình ảnh cho bài hát
        holder.tvTitleSong.setText(song.getNameSong());
        holder.tvSinger.setText(song.getSinger());
        Glide.with(mContext)
                .load(song.getImage())
                .error(R.drawable.anhsedonem)
                .into(holder.imgSong);


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

        public SongsViewHolder(@NonNull View itemView, final OnChillItemClickListener listener) {
            super(itemView);

            //ánh xạ view
            imgSong = itemView.findViewById(R.id.imgSong);
            tvTitleSong = itemView.findViewById(R.id.tvTitleSong);
            tvSinger = itemView.findViewById(R.id.tvSinger);

            //bắt sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAbsoluteAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Chill song = songsList.get(position);
                            listener.onChillItemClick(song,songsList);
                        }
                    }
                }
            });
        }
    }
}

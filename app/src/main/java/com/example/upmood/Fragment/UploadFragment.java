package com.example.upmood.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.upmood.Activity.MainActivity;
import com.example.upmood.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadFragment extends Fragment {

    private EditText edtImage,edtSong,edtNameSong,edtSinger;
    private Button btnUpload;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_layout, container, false);

        edtImage = view.findViewById(R.id.edtImage);
        edtSong = view.findViewById(R.id.edtSong);
        edtNameSong = view.findViewById(R.id.edtNameSong);
        edtSinger = view.findViewById(R.id.edtSinger);
        btnUpload = view.findViewById(R.id.btnUpload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //lấy thông tin
                String id = edtNameSong.getText().toString().toLowerCase().trim();
                String getImage = edtImage.getText().toString();
                String getSong = edtSong.getText().toString();
                String getName = edtNameSong.getText().toString();
                String getSinger = edtSinger.getText().toString();

                //khởi tạo lên firebase
                DatabaseReference image = database.getReference("UserSong/"+id+"/image");
                DatabaseReference liked = database.getReference("UserSong/"+id+"/liked");
                DatabaseReference linkSong = database.getReference("UserSong/"+id+"/linkSong");
                DatabaseReference nameSong = database.getReference("UserSong/"+id+"/nameSong");
                DatabaseReference singer = database.getReference("UserSong/"+id+"/singer");

                image.setValue(getImage);
                liked.setValue("0");
                linkSong.setValue(getSong);
                nameSong.setValue(getName);
                singer.setValue(getSinger);

                Toast.makeText(getContext(), "Upload thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

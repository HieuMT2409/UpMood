package com.example.upmood.fragment_nav;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.example.upmood.Activity.MainActivity;
import com.example.upmood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView avatar_edit;
    EditText username_edit,email_edit,phone_edit;
    Button btnUpgrade;
    View view;
    MainActivity mainActivity;
    private ProgressDialog progressDialog;

    private Uri uri;

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_nav_layout,container,false);
        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //anh xa view
        avatar_edit = view.findViewById(R.id.avatar_edit);
        username_edit = view.findViewById(R.id.username_edit);
        email_edit = view.findViewById(R.id.email_edit);
        phone_edit = view.findViewById(R.id.phone_edit);
        btnUpgrade = view.findViewById(R.id.btnUpgrade);

        //goi hien thi thong tin user trong profile
        setInformation();

        //goi ham cap nhat thong tin user
        initListener();
    }

    //hien thi thong tin user trong profile
    private void setInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        username_edit.setText(user.getDisplayName());
        email_edit.setText(user.getEmail());
        phone_edit.setText(user.getPhoneNumber());
        Glide.with(getActivity())
                .load(user.getPhotoUrl())
                .error(R.drawable.avatar_default)
                .into(avatar_edit);
    }

    //cap nhat thong tin user
    private void initListener() {

        avatar_edit.setOnClickListener(view -> {
            onClickRequestPermission(); // kiem tra quyen truy cap
        });

        btnUpgrade.setOnClickListener(view1 -> {
            onClickUpdateProfile();
        });

    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            return;
        }

        progressDialog.show();
        String username = username_edit.getText().toString();
        UserProfileChangeRequest profileChangeRequest =
                new UserProfileChangeRequest.Builder().setDisplayName(username)
                        .setPhotoUri(uri).build();

        user.updateProfile(profileChangeRequest).addOnCompleteListener(
                task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(),"Update thàng công", Toast.LENGTH_SHORT).show();
                        mainActivity.showUserInformation();
                    }
                }
        );
    }
    public void setBitMapImageView(Bitmap bitmap){
        if (avatar_edit != null) {
            avatar_edit.setImageBitmap(bitmap);
        }
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//neu version android < 6
            mainActivity.openGallery();
            return;
        }

        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            mainActivity.openGallery();
        }else{
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MainActivity.MY_REQUEST_CODE);
        }
    }


}

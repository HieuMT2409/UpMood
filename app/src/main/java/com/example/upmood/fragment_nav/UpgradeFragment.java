package com.example.upmood.fragment_nav;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.upmood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpgradeFragment extends Fragment {
    private TextView noidung;
    private ImageView btnCopy;
    private FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upgrade_premium_layout,container,false);
        noidung = view.findViewById(R.id.noidung);
        btnCopy = view.findViewById(R.id.btnCopy);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        noidung.setText("premium "+email);

        btnCopy.bringToFront();

        //xu ly tu dong copy
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                String textToCopy = "premium "+email;
                ClipData clip = ClipData.newPlainText("label", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getContext(), "Đã sao chép nội dung!", Toast.LENGTH_SHORT).show();

            }
        });
        return view;
    }
}

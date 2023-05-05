package com.example.upmood.Activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.upmood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpgradeActivity extends AppCompatActivity {
    private TextView noidung;
    private ImageView btnCopy;
    private LinearLayout click,clickPa;
    private FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_premium_layout);

        noidung = findViewById(R.id.noidung);
        btnCopy = findViewById(R.id.btnCopy);
        click = findViewById(R.id.click);
        clickPa = findViewById(R.id.clickPa);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        String email = user.getEmail();
        noidung.setText("premium "+email);

        btnCopy.bringToFront();
        clickPa.invalidate();

        //xu ly tu dong copy
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                String textToCopy = "premium "+email;
                ClipData clip = ClipData.newPlainText("label", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Đã sao chép nội dung!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

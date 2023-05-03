package com.example.upmood.Activity;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.upmood.R;
import com.example.upmood.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    //khai bao bien
    TabLayout mTabLayout;
    ViewPager2 view_pager;
    private static final int REQUEST_CODE = 2413;

    // Danh sách các quyền cần yêu cầu
    private String[] permissionList = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        //xin cac quyen can thiet
        List<String> perNeed = new ArrayList<>();
        for (String permission : permissionList) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                perNeed.add(permission);
            }
        }

        if (!perNeed.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    perNeed.toArray(new String[perNeed.size()]),
                    REQUEST_CODE);
        }

        //anh xa view
        mTabLayout = findViewById(R.id.mTabLayout);
        view_pager = findViewById(R.id.view_pager);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        LinearLayout loginLayout = findViewById(R.id.login_layout);
        loginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Ẩn bàn phím ảo
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return false;
            }
        });

        // cau hinh viewpager
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        view_pager.setAdapter(viewPagerAdapter);

        //cau hinh tablayout
        new TabLayoutMediator(mTabLayout, view_pager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Sign In");
                    break;
                case 1:
                    tab.setText("Sign Up");
                    break;
            }
        }).attach();
    }
}

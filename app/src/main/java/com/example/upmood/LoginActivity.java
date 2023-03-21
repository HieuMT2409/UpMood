package com.example.upmood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LoginActivity extends AppCompatActivity {
    //khai bao bien
    TabLayout mTabLayout;
    ViewPager2 view_pager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        //anh xa view
        mTabLayout = findViewById(R.id.mTabLayout);
        view_pager = findViewById(R.id.view_pager);
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

package com.example.upmood;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.upmood.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    BottomNavigationView navigationView;
    Button btn_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.action_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.action_library:
                    replaceFragment(new LibraryFragment());
                    break;
                case R.id.action_search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.action_premium:
                    replaceFragment(new PremiumFragment());
                    break;
            }
            return true;
        });

    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}
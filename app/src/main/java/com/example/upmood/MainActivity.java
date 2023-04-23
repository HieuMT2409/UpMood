package com.example.upmood;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.upmood.databinding.ActivityMainBinding;
import com.example.upmood.fragment_nav.ProfileFragment;
import com.example.upmood.fragment_nav.SettingFragment;
import com.example.upmood.fragment_nav.UpgradeFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int MY_REQUEST_CODE = 10;

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private Toolbar toolBar;
    private NavigationView mnavigationView;
    private CircleImageView avatar;
    private TextView tvUserName,tvUserEmail;

    ProfileFragment profileFragment;
    Uri uri;

    final private ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

                if(result.getResultCode() == RESULT_OK){
                    Intent intent = result.getData();

                    if (intent == null) {
                        return;
                    }

                    uri = intent.getData();

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

                        if (profileFragment != null) {
                            profileFragment.setBitMapImageView(bitmap);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //khoi tao phuong thuc profileFragment
        profileFragment = new ProfileFragment();

        //xu ly action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

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

        //xu ly cac su kien header
        drawerLayout = findViewById(R.id.drawerLayout);
        toolBar = findViewById(R.id.toolBar);
        mnavigationView = findViewById(R.id.navigationView);

        //anh xa cac item trong header title
        avatar = mnavigationView.getHeaderView(0).findViewById(R.id.avatar);
        tvUserName = mnavigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        tvUserEmail = mnavigationView.getHeaderView(0).findViewById(R.id.tvUserEmail);


        //add toolbar va su kien dong/mo navigation
        setSupportActionBar(toolBar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open_nav_drawer,R.string.close_nav_drawer);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.primary));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //xu ly su kien tren navigation
        mnavigationView.setNavigationItemSelectedListener(this);
        mnavigationView.bringToFront();

        //hien thi thong tin user
        showUserInformation();
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //hien thi menu va xu ly khi nhan nut
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notify,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    //ham xu ly cac su kien trong navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.my_profile:
                replaceFragment(new ProfileFragment());
                break;
            case R.id.upgrade:
                replaceFragment(new UpgradeFragment());
                break;
            case R.id.setting:
                replaceFragment(new SettingFragment());
                break;
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
        }

        item.setChecked(true);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //xu ly nut back ve

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    //xu ly hien ten user tren header title
    public void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }else{
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUri = user.getPhotoUrl();

            if(name == null){
                tvUserName.setVisibility(View.GONE);
            }else{
                tvUserName.setVisibility(View.VISIBLE);
                tvUserName.setText(name);
            }

            tvUserEmail.setText(email);
            Glide.with(this).load(photoUri)
                    .error(R.drawable.avatar_default)
                    .into(avatar);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==  MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        activityResultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
}
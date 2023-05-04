package com.example.upmood.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.upmood.Adapter.CategoryAdapterSetting;
import com.example.upmood.R;
import com.example.upmood.model.CategorySetting;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Spinner snCategory;
    private CategoryAdapterSetting categoryAdapterSetting;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_setting);

        snCategory = findViewById(R.id.sn_category);
        categoryAdapterSetting = new CategoryAdapterSetting(this, R.layout.setting_nav_layout,getListCategory());
        snCategory.setAdapter(categoryAdapterSetting);
        snCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SettingActivity.this,categoryAdapterSetting.getItem(i).getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private List<CategorySetting> getListCategory(){
        List<CategorySetting> list = new ArrayList<>();
        list.add(new CategorySetting("Yên lặng"));
        list.add(new CategorySetting("Bình thường"));
        list.add(new CategorySetting("Lớn"));

        return list;
    }
}
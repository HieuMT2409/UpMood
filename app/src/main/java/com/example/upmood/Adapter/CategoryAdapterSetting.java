package com.example.upmood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.upmood.model.CategorySetting;
import com.example.upmood.R;

import java.util.List;

public class CategoryAdapterSetting extends ArrayAdapter<CategorySetting> {
    public CategoryAdapterSetting(@NonNull Context context, int resource, @NonNull List<CategorySetting> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_nav_layout, parent, false);
        TextView txtViewSelect = convertView.findViewById(R.id.tv_select_item);

        CategorySetting categorySetting = this.getItem(position);
        if(categorySetting != null){
            txtViewSelect.setText(categorySetting.getName());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_nav_layout, parent, false);
        TextView txtViewCategory = convertView.findViewById(R.id.tvMucAmlg);

        CategorySetting categorySetting = this.getItem(position);
        if(categorySetting != null){
            txtViewCategory.setText(categorySetting.getName());
        }

        return convertView;
    }
}
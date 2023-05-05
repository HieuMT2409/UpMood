package com.example.upmood.Fragment;

import static com.example.upmood.R.layout.activity_main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upmood.Activity.MainActivity;
import com.example.upmood.Adapter.SearchAdapter;
import com.example.upmood.R;
import com.example.upmood.model.Search;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private TextView tvNoData;
    private RecyclerView rcSearch;
    private ArrayList<Search> searchArrayList;
    private SearchAdapter searchAdapter;
    private DatabaseReference databaseReference;
    private FirebaseOptions firebaseOptions;

    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        tvNoData = view.findViewById(R.id.tvNoData);
        rcSearch = view.findViewById(R.id.rcSearch);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Search");

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notify,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchfromFirebase(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchfromFirebase(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    private void SearchfromFirebase(String text){
        String searchText = text.toLowerCase();
        Query query = databaseReference
                .orderByChild("nameSong")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Log.d("CHECKKKKKKKKKK", String.valueOf(snapshot));
//                        searchArrayList.add(snapshot.getValue(Search.class));
                    }
//                    searchAdapter = new SearchAdapter(getContext(),searchArrayList);
//                    rcSearch.setAdapter(searchAdapter);
                    
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
    }
}
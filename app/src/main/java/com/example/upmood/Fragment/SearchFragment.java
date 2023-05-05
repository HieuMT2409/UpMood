package com.example.upmood.Fragment;

import static com.example.upmood.R.layout.activity_main;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.upmood.Activity.DanhsachbaihatActivity;
import com.example.upmood.Activity.MainActivity;
import com.example.upmood.Activity.SearchActivity;
import com.example.upmood.Adapter.SearchAdapter;
import com.example.upmood.Adapter.SongsAdapter;
import com.example.upmood.Interface.OnItemClickListener;
import com.example.upmood.Interface.OnSearchItemClickListener;
import com.example.upmood.R;
import com.example.upmood.model.Search;
import com.example.upmood.model.Songs;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private TextView tvNoData;
    private RecyclerView rcSearch;
    private List<Search> searchArrayList;
    private SearchAdapter searchAdapter;
    private LinearLayout linearSearch;

    public SearchFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        tvNoData = view.findViewById(R.id.tvNoData);
        rcSearch = view.findViewById(R.id.rcSearch);
        linearSearch = view.findViewById(R.id.linearSearch);
        searchArrayList = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(),searchArrayList);
        rcSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        rcSearch.setAdapter(searchAdapter);
        setHasOptionsMenu(true);

        getData();

        searchAdapter.setOnItemClickListener(new OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(Search song, List<Search> songsList) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Search",song);
                bundle.putSerializable("listBaiHat", (Serializable) songsList);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Search");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    Search song = snap.getValue(Search.class);
                    searchArrayList.add(song);
                }
                searchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled: " + databaseError.getMessage());
            }
        });
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
                return false;
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
        ArrayList<Search> searches = new ArrayList<>();
        for(Search search : searchArrayList){
            if(search.getNameSong().toLowerCase().contains(text.toLowerCase())){
                searches.add(search);
            }else{
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
        searchAdapter = new SearchAdapter(getContext(),searches);
        rcSearch.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

        searchAdapter.setOnItemClickListener(new OnSearchItemClickListener() {
            @Override
            public void onSearchItemClick(Search song, List<Search> songsList) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Search",song);
                bundle.putSerializable("listBaiHat", (Serializable) songsList);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
    }
}
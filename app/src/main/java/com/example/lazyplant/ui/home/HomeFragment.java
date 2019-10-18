package com.example.lazyplant.ui.home;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.DisplayHelper;
import com.example.lazyplant.ui.PlantSearchViewModel;
import com.example.lazyplant.ui.search.BrowseAdapter;
import com.example.lazyplant.ui.shopmap.ShopsMapActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;
import static com.example.lazyplant.ui.plantDetails.PlantDetailsActivity.TAG;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeFragment extends Fragment {
    private View root;
    private RecyclerView popular_view;
    private RecyclerView.Adapter adapter;
    private List<PlantInfoEntity> plant_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Lazy Plant");
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        final Fragment f_fragment = this;

        Button explore_go = (Button) root.findViewById(R.id.home_explore_go);
        explore_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_to_navigation_search);
            }
        });
        Button design_go = (Button) root.findViewById(R.id.home_design_go);
        design_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_to_navigation_design);
            }
        });
        Button shopping_go = (Button) root.findViewById(R.id.home_shopping_go);
        shopping_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), ShopsMapActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        //ed.putString(Constants.SHARED_PREFERENCE_POSTCODE, null);
        //ed.commit();
        if(!pref.contains(Constants.SHARED_PREFERENCE_POSTCODE)){
            NavHostFragment.findNavController(f_fragment).navigate(
                    R.id.action_navigation_home_to_navigation_postcode);
        }

        this.plant_list = new ArrayList<>();
        this.popular_view = (RecyclerView) root.findViewById(R.id.home_recycler);
        //GridLayoutManager mLayoutManager = new GridLayoutManager(
        //       getContext(),2, LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(
                getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        this.popular_view.setLayoutManager(mLayoutManager);
        adapter = new BrowseAdapter(this.plant_list, this);
        this.popular_view.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("plantsInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> idList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                idList.add(document.getData().get("plantID").toString());
                            }

                            final Map<String, Integer> counter = new HashMap<String, Integer>();
                            for (String str : idList)
                                counter.put(str, 1 + (counter.containsKey(str) ? counter.get(str) : 0));

                            List<String> list = new ArrayList<String>(counter.keySet());
                            Collections.sort(list, new Comparator<String>() {
                                @Override
                                public int compare(String x, String y) {
                                    return counter.get(y) - counter.get(x);
                                }
                            });
                            updatePopularPlants(list);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return root;
    }

    private void updatePopularPlants(List<String> list){
        this.plant_list.clear();
        DbAccess databaseAccess = DbAccess.getInstance(this.root.getContext());
        databaseAccess.open();
        for (String x : list) {
            this.plant_list.add(databaseAccess.getShortPlantInfo(x));
        }
        databaseAccess.close();
        this.adapter.notifyDataSetChanged();
    }

}

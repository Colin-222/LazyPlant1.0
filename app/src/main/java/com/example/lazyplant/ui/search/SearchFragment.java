package com.example.lazyplant.ui.search;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.ui.DisplayHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SearchFragment extends Fragment {

    private FusedLocationProviderClient client;
    private Integer current_display_option;
    private SelectedFiltersEntity selected_filters = new SelectedFiltersEntity();
    private RecyclerView srv;
    private RecyclerView.Adapter adapter;
    private List<SearchCategory> category_list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_search, container, false);
        final Fragment f_fragment = this;
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        TabLayout tabLayout = root.findViewById(R.id.search_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TAB_GENERAL));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TAB_ANIMAL));
        tabLayout.addTab(tabLayout.newTab().setText(Constants.TAB_EDIBLE));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setSearchCategory(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        this.category_list = createGeneralCategories();
        this.srv = (RecyclerView) root.findViewById(R.id.search_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.srv.setLayoutManager(mLayoutManager);
        this.adapter = new SearchAdapter(this.category_list, this);
        this.srv.setAdapter(adapter);

        final EditText et = (EditText) root. findViewById(R.id.search_edit);
        final ImageButton button = (ImageButton) root.findViewById(R.id.search_go);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_term = et.getText().toString();
                if (!search_term.equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.NAME_SEARCH_TAG, search_term);
                    NavHostFragment.findNavController(f_fragment).navigate(
                            R.id.action_navigation_search_to_navigation_browse, bundle);
                } else {
                    et.setError("Please Enter a Valid Plant Name");
                }
            }
        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                    DisplayHelper.hideKeyboard(getContext(), root);
                    return button.callOnClick();
                }
                return false;
            }
        });
        et.setOnFocusChangeListener(editFocusListener);

        return root;
    }

    private void setSearchCategory(String category) {
        this.category_list.clear();
        if (category.equals(Constants.TAB_GENERAL)) {
            this.category_list.addAll(createGeneralCategories());
        } else if (category.equals(Constants.TAB_ANIMAL)) {
            this.category_list.addAll(createAnimalCategories());
        } else if (category.equals(Constants.TAB_EDIBLE)) {
            this.category_list.addAll(createEdibleCategories());
        }
        this.adapter.notifyDataSetChanged();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private List<SearchCategory> createGeneralCategories() {
        List<SearchCategory> l = new ArrayList<>();
        l.add(new SearchCategory("All Plants", null, Constants.GENERAL_PLANTS_TAG,
                R.drawable.all_plants));
        l.add(new SearchCategory("Drought resistant", "Drought resistant",
                Constants.GENERAL_PLANTS_TAG, R.drawable.all_plants));
        l.add(new SearchCategory("Low maintenance", "Low maintenance",
                Constants.GENERAL_PLANTS_TAG, R.drawable.all_plants));
        return l;
    }

    private List<SearchCategory> createAnimalCategories() {
        List<SearchCategory> l = new ArrayList<>();
        l.add(new SearchCategory("Butterfly/Moth", "Butterfly/Moth",
                Constants.HABITAT_TAG, R.drawable.butterflies));
        l.add(new SearchCategory("Birds", "Bird",
                Constants.HABITAT_TAG, R.drawable.birds));
        l.add(new SearchCategory("Honeyeaters", "Honeyeater",
                Constants.HABITAT_TAG, R.drawable.honeyeater));
        l.add(new SearchCategory("Parrots", "Parrot",
                Constants.HABITAT_TAG, R.drawable.parrot));
        l.add(new SearchCategory("Reptile/Frog", "Reptile/Frog",
                Constants.HABITAT_TAG, R.drawable.frogs));
        l.add(new SearchCategory("Mammals", "Mammal",
                Constants.HABITAT_TAG, R.drawable.mammals));
        l.add(new SearchCategory("Bees", "Bees",
                Constants.HABITAT_TAG, R.drawable.bees));
        l.add(new SearchCategory("Insects", "Insect",
                Constants.HABITAT_TAG, R.drawable.insects));
        return l;
    }

    private List<SearchCategory> createEdibleCategories() {
        List<SearchCategory> l = new ArrayList<>();
        l.add(new SearchCategory("Fruit", "fruit",
                Constants.EDIBLE_TAG, R.drawable.fruits));
        l.add(new SearchCategory("Vegetable", "vegetable",
                Constants.EDIBLE_TAG, R.drawable.vegetables));
        l.add(new SearchCategory("Herb", "herb",
                Constants.EDIBLE_TAG, R.drawable.herbs));
        l.add(new SearchCategory("Spices", "spice",
                Constants.EDIBLE_TAG, R.drawable.spices));
        return l;
    }

    private View.OnFocusChangeListener editFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                DisplayHelper.hideKeyboard(view.getContext(), view);
            }
        }};

}
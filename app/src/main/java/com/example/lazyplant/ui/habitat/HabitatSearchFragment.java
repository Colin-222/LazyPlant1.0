package com.example.lazyplant.ui.habitat;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.ui.DisplayHelper;
import com.example.lazyplant.ui.PlantSearchViewModel;
import com.example.lazyplant.ui.search.BrowseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HabitatSearchFragment extends Fragment {
    private View root;
    private RecyclerView favs_view;
    private RecyclerView.Adapter adapter;
    private PlantSearchViewModel model;
    private String animal;

    public HabitatSearchFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_show_plants, container, false);
        Bundle bundle = this.getArguments();
        this.animal = bundle.getString(Constants.HABITAT_TAG);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle("Plants for attracting " + this.animal);
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        this.model.plant_list = new ArrayList<>();

        final Context context = this.getContext();
        final EditText pc_edit = (EditText) this.root.findViewById(R.id.show_plants_postcode_edit);
        final Button pc_go = (Button) this.root.findViewById(R.id.show_plants_postcode_go);
        pc_edit.setOnFocusChangeListener(postcodeListener);
        pc_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                    DisplayHelper.hideKeyboard(context, root);
                    changeLocation(pc_edit.getText().toString());
                    return true;
                }
                return false;
            }
        });
        pc_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeLocation(pc_edit.getText().toString());
            }
        });
        if(this.model.postcode != null){
            ClimateZoneGetter czg = new ClimateZoneGetter();
            int zone = czg.getZone(this.model.postcode);
            if (zone != -1){
                pc_edit.setText(this.model.postcode);
                this.getPlantList(this.animal, zone);
            }else{
                pc_edit.setText("");
            }
        }else{
            this.getPlantList(this.animal, -1);
        }

        this.favs_view = (RecyclerView) root.findViewById(R.id.show_plants_search_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.favs_view.setLayoutManager(mLayoutManager);
        adapter = new BrowseAdapter(this.model.plant_list, this);
        this.favs_view.setAdapter(adapter);

        return root;
    }

    private void getPlantList(String search_term, int zone){
        DbAccess databaseAccess = DbAccess.getInstance(this.root.getContext());
        databaseAccess.open();
        List<String> found = databaseAccess.searchPlantsByAnimalsAttracted(search_term);
        if (zone >= 1 && zone <= 7){
            List<String> tmp = databaseAccess.searchOnConditions(Constants.SPECIES_ID_FIELD,
                    "zone", "climate_zone",
                    Arrays.asList(String.valueOf(zone)));
            found.retainAll(tmp);
        }
        for (String f : found) {
            this.model.plant_list.add(databaseAccess.getShortPlantInfo(f));
        }
        databaseAccess.close();
    }

    private void changeLocation(String location){
        if(location.matches("[0-9][0-9][0-9][0-9]")){
            ClimateZoneGetter czg = new ClimateZoneGetter();
            int zone = czg.getZone(location);
            if (zone != -1){
                this.model.postcode = location;
                this.model.plant_list.clear();
                this.getPlantList(this.animal, zone);
                this.adapter.notifyDataSetChanged();
            }else{
                Toast toast = Toast.makeText(getActivity(),
                        "Sorry, your postcode is invalid.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                this.model.postcode = "";
            }
        }else{
            Toast toast = Toast.makeText(getActivity(),
                    "Sorry, your postcode is invalid.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            this.model.postcode = "";
        }

        TextView error_text = (TextView) root.findViewById(R.id.show_plants_error_text);
        if(this.model.plant_list.size() <= 0){
            error_text.setText("No plants found.");
        }else{
            error_text.setText("");
        }
    }


    private View.OnFocusChangeListener postcodeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                DisplayHelper.hideKeyboard(view.getContext(), view);
                changeLocation(((EditText)view).getText().toString());
            }
        }};

}

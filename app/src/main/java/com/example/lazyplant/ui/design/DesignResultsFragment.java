package com.example.lazyplant.ui.design;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class DesignResultsFragment extends Fragment {
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_design_result, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        Bundle bundle = this.getArguments();
        int garden = bundle.getInt(Constants.EDIBLE_TAG);
        ImageView im = (ImageView)root.findViewById(R.id.design_result_image);
        List<String> plants = new ArrayList<>();
        switch(garden){
            case 0:
                Glide.with(getContext()).load(R.drawable.garden_natural).into(im);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Informal Natural Garden");
                plants.add("Allocasuarina verticillata ‘Noodles’ – She-Oak");
                plants.add("Acacia acinacea – Gold Dust Wattle");
                plants.add("Dianella revoluta ‘Petite Marie’ Native Flax");
                plants.add("Poa labillardieri ‘Eskdale’ – Tussock Grass");
                plants.add("Indigofera australis – Australian Indigo");
                plants.add("Callistemon sieberi ‘Sugar Candy’ – Bottlebrush");
                plants.add("Wahlenbergia capillaris");
                plants.add("Chrysocephalum apiculatum ‘Desert Flame’ – Yellow Buttons");
                break;
            case 1:
                Glide.with(getContext()).load(R.drawable.garden_formal).into(im);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Formal Style Garden");
                plants.add("Goodenia ovata ‘Gold Cover’");
                plants.add("Wahlenbergia capillaris");
                plants.add("Correa reflexa – Native Fuchsia");
                plants.add("Grevillea rosmarinifolia ‘Scarlet Sprite’");
                plants.add("Chrysocephalum apiculatum ‘Desert Flame’ – Yellow Buttons");
                break;
            case 2:
                Glide.with(getContext()).load(R.drawable.garden_japanese).into(im);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Japanese Style Garden");
                plants.add("Poa labillardieri ‘Eskdale’ – Tussock Grass");
                plants.add("Themeda triandra ‘Quokka’ – Kangaroo Grass");
                plants.add("Banksia marginata ‘Minimarg’");
                plants.add("Correa glabra ‘Ivory Lantern’ – Rock Correa");
                break;
            case 3:
                Glide.with(getContext()).load(R.drawable.garden_cottage).into(im);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cottage Garden");
                plants.add("Chrysocephalum apiculatum ‘Desert Flame’ – Yellow Buttons");
                plants.add("Dianella revoluta ‘Petite Marie’ Native Flax");
                plants.add("Wahlenbergia capillaris");
                plants.add("Brachyscome multifida ‘Break O Day’ Native Daisy");
                plants.add("Correa reflexa – Native Fuchsia");
                plants.add("Correa glabra ‘Ivory Lantern’ – Rock Correa");
                plants.add("Poa labillardieri ‘Eskdale’ – Tussock Grass");
                break;
        }
        LinearLayout ll = root.findViewById(R.id.design_result_layout);
        ViewGroup.LayoutParams params = ll.getLayoutParams();
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        int i = 1;
        for(String plant_id : plants){
            Button button = new Button(getContext());
            PlantInfoEntity pie = databaseAccess.getShortPlantInfo(plant_id);
            final String pid = plant_id;
            button.setText(String.valueOf(i) + ") " + pie.getCommon_name());
            button.setLayoutParams(params);
            ll.addView(button);
            final Fragment f_fragment = this;
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //open details page with id
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PLANT_DETAILS_BUNDLE_TAG, pid);
                    NavHostFragment.findNavController(f_fragment).navigate(
                            R.id.action_navigation_design_result_to_navigation_plant_details, bundle);
                    NavController navi = NavHostFragment.findNavController(f_fragment);
                    if(navi.getCurrentDestination().getId() == R.id.navigation_browse){
                        navi.navigate(R.id.action_navigation_browse_to_navigation_plant_details, bundle);
                    }else if(navi.getCurrentDestination().getId() == R.id.navigation_favourites){
                        navi.navigate(R.id.action_navigation_favorite_to_navigation_plant_details, bundle);
                    }else if(navi.getCurrentDestination().getId() == R.id.navigation_habitat_search){
                        navi.navigate(R.id.action_navigation_habitat_search_to_navigation_plant_details, bundle);
                    } else if(navi.getCurrentDestination().getId() == R.id.navigation_edible_search){
                        navi.navigate(R.id.action_navigation_edible_search_to_navigation_plant_details, bundle);
                    }
                }
            });
            i++;
        }
        databaseAccess.close();

        return root;
    }



}

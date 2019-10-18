package com.example.lazyplant.ui;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.search.FilterOptionSelector;
import com.example.lazyplant.ui.search.PlantSearcher;

import java.util.List;

public class PlantSearchViewModel extends ViewModel {

    public List<PlantInfoEntity> plant_list;
    public PlantSearcher plant_searcher;
    public String postcode;
    public List<String> my_plants;

}

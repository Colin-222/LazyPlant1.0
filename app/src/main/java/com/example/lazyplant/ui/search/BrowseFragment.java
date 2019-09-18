package com.example.lazyplant.ui.search;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.DisplayHelper;
import com.example.lazyplant.ui.plantListDisplayHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

public class BrowseFragment extends Fragment {
    private View root;
    private boolean show_filters = false;
    private View filter_view;
    private ConstraintLayout filter_cl;
    private FilterOptionSelector fos;
    private String current_filter;
    private SelectedFiltersEntity selected_filters = new SelectedFiltersEntity();
    private ConstraintLayout results_cl;
    final private String LOCATION_TEXT = "Plants for postcode: ";

    final private List<String> FILTER_OPTIONS = Arrays.asList("Type", "Height", "Width", "Shade", "Frost");
    final private int CHIP_SPACING = 8;
    final private int H_MARGIN = 8;
    final private int V_MARGIN = 2;
    final private int HEIGHT_CLOSED = 40;
    final private int HEIGHT_OPEN = 165;
    final private String HEIGHT = "height";
    final private String WIDTH = "width";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_browse, container, false);
        this.current_filter = this.FILTER_OPTIONS.get(0);
        this.filter_cl = (ConstraintLayout) this.root.findViewById(R.id.browse_filter_area);
        this.results_cl = (ConstraintLayout) this.root.findViewById(R.id.browse_results_layout);

        ImageButton filters_button = (ImageButton) root.findViewById(R.id.browse_display_filters);
        filters_button.setOnClickListener(filterButtonListener);

        Bundle bundle = this.getArguments();
        if(bundle == null){
            updateLocation(null);
        }else{
            String postcode = bundle.getString(Constants.LOCATION_TAG);
            updateLocation(postcode);
        }
        updateResults();

        return this.root;
    }

    private boolean updateLocation(String postcode){
        String location = null;
        if (postcode == null) {
            SharedPreferences pref = this.getContext().getApplicationContext()
                    .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
            location = pref.getString(Constants.DEFAULT_POSTCODE, null);
        } else {
            location = postcode;
        }
        if (location == null){ return false; }
        TextView location_text = (TextView)this.root.findViewById(R.id.browse_location_text);
        location_text.setText(LOCATION_TEXT + location);
        ClimateZoneGetter czg = new ClimateZoneGetter();
        int zone = czg.getZone(location);
        FilterOptionEntity fo = FilterDisplayHelper.createZoneFilter();
        FilterOptionSelector location_fos = FilterDisplayHelper.createFilter(fo, getContext());
        if(zone >= 1 && zone <= 7){
            ((Chip)location_fos.getChildAt(zone - 1)).setChecked(true);
            this.selected_filters.editSelectedOptions(location_fos);
        }else{
            return false;
        }
        return true;
    }

    private void configure_filter_display(){
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.filter_cl.getLayoutParams();
        int h = this.dpToPx(this.H_MARGIN, this.getContext());
        int v = this.dpToPx(this.V_MARGIN, this.getContext());

        if(this.show_filters){
            //Close filter options
            params.height = dpToPx(this.HEIGHT_CLOSED, this.getContext());
            ((ViewGroup)this.filter_view.getParent()).removeView(this.filter_view);
            ((ViewGroup)this.fos.getParent()).removeView(this.fos);
            this.fos = null;
            this.show_filters = false;
        }else{
            //Show filter options
            params.height = dpToPx(this.HEIGHT_OPEN, this.getContext());
            View im = (View) root.findViewById(R.id.browse_display_filters);
            this.filter_view = inflater.inflate(R.layout.layout_filter_options, null, false);
            final ChipGroup cg = this.filter_view.findViewById(R.id.filter_selector);
            this.filter_cl.addView(this.filter_view);
            DisplayHelper.setViewConstraints(this.filter_view, this.filter_cl, this.filter_cl, im, h, v);
            cg.setOnCheckedChangeListener(chipGroupChangeListener);
            View divider = this.filter_view.findViewById(R.id.filter_divider);
            this.showCurrentFilter();
            this.show_filters = true;
        }
        this.filter_cl.setLayoutParams(params);
    }

    private void showCurrentFilter(){
        if(this.fos != null) {
            ((ViewGroup)this.fos.getParent()).removeView(this.fos);
            this.fos = null;
        }
        FilterOptionEntity fo = null;
        List<String> current = new ArrayList<>();
        switch(this.current_filter) {
            case "Type":
                fo = FilterDisplayHelper.createTypeFilter();
                current = this.selected_filters.getCurrentSelections("Type");
                break;
            case "Height":
                fo = FilterDisplayHelper.createHeightFilter();
                current = this.selected_filters.getCurrentSelections("Height");
                break;
            case "Width":
                fo = FilterDisplayHelper.createWidthFilter();
                current = this.selected_filters.getCurrentSelections("Width");
                break;
            case "Shade":
                fo = FilterDisplayHelper.createShadeFilter();
                current = this.selected_filters.getCurrentSelections("Shade");
                break;
            case "Frost":
                fo = FilterDisplayHelper.createFrostFilter();
                current = this.selected_filters.getCurrentSelections("Frost");
                break;
        }
        this.fos = FilterDisplayHelper.createFilter(this.filter_view, this.filter_cl, fo, false,
                filterGroupChangeListener, this.getContext());
        addCurrentSelections(current);
    }

    private void addCurrentSelections(List<String> current){
        for (int i = 0; i < this.fos.getChildCount(); ++i) {
            Chip c = (Chip)this.fos.getChildAt(i);
            String s = c.getText().toString();
            if(current.contains(s)){
                c.setChecked(true);
            }
        }
    }

    private void updateSelectedFilters(){
        this.selected_filters.editSelectedOptions(this.fos);
    }

    private void updateResults(){
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> found = databaseAccess.searchPlantDatabase(Constants.SPECIES_ID_FIELD,
                this.selected_filters.getOptions_selected(), this.selected_filters.getSearch_tables(),
                this.selected_filters.getSearch_fields(), this.selected_filters.getSelected_filters(),
                HEIGHT, WIDTH);
        databaseAccess.close();
        this.results_cl.removeAllViews();
        plantListDisplayHelper.drawPlantList(root,this, this.results_cl, found);
    }

    private void setCurrentFilter(String current) { this.current_filter=current; }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private FilterOptionSelector.OnClickListener filterGroupChangeListener = new
            FilterOptionSelector.OnClickListener() {
            @Override public void onClick(View v) {
                updateSelectedFilters();
                updateResults();
            }};

    private ChipGroup.OnCheckedChangeListener chipGroupChangeListener = new
        ChipGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(ChipGroup chip_group, int ind) {
                Chip chip = chip_group.findViewById(ind);
                if (chip != null) {
                    for (int i = 0; i < chip_group.getChildCount(); ++i) {
                        chip_group.getChildAt(i).setClickable(true);
                    }
                    chip.setClickable(false);
                    chip.setChecked(true);
                    setCurrentFilter(chip.getText().toString());
                    showCurrentFilter();
                }
            }};

    private View.OnClickListener filterButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            configure_filter_display();
        }
    };

}



package com.example.lazyplant.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.ResultSpaceItemDecoration;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.DisplayHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class BrowseFragment extends Fragment {
    private View root;
    private boolean show_filters = false;
    private View filter_view;
    private ConstraintLayout filter_cl;
    private FilterOptionSelector fos;
    private String current_filter;
    private RecyclerView result_view;
    private RecyclerView.Adapter adapter;
    private List<PlantInfoEntity> plant_list;
    private PlantSearcher plant_searcher;

    final private List<String> FILTER_OPTIONS = Arrays.asList("Type", "Height", "Width", "Shade", "Frost");
    final private int H_MARGIN = 8;
    final private int V_MARGIN = 2;
    final private int HEIGHT_CLOSED = 40;
    final private int HEIGHT_OPEN = 240;
    final private int PLANT_SPACING = 6;
    final private String LOCATION_TEXT = "Postcode: ";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        this.root = inflater.inflate(R.layout.fragment_browse, container, false);
        this.plant_searcher = new PlantSearcher(getContext());
        this.current_filter = this.FILTER_OPTIONS.get(0);
        this.filter_cl = (ConstraintLayout) this.root.findViewById(R.id.browse_filter_area);
        this.plant_list = new ArrayList<>();

        Button reset_button = (Button) root.findViewById(R.id.browse_reset);
        reset_button.setOnClickListener(resetButtonListener);
        Button filters_button = (Button) root.findViewById(R.id.browse_display_filters);
        filters_button.setOnClickListener(filterButtonListener);

        this.result_view = (RecyclerView) root.findViewById(R.id.browse_result_view);
        ResultSpaceItemDecoration rsid = new ResultSpaceItemDecoration(
                DisplayHelper.dpToPx(this.PLANT_SPACING, getContext())) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        };

        this.result_view.addItemDecoration(rsid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.result_view.setLayoutManager(mLayoutManager);
        this.adapter = new BrowseAdapter(this.plant_list, this);
        this.result_view.setAdapter(this.adapter);
        Bundle bundle = this.getArguments();
        if(bundle == null){
            this.setLocation(null);
        }else{
            String postcode = bundle.getString(Constants.LOCATION_TAG);
            this.setLocation(postcode);
            String term = bundle.getString(Constants.NAME_SEARCH_TAG);
            this.plant_searcher.setSearchTerm(term);
        }
        updateResults();
        this.adapter.notifyDataSetChanged();

        return this.root;
    }

    private boolean setLocation(String postcode){
        String location = null;
        if (postcode == null) {
            SharedPreferences pref = this.getContext().getApplicationContext()
                    .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
            location = pref.getString(Constants.DEFAULT_POSTCODE, null);
        } else {
            location = postcode;
        }
        if (location == null){ return false; }
        //TextView location_text = (TextView)this.root.findViewById(R.id.browse_location_text);
        //location_text.setText(LOCATION_TEXT + location);
        return this.plant_searcher.updateLocation(location);
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
            this.showCurrentFilter();
            this.show_filters = true;

            EditText st_edit = (EditText) this.filter_view.findViewById(R.id.browse_top_name_edit);
            String current_term = this.plant_searcher.getSearch_term();
            st_edit.setText(current_term);
            st_edit.setOnFocusChangeListener(searchTermListener);
            EditText pc_edit = (EditText) this.filter_view.findViewById(R.id.browse_top_postcode_edit);
            pc_edit.setOnFocusChangeListener(postcodeListener);
        }
        this.filter_cl.setLayoutParams(params);
    }

    /**
     * Display the current filter option on screen. This depends on 'this.current_filter'.
     */
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
                current = this.plant_searcher.getCurrentSelections("Type");
                break;
            case "Height":
                fo = FilterDisplayHelper.createHeightFilter();
                current = this.plant_searcher.getCurrentSelections("Height");
                break;
            case "Width":
                fo = FilterDisplayHelper.createWidthFilter();
                current = this.plant_searcher.getCurrentSelections("Width");
                break;
            case "Shade":
                fo = FilterDisplayHelper.createShadeFilter();
                current = this.plant_searcher.getCurrentSelections("Shade");
                break;
            case "Frost":
                fo = FilterDisplayHelper.createFrostFilter();
                current = this.plant_searcher.getCurrentSelections("Frost");
                break;
        }
        this.fos = FilterDisplayHelper.createFilter(this.filter_view, this.filter_cl, fo, true,
                filterGroupChangeListener, this.getContext());
        addCurrentSelections(current);
    }

    /**
     * This shows the current selection on the filter displays.
     * @param current
     */
    private void addCurrentSelections(List<String> current){
        for (int i = 0; i < this.fos.getChildCount(); ++i) {
            Chip c = (Chip)this.fos.getChildAt(i);
            String s = c.getText().toString();
            if(current.contains(s)){
                c.setChecked(true);
            }
        }
    }

    /**
     * Update the results fragment to show the selected plants based on the options.
     */
    private void updateResults() {
        this.plant_list.clear();
        this.plant_list.addAll(this.plant_searcher.getResults());
        this.adapter.notifyDataSetChanged();
        TextView error_text = (TextView) root.findViewById(R.id.browse_error_text);
        if(this.plant_list.size() <= 0){
            error_text.setText("No plants found.");
        }else{
            error_text.setText("");
        }
    }

    private void changeSearchTerm(String search_term){
        this.plant_searcher.setSearchTerm(search_term);
        updateResults();
    }

    private void changeLocation(EditText et, String postcode){
        String pc = et.getText().toString();
        if(pc.matches("[0-9][0-9][0-9][0-9]")){
            ClimateZoneGetter czg = new ClimateZoneGetter();
            int zone = czg.getZone(pc);
            if (zone != -1){
                setLocation(postcode);
                updateResults();
            }else{
                Toast toast = Toast.makeText(getActivity(),
                        "Sorry, your postcode is invalid.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getActivity(),
                    "Sorry, your postcode is invalid.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void updateSelectedFilters(){
        this.plant_searcher.editOptions(this.fos);
    }

    private void setCurrentFilter(String current) { this.current_filter=current; }

    private void resetFilters(){
        this.plant_searcher.clearFilters();
        this.plant_searcher.updateZone(this.plant_searcher.getZone());
        if(this.fos != null){
            this.fos.clear();
        }
        this.updateResults();
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
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
        }};

    private View.OnClickListener resetButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetFilters();
        }
    };

    private View.OnFocusChangeListener searchTermListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                changeSearchTerm(((EditText)view).getText().toString());
            }
        }};

    private View.OnFocusChangeListener postcodeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                changeLocation((EditText)view, ((EditText)view).getText().toString());
            }
        }};

}

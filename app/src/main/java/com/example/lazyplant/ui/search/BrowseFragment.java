package com.example.lazyplant.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.ResultSpaceItemDecoration;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.ui.DisplayHelper;
import com.example.lazyplant.ui.PlantSearchViewModel;
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
    private PlantSearchViewModel model;

    final private List<String> FILTER_OPTIONS = Arrays.asList("Type", "Height", "Width", "Shade", "Frost");
    final private int FILTER_H_MARGIN = 8;
    final private int FILTER_V_MARGIN = 2;
    final private int HEIGHT_CLOSED = 48;
    final private int HEIGHT_OPEN = 252;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_browse, container, false);
        this.current_filter = this.FILTER_OPTIONS.get(0);
        this.filter_cl = (ConstraintLayout) this.root.findViewById(R.id.browse_filter_area);
        this.model.plant_list = new ArrayList<>();
        if(this.model.plant_searcher == null){
            this.model.plant_searcher = new PlantSearcher(getContext());
        }

        Button reset_button = (Button) root.findViewById(R.id.browse_reset);
        reset_button.setOnClickListener(resetButtonListener);
        ImageButton filters_button = (ImageButton) root.findViewById(R.id.browse_display_filters);
        filters_button.setOnClickListener(filterButtonListener);

        this.result_view = (RecyclerView) root.findViewById(R.id.browse_result_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.result_view.setLayoutManager(mLayoutManager);
        this.adapter = new BrowseAdapter(this.model.plant_list, this);
        this.result_view.setAdapter(this.adapter);
        this.model.plant_searcher = new PlantSearcher(getContext());

        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        String postcode = pref.getString(Constants.SHARED_PREFERENCE_POSTCODE, null);
        if(postcode != null){
            this.model.postcode = postcode;
            this.setLocation(this.model.postcode);
        }

        Bundle bundle = this.getArguments();
        if(bundle.getString(Constants.CATEGORY_TYPE) != null){
            this.model.plant_searcher.setCategory(bundle.getString(Constants.CATEGORY_TYPE),
                    bundle.getString(Constants.CATEGORY_TERM));
        }else if (bundle.getString(Constants.NAME_SEARCH_TAG) != null) {
            String term = bundle.getString(Constants.NAME_SEARCH_TAG);
            this.model.plant_searcher.setSearchTerm(term);
        }
        updateResults();
        this.adapter.notifyDataSetChanged();

        return this.root;
    }

    /**
     * Update the results fragment to show the selected plants based on the options.
     */
    private void updateResults() {
        this.model.plant_list.clear();
        this.model.plant_list.addAll(this.model.plant_searcher.getSearchResult());
        this.adapter.notifyDataSetChanged();
        TextView error_text = (TextView) root.findViewById(R.id.browse_error_text);
        if(this.model.plant_list.size() <= 0){
            error_text.setText("No plants found.");
        }else{
            error_text.setText("");
        }
    }

    private boolean setLocation(String postcode){
        if (postcode == null){ return false; }
        return this.model.plant_searcher.updateLocation(postcode);
    }

    private void configure_filter_display(){
        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) this.filter_cl.getLayoutParams();
        int h = this.dpToPx(this.FILTER_H_MARGIN, this.getContext());
        int v = this.dpToPx(this.FILTER_V_MARGIN, this.getContext());
        final Context context = this.getContext();

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
            this.setDisplayedFilter(this.current_filter, cg);
            this.showCurrentFilter();
            this.show_filters = true;

            final EditText st_edit = (EditText) this.filter_view.findViewById(R.id.get_user_name_edit);
            String current_term = this.model.plant_searcher.getSearch_term();
            st_edit.setText(current_term);
            st_edit.setOnFocusChangeListener(searchTermListener);
            final EditText pc_edit = (EditText) this.filter_view.findViewById(R.id.get_user_postcode_edit);
            pc_edit.setText(this.model.postcode);
            pc_edit.setOnFocusChangeListener(postcodeListener);

            st_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                        DisplayHelper.hideKeyboard(context, root);
                        changeLocation(st_edit.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
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
                current = this.model.plant_searcher.getCurrentSelections("Type");
                break;
            case "Height":
                fo = FilterDisplayHelper.createHeightFilter();
                current = this.model.plant_searcher.getCurrentSelections("Height");
                break;
            case "Width":
                fo = FilterDisplayHelper.createWidthFilter();
                current = this.model.plant_searcher.getCurrentSelections("Width");
                break;
            case "Shade":
                fo = FilterDisplayHelper.createShadeFilter();
                current = this.model.plant_searcher.getCurrentSelections("Shade");
                break;
            case "Frost":
                fo = FilterDisplayHelper.createFrostFilter();
                current = this.model.plant_searcher.getCurrentSelections("Frost");
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

    private void changeSearchTerm(String search_term){
        this.model.plant_searcher.setSearchTerm(search_term);
        updateResults();
    }

    private void changeLocation(String location){
        this.model.postcode = location;
        if(this.model.postcode.matches("[0-9][0-9][0-9][0-9]")){
            ClimateZoneGetter czg = new ClimateZoneGetter();
            int zone = czg.getZone(this.model.postcode);
            if (zone != -1){
                setLocation(this.model.postcode);
                updateResults();
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
    }

    private void updateSelectedFilters(){
        this.model.plant_searcher.editOptions(this.fos);
    }

    private void setCurrentFilter(String current) { this.current_filter=current; }

    private void setDisplayedFilter(String filter, ChipGroup chip_group){
        for (int i = 0; i < chip_group.getChildCount(); ++i) {
            Chip c = (Chip)chip_group.getChildAt(i);
            if(c.getText().toString().equals(filter)) {
                c.setChecked(true);
            }
        }

    }

    private void resetFilters(){
        this.model.plant_searcher.clearFilters();
        this.model.plant_searcher.updateZone(this.model.plant_searcher.getZone());
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
                ((Chip)v).setTextColor(getResources().getColor(R.color.pureWhite));
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
                DisplayHelper.hideKeyboard(view.getContext(), view);
                changeSearchTerm(((EditText)view).getText().toString());
            }
        }};

    private View.OnFocusChangeListener postcodeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                DisplayHelper.hideKeyboard(view.getContext(), view);
                changeLocation(((EditText)view).getText().toString());
            }
        }};

    @Override
    public void onDetach(){
        super.onDetach();
        if(filter_cl != null){
            this.filter_cl.removeAllViews();
            ((ViewManager)this.filter_cl.getParent()).removeView(this.filter_cl);
        }
        if(this.root != null){
            ((ViewManager)this.root).removeView(this.root);
        }
    }

}

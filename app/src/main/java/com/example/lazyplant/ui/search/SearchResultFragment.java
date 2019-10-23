package com.example.lazyplant.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;

import java.util.List;

public class SearchResultFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.example.lazyplant.ui.search.MESSAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_results, container, false);
        //Get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        //Get search term data
        Bundle bundle = this.getArguments();
        SelectedFiltersEntity selected_filters = (SelectedFiltersEntity) bundle.getSerializable(SelectedFiltersEntity.TAG);
        int current_display_option = Integer.parseInt(bundle.getString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL));

        //Search through database with each filter
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> found = databaseAccess.searchPlantDatabase(Constants.SPECIES_ID_FIELD,
                selected_filters.getOptions_selected(), selected_filters.getSearch_tables(),
                selected_filters.getSearch_fields(), selected_filters.getSelected_filters(),
                FilterDisplayHelper.HEIGHT, FilterDisplayHelper.WIDTH);
        databaseAccess.close();

        //Get next filter
        current_display_option = FilterDisplayHelper.getNextFilter(current_display_option);

        ConstraintLayout cl = (ConstraintLayout) root.findViewById(R.id.search_result_constraint_layout);
        //Process results
        if(found.size() == 0) {
            // No result found screen
            Toast.makeText(getActivity(), "Yo yo yo! Sorry bro! We didn't find nothing",
                    Toast.LENGTH_LONG).show();
        } else if (current_display_option == FilterDisplayHelper.FN_END) {
            // All filters used
            plantListDisplayHelper.drawPlantList(root,this, cl, found);
        } else if (found.size() > FilterDisplayHelper.MAX_NUM_SEARCHES_DISPLAY) {
            // More filters, change to show more searches
            bundle.putString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL, String.valueOf(current_display_option));
            SearchFragment sf = new SearchFragment();
            sf.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, sf).commit();
        } else {
            // Display options
            plantListDisplayHelper.drawPlantList(root,this, cl, found);
        }
        
        return root;
    }

}

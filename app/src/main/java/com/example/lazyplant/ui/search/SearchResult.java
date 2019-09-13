package com.example.lazyplant.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchResult extends Fragment {

    public static final String EXTRA_MESSAGE = "com.example.lazyplant.ui.search.MESSAGE";
    private static final int MIN_EDIT_DISTANCE = 1;
    private static final double EDIT_LENGTH_PERCENTAGE = 0.40;

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

        //Process results
        if(found.size() == 0) {
            // No result found screen
            Toast.makeText(getActivity(), "Yo yo yo! Sorry bro! We didn't find nothing",
                    Toast.LENGTH_LONG).show();
        } else if (current_display_option == FilterDisplayHelper.FN_END) {
            // All filters used
            drawMaPlants(root, found, height, width);
        } else if (found.size() > FilterDisplayHelper.MAX_NUM_SEARCHES_DISPLAY) {
            // More filters, change to show more searches
            bundle.putString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL, String.valueOf(current_display_option));
            SearchFragment sf = new SearchFragment();
            sf.setArguments(bundle);
            getFragmentManager() .beginTransaction().replace(R.id.nav_host_fragment, sf).commit();
        } else {
                // Display options
                drawMaPlants(root, found, height, width);
        }

        return root;
    }

    private void drawMaPlants(View root, List<String> ids, int height, int width) {
        final int padding = 40;
        for (int i = 0; i < ids.size(); i++) {
            String pid = ids.get(i);
            final Button myButton = new Button(root.getContext());
            myButton.setText(pid);
            final String bid = pid;
            myButton.setId(i+1);
            final String id = pid;
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //open details page with id
                    Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, bid);
                    startActivity(intent);
                }
            });
            myButton.setPadding(20, 0, 20, 0);
            RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.linear_search_results);
            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams((int)(Math.round(width*0.45)), height/5);
            if (i == 0) {
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            } else if (i % 2 == 1) {
                buttonParams.addRule(RelativeLayout.RIGHT_OF, i);
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            } else if (i % 2 == 0) {
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            }
            relativeLayout.addView(myButton, buttonParams);
        }
    }

    private List<String> searchForName(List<String> search_space, String search_term) {
        Set<String> e = new HashSet<String>();
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        for (String i : databaseAccess.getAllFieldFromTable("scientific_name", "plant_data")){
            if(namesSimilar(search_term, i)){
            e.add(databaseAccess.searchOnCondition(Constants.SPECIES_ID_FIELD, "plant_data",
                    "scientific_name = \"" + i + "\"").get(0)); }
        }
        for (String i : databaseAccess.getAllFieldFromTable("common_name", "plant_data")){
            if(namesSimilar(search_term, i)){
                e.add(databaseAccess.searchOnCondition(Constants.SPECIES_ID_FIELD, "plant_data",
                        "common_name = \"" + i + "\"").get(0)); }
        }
        for (String i : databaseAccess.getAllFieldFromTable("name", "alt_names")){
            if(namesSimilar(search_term, i)){
                e.add(databaseAccess.searchOnCondition(Constants.SPECIES_ID_FIELD, "alt_names",
                        "name = \"" + i + "\"").get(0)); }
        }
        databaseAccess.close();
        List<String> found = new ArrayList<>();
        for (String i : e){
            found.add(i);
        }
        return found;
    }

    /**
     * Tells whether a user input is close enough to a name. Essentially it checks similarity.
     * @param name Name of something
     * @param userInput User search input
     * @return Whether the two provided names are similar.
     */
    private static boolean namesSimilar(String name, String userInput) {
        String[] s1 = name.toUpperCase().split("\\s+");
        String[] s2 = userInput.toUpperCase().split("\\s+");
        for (String word1 : s1) {
            for (String word2 : s2) {
                if (stringSimilarity(word1, word2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Uses edit distance to determine if two strings are similar.
     * @return True if they are similar.
     */
    private static boolean stringSimilarity(String n1, String n2) {
        int minLen = Math.min(n1.length(), n2.length());
        int minEdit = Math.max(MIN_EDIT_DISTANCE, (int)Math.ceil(minLen * EDIT_LENGTH_PERCENTAGE)); //Calculate min edit distance to be similar. Is larger for longer strings.
        if (editDistance(n1, n2) <= minEdit) {
            return true;
        }
        return false;
    }

    /**
     * Calculates edit distance
     * @author gabhi/gist:11243437
     */
    private static int editDistance(String s1, String s2){
        int edits[][]=new int[s1.length()+1][s2.length()+1];
        for(int i=0;i<=s1.length();i++)
            edits[i][0]=i;
        for(int j=1;j<=s2.length();j++)
            edits[0][j]=j;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(
                        edits[i-1][j]+1,
                        Math.min(
                                edits[i][j-1]+1,
                                edits[i-1][j-1]+u
                        )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }



}

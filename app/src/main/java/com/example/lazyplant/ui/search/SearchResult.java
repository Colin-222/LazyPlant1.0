package com.example.lazyplant.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;

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
        ArrayList<String> curr_options_selected = bundle.getStringArrayList(FilterOptionSelector.CATEGORY_LABEL);
        ArrayList<String> curr_search_tables = bundle.getStringArrayList(FilterOptionSelector.TABLE_LABEL);
        ArrayList<String> curr_search_fields = bundle.getStringArrayList(FilterOptionSelector.FIELD_LABEL);
        List<List<String>> curr_selected_filters = new ArrayList<>();
        for (String i : curr_options_selected){
            curr_selected_filters.add(bundle.getStringArrayList(i));
        }
        int current_display_option = Integer.parseInt(bundle.getString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL));

        //Search through database with each filter
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> found = FilterDisplayHelper.searchPlantDatabase(databaseAccess, "species_id",
                curr_options_selected, curr_search_tables, curr_search_fields, curr_selected_filters);
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

        //check for name
        /*if(join_lists.size() == 0){
            //text only
            DbAccess da = DbAccess.getInstance(getContext());
            da.open();
            List<String> all_id = da.getFromPlantData("id");
            da.close();
            found = searchForName(all_id, search_term);
        } else {
            found = new ArrayList<>(join_lists.get(0));
            for (int i = 0; i < join_lists.size(); i++){
                found.retainAll(join_lists.get(i));
            }
            if(!search_term.equals("")){
                //filter and text, else if skipped it's just filter
                found = searchForName(found, search_term);
            }
        }*/

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
        for (String i : databaseAccess.getFromPlantData("scientific_name")){
            if(namesSimilar(search_term, i)){
            e.add(databaseAccess.searchOnCondition("id", "plant_data",
                    "scientific_name = \"" + i + "\"").get(0)); }
        }
        for (String i : databaseAccess.getFromPlantData("common_name")){
            if(namesSimilar(search_term, i)){
                e.add(databaseAccess.searchOnCondition("id", "plant_data",
                        "common_name = \"" + i + "\"").get(0)); }
        }
        for (String i : databaseAccess.getAltNames()){
            if(namesSimilar(search_term, i)){
                e.add(databaseAccess.searchOnCondition("species_id", "alt_names",
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

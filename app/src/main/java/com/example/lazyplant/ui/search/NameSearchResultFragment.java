package com.example.lazyplant.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.ui.plantListDisplayHelper;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NameSearchResultFragment extends Fragment {
    private static final int MIN_EDIT_DISTANCE = 1;
    private static final double EDIT_LENGTH_PERCENTAGE = 0.40;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search_results, container, false);

        Bundle bundle = this.getArguments();
        String search_term = bundle.getString(Constants.NAME_SEARCH_TAG);
        List<String> found = searchForName(search_term);
        ConstraintLayout cl = (ConstraintLayout) root.findViewById(R.id.search_result_constraint_layout);
        plantListDisplayHelper.drawPlantList(root,this, cl, found);

        return root;
    }


    private List<String> searchForName(String search_term) {
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



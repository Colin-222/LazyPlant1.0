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
import java.util.List;

public class SearchResult extends Fragment {

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
        String search_term = bundle.getString("search_term");
        ArrayList<String> type_search = bundle.getStringArrayList("list");
        ArrayList<String> shade_search = bundle.getStringArrayList("shade");
        ArrayList<String> frost_search = bundle.getStringArrayList("frost");
        ArrayList<String> zone_search = bundle.getStringArrayList("zone");
        String height_search = bundle.getString("height");
        String width_search = bundle.getString("width");
        //Construct sql query
        //Get union select within filter categories
        String conditions = "";
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> l_type = databaseAccess.getIdByCondition("type",
                getConditions("type", type_search));
        List<String> l_shade = databaseAccess.getIdByCondition("light",
                getConditions("light_required", shade_search));
        List<String> l_frost = databaseAccess.getIdByCondition("frost",
                getConditions("frost_tolerance", frost_search));
        List<String> l_zone = databaseAccess.getIdByCondition("zone",
                getConditions("climate_zone", zone_search));
        List<String> l_height = new ArrayList<>();
        if(height_search.equals("S")){
            l_height = databaseAccess.getIdByConditionPd("plant_data", " OR height_upper < 1");
        }else if(height_search.equals("M")){
            l_height = databaseAccess.getIdByConditionPd("plant_data",
                    " OR (height_upper >= 1 AND height_upper < 10)");
        }else if(height_search.equals("L")){
            l_height = databaseAccess.getIdByConditionPd("plant_data", " OR height_upper >= 10");
        }
        List<String> l_width = new ArrayList<>();
        if(width_search.equals("S")){
            l_width = databaseAccess.getIdByConditionPd("plant_data", " OR width_upper < 1");
        }else if(width_search.equals("M")){
            l_width = databaseAccess.getIdByConditionPd("plant_data",
                    " OR (width_upper >= 1 AND width_upper < 10)");
        }else if(width_search.equals("L")){
            l_width = databaseAccess.getIdByConditionPd("plant_data", " OR width_upper >= 10");
        }
        databaseAccess.close();
        //AND results
        List<List<String>> join_lists = new ArrayList<List<String>>();
        if(type_search.size() != 0){ join_lists.add(l_type); }
        if(shade_search.size() != 0){ join_lists.add(l_shade); }
        if(frost_search.size() != 0){ join_lists.add(l_frost); }
        if(zone_search.size() != 0){ join_lists.add(l_zone); }
        if(!height_search.equals("")) { join_lists.add(l_height); }
        if(!width_search.equals("")) { join_lists.add(l_width); }
        List<String> found = new ArrayList<>();
        if(join_lists.size() == 0){
            //text only
            
        } else {
            found = new ArrayList<>(join_lists.get(0));
            for (int i = 0; i < join_lists.size(); i++){
                found.retainAll(join_lists.get(i));
            }
            if(!search_term.equals("")){
                //filter and text, else if skipped it's just filter
            }
        }
        drawMaPlants(root, found, height, width);
        return root;
    }

    private String getConditions(String category, List<String> checks){
        String conditions = "";
        for (String i : checks){
            conditions += " OR " + category + "=\"" + i + "\"";
        }
        return conditions;
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

}

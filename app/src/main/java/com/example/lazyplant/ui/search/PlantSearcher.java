package com.example.lazyplant.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static android.content.Context.MODE_PRIVATE;

public class PlantSearcher {

    Context context;
    private SelectedFiltersEntity selected_filters;
    private String search_term;
    private int zone;
    private static final int MIN_EDIT_DISTANCE = 1;
    private static final double EDIT_LENGTH_PERCENTAGE = 0.25;
    final private String HEIGHT = "height";
    final private String WIDTH = "width";
    private String category_type;
    private String category_term;

    public PlantSearcher(Context context) {
        this.context = context;
        this.selected_filters = new SelectedFiltersEntity();
        this.search_term = "";
        this.category_type = null;
        this.category_term = null;
    }

    public void setCategory(String type, String term){
        this.category_type = type;
        this.category_term = term;
    }

    public void clearFilters(){
        this.selected_filters = new SelectedFiltersEntity();
        this.setSearchTerm("");
    }

    public void setSearchTerm(String term){
        this.search_term = term;
    }

    public boolean updateZone(int zone){
        FilterOptionEntity fo = FilterDisplayHelper.createZoneFilter();
        FilterOptionSelector location_fos = FilterDisplayHelper.createFilter(fo, context);
        if(zone >= 1 && zone <= 7){
            ((Chip)location_fos.getChildAt(zone - 1)).setChecked(true);
            this.editOptions(location_fos);
        }else{
            return false;
        }
        return true;
    }

    public boolean updateLocation(String location){
        ClimateZoneGetter czg = new ClimateZoneGetter();
        try {
            this.zone = czg.getZone(location);
        }catch (IllegalArgumentException e) {
            return false;
        }
        return this.updateZone(this.zone);
    }

    public List<PlantInfoEntity> getSearchResult(){
        List<PlantInfoEntity> pl = new ArrayList<>();
        DbAccess databaseAccess = DbAccess.getInstance(context);
        databaseAccess.open();
        String command = databaseAccess.getSearchCommand(Constants.SPECIES_ID_FIELD,
                this.selected_filters.getOptions_selected(), this.selected_filters.getSearch_tables(),
                this.selected_filters.getSearch_fields(), this.selected_filters.getSelected_filters(),
                HEIGHT, WIDTH);
        if(this.category_term != null){
            command += " INTERSECT ";
            if(this.category_type.equals(Constants.GENERAL_PLANTS_TAG)){
                command += databaseAccess.getPropertyCommand(this.category_term);
            }else if(this.category_type.equals(Constants.HABITAT_TAG)){
                command += databaseAccess.getAnimalCommand(this.category_term);
            }else if(this.category_type.equals(Constants.EDIBLE_TAG)){
                command += databaseAccess.getEdibleCommand(this.category_term);
            }
        }
        List<String> found = databaseAccess.runCommand(command);
        databaseAccess.close();
        String term = this.getSearch_term();
        if(!(term == null || term.equals(""))){
            List<String> found_name = this.searchForName(term);
            found.retainAll(found_name);
            Set<String> tmp = new HashSet<String>(found);
            found = new ArrayList<>(tmp);
        }
        databaseAccess.open();
        for (String id : found) {
            pl.add(databaseAccess.getShortPlantInfo(id));
        }
        databaseAccess.close();
        Collections.sort(pl);
        return pl;
    }

    public List<PlantInfoEntity> getResults(){
        List<PlantInfoEntity> pl = new ArrayList<>();
        DbAccess databaseAccess = DbAccess.getInstance(context);
        databaseAccess.open();
        List<String> found = databaseAccess.searchPlantDatabase(Constants.SPECIES_ID_FIELD,
                this.selected_filters.getOptions_selected(), this.selected_filters.getSearch_tables(),
                this.selected_filters.getSearch_fields(), this.selected_filters.getSelected_filters(),
                HEIGHT, WIDTH);
        databaseAccess.close();
        String term = this.getSearch_term();
        if(!(term == null || term.equals(""))){
            List<String> found_name = this.searchForName(term);
            found.retainAll(found_name);
            Set<String> tmp = new HashSet<String>(found);
            found = new ArrayList<>(tmp);
        }
        databaseAccess.open();
        for (String id : found) {
            pl.add(databaseAccess.getShortPlantInfo(id));
        }
        databaseAccess.close();
        Collections.sort(pl);
        return pl;
    }

    public List<String> getCurrentSelections(String category){
        return this.selected_filters.getCurrentSelections(category);
    }

    public void editOptions(FilterOptionSelector fos){
        this.selected_filters.editSelectedOptions(fos);
    }

    public List<String> searchForName(String search_term) {
        Set<String> e = new HashSet<String>();
        DbAccess databaseAccess = DbAccess.getInstance(context);
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

    public String getSearch_term() {
        return search_term;
    }

    public void setSearch_term(String search_term) {
        this.search_term = search_term;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

}

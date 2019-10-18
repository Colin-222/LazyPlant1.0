package com.example.lazyplant.ui.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores filter options that have been selected by a user. Can be passed around as an
 * intent.
 */
public class SelectedFiltersEntity implements Serializable {

    final static public String TAG = "selected_filters";
    private List<String> options_selected;
    private List<String> search_tables;
    private List<String> search_fields;
    private List<List<String>> selected_filters;
    private List<List<String>> selected_filters_names;

    public SelectedFiltersEntity() {
        this.options_selected = new ArrayList<>();
        this.search_tables = new ArrayList<>();
        this.search_fields = new ArrayList<>();
        this.selected_filters = new ArrayList<>();
        this.selected_filters_names = new ArrayList<>();
    }

    /**
     * Gets the currently selection options for a filter.
     * @param category The name of the category whose options we want to obtain.
     * @return A list containing currently selected options.
     */
    public List<String> getCurrentSelections(String category){
        int ind = this.options_selected.indexOf(category);
        if (ind == -1){
            return new ArrayList<>();
        }
        return selected_filters_names.get(ind);
    }

    /**
     * Edit selected options from a filter.
     * @param filter A FilterOptionSelector that represent the filter options currently shown
     *                on the screen.
     */
    public void editSelectedOptions(FilterOptionSelector filter){
        ArrayList<String> x = (ArrayList) filter.getSearchOptions();
        ArrayList<String> x_name = (ArrayList) filter.getSearchOptionNames();
        int ind = this.options_selected.indexOf(filter.getCategory());
        if(x.size() > 0){ //Make sure there's at least one option selected
            if (ind == -1){ //Hasn't been added yet
                this.options_selected.add(filter.getCategory());
                this.search_tables.add(filter.getSearch_table());
                this.search_fields.add(filter.getField());
                this.selected_filters.add(x);
                this.selected_filters_names.add(x_name);
            }else{ //Already exists, just update
                this.selected_filters.set(ind, x);
                this.selected_filters_names.set(ind, x_name);
            }
        }else{ //Nothing selected
            if (ind != -1){ //Remove it if there's nothing there
                this.options_selected.remove(ind);
                this.search_tables.remove(ind);
                this.search_fields.remove(ind);
                this.selected_filters.remove(ind);
                this.selected_filters_names.remove(ind);
            }
        }
    }

    /**
     * Adds the selected options from a filter.
     * @param filters A FilterOptionSelector that represent the filter options currently shown
     *                on the screen.
     * @return True if something is added, false if it's empty. Essentially it's false if the user
     * doesn't select anything.
     */
    public boolean addOptionsFromFilter(List<FilterOptionSelector> filters){
        boolean something_selected = false;
        for (FilterOptionSelector fos : filters){
            ArrayList<String> x = (ArrayList) fos.getSearchOptions();
            if(x.size() > 0){ //Make sure there's at least one option selected
                this.options_selected.add(fos.getCategory());
                this.search_tables.add(fos.getSearch_table());
                this.search_fields.add(fos.getField());
                this.selected_filters.add(x);
                something_selected = true;
            }
        }
        return something_selected;
    }

    public List<String> getOptions_selected() {
        return options_selected;
    }

    public void setOptions_selected(ArrayList<String> options_selected) {
        this.options_selected = options_selected;
    }

    public List<String> getSearch_tables() {
        return search_tables;
    }

    public void setSearch_tables(ArrayList<String> search_tables) {
        this.search_tables = search_tables;
    }

    public List<String> getSearch_fields() {
        return search_fields;
    }

    public void setSearch_fields(ArrayList<String> search_fields) {
        this.search_fields = search_fields;
    }

    public List<List<String>> getSelected_filters() {
        return selected_filters;
    }

    public void setSelected_filters(ArrayList<List<String>> selected_filters) {
        this.selected_filters = selected_filters;
    }
}

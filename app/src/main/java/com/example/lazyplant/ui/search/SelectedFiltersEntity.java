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

    public SelectedFiltersEntity() {
        this.options_selected = new ArrayList<>();
        this.search_tables = new ArrayList<>();
        this.search_fields = new ArrayList<>();
        this.selected_filters = new ArrayList<>();
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

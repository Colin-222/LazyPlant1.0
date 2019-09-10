package com.example.lazyplant.ui.search;

import java.util.List;

/**
 * Entity that stores the details of a filter option.
 */
public class FilterOptionEntity {
    final private String category;
    final private String description;
    final private String field;
    final private String search_table;
    private List<String> options;
    private List<String> option_descriptions;

    public String getField() {
        return field;
    }

    public String getSearch_table() {
        return search_table;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<String> getOption_descriptions() {
        return option_descriptions;
    }

    //public FilterOptionEntity() { }

    public FilterOptionEntity(String category, String desc, List<String> options,
                              List<String> options_desc, String table, String field) {
        this.category = category;
        this.description = desc;
        this.options = options;
        this.option_descriptions = options_desc;
        if(options.size() != options_desc.size()){
            throw new IllegalArgumentException("options and options_desc should be the same size.");
        }
        this.search_table = table;
        this.field = field;
    }

}

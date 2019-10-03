package com.example.lazyplant.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;

import com.example.lazyplant.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents a selector, i.e a chip group in the app, that can be interacted with to select
 * filter options by the user. Must be displayed in UI or user can't interact with it.
 */
public class FilterOptionSelector extends ChipGroup{

    final static public String CATEGORY_LABEL = "categories";
    final static public String TABLE_LABEL = "tables";
    final static public String FIELD_LABEL = "fields";
    private String category;
    private String description;
    private String search_table;
    private String field;
    private int h_margin;
    private int v_margin;
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

    public void setCategory(String category) { this.category = category; }

    public void setDescription(String description) { this.description = description; }

    public void setSearch_table(String search_table) { this.search_table = search_table; }

    public void setField(String field) { this.field = field; }

    public void setOptions(List<String> options) { this.options = options; }

    public void setOption_descriptions(List<String> option_descriptions) { this.option_descriptions = option_descriptions; }

    public void setH_margin(int h_margin) { this.h_margin = h_margin; }

    public void setV_margin(int v_margin) { this.v_margin = v_margin; }

    public FilterOptionSelector(Context context, AttributeSet as){
        super(context, as);
    }

    /**
     * Creates the filter option selector. Displays it in a layout but doesn't specify where. Any
     * layout can be used and it can be displayed anywhere.
     * @param category Filter category name.
     * @param desc A description of what the filter is.
     * @param options A list of names for filter options that should be displayed to the user. This
     *               is consumer-facing.
     * @param options_desc A list of names showing what the filter options are named according to
     *                     the plant database. These should be values of fields in the databases.
     * @param table The name of the table that contains the information related to this filter.
     * @param field The field in a table that this information represents.
     * @param hm The horizontal margin that should be used when creating the chip group, i.e
     *           spacing between chips.
     * @param vm The vertical margin that should be used when creating the chip group, i.e
     *           spacing between chips.
     * @param single_selection True if the user should only be able to select a single option.
     * @param context Context this should be displayed in. Should use 'this.getContext()' in
     *                fragments to get this value.
     */
    public FilterOptionSelector(String category, String desc, List<String> options, List<String> options_desc,
                                String table, String field, int hm, int vm, boolean single_selection, Context context) {
        super(context);
        this.setId(ViewCompat.generateViewId());
        this.category = category;
        this.description = desc;
        this.options = options;
        this.option_descriptions = options_desc;
        if(options.size() != options_desc.size()){
            throw new IllegalArgumentException("options and options_desc should be the same size.");
        }
        this.search_table = table;
        this.field = field;
        this.h_margin = hm;
        this.v_margin = vm;
        this.setSingleSelection(single_selection);
        init();
    }

    /**
     * Creates the filter option selector. Displays it in a layout but doesn't specify where. Any
     * layout can be used and it can be displayed anywhere.
     * @param category Filter category name.
     * @param desc A description of what the filter is.
     * @param options A list of names for filter options that should be displayed to the user. This
     *               is consumer-facing.
     * @param options_desc A list of names showing what the filter options are named according to
     *                     the plant database. These should be values of fields in the databases.
     * @param table The name of the table that contains the information related to this filter.
     * @param field The field in a table that this information represents.
     * @param hm The horizontal margin that should be used when creating the chip group, i.e
     *           spacing between chips.
     * @param vm The vertical margin that should be used when creating the chip group, i.e
     *           spacing between chips.
     * @param single_selection True if the user should only be able to select a single option.
     * @param context Context this should be displayed in. Should use 'this.getContext()' in
     *                fragments to get this value.
     */
    public FilterOptionSelector(String category, String desc, List<String> options, List<String> options_desc,
                                String table, String field, int hm, int vm, boolean single_selection,
                                OnClickListener on_click, Context context) {
        super(context);
        this.setId(ViewCompat.generateViewId());
        this.category = category;
        this.description = desc;
        this.options = options;
        this.option_descriptions = options_desc;
        if(options.size() != options_desc.size()){
            throw new IllegalArgumentException("options and options_desc should be the same size.");
        }
        this.search_table = table;
        this.field = field;
        this.h_margin = hm;
        this.v_margin = vm;
        this.setSingleSelection(single_selection);
        init(on_click);
    }

    /**
     * This class creates the chip groups and all contained chips with onClickListeners.
     */
    public void init(OnClickListener on_click){
        for (String i : this.options){
            final Chip x = new Chip(this.getContext());
            x.setChipDrawable(ChipDrawable.createFromResource(this.getContext(), R.xml.chip_filter));
            ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(this.h_margin, this.v_margin, this.h_margin, this.v_margin);
            x.setText(i);
            x.setCheckedIconVisible(false);
            x.setChipBackgroundColorResource(R.color.filter_chip_bg);
            x.setOnClickListener(on_click);
            x.setTextColor(this.getResources().getColorStateList(R.color.chip_text));
            this.addView(x, params);
        }
    }

    /**
     * This class creates the chip groups and all contained chips.
     */
    public void init(){
        for (String i : this.options){
            final Chip x = new Chip(this.getContext());
            x.setChipDrawable(ChipDrawable.createFromResource(this.getContext(), R.xml.chip_filter));
            ChipGroup.LayoutParams params = new ChipGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(this.h_margin, this.v_margin, this.h_margin, this.v_margin);
            x.setText(i);
            this.addView(x, params);
        }
    }

    /**
     * This obtains the selected options.
     * @return A list containing which options were selected.
     */
    public List<String> getSearchOptions(){
        List<String> l = new ArrayList<>();
        for (int i = 0; i < this.getChildCount(); i++){
            Chip c = (Chip) this.getChildAt(i);
            if (c.isChecked()){
                int index = this.getOptions().indexOf(c.getText());
                l.add(this.getOption_descriptions().get(index));
            }
        }
        return l;
    }

    /**
     * This obtains the names of the selected options.
     * @return A list containing which the names of the options which were selected.
     */
    public List<String> getSearchOptionNames(){
        List<String> l = new ArrayList<>();
        for (int i = 0; i < this.getChildCount(); i++){
            Chip c = (Chip) this.getChildAt(i);
            if (c.isChecked()){
                int index = this.getOptions().indexOf(c.getText());
                l.add(this.getOptions().get(index));
            }
        }
        return l;
    }

    /**
     * Turns all chips to be off.
     */
    public void clear(){
        for (int i = 0; i < this.getChildCount(); i++){
            Chip c = (Chip) this.getChildAt(i);
            c.setChecked(false);
        }
    }

}


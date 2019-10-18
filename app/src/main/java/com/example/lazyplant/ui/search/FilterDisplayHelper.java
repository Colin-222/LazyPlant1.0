package com.example.lazyplant.ui.search;

import android.content.Context;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.lazyplant.ui.DisplayHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Contains function and variables to add is displaying filter options to the user.
 */
public class FilterDisplayHelper extends DisplayHelper {
    public FilterDisplayHelper() { }

    final static private int H_MARGIN = 6;
    final static private int V_MARGIN = 6;
    final static public int FN_START = 0;
    final static public int FN_TYPE = 1;
    final static public int FN_FROST = 2;
    final static public int FN_SHADE = 3;
    final static public int FN_SIZE = 4;
    final static public int FN_END = -666;
    final static public int MAX_NUM_SEARCHES_DISPLAY = 9;
    final static public String CURRENT_DISPLAY_LABEL = "current_display";
    final static private String LOWER_TAG = "lower";
    final static private String UPPER_TAG = "upper";
    final static public String HEIGHT = "height";
    final static public String WIDTH = "width";
    final static private List<Integer> FILTER_ORDER = Arrays.asList(FN_TYPE, FN_SIZE, FN_SHADE, FN_FROST);

    /**
     * Gets the next filter display. Order is dictated by FILTER_ORDER.
     * @param current The current filter option.
     * @return int relating to the next filter option.
     */
    public static int getNextFilter(int current){
        int index = FILTER_ORDER.indexOf(current);
        if (index == -1){//Start
            return FILTER_ORDER.get(0);
        } else if (index == (FILTER_ORDER.size() - 1)){
            return FN_END;
        } else {
            return FILTER_ORDER.get(index + 1);
        }
    }

    /**
     * This creates a filter option selector and displays it in a ConstraintLayout.
     * @param fo This is an entity that stores the data on the filter option to create.
     * @param context Context this should be displayed in. Should use 'this.getContext()' in
     *                fragments to get this value.
     * @return The created filter option selector.
     */
    public static FilterOptionSelector createFilter(FilterOptionEntity fo, Context context){
        final FilterOptionSelector x = new FilterOptionSelector(fo.getCategory(),
                fo.getDescription(), fo.getOptions(), fo.getOption_descriptions(), fo.getSearch_table(),
                fo.getField(), H_MARGIN, V_MARGIN, false, context);
        return x;
    }

    /**
     * This creates a filter option selector and displays it in a ConstraintLayout.
     * @param top_ref The filter option selector will be below this View.
     * @param cl The ConstraintLayout to show the filter options is.
     * @param fo This is an entity that stores the data on the filter option to create.
     * @param single_selection Whether only a single filter option should be selectable or not.
     * @param context Context this should be displayed in. Should use 'this.getContext()' in
     *                fragments to get this value.
     * @return The created filter option selector.
     */
    public static FilterOptionSelector createFilter(View top_ref, ConstraintLayout cl,
                                                    FilterOptionEntity fo, boolean single_selection,
                                                    View.OnClickListener on_click, Context context){
        final FilterOptionSelector x = new FilterOptionSelector(fo.getCategory(),
                fo.getDescription(), fo.getOptions(), fo.getOption_descriptions(), fo.getSearch_table(),
                fo.getField(), H_MARGIN, V_MARGIN, single_selection, on_click, context);
        cl.addView(x);
        DisplayHelper.setViewConstraints(x, cl, cl, cl, top_ref, H_MARGIN, V_MARGIN);
        return x;
    }

    /**
     * This assigns values to an already created filter option selector..
     * @param fo This is an entity that stores the data on the filter option to create.
     * @param single_selection Whether only a single filter option should be selectable or not.
     * @return The created filter option selector.
     */
    public static void initFilter(FilterOptionSelector fos, FilterOptionEntity fo,
                                  boolean single_selection){
        fos.setCategory(fo.getCategory());
        fos.setDescription(fo.getDescription());
        fos.setOptions(fo.getOptions());
        fos.setOption_descriptions(fo.getOption_descriptions());
        fos.setSearch_table(fo.getSearch_table());
        fos.setField(fo.getField());
        fos.setSingleSelection(single_selection);
        fos.setH_margin(H_MARGIN);
        fos.setV_margin(V_MARGIN);
    }

    public static FilterOptionEntity createTypeFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Type", "The type of plant.",
                Arrays.asList("Tree", "Shrub", "Ground cover", "Grass",
                        "Clumping perennial", "Climber", "Bulb"),
                Arrays.asList("Tree", "Shrub", "Ground cover", "Grass",
                        "Clumping perennial", "Climber", "Bulb"),
                "type", "type");
        return fo;
    }

    public static FilterOptionEntity createFrostFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Frost",
                "How much frost a plant can tolerate and still survive",
                Arrays.asList("None", "Light", "Heavy"), Arrays.asList("None", "Light", "Heavy"),
                "frost", "frost_tolerance");
        return fo;
    }

    public static FilterOptionEntity createShadeFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Shade", "How much light/shade a plant needs.",
                Arrays.asList("Sunny", "Light shade", "Half shade", "Heavy shade"),
                Arrays.asList("Sunny", "Light shade", "Half shade", "Heavy shade"),
                "light", "light_required");
        return fo;
    }

    public static FilterOptionEntity createHeightFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Height", "Height of a plant.",
                Arrays.asList("Small (0-1m)", "Medium (1-10m)", "Large(10+m)"),
                Arrays.asList("0-1", "1-10", "10-1000"), "plant_data", "height_upper");
        return fo;
    }

    public static FilterOptionEntity createWidthFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Width", "Width of a plant.",
                Arrays.asList("Small (0-1m)", "Medium (1-10m)", "Large(10+m)"),
                Arrays.asList("0-1", "1-10", "10-1000"), "plant_data", "width_upper");
        return fo;
    }

    public static FilterOptionEntity createZoneFilter(){
        FilterOptionEntity fo = new FilterOptionEntity("Zone", "Hardiness zone a plant can grow in.",
                Arrays.asList("1", "2", "3", "4", "5", "6", "7"),
                Arrays.asList("1", "2", "3", "4", "5", "6", "7"), "zone", "climate_zone");
        return fo;
    }

}

package com.example.lazyplant.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;
import com.example.lazyplant.ui.plantDetails.PlantDetailsAdapter;
import com.example.lazyplant.ui.profile.NotesAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A class that contains a bunch of display functions that can be re-used.
 * These are primarily for displaying data on screen, e.g plant details or plant search results.
 */
public class plantDetailsDisplayHelper extends DisplayHelper {
    final static private int TITLE_H_MARGIN = 16;
    final static private int TITLE_V_MARGIN = 5;
    private static final int TITLE_SIZE = 26;
    private static final int SUBTITLE_SIZE = 14;
    final static private int DRAWABLE_IMAGE_NAME_MAX = 5;
    final static private int[] ICON_IMAGES = {R.id.linear_icon_image1, R.id.linear_icon_image2,
            R.id.linear_icon_image3, R.id.linear_icon_image4};
    final static private int[] ICON_NAMES = {R.id.linear_icon_text1, R.id.linear_icon_text2,
            R.id.linear_icon_text3, R.id.linear_icon_text4};

    public plantDetailsDisplayHelper() { }


    static public void displayDetailsPageImage(String image_name, ImageView iv, Context context, double size){
        int height = context.getResources().getDisplayMetrics().heightPixels;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) iv.getLayoutParams();
        params.height = (int) (height * size);
        iv.setLayoutParams(params);
        displayImage(image_name, iv, context);
    }

    static private void displayImage(String name, ImageView iv, Context context){
        String image_name = convertToImageName(name);
        int image_id = context.getResources().getIdentifier(image_name,
                Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
        if (image_id == 0){
            for (int i = 0; i < DRAWABLE_IMAGE_NAME_MAX; i++){
                image_id = context.getResources().getIdentifier(image_name + "_" + i,
                        Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
                if (image_id != 0){
                    break;
                }
            }
        }
        setImage(name, iv, context);
    }

    static public List<TextView> displayPlantTitle(PlantInfoEntity p, ConstraintLayout cl, View im, Context context){
        List<TextView> l = new ArrayList<>();
        final TextView title = new TextView(new ContextThemeWrapper(
                cl.getRootView().getContext(), R.style.Title1), null, 0);
        title.setText(p.getCommon_name());
        cl.addView(title);
        setViewConstraints(title, cl, cl, cl, im, TITLE_H_MARGIN, TITLE_V_MARGIN);
        final TextView subtitle = new TextView(new ContextThemeWrapper(
                cl.getRootView().getContext(), R.style.SubtitleText), null, 0);
        subtitle.setText(p.getScientific_name());
        cl.addView(subtitle);
        setViewConstraints(subtitle, cl, cl, cl, title, TITLE_H_MARGIN, TITLE_V_MARGIN);
        l.add(title);
        l.add(subtitle);
        return l;
    }

    static public List<View> showPlantDetails(PlantInfoEntity p, ConstraintLayout cl,Context context) {
        List<View> l = new ArrayList<>();
        //List<View> last = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);

        List<String> type_list = Arrays.asList(p.getType().split(", "));
        View layout = (View) cl.findViewById(R.id.linear_layout);
        for (int i = 0; i < type_list.size(); i++) {
            if (i == 4) {
                break;
            }
            TextView tv = (TextView) layout.findViewById(ICON_NAMES[i]);
            tv.setText(type_list.get(i));
            ImageView im = (ImageView) layout.findViewById(ICON_IMAGES[i]);
            Glide.with(context).load(getIcon("Type", type_list.get(i))).into(im);

        }

        List<String> plant_details = p.getShortPlantDetailList();
        List<List<String>> details = new ArrayList<>();
        for(String x : plant_details){
            details.add(Arrays.asList(x.split(": ")));
        }
        AppDatabase database = Room.databaseBuilder(cl.getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        PlantNotesDAO dao = database.getPlantNotesDAO();
        List<PlantNotes> pn = dao.getNotesRecent();
        RecyclerView ds = (RecyclerView) cl.findViewById(R.id.plant_details_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(cl.getContext());
        ds.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter adapter = new PlantDetailsAdapter(details);
        ds.setAdapter(adapter);

        /*View last = layout;
        for(String x : details) {
            final TextView i = new TextView(cl.getRootView().getContext());
            configureTextView(i, x, 14, ContextCompat.getColor(context, R.color.TextBlack));
            Typeface a = ResourcesCompat.getFont(context, R.font.andale_mono);
            i.setTypeface(a);
            cl.addView(i);
            setViewConstraintsLeft(i, cl, cl, last, 18, 8);
            last = i;
            l.add(i);
        }*/


        return l;
    }

    static public List<View> displayPlantDetails(PlantInfoEntity p, ConstraintLayout cl, View root, Context context) {
        List<View> l = new ArrayList<>();
        //List<View> last = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);

        View layout = (View) root.findViewById(R.id.linear_layout);
        setDetailIcon(layout, 0, p.getHeight(), R.drawable.ic_height, context);
        setDetailIcon(layout, 1, p.getWidth(), R.drawable.ic_width, context);

        List<List<String>> icons = new ArrayList<>();
        List<String> frost_list = new ArrayList<String>();
        frost_list.addAll(Arrays.asList("Frost"));
        frost_list.addAll(Arrays.asList(p.getFrost_tolerance().split(", ")));
        List<String> light_list = new ArrayList<String>();
        light_list.addAll(Arrays.asList("Light"));
        light_list.addAll(Arrays.asList(p.getLight().split(", ")));
        List<String> type_list = new ArrayList<>();
        type_list.addAll(Arrays.asList("Type"));
        type_list.addAll(Arrays.asList(p.getType().split(", ")));


        /*List<String> details = p.getShortPlantDetailList();
        for(String x : details) {
            final TextView i = new TextView(cl.getRootView().getContext());
            configureTextView(i, x, 16, ContextCompat.getColor(context, R.color.detailsTextColor));
            Typeface a = ResourcesCompat.getFont(context, R.font.american_typewriter_bold);
            i.setTypeface(a);
            cl.addView(i);
            setViewConstraintsLeft(i, cl, cl, last, DESC_H_MARGIN, DESC_V_MARGIN);
            last = i;
            l.add(i);
        }*/

        /*List<String> details = p.getPlantDetailList();
        View last = top_ref;
        for(String x : details){
            final TextView i = new TextView(cl.getRootView().getContext());
            configureTextView(i, x, DESC_SIZE, ContextCompat.getColor(context, R.color.detailsTextColor));
            Typeface a = ResourcesCompat.getFont(context, R.font.american_typewriter_bold);
            i.setTypeface(a);
            cl.addView(i);
            setViewConstraintsLeft(i, cl, cl, last, DESC_H_MARGIN, DESC_V_MARGIN);
            last = i;
            l.add(i);
        }*/
        return l;
    }

    static int  getIcon(String category, String name){
        if(category.equals("Type")){
            switch(name){
                case "Tree":
                    return R.drawable.ic_tree;
                case "Shrub":
                    return R.drawable.ic_shrub;
                case "Ground cover":
                    return R.drawable.ic_ground_cover;
                case "Grass":
                    return R.drawable.ic_grass;
                case "Clumping perennial":
                    return R.drawable.ic_clumpling_perennial;
                case "Climber":
                    return R.drawable.ic_climber;
                case "Bulb":
                    return R.drawable.ic_bulb;
            }
        }else if(category.equals("Frost")){
            switch(name) {
                case "Light":
                    return R.drawable.ic_light_frost;
                case "None":
                    return R.drawable.ic_none;
                case "Heavy":
                    return R.drawable.ic_heavy_frost;
            }
        }else if(category.equals("Light")){
            switch(name) {
                case "Sunny":
                    return R.drawable.ic_sunny;
                case "Light shade":
                    return R.drawable.ic_light_shade;
                case "Heavy shade":
                    return R.drawable.ic_heavy_shade;
                case "Half shade":
                    return R.drawable.ic_half_shade;
            }
        }
        return 0;
    }

    static private void setDetailIcon(View layout, int position, String text, int image, Context context){
        TextView tv = (TextView) layout.findViewById(ICON_NAMES[position]);
        tv.setText(text);
        ImageView im = (ImageView) layout.findViewById(ICON_IMAGES[position]);
        Glide.with(context).load(image).into(im);
    }

    static private void configureTextView(TextView tv, String text, int size, int color){
        tv.setId(ViewCompat.generateViewId());
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
    }

}

package com.example.lazyplant.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains a bunch of display functions that can be re-used.
 * These are primarily for displaying data on screen, e.g plant details or plant search results.
 */
public class plantDetailsDisplayHelper extends DisplayHelper {
    final private int NUM_COLUMNS = 4;
    final static private int TITLE_H_MARGIN = 16;
    final static private int TITLE_V_MARGIN = 5;
    private static final int TITLE_SIZE = 26;
    private static final int SUBTITLE_SIZE = 14;
    final static private int DRAWABLE_IMAGE_NAME_MAX = 5;

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
        final TextView title = new TextView(cl.getRootView().getContext());
        configureTextView(title, p.getCommon_name(), TITLE_SIZE, ContextCompat.getColor(context, R.color.detailsTitleColor));
        cl.addView(title);
        setViewConstraints(title, cl, cl, cl, im, TITLE_H_MARGIN, TITLE_V_MARGIN);
        final TextView subtitle = new TextView(cl.getRootView().getContext());
        configureTextView(subtitle, p.getScientific_name(), SUBTITLE_SIZE, ContextCompat.getColor(context, R.color.detailsSubtitleColor));
        cl.addView(subtitle);
        setViewConstraints(subtitle, cl, cl, cl, title, TITLE_H_MARGIN, TITLE_V_MARGIN);
        Typeface a = ResourcesCompat.getFont(context, R.font.american_typewriter_bold);
        title.setTypeface(a);
        subtitle.setTypeface(a);
        l.add(title);
        l.add(subtitle);
        return l;
    }

    static public List<View> displayPlantDetails(PlantInfoEntity p, ConstraintLayout cl, View top_ref, Context context){
        List<View> l = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(context);

        /*LinearLayout il = (LinearLayout)inflater.inflate(R.layout.linear_layout_hori, null, false);
        l.add(il);
        setViewConstraints(il, cl, 4, 0);
        View last = il;*/
        //Generate list
        for(int i = 0; i < 3; i++){
            /*final TextView v = new TextView(cl.getRootView().getContext());
            configureTextView(v, x, DESC_SIZE, ContextCompat.getColor(context, R.color.detailsTextColor));
            Typeface a = ResourcesCompat.getFont(context, R.font.american_typewriter_bold);
            v.setTypeface(a);
            cl.addView(v);


            /*LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.linear_layout_hori, null, false);
            l.add(ll);
            setViewConstraints(ll, cl, cl, cl, last, 4, 2);

            final TextView v = new TextView(cl.getRootView().getContext());
            configureTextView(v, x, DESC_SIZE, ContextCompat.getColor(context, R.color.detailsTextColor));
            Typeface a = ResourcesCompat.getFont(context, R.font.american_typewriter_bold);
            v.setTypeface(a);
            cl.addView(v);


            last = v;
            l.add(v);*/
        }


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

    static private void configureTextView(TextView tv, String text, int size, int color){
        tv.setId(ViewCompat.generateViewId());
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
    }

}

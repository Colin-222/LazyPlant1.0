package com.example.lazyplant.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.PlantInfoEntity;

import java.util.List;

/**
 * A class that contains a bunch of display functions that can be re-used.
 * These are primarily for displaying data on screen, e.g plant details or plant search results.
 */
public class plantDisplayHelper extends DisplayHelper{

    final static private int TITLE_H_MARGIN = 16;
    final static private int TITLE_V_MARGIN = 16;
    private static final int TITLE_SIZE = 26;
    private static final int SUBTITLE_SIZE = 14;
    final static private int DESC_H_MARGIN = 16;
    final static private int DESC_V_MARGIN = 16;
    final static private int DESC_SIZE = 14;
    final static private int DRAWABLE_IMAGE_NAME_MAX = 5;

    public plantDisplayHelper() { }


    static public void displayDetailsPageImage(String image_name, ImageView iv, Context context, double size){
        int height = context.getResources().getDisplayMetrics().heightPixels;
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) iv.getLayoutParams();
        params.height = (int) (height * size);
        iv.setLayoutParams(params);
        displayImage(image_name, iv, context);
    }

    static private void displayImage(String image_name, ImageView iv, Context context){
        int image_id = context.getResources().getIdentifier(image_name,
                "drawable", context.getPackageName());
        if (image_id == 0){
            for (int i = 0; i < DRAWABLE_IMAGE_NAME_MAX; i++){
                image_id = context.getResources().getIdentifier(image_name + "_" + i,
                        "drawable", context.getPackageName());
                if (image_id != 0){
                    break;
                }
            }

        }
        iv.setImageResource(image_id);
    }

    static public void displayPlantTitle(PlantInfoEntity p, ConstraintLayout cl, View im, Context context){
        final TextView title = new TextView(cl.getContext());
        configureTextView(title, p.getCommon_name(), TITLE_SIZE, ContextCompat.getColor(context, R.color.detailsTitleColor));
        cl.addView(title);
        setViewConstraints(title, cl, cl, cl, im, TITLE_H_MARGIN, TITLE_V_MARGIN);
        final TextView subtitle = new TextView(cl.getContext());
        configureTextView(subtitle, p.getScientific_name(), SUBTITLE_SIZE, ContextCompat.getColor(context, R.color.detailsSubtitleColor));
        cl.addView(subtitle);
        setViewConstraints(subtitle, cl, cl, cl, title, TITLE_H_MARGIN, TITLE_V_MARGIN);
    }

    static public void displayPlantDetails(PlantInfoEntity p, ConstraintLayout cl, View top_ref, Context context){
        List<String> details = p.getPlantDetailList();
        View last = top_ref;
        for(String x : details){
            final TextView i = new TextView(cl.getContext());
            configureTextView(i, x, DESC_SIZE, ContextCompat.getColor(context, R.color.detailsTextColor));
            cl.addView(i);
            setViewConstraints(i, cl, cl, last, DESC_H_MARGIN, DESC_V_MARGIN);
            last = i;
        }
    }

    static private void configureTextView(TextView tv, String text, int size, int color){
        tv.setId(ViewCompat.generateViewId());
        tv.setText(text);
        tv.setTextSize(size);
        tv.setTextColor(color);
    }

    static private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}
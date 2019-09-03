package com.example.lazyplant.ui;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.PlantInfo;

import java.util.List;

/**
 * A class that contains a bunch of display functions that can be re-used.
 * These are primarily for displaying data on screen, e.g plant details or plant search results.
 */
public class plantDisplayHelper {

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

    static public void displayPlantTitle(PlantInfo p, ConstraintLayout cl, View im, Context context){
        final TextView title = new TextView(cl.getContext());
        configureTextView(title, p.getCommon_name(), TITLE_SIZE, ContextCompat.getColor(context, R.color.detailsTitleColor));
        cl.addView(title);
        setViewConstraints(title, cl, cl, cl, im, TITLE_H_MARGIN, TITLE_V_MARGIN);
        final TextView subtitle = new TextView(cl.getContext());
        configureTextView(subtitle, p.getScientific_name(), SUBTITLE_SIZE, ContextCompat.getColor(context, R.color.detailsSubtitleColor));
        cl.addView(subtitle);
        setViewConstraints(subtitle, cl, cl, cl, title, TITLE_H_MARGIN, TITLE_V_MARGIN);
    }

    static public void displayPlantDetails(PlantInfo p, ConstraintLayout cl, View top_ref, Context context){
        View top = top_ref;
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

    static private void setViewConstraints(View v, ConstraintLayout cl, View left_ref,View top_ref,
                                           int h_margin, int v_margin){
        ConstraintLayout.LayoutParams tv_params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, left_ref.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, left_ref.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, top_ref.getId(), ConstraintSet.BOTTOM, v_margin);
        constraintSet.constrainDefaultHeight(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainDefaultWidth(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(cl);
    }

    static private void setViewConstraints(View v, ConstraintLayout cl, View left_ref,
                                           View right_ref, View top_ref, int h_margin, int v_margin){
        ConstraintLayout.LayoutParams tv_params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, left_ref.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, left_ref.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.END, right_ref.getId(), ConstraintSet.END, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.RIGHT, right_ref.getId(), ConstraintSet.RIGHT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, top_ref.getId(), ConstraintSet.BOTTOM, v_margin);
        constraintSet.constrainDefaultHeight(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainDefaultWidth(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(cl);
    }

    static private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}

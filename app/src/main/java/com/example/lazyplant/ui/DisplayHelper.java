package com.example.lazyplant.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;

/**
 * Abstract class that contains some functions to help display elements in UI.
 */
abstract public class DisplayHelper {

    static private int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    static public void setViewConstraints(View v, ConstraintLayout cl, View left_ref,
                                          View right_ref, View top_ref, int hori_margin, int vert_margin){
        int h_margin = dpToPx(hori_margin, v.getContext());
        int v_margin = dpToPx(vert_margin, v.getContext());
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

    static public void setViewConstraintsLeft(View v, ConstraintLayout cl, View side_ref,View top_ref,
                                          int hori_margin, int vert_margin){
        int h_margin = dpToPx(hori_margin, v.getContext());
        int v_margin = dpToPx(vert_margin, v.getContext());
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, side_ref.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, side_ref.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, top_ref.getId(), ConstraintSet.BOTTOM, v_margin);
        constraintSet.constrainDefaultHeight(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainDefaultWidth(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(cl);
    }

    static public void setViewConstraints(View v, ConstraintLayout cl, View side_ref,View top_ref,
                                          int hori_margin, int vert_margin){
        int h_margin = dpToPx(hori_margin, v.getContext());
        int v_margin = dpToPx(vert_margin, v.getContext());
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, side_ref.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, side_ref.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.END, side_ref.getId(), ConstraintSet.END, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.RIGHT, side_ref.getId(), ConstraintSet.RIGHT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, top_ref.getId(), ConstraintSet.BOTTOM, v_margin);
        constraintSet.constrainDefaultHeight(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainDefaultWidth(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(cl);
    }

    static public void setViewConstraints(View v, ConstraintLayout cl, int hori_margin, int vert_margin){
        int h_margin = dpToPx(hori_margin, v.getContext());
        int v_margin = dpToPx(vert_margin, v.getContext());
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, cl.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, cl.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, v_margin);
        constraintSet.connect(v.getId(), ConstraintSet.END, cl.getId(), ConstraintSet.END, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.RIGHT, cl.getId(), ConstraintSet.RIGHT, h_margin);
        constraintSet.constrainDefaultHeight(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainDefaultWidth(v.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(cl);
    }

    static protected String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

    static protected void setImage(String name, ImageView iv, Context context){
        String image_name = convertToImageName(name);
        int image_id = context.getResources().getIdentifier(image_name,
                Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
        Glide.with(context).load(image_id).into(iv);
    }

}

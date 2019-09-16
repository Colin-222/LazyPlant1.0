package com.example.lazyplant.ui;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.lazyplant.Constants;

/**
 * Abstract class that contains some functions to help display elements in UI.
 */
abstract public class DisplayHelper {

    static public void setViewConstraints(View v, ConstraintLayout cl, View left_ref,
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

    static public void setViewConstraints(View v, ConstraintLayout cl, View left_ref,View top_ref,
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

    static public void setViewConstraints(View v, ConstraintLayout cl, int h_margin, int v_margin){
        ConstraintLayout.LayoutParams tv_params = (ConstraintLayout.LayoutParams) v.getLayoutParams();
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);
        constraintSet.connect(v.getId(), ConstraintSet.START, cl.getId(), ConstraintSet.START, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.LEFT, cl.getId(), ConstraintSet.LEFT, h_margin);
        constraintSet.connect(v.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, v_margin);
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
        iv.setImageResource(image_id);
    }

}

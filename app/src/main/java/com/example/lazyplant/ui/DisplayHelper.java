package com.example.lazyplant.ui;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

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

}

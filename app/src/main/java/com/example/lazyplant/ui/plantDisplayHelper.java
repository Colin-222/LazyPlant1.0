package com.example.lazyplant.ui;

import android.content.Context;
import android.widget.ImageView;

/**
 * A class that contains a bunch of display functions that can be re-used.
 * These are primarily for displaying data on screen, e.g plant details or plant search results.
 */
public class plantDisplayHelper {

    public plantDisplayHelper() {
    }

    static public void displayImage(String image_name, ImageView iv, Context context){
        String name = image_name;
        int image_id = context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
        if (image_id == 0){
            image_id = context.getResources().getIdentifier(name + "_1",
                    "drawable", context.getPackageName());
        }
        iv.setImageResource(image_id);
    }

    private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}

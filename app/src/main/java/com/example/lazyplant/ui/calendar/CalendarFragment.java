package com.example.lazyplant.ui.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lazyplant.R;

public class CalendarFragment extends Fragment {

    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        for (int i = 0; i < 40; i++) {

            final Button myButton = new Button(root.getContext());
            myButton.setText(Integer.toString(i+1));
            myButton.setId(i + 1);
//            myButton.setOnClickListener(this);

            myButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            myButton.setTextSize(18);

            myButton.setPadding(20, 0, 20, 0);

            RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.linear_calendar);

            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams((int)(Math.round(width*0.45)),
height/5);
            if (i == 0) {
                buttonParams.setMargins(width/40, width/40, width/40, width/40);
            } else if (i % 2 == 1) {
                buttonParams.addRule(RelativeLayout.RIGHT_OF, i);
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/40, width/40, width/40, width/40);
            } else if (i % 2 == 0) {
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/40, width/40, width/40, width/40);
            }

            relativeLayout.addView(myButton, buttonParams);
        }
        return root;
    }
}
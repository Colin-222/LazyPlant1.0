package com.example.lazyplant.ui.calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import androidx.room.Room;

import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CalendarFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.example.lazyplant.ui.calendar.MESSAGE";


    private CalendarViewModel calendarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        final View root_f = root;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        drawMaPlants(root_f, "Spring", height, width);
                        break;
                    case 1:
                        drawMaPlants(root_f, "Summer", height, width);
                        break;
                    case 2:
                        drawMaPlants(root_f, "Autumn", height, width);
                        break;
                     case 3:
                         drawMaPlants(root_f, "Winter", height, width);
                         break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        drawMaPlants(root_f, "Spring", height, width);
        return root;
    }

    private void drawMaPlants(View root, String season, int height, int width) {
        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.linear_calendar);
        relativeLayout.removeAllViews();
        final int padding = 40;

        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        List<String> x = databaseAccess.search("species_id", "flower_time",
                "flowering_period = \"" + season + "\" or flowering_period = \"All year\"");
        databaseAccess.close();

        for (int i = 0; i < x.size(); i++) {
            String pid = x.get(i);
            final Button myButton = new Button(root.getContext());
            myButton.setText(pid);
            myButton.setTextColor(getResources().getColor(R.color.colorRed));
            myButton.setBackgroundResource(R.drawable.goodenia_ovata);
            final String bid = pid;
            myButton.setId(i+1);
            final String id = pid;
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //open details page with id
                    Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, bid);
                    startActivity(intent);
                }
            });
            myButton.setPadding(20, 0, 20, 0);
            RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams((int)(Math.round(width*0.45)), height/5);
            if (i == 0) {
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            } else if (i % 2 == 1) {
                buttonParams.addRule(RelativeLayout.RIGHT_OF, i);
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            } else if (i % 2 == 0) {
                buttonParams.addRule(RelativeLayout.BELOW, i - 1);
                buttonParams.setMargins(width/padding, width/padding, width/padding, width/padding);
            }
            relativeLayout.addView(myButton, buttonParams);
        }
    }

}
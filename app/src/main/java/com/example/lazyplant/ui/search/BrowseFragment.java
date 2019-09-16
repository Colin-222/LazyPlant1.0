package com.example.lazyplant.ui.search;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BrowseFragment extends Fragment {
    private View root;
    private boolean show_filters = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_browse, container, false);

        ImageButton filters_button = (ImageButton) root.findViewById(R.id.browse_display_filters);
        filters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configure_filter_display();
            }
        });


        return this.root;
    }

    private void configure_filter_display(){
        ConstraintLayout cl = (ConstraintLayout) this.root.findViewById(R.id.browse_filter_area);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) cl.getLayoutParams();

        if(this.show_filters){
            //Close filter options
            params.height = dpToPx(52, this.getContext());

            this.show_filters = false;
        }else{
            //Show filter options
            params.height = dpToPx(160, this.getContext());

            this.show_filters = true;
        }
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

}



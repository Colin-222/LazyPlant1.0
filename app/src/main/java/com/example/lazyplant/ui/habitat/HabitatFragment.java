package com.example.lazyplant.ui.habitat;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
import com.example.lazyplant.ui.DisplayHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

public class HabitatFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_habitat, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        ImageButton b1 = (ImageButton) root.findViewById(R.id.habitat_button_butterfly_moth);
        b1.setOnClickListener(habitatButtonListener);
        ImageButton b2 = (ImageButton) root.findViewById(R.id.habitat_button_bird);
        b2.setOnClickListener(habitatButtonListener);
        ImageButton b3 = (ImageButton) root.findViewById(R.id.habitat_button_parrot);
        b3.setOnClickListener(habitatButtonListener);
        ImageButton b4 = (ImageButton) root.findViewById(R.id.habitat_button_honeyeater);
        b4.setOnClickListener(habitatButtonListener);
        ImageButton b5 = (ImageButton) root.findViewById(R.id.habitat_button_reptile_frog);
        b5.setOnClickListener(habitatButtonListener);
        ImageButton b6 = (ImageButton) root.findViewById(R.id.habitat_button_mammal);
        b6.setOnClickListener(habitatButtonListener);

        return root;
    }

    private View.OnClickListener habitatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.HABITAT_TAG, ((ImageButton)view).getTag().toString());
            NavHostFragment.findNavController(getThis())
                    .navigate(R.id.action_navigation_habitat_to_navigation_habitat_search, bundle);
        }};

    private Fragment getThis(){
        return this;
    }

}

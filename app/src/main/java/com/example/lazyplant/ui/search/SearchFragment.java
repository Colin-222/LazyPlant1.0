package com.example.lazyplant.ui.search;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.R;
import com.example.lazyplant.httphost.HttpDataHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.lazyplant.PlantDetailsActivity.TAG;

public class SearchFragment extends Fragment {

    private FusedLocationProviderClient client;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        Button locationButton = (Button) root.findViewById(R.id.search_location);
        final TextView postcodeText = (TextView) root.findViewById(R.id.hint_location);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder mGeocoder = new Geocoder(getContext(), Locale.ENGLISH);
                        try {
                            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            String zipcode = addresses.get(0).getPostalCode();
                            postcodeText.setText(zipcode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }


        });


        Button button = (Button) root.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit = getView().findViewById(R.id.search_name);

                ChipGroup type_cg = getView().findViewById(R.id.type_chip_group);
                List<String> type_search = new ArrayList<String>();
                if (((Chip) type_cg.findViewById(R.id.chip_tree)).isChecked()) {
                    type_search.add("Big tree");
                    type_search.add("Small tree");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_shrub)).isChecked()) {
                    type_search.add("Big shrub");
                    type_search.add("Small shrub");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_fern)).isChecked()) {
                    type_search.add("Fern");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_ground_cover)).isChecked()) {
                    type_search.add("Ground cover");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_grass)).isChecked()) {
                    type_search.add("Grass");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_clumping)).isChecked()) {
                    type_search.add("Clumping perennial");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_climber)).isChecked()) {
                    type_search.add("Climber");
                }
                if (((Chip) type_cg.findViewById(R.id.chip_bulb)).isChecked()) {
                    type_search.add("Bulb");
                }

                ChipGroup shade_cg = getView().findViewById(R.id.shade_chip_group);
                List<String> shade_search = new ArrayList<String>();
                if (((Chip) shade_cg.findViewById(R.id.chip_sunny)).isChecked()) {
                    type_search.add("Sunny");
                }
                if (((Chip) shade_cg.findViewById(R.id.chip_light_shade)).isChecked()) {
                    type_search.add("Light shade");
                }
                if (((Chip) shade_cg.findViewById(R.id.chip_half_shade)).isChecked()) {
                    type_search.add("Half shade");
                }
                if (((Chip) shade_cg.findViewById(R.id.chip_heavy_shade)).isChecked()) {
                    type_search.add("Heavy shade");
                }

                ChipGroup frost_cg = getView().findViewById(R.id.frost_chip_group);
                List<String> frost_search = new ArrayList<String>();
                if (((Chip) frost_cg.findViewById(R.id.chip_light_frost)).isChecked()) {
                    frost_search.add("Light");
                }
                if (((Chip) frost_cg.findViewById(R.id.chip_no_frost)).isChecked()) {
                    frost_search.add("None");
                }
                if (((Chip) frost_cg.findViewById(R.id.chip_heavy_frost)).isChecked()) {
                    frost_search.add("Heavy");
                }

                ChipGroup zone_cg = getView().findViewById(R.id.zone_chip_group);
                List<String> zone_search = new ArrayList<String>();
                if (((Chip) zone_cg.findViewById(R.id.chip_cool_temperate)).isChecked()) {
                    zone_search.add("Cool temperate");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_semi_arid)).isChecked()) {
                    zone_search.add("Semi-arid");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_sub_tropical)).isChecked()) {
                    zone_search.add("Sub-tropical");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_mediterranean)).isChecked()) {
                    zone_search.add("Mediterranean");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_cool)).isChecked()) {
                    zone_search.add("Cool");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_arid)).isChecked()) {
                    zone_search.add("Arid");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_warm_temperate)).isChecked()) {
                    zone_search.add("Warm temperate");
                }
                if (((Chip) zone_cg.findViewById(R.id.chip_tropical)).isChecked()) {
                    zone_search.add("Tropical");
                }

                ChipGroup height_cg = getView().findViewById(R.id.height_chip_group);
                List<String> height_search = new ArrayList<String>();
                if (((Chip) height_cg.findViewById(R.id.chip_height_small)).isChecked()) {
                    height_search.add("S");
                }
                if (((Chip) height_cg.findViewById(R.id.chip_height_medium)).isChecked()) {
                    height_search.add("M");
                }
                if (((Chip) height_cg.findViewById(R.id.chip_height_large)).isChecked()) {
                    height_search.add("L");
                }

                ChipGroup width_cg = getView().findViewById(R.id.width_chip_group);
                List<String> width_search = new ArrayList<String>();
                if (((Chip) width_cg.findViewById(R.id.chip_width_small)).isChecked()) {
                    width_search.add("S");
                }
                if (((Chip) width_cg.findViewById(R.id.chip_width_medium)).isChecked()) {
                    width_search.add("M");
                }
                if (((Chip) width_cg.findViewById(R.id.chip_width_large)).isChecked()) {
                    width_search.add("L");
                }

                edit = edit;

            }
        });

        return root;
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}



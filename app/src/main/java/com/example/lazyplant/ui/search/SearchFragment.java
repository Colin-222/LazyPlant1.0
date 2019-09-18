package com.example.lazyplant.ui.search;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.lazyplant.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SearchFragment extends Fragment {

    private FusedLocationProviderClient client;
    private Integer current_display_option;
    private SelectedFiltersEntity selected_filters = new SelectedFiltersEntity();

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

        //Get intent and previous results
        Bundle bundle = this.getArguments();
        current_display_option = FilterDisplayHelper.FN_TYPE;
        if (bundle != null){
            selected_filters = (SelectedFiltersEntity) bundle.getSerializable(SelectedFiltersEntity.TAG);
            current_display_option = Integer.parseInt(bundle.getString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL));
        }

        //Initialise some variables
        final List<FilterOptionSelector> filters = new ArrayList<>();
        View top = (View) root.findViewById(R.id.hint_location);
        ConstraintLayout cl = (ConstraintLayout) root.findViewById(R.id.search_constraint_layout);

        //Display filters
        /*switch(current_display_option) {
            case FilterDisplayHelper.FN_TYPE:
                FilterOptionSelector type_fos = FilterDisplayHelper.createTypeFilter(filters,
                        top, cl, false, this.getContext());
                break;
            case FilterDisplayHelper.FN_SIZE:
                FilterOptionSelector height_fos = FilterDisplayHelper.createHeightFilter(filters,
                        top, cl, false, this.getContext());
                FilterOptionSelector width_fos = FilterDisplayHelper.createWidthFilter(filters,
                        height_fos, cl, false, this.getContext());
                break;
            case FilterDisplayHelper.FN_SHADE:
                FilterOptionSelector shade_fos = FilterDisplayHelper.createShadeFilter(filters,
                        top, cl, false, this.getContext());
                break;
            case FilterDisplayHelper.FN_FROST:
                FilterOptionSelector frost_fos = FilterDisplayHelper.createFrostFilter(filters,
                        top, cl, false, this.getContext());
                break;
            default:
        }*/

        //Set search button behaviour
        Button button = (Button) root.findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(FilterDisplayHelper.CURRENT_DISPLAY_LABEL, current_display_option.toString());
                boolean something_selected = selected_filters.addOptionsFromFilter(filters);
                //Determine whether to send intent or not depending if any option is selected
                if (something_selected){
                    bundle.putSerializable(SelectedFiltersEntity.TAG, selected_filters);
                    //Send out stuff
                    SearchResultFragment sr = new SearchResultFragment();
                    sr.setArguments(bundle);
                    getFragmentManager() .beginTransaction()
                            .replace(R.id.nav_host_fragment, sr).commit();
                } else {
                    Toast.makeText(getActivity(), "Please select an option.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}



package com.example.lazyplant.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

public class HomeSearchFragment extends Fragment {
    private FusedLocationProviderClient client;
    final private String LOCATION_TEXT = "Postcode: ";
    private String postcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home_search, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        final Fragment f_fragment = this;
        final Context context = getContext();

        final EditText location_et = (EditText) root.findViewById(R.id.home_search_location_edit);
        final ImageButton location_go = (ImageButton)root.findViewById(R.id.home_search_location_go);
        final EditText name_et = (EditText) root.findViewById(R.id.home_search_name_edit);
        final ImageButton name_go = (ImageButton)root.findViewById(R.id.home_search_name_button);

        ImageButton locationButton = (ImageButton) root.findViewById(R.id.home_search_location_button);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder mGeocoder = new Geocoder(getContext(), Locale.ENGLISH);
                        SharedPreferences pref = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                        try {
                            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            postcode = addresses.get(0).getPostalCode();
                            location_et.setText(postcode);
                            /*SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.DEFAULT_POSTCODE, postcode);
                            editor.putInt(Constants.REMINDER_HOUR, 8);
                            editor.putInt(Constants.REMINDER_MINUTE, 12);
                            editor.commit();*/
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        location_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String pc = location_et.getText().toString();
                if(pc.matches("[0-9][0-9][0-9][0-9]")){
                    ClimateZoneGetter czg = new ClimateZoneGetter();
                    int zone = czg.getZone(pc);
                    if (zone != -1){
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.LOCATION_TAG, pc);
                        NavHostFragment.findNavController(f_fragment).navigate(
                                R.id.action_navigation_home_search_to_navigation_browse, bundle);
                    }else{
                        Toast toast = Toast.makeText(getActivity(),
                                "Sorry, your postcode is invalid.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }else{
                    Toast toast = Toast.makeText(getActivity(),
                            "Sorry, please type a postcode.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
             }
        });

        location_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                    DisplayHelper.hideKeyboard(context, root);
                    return location_go.callOnClick();
                }
                return false;
            }
        });

        name_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String search_term = name_et.getText().toString();
                if (!search_term.equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.NAME_SEARCH_TAG, search_term);
                    NavHostFragment.findNavController(f_fragment).navigate(
                            R.id.action_navigation_home_search_to_navigation_browse, bundle);
                } else {
                    name_et.setError("Please Enter a Valid Plant Name");
                }
            }
        });

        name_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                    DisplayHelper.hideKeyboard(context, root);
                    return name_go.callOnClick();
                }
                return false;
            }
        });

        final Button designButton = (Button) root.findViewById(R.id.home_search_button_design);
        designButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_search_to_navigation_design);
            }
        });

        final Button habitatButton = (Button) root.findViewById(R.id.home_search_button_animals);
        habitatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_search_to_navigation_habitat);
            }
        });

        final Button edibleButton = (Button) root.findViewById(R.id.home_search_button_edible);
        edibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                NavHostFragment.findNavController(f_fragment).navigate(
                        R.id.action_navigation_home_search_to_navigation_edible);
            }
        });

        return root;
    }

    private String getPostcode() { return this.postcode; }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}

package com.example.lazyplant.ui.search;

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

public class PostcodeFragment extends Fragment {
    private static final int MIN_EDIT_DISTANCE = 1;
    private static final double EDIT_LENGTH_PERCENTAGE = 0.25;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_postcode, container, false);
        final Fragment f_fragment = this;
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        final EditText location_et = (EditText) root.findViewById(R.id.postcode_location_edit);
        final EditText name_et = (EditText) root.findViewById(R.id.postcode_username_edit);
        final Button button = (Button) root.findViewById(R.id.postcode_location_go);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String pc = location_et.getText().toString();
                String name = name_et.getText().toString();
                if(pc.matches("[0-9][0-9][0-9][0-9]")){
                    ClimateZoneGetter czg = new ClimateZoneGetter();
                    int zone = czg.getZone(pc);
                    if (zone != -1){
                        SharedPreferences pref = getContext().getApplicationContext()
                                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor ed = pref.edit();
                        ed.putString(Constants.SHARED_PREFERENCE_POSTCODE, pc);
                        ed.putString(Constants.SHARED_PREFERENCE_NAME, name);
                        ed.commit();

                        NavHostFragment.findNavController(f_fragment).navigate(
                                R.id.action_navigation_postcode_to_navigation_home);
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
                    DisplayHelper.hideKeyboard(getContext(), root);
                    return button.callOnClick();
                }
                return false;
            }
        });
        location_et.setOnFocusChangeListener(editFocusListener);

        requestPermission();
        final FusedLocationProviderClient client;
        Button locationButton = (Button) root.findViewById(R.id.locate_user_button);
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
                            String postcode = addresses.get(0).getPostalCode();
                            location_et.setText(postcode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        return root;
    }



    private View.OnFocusChangeListener editFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                DisplayHelper.hideKeyboard(view.getContext(), view);
            }
        }};

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}



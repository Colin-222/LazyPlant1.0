package com.example.lazyplant.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.ui.plantDetails.PlantDetailsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private FusedLocationProviderClient client;
    final static private String LOCATION_TEXT = "Postcode: ";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)this.getActivity()).getSupportActionBar().hide();

        final TextView location_tv = (TextView) root.findViewById(R.id.home_location_text);
        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        String postcode = pref.getString(Constants.DEFAULT_POSTCODE, null);
        if(postcode != null){
            location_tv.setText(LOCATION_TEXT + postcode);
        }else{
            location_tv.setText("No current location.");
        }

        ImageButton locationButton = (ImageButton) root.findViewById(R.id.home_location_button);
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
                            String postcode = addresses.get(0).getPostalCode();
                            location_tv.setText(LOCATION_TEXT + postcode);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }


        });

        return root;
    }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}

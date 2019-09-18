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
import com.example.lazyplant.shopmap.ShopMapActivity;
import com.example.lazyplant.shopmap.StoreMap;
import com.example.lazyplant.ui.plantDetails.PlantDetailsActivity;
import com.example.lazyplant.ui.profile.NotesFragment;
import com.example.lazyplant.ui.search.BrowseFragment;
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
    final private String LOCATION_TEXT = "Postcode: ";
    private String postcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //((AppCompatActivity)this.getActivity()).getSupportActionBar().hide();

        final TextView location_tv = (TextView) root.findViewById(R.id.home_location_text);
        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        postcode = pref.getString(Constants.DEFAULT_POSTCODE, null);
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

        final Fragment f_fragment = this;
        ImageButton browse = (ImageButton)root.findViewById(R.id.home_browse_go);
        browse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.LOCATION_TAG, getPostcode());
                BrowseFragment f = new BrowseFragment();
                f.setArguments(bundle);
                f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, f).commit();
            }
        });

        return root;
    }

    private String getPostcode() { return this.postcode; }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}

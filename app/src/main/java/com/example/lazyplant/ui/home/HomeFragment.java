package com.example.lazyplant.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Room;

import com.example.lazyplant.AlarmBroadcastReceiver;
import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.ClimateZoneGetter;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        final EditText location_tv = (EditText) root.findViewById(R.id.home_location_text);
        SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        postcode = pref.getString(Constants.DEFAULT_POSTCODE, null);
        if(postcode != null){
            location_tv.setText(/*LOCATION_TEXT + */postcode);
        }/*else{
            location_tv.setText("No current location.");
        }*/

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
                        SharedPreferences pref = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                        try {
                            List<Address> addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            postcode = addresses.get(0).getPostalCode();
                            location_tv.setText(postcode);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString(Constants.DEFAULT_POSTCODE, postcode);
                            editor.putInt(Constants.REMINDER_HOUR, 8);
                            editor.putInt(Constants.REMINDER_MINUTE, 12);
                            editor.commit();
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
                String pc = location_tv.getText().toString();
                if(pc.matches("[0-9][0-9][0-9][0-9]")){
                    ClimateZoneGetter czg = new ClimateZoneGetter();
                    int zone = czg.getZone(pc);
                    if (zone != -1){
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.LOCATION_TAG, pc);//getPostcode());
                        NavHostFragment.findNavController(f_fragment).navigate(R.id.action_navigation_home_to_navigation_browse, bundle);
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

        return root;
    }

    private String getPostcode() { return this.postcode; }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

}

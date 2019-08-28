package com.example.lazyplant.ui.search;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.lazyplant.MainActivity;
import com.example.lazyplant.PlantDetailsActivity;
import com.example.lazyplant.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    Geocoder geocoder;
    List<Address> addresses;

    Double latitude = 18.944620;
    Double longtitude = 72.822278;

    private SearchViewModel searchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        final TextView locationText = (TextView) root.findViewById(R.id.location_text);

        Button locationButton = (Button) root.findViewById(R.id.search_location);
        locationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude,longtitude, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    String area = addresses.get(0).getLocality();
                    String city = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();

                    String fullAddress = address+","+area+","+city+","+country+","+postalcode;
                    locationText.setText(fullAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });






        Button button= (Button) root.findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlantDetailsActivity.class);
                startActivity(intent);
            }
        });



        return root;
    }


}
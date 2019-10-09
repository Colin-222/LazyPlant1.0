package com.example.lazyplant.ui.home;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;
import static com.example.lazyplant.ui.plantDetails.PlantDetailsActivity.TAG;

public class HomeFragment extends Fragment {
    private FusedLocationProviderClient client;
    final private String LOCATION_TEXT = "Postcode: ";
    private String postcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        final EditText location_tv = (EditText) root.findViewById(R.id.home_location_text);
        final Fragment f_fragment = this;
        final ImageButton browse = (ImageButton)root.findViewById(R.id.home_browse_go);
        final Context context = getContext();

        /*SharedPreferences pref = this.getContext().getApplicationContext()
                .getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
        postcode = pref.getString(Constants.DEFAULT_POSTCODE, null);
        if(postcode != null){
            location_tv.setText(postcode);
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

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();
                    user.put("first", "Ada");
                    user.put("last", "Lovelace");
                    user.put("born", 1815);

                    // Add a new document with a generated ID
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
             }
        });

        location_tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if  ((actionId == EditorInfo.IME_ACTION_GO)) {
                    DisplayHelper.hideKeyboard(context, root);
                    return browse.callOnClick();
                }
                return false;
            }
        });
        location_tv.setOnFocusChangeListener(editFocusListener);

        return root;
    }

    private String getPostcode() { return this.postcode; }

    private  void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(), new String[] {ACCESS_FINE_LOCATION}, 1);
    }

    private View.OnFocusChangeListener editFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus){
                DisplayHelper.hideKeyboard(view.getContext(), view);
                Log.i("TAG", "ASDF");
            }
        }};


}

package com.example.lazyplant.ui.plantDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.lazyplant.Constants;
import com.example.lazyplant.DialogHelper;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.PlantSearchViewModel;
import com.example.lazyplant.ui.search.plantListDisplayHelper;
import com.example.lazyplant.ui.profile.ReminderControl;
import com.example.lazyplant.ui.shopmap.ShopsMapActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class PlantDetailsFragment extends Fragment {
    private View root;
    private PlantInfoEntity p;
    private LayoutInflater inflater;
    private ViewGroup container;
    private PlantSearchViewModel model;
    private List<View> plant_details;
    private final String TAG = "PlantDetailsActivity";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_plant_details, container, false);
        this.inflater = inflater;
        this.container = container;
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        this.plant_details = new ArrayList<>();

        ImageButton add = (ImageButton)this.root.findViewById(R.id.plant_details_button_add);
        //ImageButton notes = (ImageButton)this.root.findViewById(R.id.plant_details_button_notes);
        ImageButton back = (ImageButton)this.root.findViewById(R.id.plant_details_button_back);

        add.setOnClickListener(addListener);
        //notes.setOnClickListener(notesListener);
        back.setOnClickListener(backListener);

        Bundle bundle = this.getArguments();
        String pid = bundle.getString(Constants.PLANT_DETAILS_BUNDLE_TAG);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        displayPlant(pid);

        return this.root;
    }

    private String changePlant(int change){
        int size = this.model.plant_list.size();
        int current = this.model.plant_list.indexOf(this.p);
        int ni = (current + change);
        ni = (ni % size + size) % size;
        return this.model.plant_list.get(ni).getId();
    }


    private PlantInfoEntity getPlantInfo(String pid){
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        PlantInfoEntity pie = databaseAccess.getPlantInfo(pid);
        databaseAccess.close();
        return pie;
    }

    private void displayPlant(String pid){
        this.p = getPlantInfo(pid);
        for (View v : this.plant_details){
            ((ViewManager)v.getParent()).removeView(v);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(p.getCommon_name());
        //((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(p.getScientific_name());

        this.plant_details = new ArrayList<>();
        if(p != null){
            ImageView image = this.root.findViewById(R.id.plant_details_image_main);
            ConstraintLayout cl = (ConstraintLayout) this.root.findViewById(R.id.plant_details_constraint_layout);
            ConstraintLayout cl_bottom = (ConstraintLayout) this.root.findViewById(R.id.plant_text_constraint_layout);
            plantDetailsDisplayHelper.displayDetailsPageImage(pid, image, this.getContext(), 0.4);
            View top_wo_nerae = (View) this.root.findViewById(R.id.plants_details_top);

            TextView title = (TextView) root.findViewById(R.id.plant_details_title);
            title.setText(p.getCommon_name());
            TextView subtitle = (TextView) root.findViewById(R.id.plant_details_subtitle);
            subtitle.setText(p.getScientific_name());
            this.plant_details.addAll(plantDetailsDisplayHelper.showPlantDetails(this.p, cl,
                    this.getContext()));

            //List<TextView> l = plantDetailsDisplayHelper.displayPlantTitle(this.p, cl,
            // top_wo_nerae, this.getContext());
            //List<TextView> l2 = plantDetailsDisplayHelper.displayPlantDetails(this.p, cl,
            // image, this.getContext());

            ToggleButton tb = (ToggleButton) this.root.findViewById(R.id.plant_details_button_favourites);
            plantListDisplayHelper.createToggleButton(tb, this.getContext(), pid);

            this.root.setOnTouchListener(new OnSwipePlantDetailsListener(this.getContext()){
                @Override
                public void onSwipeLeft(){
                    String pid = changePlant(1);
                    displayPlant(pid);
                }
                @Override
                public void onSwipeRight(){
                    String pid = changePlant(-1);
                    displayPlant(pid);
                }
            });

        } else {
            TextView tv = root.findViewById(R.id.plant_details_message);
            tv.setText("Sorry we ran into an error and did not find the plant.");
        }
    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().onBackPressed();
        }
    };

    private View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addPlant();
        }
    };

    private View.OnClickListener notesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogHelper.openNotesPopup(p, getContext());
        }
    };

    private View.OnClickListener shoppingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), ShopsMapActivity.class);
            startActivity(intent);
        }
    };

    private void addPlant(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
        alert.setTitle("Give a name to this plant:");
        final EditText input = new EditText(this.getContext());
        alert.setView(input);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ReminderControl rc = new ReminderControl(getContext(), Constants.GARDEN_DB_NAME);
                String namae = input.getText().toString();
                rc.addGardenPlant(namae, p.getId());
                Toast.makeText(getActivity(), "New plant \'" + namae + "\' added.", Toast.LENGTH_SHORT).show();
                /*AlarmBroadcastReceiver.startAlarmBroadcastReceiver(getContext(),
                        rc.getGardenPlant(p.getId()).getWatering_interval());*/
                SharedPreferences pref = getContext().getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                String postcode = pref.getString("postcode", "3000");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> plant = new HashMap<>();
                plant.put("plantID", p.getId());
                plant.put("postcode", postcode);

                // Add a new document with a generated ID
                db.collection("plantsInfo")
                        .add(plant)
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
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { }
        });

        AlertDialog ad = alert.create();
        final int bg = getContext().getResources().getColor(R.color.pureWhite);
        final int tc = getContext().getResources().getColor(R.color.colorPrimaryDark);
        ad.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 3f);
                negativeButton.setLayoutParams(params);
                positiveButton.setLayoutParams(params);
                negativeButton.setBackgroundColor(bg);
                positiveButton.setBackgroundColor(bg);
                negativeButton.setTextColor(tc);
                positiveButton.setTextColor(tc);
                negativeButton.invalidate();
                positiveButton.invalidate();
            }
        });
        ad.show();
        //alert.show();
    }

}

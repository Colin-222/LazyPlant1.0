package com.example.lazyplant.ui.plantDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.lazyplant.ui.plantDetailsDisplayHelper;
import com.example.lazyplant.ui.plantListDisplayHelper;
import com.example.lazyplant.ui.profile.ReminderControl;
import com.example.lazyplant.ui.shopmap.ShopsMapActivity;

import java.util.ArrayList;
import java.util.List;

public class PlantDetailsFragment extends Fragment {
    private View root;
    private PlantInfoEntity p;
    private LayoutInflater inflater;
    private ViewGroup container;
    private PlantSearchViewModel model;
    private List<View> plant_details;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_plant_details, container, false);
        this.inflater = inflater;
        this.container = container;
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        this.plant_details = new ArrayList<>();

        ImageButton add = (ImageButton)this.root.findViewById(R.id.plant_details_button_add);
        ImageButton shopping = (ImageButton)this.root.findViewById(R.id.plant_details_button_shopping);
        ImageButton notes = (ImageButton)this.root.findViewById(R.id.plant_details_button_notes);
        ImageButton back = (ImageButton)this.root.findViewById(R.id.plant_details_button_back);

        add.setOnClickListener(addListener);
        shopping.setOnClickListener(shoppingListener);
        notes.setOnClickListener(notesListener);
        back.setOnClickListener(backListener);

        Bundle bundle = this.getArguments();
        String pid = bundle.getString(Constants.PLANT_DETAILS_BUNDLE_TAG);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        displayPlant(pid);

        return this.root;
    }

    private String changePlant(int change){
        int size = this.model.plant_list.size();
        int current = this.model.plant_list.indexOf(p);
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
            View bottom_wo_nerae = (View) this.root.findViewById(R.id.plant_text_constraint_layout_top);

            this.plant_details.addAll(plantDetailsDisplayHelper.displayPlantTitle(this.p,
                    cl, top_wo_nerae, this.getContext()));
            this.plant_details.addAll(plantDetailsDisplayHelper.displayPlantDetails(this.p, cl_bottom,
                    bottom_wo_nerae, this.getContext()));

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
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { }
        });
        alert.show();
    }

}

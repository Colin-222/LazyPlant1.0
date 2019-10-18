package com.example.lazyplant.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.lazyplant.Constants;
import com.example.lazyplant.DialogHelper;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantCareRecord;
import com.example.lazyplant.plantdata.PlantCareRecordDAO;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;
import com.example.lazyplant.ui.PlantSearchViewModel;
import com.example.lazyplant.ui.plantDetails.OnSwipePlantDetailsListener;
import com.example.lazyplant.ui.plantDetailsDisplayHelper;
import com.example.lazyplant.ui.plantListDisplayHelper;
import com.example.lazyplant.ui.shopmap.ShopsMapActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.lazyplant.ui.plantDetails.PlantDetailsActivity.TAG;

public class UserPlantDetailsFragment extends Fragment{
    private View root;
    private GardenPlant gp;
    private PlantSearchViewModel model;
    private String plant_id;
    private RecyclerView h_view;
    private RecyclerView.Adapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_user_plant_details, container, false);
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        ImageButton back = (ImageButton)this.root.findViewById(R.id.upd_button_back);
        back.setOnClickListener(backListener);
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

        Bundle bundle = this.getArguments();
        this.plant_id= bundle.getString(Constants.USER_PLANT_DETAILS_BUNDLE_TAG);

        displayPlant(this.plant_id);
        return this.root;
    }

    private String changePlant(int change){
        int size = this.model.my_plants.size();
        int current = this.model.my_plants.indexOf(this.plant_id);
        int ni = (current + change);
        ni = (ni % size + size) % size;
        return this.model.my_plants.get(ni);
    }

    private GardenPlant getGardenPlant(String pid){
        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
       return dao.getGardenPlant(pid).get(0);
    }

    private PlantInfoEntity getPlantInfo(String pid){
        DbAccess databaseAccess = DbAccess.getInstance(getContext());
        databaseAccess.open();
        PlantInfoEntity pie = databaseAccess.getPlantInfo(pid);
        databaseAccess.close();
        return pie;
    }

    private void displayPlant(String pid){
        this.gp = getGardenPlant(pid);
        final GardenPlant fgp = this.gp;
        this.plant_id = pid;
        final String plid = pid;
        PlantInfoEntity pie = getPlantInfo(gp.getSpecies_id());
        final Context context = getContext();

        ((TextView)this.root.findViewById(R.id.upd_title)).setText(this.gp.getName());
        ImageView im = ((ImageView)this.root.findViewById(R.id.upd_image_main));
        String image_name = convertToImageName(pie.getId());
        int image_id = context.getResources().getIdentifier(image_name,
                Constants.PLANT_IMAGES_FOLDER, context.getPackageName());
        Glide.with(context).load(image_id).into(im);
        String common_name = "Species: "+ pie.getCommon_name();
        ((TextView)this.root.findViewById(R.id.upd_species)).setText(common_name);
        Date date = this.gp.getPlant_date().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = "Planted on " + dateFormat.format(date);
        ((TextView)this.root.findViewById(R.id.upd_plant_date)).setText(str_date);
        Date date2 = this.gp.getLast_watering().getTime();
        String str_last = "Last watered on " + dateFormat.format(date2);
        ((TextView)this.root.findViewById(R.id.upd_last_watering)).setText(str_last);
        this.displayNotes();
        this.root.findViewById(R.id.upd_button_back).setOnClickListener(backListener);

        String wi = "Watering Interval:" + this.gp.getWatering_interval() + "days";
        ((TextView)this.root.findViewById(R.id.upd_watering_interval_text)).setText(wi);

        /*EditText et_interval = (EditText)this.root.findViewById(R.id.upd_watering_interval_edit);
        et_interval.setText(String.valueOf(this.gp.getWatering_interval()));*/

        ImageButton edit_button = (ImageButton) this.root.findViewById(R.id.upd_notes_edit);
        edit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogHelper.openPlantNotesPopup(fgp, context, onDismissListener);
            }
        });

        //Set up recycler for watering history.
        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        PlantCareRecordDAO dao = database.getPlantCareRecordDAO();
        List<PlantCareRecord> pn = dao.getPlantCareData(gp.getPlant_id());
        this.h_view = (RecyclerView) root.findViewById(R.id.upd_history_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.h_view.setLayoutManager(mLayoutManager);
        adapter = new WateringHistoryAdapter(pn);
        this.h_view.setAdapter(adapter);
    }

    private void displayNotes(){
        TextView tv_notes = (TextView)this.root.findViewById(R.id.upd_notes_text);
        AppDatabase database = Room.databaseBuilder(tv_notes.getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        final GardenPlantDAO dao = database.getGardenPlantDAO();
        GardenPlant gap = dao.getGardenPlant(this.plant_id).get(0);
        tv_notes.setText(gap.getNotes());
    }

    private View.OnClickListener backListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().onBackPressed();
        }
    };

    private DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            displayNotes();
        }
    };

    private String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

}

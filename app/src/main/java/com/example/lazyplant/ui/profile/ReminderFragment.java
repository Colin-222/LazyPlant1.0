package com.example.lazyplant.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderFragment extends Fragment {
    private View root;
    private RecyclerView thirsty_plants;
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_reminder, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        ReminderControl rc = new ReminderControl(getContext(), Constants.GARDEN_DB_NAME);
        Calendar today = Calendar.getInstance();
        //today.add(Calendar.DATE, 100);//test
        List<GardenPlant> to_water = rc.getPlantsToWater(today);
        this.thirsty_plants = (RecyclerView) root.findViewById(R.id.reminder_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.thirsty_plants.setLayoutManager(mLayoutManager);
        adapter = new ReminderAdapter(to_water);
        this.thirsty_plants.setAdapter(adapter);

        return this.root;
    }


}

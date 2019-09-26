package com.example.lazyplant.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.GardenPlant;
import com.example.lazyplant.plantdata.GardenPlantDAO;
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.util.List;

public class AllPlantsFragment extends Fragment {
    private View root;
    private RecyclerView gp_view;
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_all_plants, container, false);

        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        GardenPlantDAO dao = database.getGardenPlantDAO();
        List<GardenPlant> ap = dao.getGardenPlants();
        this.gp_view = (RecyclerView) root.findViewById(R.id.all_plants_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.gp_view.setLayoutManager(mLayoutManager);
        adapter = new AllPlantsAdapter(ap);
        this.gp_view.setAdapter(adapter);

        return this.root;
    }


}

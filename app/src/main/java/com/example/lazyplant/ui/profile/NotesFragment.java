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
import com.example.lazyplant.plantdata.PlantNotes;
import com.example.lazyplant.plantdata.PlantNotesDAO;

import java.util.List;

public class NotesFragment extends Fragment {
    private View root;
    private RecyclerView notes;
    private RecyclerView.Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_notes, container, false);

        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        PlantNotesDAO dao = database.getPlantNotesDAO();
        List<PlantNotes> pn = dao.getNotesRecent();
        this.notes = (RecyclerView) root.findViewById(R.id.notes_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        this.notes.setLayoutManager(mLayoutManager);
        adapter = new NotesAdapter(pn);
        this.notes.setAdapter(adapter);

        return this.root;
    }


}

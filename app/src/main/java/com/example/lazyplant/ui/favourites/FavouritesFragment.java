package com.example.lazyplant.ui.favourites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.ui.PlantSearchViewModel;
import com.example.lazyplant.ui.search.BrowseAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {
    private View root;
    private RecyclerView favs_view;
    private RecyclerView.Adapter adapter;
    private PlantSearchViewModel model;

    public FavouritesFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_favourites, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        this.model = ViewModelProviders.of(getActivity()).get(PlantSearchViewModel.class);
        this.model.plant_list = new ArrayList<>();
        this.getPlantList();
        this.favs_view = (RecyclerView) root.findViewById(R.id.favourites_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        this.favs_view.setLayoutManager(mLayoutManager);
        adapter = new BrowseAdapter(this.model.plant_list, this);
        this.favs_view.setAdapter(adapter);

        return root;
    }

    private void getPlantList(){
        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        FavouriteDAO favouriteDAO = database.getFavouriteDAO();
        List<Favourite> favs = favouriteDAO.getFavourites();
        DbAccess databaseAccess = DbAccess.getInstance(this.root.getContext());
        databaseAccess.open();
        for (Favourite fav : favs) {
            this.model.plant_list.add(databaseAccess.getShortPlantInfo(fav.getSpecies_id()));
        }
        databaseAccess.close();
    }

}

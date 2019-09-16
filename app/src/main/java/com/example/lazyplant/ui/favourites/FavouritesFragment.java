package com.example.lazyplant.ui.favourites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.ui.plantDetails.PlantDetailsActivity;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.ui.plantListDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    public static final String EXTRA_MESSAGE = "com.example.lazyplant.ui.favourites.MESSAGE";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        AppDatabase database = Room.databaseBuilder(getContext(), AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        FavouriteDAO favouriteDAO = database.getFavouriteDAO();
        List<Favourite> favs = favouriteDAO.getFavourites();

        //Get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        
        //Draw ma plants
        ConstraintLayout cl = (ConstraintLayout) root.findViewById(R.id.favourites_constraint_layout);
        plantListDisplayHelper.drawPlantList(root,this, cl, favouriteToId(favs));
        return root;
    }

    private List<String> favouriteToId(List<Favourite> favs){
        List<String> fav_ids = new ArrayList<>();
        for (Favourite f : favs){
            fav_ids.add(f.getSpecies_id());
        }
        return fav_ids;
    }


}

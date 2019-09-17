package com.example.lazyplant.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.lazyplant.Constants;
import com.example.lazyplant.R;
import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.plantdata.PlantInfoEntity;
import com.example.lazyplant.ui.plantDetails.PlantDetailsActivity;
import com.example.lazyplant.ui.plantDetails.PlantDetailsFragment;
import com.example.lazyplant.ui.search.FilterDisplayHelper;

import java.util.ArrayList;
import java.util.List;

public class plantListDisplayHelper extends DisplayHelper {
    final static private int DESC_H_MARGIN = 8;
    final static private int DESC_V_MARGIN = 12;

    private static View createSinglePlantDisplay(PlantInfoEntity plant, Context context, Fragment fragment){
        final String pid = plant.getId();
        final Fragment f_fragment = fragment;
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = (View) inflater.inflate(R.layout.layout_single_plant, null, false);
        TextView tv = (TextView) layout.findViewById(R.id.single_plant_name);
        tv.setText(plant.getCommon_name());
        ImageView im = (ImageView) layout.findViewById(R.id.single_plant_image);
        setImage(plant.getId(), im, context);
        im.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //open details page with id
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PLANT_DETAILS_BUNDLE_TAG, pid);
                PlantDetailsFragment pdf = new PlantDetailsFragment();
                pdf.setArguments(bundle);
                f_fragment.getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, pdf).commit();
            }
        });
        final ToggleButton tb = (ToggleButton) layout.findViewById(R.id.single_button_favorite);
        createToggleButton(tb, context, pid);
        return layout;
    }

    static public void drawPlantList(View root, Fragment fragment,
                                     ConstraintLayout cl, List<String> ids){
        List<PlantInfoEntity> plant_list = new ArrayList<>();
        DbAccess databaseAccess = DbAccess.getInstance(root.getContext());
        databaseAccess.open();
        for (String id : ids) {
            plant_list.add(databaseAccess.getShortPlantInfo(id));
        }
        databaseAccess.close();
        boolean first = true;
        View last = root;
        for (PlantInfoEntity p : plant_list) {
            final View v = createSinglePlantDisplay(p, root.getContext(), fragment);
            v.setId(ViewCompat.generateViewId());
            cl.addView(v);
            if (first){
                setViewConstraints(v, cl, DESC_H_MARGIN, DESC_V_MARGIN);
                first = false;
            } else {
                setViewConstraints(v, cl, cl, last, DESC_H_MARGIN, DESC_V_MARGIN);
            }
            last = v;
        }
    }

    public static void createToggleButton(final ToggleButton tb, final Context context, final String pid){
        AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, Constants.GARDEN_DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        FavouriteDAO favouriteDAO = database.getFavouriteDAO();
        List<Favourite> favs = favouriteDAO.getFavourites();
        database.close();
        tb.setChecked(checkIfFavourite(pid, favs));
        tb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppDatabase database = Room.databaseBuilder(context, AppDatabase.class, Constants.GARDEN_DB_NAME)
                        .allowMainThreadQueries().build();
                FavouriteDAO favouriteDAO = database.getFavouriteDAO();
                List<Favourite> favs = favouriteDAO.getFavourites();
                database.close();
                if (checkIfFavourite(pid, favs)){
                    tb.setChecked(false);
                    favouriteDAO.deleteFavourite(pid);
                } else {
                    tb.setChecked(true);
                    Favourite x = new Favourite();
                    x.setSpecies_id(pid);
                    favouriteDAO.insert(x);
                }
            }
        });
    }

    public static boolean checkIfFavourite(String id, List<Favourite> favs){
        for (Favourite i : favs){
            if (i.getSpecies_id().equals(id)){
                return true;
            }
        }
        return false;
    }

}

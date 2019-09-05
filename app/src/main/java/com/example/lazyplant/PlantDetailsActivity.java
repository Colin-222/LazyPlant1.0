package com.example.lazyplant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.DbAccess;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.plantdata.PlantInfo;
import com.example.lazyplant.ui.calendar.CalendarFragment;
import com.example.lazyplant.ui.favourites.FavouritesFragment;
import com.example.lazyplant.ui.plantDetailUI.main.DetailsFragment;
import com.example.lazyplant.ui.plantDetailUI.main.DetailsSectionPagerAdapter;
import com.example.lazyplant.ui.plantDetailUI.main.InfoFragment;
import com.example.lazyplant.ui.plantDetailUI.main.PicsFragment;
import com.example.lazyplant.ui.plantDisplayHelper;
import com.example.lazyplant.ui.search.SearchResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PlantDetailsActivity extends AppCompatActivity {

    public  static  final String TAG = "PlantDetailsActivity";
    private DetailsSectionPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);

        Button button= (Button) findViewById(R.id.detail_back_button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlantDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String message;
        message = intent.getStringExtra(CalendarFragment.EXTRA_MESSAGE);
        if (message == null){
            message = intent.getStringExtra(FavouritesFragment.EXTRA_MESSAGE);
            if (message == null){
                message = intent.getStringExtra(SearchResult.EXTRA_MESSAGE);
            }
        }
        DbAccess databaseAccess = DbAccess.getInstance(this.getApplicationContext());
        databaseAccess.open();
        PlantInfo p = databaseAccess.getPlantInfo(message);
        databaseAccess.close();
        final String pid = p.getId();

        //Change switch if favourite'd already.
        /*final Switch sw = (Switch) findViewById(R.id.details_favourite_switch);
        AppDatabase database = Room.databaseBuilder(this.getApplication().getBaseContext(), AppDatabase.class, "db-favourites")
                .allowMainThreadQueries().build();
        FavouriteDAO favouriteDAO = database.getFavouriteDAO();
        List<Favourite> favs = favouriteDAO.getFavourites();
        sw.setChecked(checkIfFavourite(p.getId(), favs));

        sw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AppDatabase database = Room.databaseBuilder(PlantDetailsActivity.this, AppDatabase.class, "db-favourites")
                        .allowMainThreadQueries().build();
                FavouriteDAO favouriteDAO = database.getFavouriteDAO();
                List<Favourite> favs = favouriteDAO.getFavourites();
                if (checkIfFavourite(pid, favs)){
                    sw.setChecked(false);
                    favouriteDAO.deleteFavourite(pid);
                } else {
                    sw.setChecked(true);
                    Favourite x = new Favourite();
                    x.setSpecies_id(pid);
                    favouriteDAO.insert(x);
                }
            }
        });*/

        if(p != null){
            ImageView image = findViewById(R.id.details_image_main);
            plantDisplayHelper.displayDetailsPageImage(convertToImageName(
                    "Hymenosporum flavum ‘Gold Nugget’ – Native Frangipani"), image, this, 0.4);
            plantDisplayHelper.displayPlantTitle(p, (ConstraintLayout) findViewById(R.id.details_constraint_layout), image, this);
            plantDisplayHelper.displayPlantDetails(p, (ConstraintLayout) findViewById(R.id.details_constraint_layout), image, this);

        }

    }



    String convertToImageName(String name){
        String x = name;
        x = x.replaceAll("[^a-zA-Z0-9]", "");
        x = x.toLowerCase();
        return x;
    }

    boolean checkIfFavourite(String id, List<Favourite> favs){
        for (Favourite i : favs){
            if (i.getSpecies_id().equals(id)){
                return true;
            }
        }
        return false;
    }

}
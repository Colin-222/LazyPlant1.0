package com.example.lazyplant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.lazyplant.ui.search.SearchResult;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

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
        final Switch sw = (Switch) findViewById(R.id.details_favourite_switch);
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
        });

        if(p != null){
            TextView tmp;
            String x;
            tmp = findViewById(R.id.details_name);
            tmp.setText(p.getCommon_name());
            tmp = findViewById(R.id.details_scientific_name);
            tmp.setText(p.getScientific_name());

            x = p.getOther_names();
            if (x.equals("")){ tmp = findViewById(R.id.details_other_name);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_other_name);
                tmp.setText(tmp.getText() + x); }
            x = p.getType();
            if (x.equals("")){ tmp = findViewById(R.id.details_type);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_type);
                tmp.setText(tmp.getText() + x); }
            x = p.getFlowering_period();
            if (x.equals("")){ tmp = findViewById(R.id.details_flowering_period);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_flowering_period);
                tmp.setText(tmp.getText() + x); }
            x = p.getFrost_tolerance();
            if (x.equals("")){ tmp = findViewById(R.id.details_frost_tolerance);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_frost_tolerance);
                tmp.setText(tmp.getText() + x); }
            x = p.getLifespan();
            if (x.equals("")){ tmp = findViewById(R.id.details_lifespan);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_lifespan);
                tmp.setText(tmp.getText() + x); }
            x = p.getLight();
            if (x.equals("")){ tmp = findViewById(R.id.details_shade);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_shade);
                tmp.setText(tmp.getText() + x); }
            x = p.getWildlife();
            if (x.equals("")){ tmp = findViewById(R.id.details_wildlife_attracted);
                tmp.setText(tmp.getText() + "N/A");
            } else { tmp = findViewById(R.id.details_wildlife_attracted);
                tmp.setText(tmp.getText() + x); }

            String x2;
            x = p.getHeight_lower();
            x2 = p.getHeight_upper();
            tmp = findViewById(R.id.details_height);
            if (x.equals("") && !x2.equals("")){
                tmp.setText(tmp.getText() + x2 + "m");
            } else if (!x.equals("") && x2.equals("")) {
                tmp.setText(tmp.getText() + x + "m");
            } else if (!x.equals("") && !x2.equals("")){
                tmp.setText(tmp.getText() + x + "-" + x2 + "m");
            }
            x = p.getWidth_lower();
            x2 = p.getWidth_upper();
            tmp = findViewById(R.id.details_width);
            if (x.equals("") && !x2.equals("")){
                tmp.setText(tmp.getText() + x2 + "m");
            } else if (!x.equals("") && x2.equals("")) {
                tmp.setText(tmp.getText() + x + "m");
            } else if (!x.equals("") && !x2.equals("")){
                tmp.setText(tmp.getText() + x + "-" + x2 + "m");
            }


        }
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
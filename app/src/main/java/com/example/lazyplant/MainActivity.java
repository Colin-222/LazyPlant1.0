package com.example.lazyplant;

import android.os.Bundle;
import android.util.Log;

import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_calendar, R.id.navigation_favourites)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        /*DbAccess databaseAccess = DbAccess.getInstance(this);
        databaseAccess.open();
        List<String> x = databaseAccess.search();
        for (int i=0; i<x.size(); i++) {
            Log.i("TEST-DB", x.get(i));
        }
        PlantInfo p = databaseAccess.getPlantInfo("Alloxylon flameum – Tree Waratah");
        Log.i("TEST-DB", p.getType());
        databaseAccess.close();

        AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "db-favourites")
                .allowMainThreadQueries().build();
        FavouriteDAO favouriteDAO = database.getFavouriteDAO();
        Favourite fav = new Favourite();
        fav.setSpecies_id("Babingtonia virgata ‘Howie’s Sweet Midget’ – Heath Myrtle");
        favouriteDAO.insert(fav);
        //favouriteDAO.deleteAll();
        List<Favourite> y = favouriteDAO.getFavourites();
        for (int i=0; i<y.size(); i++) {
            Log.i("TEST-ROOM", y.get(i).getSpecies_id());
        }*/
    }

}

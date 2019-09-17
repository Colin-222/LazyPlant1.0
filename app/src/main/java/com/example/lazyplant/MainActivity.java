package com.example.lazyplant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.lazyplant.plantdata.AppDatabase;
import com.example.lazyplant.plantdata.Favourite;
import com.example.lazyplant.plantdata.FavouriteDAO;
import com.example.lazyplant.ui.reminder.ReminderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.util.Calendar;
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
                R.id.navigation_home, R.id.navigation_favourites, R.id.navigation_browse,
                R.id.navigation_search, R.id.navigation_reminder, R.id.navigation_plant_details).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(getIntent().getExtras() != null) {
            String message = getIntent().getExtras().getString(Constants.NOTIFICATION_TAG);
            if (message != null) {
                navController.navigate(R.id.action_navigation_home_to_navigation_reminder);
            }
        }

        //AlarmBroadcastReceiver.startAlarmBroadcastReceiver(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);

        /*SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.DEFAULT_POSTCODE, "2190");
        editor.putInt(Constants.REMINDER_HOUR, 8);
        editor.putInt(Constants.REMINDER_MINUTE, 12);
        editor.commit();*/

        /*pref.getString(Constants.DEFAULT_POSTCODE, null);
        pref.getInt(Constants.REMINDER_HOUR, -1);
        pref.getInt(Constants.REMINDER_MINUTE, -1);*/

    }

}

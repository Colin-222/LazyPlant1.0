package com.example.lazyplant.plantdata;

import android.database.sqlite.SQLiteCursor;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.lazyplant.ui.profile.BackupFragment;

@Database(entities = {Favourite.class, PlantCareRecord.class, GardenPlant.class,
PlantNotes.class}, version = 7)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDAO getFavouriteDAO();

    public abstract PlantCareRecordDAO getPlantCareRecordDAO();

    public abstract GardenPlantDAO getGardenPlantDAO();

    public abstract PlantNotesDAO getPlantNotesDAO();

}

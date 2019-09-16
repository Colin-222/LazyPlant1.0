package com.example.lazyplant.plantdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;

@Dao
public interface GardenPlantDAO {

    @Insert
    void insert(GardenPlant... gardenPlants);

    @Update
    void update(GardenPlant... gardenPlants);

    @Delete
    void delete(GardenPlant gardenPlants);

    @Query("SELECT * FROM gardenPlants")
    List<GardenPlant> getGardenPlants();

    @Query("SELECT * FROM gardenPlants WHERE rain_exposed=1")
    List<GardenPlant> getExposedGardenPlants();

    @Query("SELECT * FROM gardenPlants WHERE plant_id=:id LIMIT 1")
    List<GardenPlant> getGardenPlant(String id);

    @Query("DELETE FROM gardenPlants")
    void deleteAll();

    @Query("DELETE FROM GardenPlants WHERE plant_id=:id ")
    void deleteGardenPlant(String id);


}

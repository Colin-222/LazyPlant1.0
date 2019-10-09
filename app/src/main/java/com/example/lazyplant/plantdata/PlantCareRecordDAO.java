package com.example.lazyplant.plantdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantCareRecordDAO {

    @Insert
    void insert(PlantCareRecord... plantCareRecords);

    @Update
    void update(PlantCareRecord... plantCareRecords);

    @Delete
    void delete(PlantCareRecord plantCareRecords);

    @Query("SELECT * FROM plantCareRecords ORDER BY date DESC")
    List<PlantCareRecord> getPlantCareRecordsByDate();

    @Query("SELECT * FROM plantCareRecords")
    List<PlantCareRecord> getPlantCareRecords();

    @Query("SELECT * FROM plantCareRecords WHERE watering_id=:watering_id LIMIT 1")
    List<PlantCareRecord> getPlantCareRecord(String watering_id);

    @Query("SELECT * FROM plantCareRecords WHERE plant_id=:plant_id")
    List<PlantCareRecord> getPlantCareData(String plant_id);

    @Query("DELETE FROM plantCareRecords")
    void deleteAll();

    @Query("DELETE FROM plantCareRecords WHERE watering_id=:watering_id ")
    void deleteFavourite(String watering_id);


}

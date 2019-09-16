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

    @Query("SELECT * FROM plantCareRecords")
    List<PlantCareRecord> getPlantCareRecords();

    @Query("SELECT * FROM plantCareRecords WHERE plant_id=:id LIMIT 1")
    List<PlantCareRecord> getPlantCareRecord(String id);

    @Query("DELETE FROM plantCareRecords")
    void deleteAll();

    @Query("DELETE FROM plantCareRecords WHERE plant_id=:id ")
    void deleteFavourite(String id);


}

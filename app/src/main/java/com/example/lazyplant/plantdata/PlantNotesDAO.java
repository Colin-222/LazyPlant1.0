package com.example.lazyplant.plantdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Contains functions that can be used to access the Plant Notes database.
 */
@Dao
public interface PlantNotesDAO {

    @Insert
    void insert(PlantNotes... plant_notes);

    @Delete
    void delete(PlantNotes plant_notes);

    @Update
    void update(PlantNotes... plant_notes);

    @Query("SELECT * FROM plant_notes ")
    List<PlantNotes> getAll();

    @Query("SELECT * FROM plant_notes WHERE species_id=:id")
    PlantNotes getPlantNotes(String id);

    @Query("SELECT notes FROM plant_notes WHERE species_id=:id")
    String getNotes(String id);

    @Query("DELETE FROM plant_notes")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM plant_notes WHERE species_id=:id")
    int checkIfExists(String id);

    @Query("SELECT * FROM plant_notes ORDER BY last_edit")
    List<PlantNotes> getNotesRecent();

}


package com.example.lazyplant.plantdata;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

/**
 * Plant notes. It's used with Room.
 */
@Entity(tableName = "plant_notes")
public class PlantNotes {
    @NonNull
    @PrimaryKey
    private String species_id;

    @NonNull
    public String getSpecies_id() {
        return species_id;
    }

    public void setSpecies_id(@NonNull String species) {
        this.species_id = species;
    }


    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    private String notes;

    @NonNull
    private Calendar last_edit;
    @NonNull
    public Calendar getLast_edit() { return last_edit; }
    public void setLast_edit(@NonNull Calendar last_edit) { this.last_edit = last_edit; }


}

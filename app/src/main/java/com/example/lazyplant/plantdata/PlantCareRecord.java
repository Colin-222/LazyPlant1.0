package com.example.lazyplant.plantdata;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

/**
 * PlantCareRecord entity. Used as a table to store details on watering events.
 */
@Entity(tableName = "plantCareRecords")
public class PlantCareRecord {

    @NonNull
    @PrimaryKey
    private String plant_id;
    @NonNull
    public String getPlant_id() {
        return plant_id;
    }
    public void setPlant_id(@NonNull String plant) { this.plant_id = plant; }

    private String species_id;
    public String getSpecies_id() {
        return species_id;
    }
    public void setSpecies_id(@NonNull String species) {
        this.species_id = species;
    }

    @NonNull
    private String event;
    @NonNull
    public String getEvent() { return event; }
    public void setEvent(@NonNull String event) { this.event = event; }

    @NonNull
    private Calendar date;
    @NonNull
    public Calendar getDate() { return date; }
    public void setDate(@NonNull Calendar date) { this.date = date; }

    private String source;
    public String getSource() { return source; }
    public void setSource(@NonNull String source) { this.source = source; }


}

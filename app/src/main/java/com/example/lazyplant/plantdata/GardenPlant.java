package com.example.lazyplant.plantdata;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "gardenPlants")
public class GardenPlant {

    @NonNull
    @PrimaryKey
    private String plant_id;
    @NonNull
    public String getPlant_id() {
        return plant_id;
    }
    public void setPlant_id(@NonNull String plant) { this.plant_id = plant; }

    private String name;
    public String getName() {return name;}
    public void setName(String name) { this.name = name; }

    private String species_id;
    public String getSpecies_id() {
        return species_id;
    }
    public void setSpecies_id(@NonNull String species) {
        this.species_id = species;
    }

    private String notes;
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @NonNull
    private Calendar plant_date;
    @NonNull
    public Calendar getPlant_date() { return plant_date; }
    public void setPlant_date(@NonNull Calendar plant_date) { this.plant_date = plant_date; }

    @NonNull
    private Integer watering_interval;
    @NonNull
    public Integer getWatering_interval() { return watering_interval; }
    public void setWatering_interval(@NonNull Integer watering_interval) { this.watering_interval = watering_interval; }

    @NonNull
    private Calendar last_watering;
    @NonNull
    public Calendar getLast_watering() { return last_watering; }
    public void setLast_watering(@NonNull Calendar last_watering) { this.last_watering = last_watering; }

    @NonNull
    private boolean rain_exposed;
    @NonNull
    public boolean getRain_exposed() { return rain_exposed; }
    public void setRain_exposed(boolean rain_exposed) { this.rain_exposed = rain_exposed; }

}

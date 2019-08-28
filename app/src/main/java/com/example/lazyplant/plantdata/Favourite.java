package com.example.lazyplant.plantdata;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourites")
public class Favourite {
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
}

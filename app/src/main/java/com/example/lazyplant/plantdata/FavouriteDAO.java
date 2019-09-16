package com.example.lazyplant.plantdata;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavouriteDAO {

    @Insert
    void insert(Favourite... favourites);

    @Delete
    void delete(Favourite favourites);

    @Query("SELECT * FROM favourites")
    List<Favourite> getFavourites();

    @Query("SELECT * FROM favourites ORDER BY species_id")
    List<Favourite> getFavouriteAlphabetical();

    @Query("DELETE FROM favourites")
    void deleteAll();

    @Query("DELETE FROM favourites WHERE species_id=:id ")
    void deleteFavourite(String id);

}


package com.example.lazyplant;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.lazyplant.Favourite;
import com.example.lazyplant.FavouriteDAO;

@Database(entities = {Favourite.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavouriteDAO getFavouriteDAO();

}

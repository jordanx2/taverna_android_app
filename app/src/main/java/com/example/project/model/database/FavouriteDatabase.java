package com.example.project.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.project.model.Place;
import com.example.project.model.dao.PlaceDAO;

// Room Database class definition
@Database(entities = {Place.class}, version = 1)
public abstract class FavouriteDatabase extends RoomDatabase {

    // Abstract method to retrieve the Data Access Object (DAO) for the Place entity
    public abstract PlaceDAO getPlaceDAO();
}

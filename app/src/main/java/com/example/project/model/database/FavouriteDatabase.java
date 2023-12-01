package com.example.project.model.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.project.model.Place;
import com.example.project.model.dao.PlaceDAO;

@Database(entities = {Place.class}, version = 1)
public abstract class FavouriteDatabase extends RoomDatabase {
    public abstract PlaceDAO getPlaceDAO();
}

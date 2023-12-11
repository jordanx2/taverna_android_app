package com.example.project.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project.model.Place;
import java.util.List;

// Data Access Object (DAO) interface for the Place entity
@Dao
public interface PlaceDAO {
    // Methods to perform basic CRUD operations for Place entity
    @Insert
    public void insert(Place place);

    @Delete
    public void remove(Place place);

    @Query("SELECT * FROM Place;")
    public List<Place> getAllPlaces();

    @Update
    public void updatePlace(Place place);
}

package com.example.project.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.project.model.Place;
import java.util.List;

@Dao
public interface PlaceDAO {
    @Insert
    public void insert(Place place);

    @Delete
    public void remove(Place place);

    @Query("SELECT * FROM Place;")
    public List<Place> getAllPlaces();
}

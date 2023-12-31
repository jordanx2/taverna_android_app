package com.example.project.model;

import java.io.Serializable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName= "place")
public class Place implements Serializable {
    @NotNull
    @PrimaryKey
    private String placeID;
    @ColumnInfo(name = "place_name")
    private String placeName;

    @NotNull
    @ColumnInfo(name = "place_latitude")
    private double placeLatitude;

    @NotNull
    @ColumnInfo(name = "place_longitude")
    private double placeLongitude;

    @NotNull
    @ColumnInfo(name = "place_rating")
    private float placeRating;

    @ColumnInfo(name = "place_photo_reference")
    private String placePhotoReference;

    // Constructor to initialize a Place object with necessary information
    public Place(String placeName, double placeLatitude, double placeLongitude, String placeID, float placeRating, String placePhotoReference) {
        this.placeName = placeName;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.placeID = placeID;
        this.placeRating = placeRating;
        this.placePhotoReference = placePhotoReference;
    }

    // Getters & Setters for each property
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(long placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public void setPlaceLongitude(long placeLongitude) {
        this.placeLongitude = placeLongitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public float getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(float placeRating) {
        this.placeRating = placeRating;
    }

    public String getPlacePhotoReference() {
        return placePhotoReference;
    }

    public void setPlacePhotoReference(String placePhotoReference) {
        this.placePhotoReference = placePhotoReference;
    }
}

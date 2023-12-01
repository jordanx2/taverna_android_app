package com.example.project.model;

import java.io.Serializable;

public class Place implements Serializable {
    private String placeName;
    private double placeLatitude;
    private double placeLongitude;
    private String placeID;
    private int placeRating;
    private String placePhotoReference;


    public Place(String placeName, double placeLatitude, double placeLongitude, String placeID, int placeRating, String placePhotoReference) {
        this.placeName = placeName;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.placeID = placeID;
        this.placeRating = placeRating;
        this.placePhotoReference = placePhotoReference;
    }

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

    public double getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(int placeRating) {
        this.placeRating = placeRating;
    }

    public String getPlacePhotoReference() {
        return placePhotoReference;
    }

    public void setPlacePhotoReference(String placePhotoReference) {
        this.placePhotoReference = placePhotoReference;
    }
}

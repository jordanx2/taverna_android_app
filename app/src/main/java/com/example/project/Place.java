package com.example.project;

public class Place {
    private String placeName;
    private long placeLatitude;
    private long placeLongitude;
    private String placeID;
    private double placeRating;
    private String placePhotoReference;

    // Temp for for the mean time
    private int resourceID;

    public Place(String placeName, long placeLatitude, long placeLongitude, String placeID, double placeRating, String placePhotoReference, int resourceID) {
        this.placeName = placeName;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.placeID = placeID;
        this.placeRating = placeRating;
        this.placePhotoReference = placePhotoReference;
        this.resourceID = resourceID;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public long getPlaceLatitude() {
        return placeLatitude;
    }

    public void setPlaceLatitude(long placeLatitude) {
        this.placeLatitude = placeLatitude;
    }

    public long getPlaceLongitude() {
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

    public void setPlaceRating(double placeRating) {
        this.placeRating = placeRating;
    }

    public String getPlacePhotoReference() {
        return placePhotoReference;
    }

    public void setPlacePhotoReference(String placePhotoReference) {
        this.placePhotoReference = placePhotoReference;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }
}

package com.example.project.view;

public class FavouriteItem {
    private String favouriteName;
    private int favouriteImgResource;
    private String favouriteMapDetails;

    public FavouriteItem(String favouriteName, int favouriteImgResource, String favouriteMapDetails){
        this.favouriteName = favouriteName;
        this.favouriteImgResource = favouriteImgResource;
        this.favouriteMapDetails = favouriteMapDetails;
    }

    public String getFavouriteName() {
        return favouriteName;
    }

    public void setFavouriteName(String favouriteName) {
        this.favouriteName = favouriteName;
    }

    public int getFavouriteImgResource() {
        return favouriteImgResource;
    }

    public void setFavouriteImgResource(int favouriteImgResource) {
        this.favouriteImgResource = favouriteImgResource;
    }

    public String getFavouriteMapDetails() {
        return favouriteMapDetails;
    }

    public void setFavouriteMapDetails(String favouriteMapDetails) {
        this.favouriteMapDetails = favouriteMapDetails;
    }
}

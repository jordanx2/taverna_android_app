package com.example.project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project.R;
import com.example.project.model.ReadImage;
import com.example.project.model.Place;
import java.util.ArrayList;

// Adapter for displaying Place objects in a RecyclerView
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    // Interface for callback methods to handle item interactions
    public interface PlaceAdapterCallback {
        void onMapDirectionsClicked(Place place);
        void onFavouritesClicked(Place place);
    }

    // List of Place objects to be displayed
    private ArrayList<Place> newPlaces;

    // Callback interface reference
    private PlaceAdapterCallback callBack;

    // Google Maps API key
    private String API_KEY;

    // Constructor with two parameters
    public PlaceAdapter(ArrayList<Place> newPlaces, PlaceAdapterCallback callBack){
        this.newPlaces = newPlaces;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);

        // Initialize the Google Maps API Key
        API_KEY = view.getContext().getString(R.string.google_maps_key);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        // Gte the Place object at the given position
        Place place = newPlaces.get(position);

        // Load and set the image for the Place
        ReadImage readImage = new ReadImage();
        readImage.setImage(holder.establishmentItemImg, place.getPlacePhotoReference(), API_KEY);

        // Set click listener for the maps icon
        holder.establishmentDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onMapDirectionsClicked(place);
            }
        });

        // Set click listener for the favourite icon
        holder.establishmentFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onFavouritesClicked(place);
            }
        });

        // Set the name of the Place in the corresponding TextView
        holder.establismentItemName.setText(place.getPlaceName());
    }

    // Return the size of the dataset
    @Override
    public int getItemCount() {
        return newPlaces.size();
    }

    // Class to hold reference to the views for each item in the RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView establishmentItemImg;
        TextView establismentItemName;
        ImageView establishmentDirections;

        ImageView establishmentFavourite;



        ViewHolder(View view) {
            super(view);
            establishmentItemImg = view.findViewById(R.id.establishmentItemImg);
            establismentItemName = view.findViewById(R.id.establismentItemName);
            establishmentDirections = view.findViewById(R.id.establishmentItemDirections);
            establishmentFavourite = view.findViewById(R.id.establishmentItemAdd);

        }
    }

}

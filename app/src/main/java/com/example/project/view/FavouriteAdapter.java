package com.example.project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.project.R;
import com.example.project.model.ReadImage;
import com.example.project.model.Place;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    // Interface for callbacks to handle events in the adapter
    public interface ListenerCalback{
        public void onDeleteOptionClicked(Place place);
        public void onRatingBarChanged(Place place);
    }

    // List of favourite places
    private List<Place> favList;

    // API Key for Google Maps
    private String API_KEY;

    // Callback listener for handling events
    private ListenerCalback callback;

    // Constructor with two parameters
    public FavouriteAdapter(List<Place> favList, ListenerCalback callback){
        this.favList = favList;
        this.callback = callback;
    }

    // Create a new View holder when needed
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);

        // Get the API key from the context
        API_KEY = view.getContext().getString(R.string.google_maps_key);
        return new ViewHolder(view);
    }

    // Bind the data to the view holder
    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        // Get the place at the current position
        Place item = favList.get(position);

        // Set the name of the place in the text view
        holder.establismentName.setText(item.getPlaceName());

        // Load the image of the place
        ReadImage readImage = new ReadImage();
        readImage.setImage(holder.locationImg, item.getPlacePhotoReference(), API_KEY);

        // Set the rating of the place in the Rating Bar
        holder.ratingBar.setRating(item.getPlaceRating());

        // Set a click listener for the remove button
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trigger the callback function
                callback.onDeleteOptionClicked(item);
            }
        });

        // Set a listener of the remove item button
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Update place rating
                item.setPlaceRating(rating);

                // Trigger callback function
                callback.onRatingBarChanged(item);
            }
        });
    }

    // Return total number of items in the dataset
    @Override
    public int getItemCount() {
        return favList.size();
    }

    // Update the dataset with the new list of favourite places
    public void updateData(List<Place> favList){
        this.favList = favList;
        notifyDataSetChanged();
    }

    // Class to hold reference to the views for each item
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locationImg;
        TextView establismentName;
        ImageView removeItem;
        RatingBar ratingBar;

        ViewHolder(View view) {
            super(view);
            locationImg = view.findViewById(R.id.favItemImg);
            establismentName = view.findViewById(R.id.favEstablismentName);
            removeItem = view.findViewById(R.id.deleteFavourite);
            ratingBar = view.findViewById(R.id.ratingBar);

        }
    }
}

package com.example.project;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    public interface PlaceAdapterCallback {
        void onMapDirectionsClicked(String placeID);
        void onFavouritesClicked(Place place);
    }

    private ArrayList<Place> newPlaces;
    PlaceAdapterCallback callBack;

    public PlaceAdapter(ArrayList<Place> newPlaces, PlaceAdapterCallback callBack){
        this.newPlaces = newPlaces;
        this.callBack = callBack;
    }
    private String API_KEY;

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        API_KEY = view.getContext().getString(R.string.google_maps_key);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        Place place = newPlaces.get(position);
        ReadImage readImage = new ReadImage();
        readImage.setImage(holder.establishmentItemImg, place.getPlacePhotoReference(), API_KEY);

        holder.establishmentDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onMapDirectionsClicked(place.getPlaceID());
            }
        });

        holder.establishmentFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onFavouritesClicked(place);
            }
        });



        holder.establismentItemName.setText(place.getPlaceName());
    }

    @Override
    public int getItemCount() {
        return newPlaces.size();
    }

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

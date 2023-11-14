package com.example.project;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {
    private ArrayList<Place> newPlaces;

    public PlaceAdapter(ArrayList<Place> newPlaces){this.newPlaces = newPlaces;}

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new PlaceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        Place place = newPlaces.get(position);
        holder.establishmentItemImg.setImageResource(place.getResourceID());
        holder.establismentItemName.setText(place.getPlaceName());
    }

    @Override
    public int getItemCount() {
        return newPlaces.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView establishmentItemImg;
        TextView establismentItemName;


        ViewHolder(View view) {
            super(view);
            establishmentItemImg = view.findViewById(R.id.establishmentItemImg);
            establismentItemName = view.findViewById(R.id.establismentItemName);
        }
    }
}

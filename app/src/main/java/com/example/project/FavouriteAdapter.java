package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    private ArrayList<FavouriteItem> favList;

    public FavouriteAdapter(ArrayList<FavouriteItem> favList){
        this.favList = favList;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        FavouriteItem item = favList.get(position);
        holder.establismentName.setText(item.getFavouriteName());
        int test = item.getFavouriteImgResource();
        holder.locationImg.setImageResource(test);
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locationImg;
        TextView establismentName;

        ViewHolder(View view) {
            super(view);
            locationImg = view.findViewById(R.id.favItemImg);
            establismentName = view.findViewById(R.id.favEstablismentName);
        }
    }
}

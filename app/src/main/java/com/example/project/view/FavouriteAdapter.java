package com.example.project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.project.R;
import com.example.project.model.ReadImage;
import com.example.project.model.Place;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder>{
    private List<Place> favList;
    private String API_KEY;

    public FavouriteAdapter(List<Place> favList){
        this.favList = favList;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        API_KEY = view.getContext().getString(R.string.google_maps_key);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        Place item = favList.get(position);
        holder.establismentName.setText(item.getPlaceName());
        ReadImage readImage = new ReadImage();
        readImage.setImage(holder.locationImg, item.getPlacePhotoReference(), API_KEY);
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

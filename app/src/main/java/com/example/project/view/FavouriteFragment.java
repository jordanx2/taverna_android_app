package com.example.project.view;

import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.model.Place;
import com.example.project.model.dao.PlaceDAO;
import com.example.project.model.database.FavouriteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment implements FavouriteAdapter.ListenerCalback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlaceDAO placeDAO;
    private FavouriteAdapter.ListenerCalback callback = this;
    private FavouriteAdapter adapter;

    public FavouriteFragment() {
        adapter = new FavouriteAdapter(null, callback);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragments layout
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.favouritesRecycle);

        // Initialize the Room database
        placeDAO = Room.databaseBuilder(view.getContext(), FavouriteDatabase.class, "Place")
                .allowMainThreadQueries()
                .build()
                .getPlaceDAO();

        // Check if a place object is passed through arguments and insert into database
        if(getArguments() != null){
            Place place = (Place) getArguments().getSerializable("place");
            if(place != null){
                insertPlace(place);
            }
        }

        // Load and display the favourite places in the RecyclerView
        loadFavouritePlaces();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    // Insert the place into the database
    public void insertPlace(Place place){
        if(!isFavourite(place)) {
            try {
                placeDAO.insert(place);
                Toast.makeText(getContext(), "Added: " + place.getPlaceName(), Toast.LENGTH_LONG).show();
            } catch (SQLiteConstraintException e) {
                Log.e("insertion failure", "error inserting place into database");

            }
        } else{
            Toast.makeText(getContext(), place.getPlaceName() + " already exists", Toast.LENGTH_SHORT).show();
        }
    }

    // Load and update the RecyclerView
    public void loadFavouritePlaces(){
        adapter.updateData(placeDAO.getAllPlaces());
    }

    // Callback method for handling the delete option click in the adapter
    @Override
    public void onDeleteOptionClicked(Place place) {
        try{
            placeDAO.remove(place);
            loadFavouritePlaces();
            Toast.makeText(getContext(), "Deleted: " + place.getPlaceName(), Toast.LENGTH_SHORT).show();

        } catch(SQLException e){
            Log.e("SQLError", "error in removing item");
        }

    }

    // Check if a place is already a favourite
    public boolean isFavourite(Place place){
        List<Place> places = placeDAO.getAllPlaces();

        for(Place p : places){
            if(p.getPlaceID().matches(place.getPlaceID())){
                return true;
            }
        }

        return false;
    }

    // Callback method for handling changes in rating bar in the adapter
    @Override
    public void onRatingBarChanged(Place place) {
        try{
            placeDAO.updatePlace(place);
            loadFavouritePlaces();
        } catch(SQLException e){
            Log.e("SQLError", "error in updating rating bar");
        }
    }
}
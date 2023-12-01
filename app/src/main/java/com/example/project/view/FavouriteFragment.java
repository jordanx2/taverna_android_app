package com.example.project.view;

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

import com.example.project.R;
import com.example.project.model.Place;
import com.example.project.model.dao.PlaceDAO;
import com.example.project.model.database.FavouriteDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlaceDAO placeDAO;

    public FavouriteFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.favouritesRecycle);

        placeDAO = Room.databaseBuilder(view.getContext(), FavouriteDatabase.class, "Place")
                .allowMainThreadQueries()
                .build()
                .getPlaceDAO();

        Place addPlace = null;
        if(getArguments() != null){
            Place place = (Place) getArguments().getSerializable("place");
            if(place != null){
                try {
                    placeDAO.insert(place);
                } catch(SQLiteConstraintException e){
                    Log.e("insertion failure", "error inserting place into database");

                }
            }
        }

        loadFavouritePlaces();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(loadFavouritePlaces());

        return view;
    }

    public FavouriteAdapter loadFavouritePlaces(){
        return new FavouriteAdapter(placeDAO.getAllPlaces());
    }
}
package com.example.project.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.controller.MainActivity;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.model.Place;
import com.example.project.model.RetrieveEstablishments;
import com.example.project.model.RetrieveEstablishmentsCallback;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import android.Manifest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements PlaceAdapter.PlaceAdapterCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    static ArrayList<Place> placesCache = new ArrayList<>();
    static ArrayList<Place> placesBuffer = new ArrayList<>();
    static int currentIdx = 0;
    private static int kmSpinnerValue = 1000;
    private PlaceAdapter.PlaceAdapterCallback thisFragment = this;
    final int PERMISSION_REQUEST_CODE = 101;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        makeRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragments layout
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the spinner by ID
        Spinner distance = view.findViewById(R.id.kmSpinner);

        // Array adapter for spinner using custom layout string array resource
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_layout,
                getResources().getStringArray(R.array.kmOptions)
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance.setAdapter(adapter);

        // Set a listener for item selection on the spinner
        distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Extract the selected item from the spinner
                String item = String.valueOf(parent.getItemAtPosition(position));

                // Convert it to metres
                kmSpinnerValue = Integer.parseInt(item.substring(0, item.length() - 2)) * 1000;

                // Make the HTTP request with basic 1000m value
                makeRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Find RecyclerView and button through ID
        RecyclerView recyclerView = view.findViewById(R.id.homeRecycleView);
        Button gntBtn = view.findViewById(R.id.generateBtn);

        // Set a click listener for the generate button
        gntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Calculate the number of items to display and fill buffer
                int threshold = placesCache.size();
                int displayItems = threshold / 4;
                fillBuffer(displayItems);

                // Check to see if all items in cache have been viewed
                if(currentIdx < threshold){
                    currentIdx += displayItems;
                } else{
                    Toast.makeText(view.getContext(),
                            "All bars viewed within selected distance\nIncrease distance radius to view new bars",
                            Toast.LENGTH_LONG).show();
                    currentIdx = 0;
                    fillBuffer(displayItems);
                }

                // Create a PlaceAdapter with the buffer and set it to the RecyclerView
                PlaceAdapter placeAdapter = new PlaceAdapter(placesBuffer, thisFragment);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(placeAdapter);

                // Clear the buffer for the next round
                placesBuffer = new ArrayList<>();
                
            }
        });

        return view;
    }

    // Make request to the Google Place API with the selected distance
    public void makeRequest(){
        try {
            // Construct the request URL
            String requestString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.354440,-6.278720&radius="
                    + kmSpinnerValue + "&types=bar|night_club&keyword=-hotel&key="
                    + getString(R.string.google_maps_key);
            RetrieveEstablishments request = new RetrieveEstablishments(requestString, new RetrieveEstablishmentsCallback() {
                @Override
                public void onResult(JSONObject response) {
                    // Process API response
                    JSONArray array = (JSONArray) response.get("results");

                    // Update the cache and reset the buffer
                    placesCache = loadPlaces(array);
                    currentIdx = 0;
                    placesBuffer = new ArrayList<>();
                }

            });
            new Thread(request).start();
        } catch(ThreadDeath e){
            Log.e("Error", String.valueOf(e));
        }
    }

    // Fill the buffer with a specified number of items from the cache
    public void fillBuffer(int displayItems){
        try {
            for (int i = currentIdx; i < currentIdx + displayItems; i++) {
                placesBuffer.add(placesCache.get(i));
            }
        } catch(IndexOutOfBoundsException e){
            Log.e("Error", "index out of bounds");
        }
    }


    // Load a list of place objects from a JSON response
    public ArrayList<Place> loadPlaces(JSONArray response){
        try {
            ArrayList<Place> result = new ArrayList<>();
            for (int i = 0; i < response.size(); i++) {
                // Extract all relevant data from response
                JSONObject place = (JSONObject) response.get(i);
                String name = String.valueOf(place.get("name"));
                String placeID = String.valueOf(place.get("place_id"));

                JSONObject geo = (JSONObject) place.get("geometry");
                JSONObject location = (JSONObject) geo.get("location");

                double lat = Double.parseDouble(String.valueOf(location.get("lat")));
                double lng = Double.parseDouble(String.valueOf(location.get("lng")));

                JSONArray photoArray = (JSONArray) place.get("photos");
                String photoReference = "";
                if (photoArray != null) {
                    JSONObject photoObj = (JSONObject) photoArray.get(0);
                    photoReference = String.valueOf(photoObj.get("photo_reference"));
                }

                Place addPlace = new Place(name, lat, lng, placeID, 0, photoReference);
                result.add(addPlace);
            }

            return result;
        } catch(Exception e){
            Log.e("Error", String.valueOf(e));
        }

        return null;
    }

    // Callback method for handling click event on the map icon
    public void onMapDirectionsClicked(Place place) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        changeFragments(bundle, new MapFragment());
    }

    // Callback method for handling click event on favourite icon
    public void onFavouritesClicked(Place place) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        changeFragments(bundle, new FavouriteFragment());
    }

    // Method to change fragments and update the bottom navigation
    private void changeFragments(Bundle bundle, Fragment fragment){
        // Reference: https://stackoverflow.com/questions/45002974/replacing-fragments-in-android-studio
        if(!bundle.isEmpty()){
            fragment.setArguments(bundle);
        }

        if(fragment.getClass().equals(MapFragment.class)){
            ((MainActivity) getActivity()).setSelectedBottomNavigationItem(R.id.map);
        }

        else if(fragment.getClass().equals(FavouriteFragment.class)){
            ((MainActivity) getActivity()).setSelectedBottomNavigationItem(R.id.favourites);
        }

        // Replace the current fragment with the new fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
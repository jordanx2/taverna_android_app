package com.example.project.view;

import android.os.Bundle;
import android.util.Log;
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
import com.example.project.model.Place;
import com.example.project.model.RetrieveEstablishments;
import com.example.project.model.RetrieveEstablishmentsCallback;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

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
    }

    static ArrayList<Place> placesCache = new ArrayList<>();
    static ArrayList<Place> placesBuffer = new ArrayList<>();
    static int currentIdx = 0;
    static boolean sendRequest = true;
    private static int kmSpinnerValue = 1000;
    private int kmSpinnerPosition = 1;
    private PlaceAdapter.PlaceAdapterCallback thisFragment = this;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Spinner distance = view.findViewById(R.id.kmSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_layout,
                getResources().getStringArray(R.array.kmOptions)
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distance.setAdapter(adapter);

        distance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = String.valueOf(parent.getItemAtPosition(position));
                kmSpinnerValue = Integer.parseInt(item.substring(0, item.length() - 2)) * 1000;
                kmSpinnerPosition = position;
                makeRequest();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        RecyclerView recyclerView = view.findViewById(R.id.homeRecycleView);
        Button gntBtn = view.findViewById(R.id.generateBtn);
        gntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int threshold = placesCache.size();
                int displayItems = threshold / 4;

                if(currentIdx < threshold) {
                    fillBuffer(displayItems);
                    currentIdx += displayItems;
                    sendRequest = false;
                }
                else{
                    Toast.makeText(view.getContext(), "All bars viewed within: " + (kmSpinnerValue / 1000) + "km.\nIncreased distance by 1km", Toast.LENGTH_LONG).show();

                    kmSpinnerPosition += 1;
                    distance.setSelection(kmSpinnerPosition);
                    String spinnerValue = String.valueOf(distance.getItemAtPosition(kmSpinnerPosition));
                    int spinnerInt = Integer.parseInt(spinnerValue.substring(0, spinnerValue.length() - 2)) * 1000;
                    kmSpinnerValue = spinnerInt;

                    sendRequest = true;
                    currentIdx = 0;
                    makeRequest();
                    fillBuffer((placesCache.size()) / 4);

                }

                PlaceAdapter placeAdapter = new PlaceAdapter(placesBuffer, thisFragment);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(placeAdapter);
                placesBuffer = new ArrayList<>();

            }
        });

        return view;
    }


    public void makeRequest(){
        try {
            if (sendRequest) {
                String requestString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.354440,-6.278720&radius="
                        + kmSpinnerValue + "&type=bar&key="
                        + getString(R.string.google_maps_key);
                RetrieveEstablishments request = new RetrieveEstablishments(requestString, new RetrieveEstablishmentsCallback() {
                    @Override
                    public void onResult(JSONObject response) {
                        JSONParser parser = new JSONParser();
                        JSONArray array = (JSONArray) response.get("results");
                        placesCache = loadPlaces(array);
                        currentIdx = 0;
                        placesBuffer = new ArrayList<>();
                    }

                });
                new Thread(request).start();
            }
        } catch(ThreadDeath e){
            Log.e("Error", String.valueOf(e));
        }
    }

    public void fillBuffer(int displayItems){
        try {
            for (int i = currentIdx; i < currentIdx + displayItems; i++) {
                placesBuffer.add(placesCache.get(i));
            }
        } catch(IndexOutOfBoundsException e){
            Log.e("Error", "index out of bounds");
        }
    }


    public ArrayList<Place> loadPlaces(JSONArray response){
        try {
            ArrayList<Place> result = new ArrayList<>();
            for (int i = 0; i < response.size(); i++) {
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

    @Override
    public void onMapDirectionsClicked(Place place) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        changeFragments(bundle, new MapFragment());
    }

    @Override
    public void onFavouritesClicked(Place place) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("place", place);
        changeFragments(bundle, new FavouriteFragment());
        Toast.makeText(getContext(), "Added: " + place.getPlaceName(), Toast.LENGTH_LONG).show();
    }

    private void changeFragments(Bundle bundle, Fragment fragment){
        if(!bundle.isEmpty()){
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
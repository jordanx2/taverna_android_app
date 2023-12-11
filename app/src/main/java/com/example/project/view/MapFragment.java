package com.example.project.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.R;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.model.Place;
import com.example.project.model.RetrieveEstablishments;
import com.example.project.model.RetrieveEstablishmentsCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.graphics.Color;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // Used for displaying the Google map
    private MapView mapView;

    // Object for interacting with the map
    private GoogleMap maps;

    // Store the users current location
    private LatLng userCoordinates;


    public MapFragment() {
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize MapView and set up async loading
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        // Reference: https://stackoverflow.com/questions/35496493/getmapasync-in-fragment
        mapView.getMapAsync(this);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    // Callback method for when the GoogleMap object is ready
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        maps = googleMap;

        // Check location permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Enable my location on map if permission is granted
            maps.setMyLocationEnabled(true);
        }

        // Configure map UI settings
        // Reference: https://developers.google.com/android/reference/com/google/android/gms/maps/UiSettings#setMyLocationButtonEnabled(boolean)
        maps.getUiSettings().setMyLocationButtonEnabled(false);

        // Get the FusedLocationProviderClient
        FusedLocationProviderClient clientLocation = LocationServices.getFusedLocationProviderClient(getContext());

        // Get the destination coordinates if provided
        LatLng destination = null;
        Place place = null;
        if(getArguments() != null){
            Place placePassed = (Place) getArguments().getSerializable("place");
            if(placePassed != null){
                place = placePassed;
            }
        }

        // Set the destination marker and camera position if a place was provided
        if(place != null){
            destination = new LatLng(place.getPlaceLatitude(), place.getPlaceLongitude());
            maps.addMarker(new MarkerOptions().position(destination)).setTitle(place.getPlaceName());
            maps.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 14f));
        }

        LatLng finalDestination = destination;

        // Get the users last known coordinates
        clientLocation.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Get the users longitude and latitude position (used in production)
                    double userLatitude = location.getLatitude();
                    double userLongitude = location.getLongitude();

                    // Update the camera position (used in production)
//                    LatLng userLatLng = new LatLng(userLatitude, userLongitude);

                    // Testing coordinates (TUD)
                    double testLat = 53.354440;
                    double testLong = -6.278720;
                    userCoordinates = new LatLng(testLat, testLong);


                    // Set the camera position and add a marker for the users location
                    maps.moveCamera(CameraUpdateFactory.newLatLngZoom(userCoordinates, 14f));
                    maps.addMarker(new MarkerOptions().position(userCoordinates)).setTitle("Your location");

                    if(finalDestination != null){
                        // Make a HTTP request
                        requestDirections(finalDestination);
                    }

                }
            }
        });

    }

    // Method to request directions from user's location to a destination
    public void requestDirections(LatLng destination){
        // Construct the directions request URL
        String request = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + userCoordinates.latitude
                + ","
                + userCoordinates.longitude
                +"&destination="
                + destination.latitude
                + ","
                + destination.longitude
                + "1&key="
                + getString(R.string.google_maps_directions_key);

        try {
            // Make a asynchronous request to retrieve directions
            RetrieveEstablishments makeRequest = new RetrieveEstablishments(request, new RetrieveEstablishmentsCallback() {
                @Override
                public void onResult(JSONObject response) {
                    // Parse response and plot directions on the map
                    JSONArray routes = (JSONArray) response.get("routes");
                    JSONObject route = (JSONObject) routes.get(0);
                    JSONObject polyline = (JSONObject) route.get("overview_polyline");
                    String encodedPoints = String.valueOf(polyline.get("points"));
                    List<LatLng> pointsList = PolyUtil.decode(encodedPoints);
                    plotDirections(pointsList);

                }

            });
            new Thread(makeRequest).start();
        } catch(Exception e){}
    }

    // Method to plot directions on the map using polyline
    // Reference: https://developers.google.com/maps/documentation/android-sdk/reference/com/google/android/libraries/maps/model/Polyline
    public void plotDirections(List<LatLng> pointsList){
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (LatLng point : pointsList) {
            options.add(point);
        }
        this.maps.addPolyline(options);
    }
}
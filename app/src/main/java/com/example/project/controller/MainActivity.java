package com.example.project.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import com.example.project.R;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.view.FavouriteFragment;
import com.example.project.view.HomeFragment;
import com.example.project.view.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // Used for requesting location permissions
    public static final int PERMISSION_REQUEST_CODE = 101 ;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Replace the initial fragment with the home fragment
        replaceFragment(new HomeFragment());

        // Request location permissions from the user
        requestLocationPermissions();

        // Set up bottom navigation view item selection listener
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            }
            if(item.getItemId() == R.id.map) {
                replaceFragment(new MapFragment());
            }

            if(item.getItemId() == R.id.favourites) {
                replaceFragment(new FavouriteFragment());
            }
            return true;
        });
    }

    // Request location permissions from user
    // Reference: https://stackoverflow.com/questions/44370162/get-location-permissions-from-user-in-android-application
    private void requestLocationPermissions() {
        // Check if the location permissions are not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check if rationale for the location permission is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                // Request the location permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Replace the current fragment with the provided fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

    // Set the selected item in the bottom navigation view
    public void setSelectedBottomNavigationItem(int itemId) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(itemId);
    }
}
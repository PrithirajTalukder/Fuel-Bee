package com.example.fuelbee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class fuelLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button btnProceed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnProceed = findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to Login Activity
                Intent intent = new Intent(fuelLocation.this, Login.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("You are here"));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        // Add some dummy markers around the current location
                        addDummyMarkers(currentLocation);

                        // Show a Toast message
                        Toast.makeText(fuelLocation.this, "These are the petrol pumps near you", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void addDummyMarkers(LatLng currentLocation) {
        // Define some dummy gas pump locations around the current location
        LatLng gasPump1 = new LatLng(currentLocation.latitude + 0.01, currentLocation.longitude);
        LatLng gasPump2 = new LatLng(currentLocation.latitude - 0.01, currentLocation.longitude);
        LatLng gasPump3 = new LatLng(currentLocation.latitude, currentLocation.longitude + 0.01);
        LatLng gasPump4 = new LatLng(currentLocation.latitude, currentLocation.longitude - 0.01);

        // Add markers for the dummy gas pumps
        gMap.addMarker(new MarkerOptions().position(gasPump1).title("Gas Pump 1").snippet("Address 1"));
        gMap.addMarker(new MarkerOptions().position(gasPump2).title("Gas Pump 2").snippet("Address 2"));
        gMap.addMarker(new MarkerOptions().position(gasPump3).title("Gas Pump 3").snippet("Address 3"));
        gMap.addMarker(new MarkerOptions().position(gasPump4).title("Gas Pump 4").snippet("Address 4"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }
}

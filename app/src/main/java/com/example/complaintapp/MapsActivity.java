package com.example.complaintapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button vGet_Lat_Lang;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        vGet_Lat_Lang = (Button) findViewById(R.id.Get_Lat_Lang);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        latLng = sydney;
        mMap.addMarker(new MarkerOptions()
                .title("Complaint Location")
                .position(sydney)
                .draggable(true));

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.d("Marker Start","lat = " + marker.getPosition().latitude + "Long  = " + marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                Log.d("Marker End","lat = " + marker.getPosition().latitude + "Long  = " + marker.getPosition().longitude);
                latLng = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        vGet_Lat_Lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MapsActivity.this,"lat = " + latLng.latitude + "Log = " +latLng.longitude,Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("lat",Double.toString(latLng.latitude));
                intent.putExtra("long",Double.toString(latLng.longitude));
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String cityName = addresses.get(0).getPostalCode();
                String stateName = addresses.get(0).getAdminArea().toUpperCase();
                String countryName = addresses.get(0).getCountryName().toUpperCase();
                intent.putExtra("Country",countryName);
                intent.putExtra("State",stateName);
                intent.putExtra("District",cityName);

                //Toast.makeText(MapsActivity.this,"Country = " + countryName + " State = " + stateName + " District" + cityName,Toast.LENGTH_LONG ).show();
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }



}

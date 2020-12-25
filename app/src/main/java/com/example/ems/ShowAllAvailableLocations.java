package com.example.ems;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ems.DriverAllActivity.Drivers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowAllAvailableLocations extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private GoogleMap mMap;
    public static String clickedKey;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public String key;
    DatabaseReference databaseReference, databaseReference2;
    double lan, lon;
    public static String clickedName, clickedPhone;
    LocationListener locationListener;
    LocationManager locationManager;
    Marker mk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_available_locations);
        databaseReference = FirebaseDatabase.getInstance().getReference("ActiveLocation");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Driver");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                final LiveLocations liveLocations = new LiveLocations(latitude, longitude);
                Geocoder geocoder = new Geocoder(getApplicationContext());

                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality() + ":";
                    result += addresses.get(0).getCountryName();
                    LatLng latLng = new LatLng(latitude, longitude);
                    if (mk != null) {
                        mk.remove();
                        mk = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    } else {
                        mk = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try{
            mMap=googleMap;
            int size=MainActivity.myArray.size();
            int j=0;
            for(int i=0;i<size;i=i+2){
                lan=MainActivity.myArray.get(i);
                lon=MainActivity.myArray.get(i+1);
                try{
                    setMarker(MainActivity.arr[j]);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
                }

                j++;

            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT);
        }


    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    public void setMarker(String keyValue){

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(lan, lon);
        mMap.addMarker(new MarkerOptions().position(latLng).title(keyValue).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambmainresized)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        setCamera();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                clickedKey=marker.getTitle();
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(@NonNull DataSnapshot ds:dataSnapshot.getChildren()){
                            if(ds.getKey().equals(clickedKey)){
                                Drivers drivers=new Drivers();
                                drivers.setdName(ds.getValue(Drivers.class).getdName());
                                drivers.setdPhone(ds.getValue(Drivers.class).getdPhone());
                                clickedName=drivers.getdName();
                                clickedPhone=drivers.getdPhone();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent=new Intent(getApplicationContext(),DriversInfo.class);
                startActivity(intent);
                return true;
            }
        });
    }
    public void setCamera(){

    }

}

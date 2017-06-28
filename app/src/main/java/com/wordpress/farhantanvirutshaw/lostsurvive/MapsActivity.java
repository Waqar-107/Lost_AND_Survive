package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mapTypeBtn;
    private double mLatitude;
    private double mLongitude;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapTypeBtn = (Button) findViewById(R.id.maptype_btn);

        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void setLocation() {
        mLatitude = Double.parseDouble(sharedPreferences.getString(Constants.PREF_KEY_LATITUDE, "0.0"));
        mLongitude = Double.parseDouble(sharedPreferences.getString(Constants.PREF_KEY_LONGITUDE, "0.0"));

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


        setLocation();
        // Add a marker in Sydney and move the camera
        LatLng currentPlace = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(currentPlace).title("My Location"));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPlace));
    }

    public void onSearch(View view) {
        EditText location_tf = (EditText)findViewById(R.id.search_edittext);
        String location = location_tf.getText().toString();
        List<Address> addressList=null;

        if(location.trim().length()>0 )
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                 addressList = geocoder.getFromLocationName(String.valueOf(location),1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addressList!=null && addressList.size()>0)
            {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        }
    }

    public void changeType(View view) {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mapTypeBtn.setText("Satelite");
        }
        else
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mapTypeBtn.setText("Normal");
        }
    }
}

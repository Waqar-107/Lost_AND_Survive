package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by utshaw on 6/25/17.
 */

public class GPSTrackerActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mCountryText;
    protected TextView mCountryCodeText;
    protected TextView mPostalCodeText;
    protected TextView mLocalityText;
    protected TextView mAreaText;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor sharedEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        sharedPreferences = getSharedPreferences("MyData",MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        mLatitudeText = (TextView) findViewById(R.id.latitude_val);
        mLongitudeText = (TextView) findViewById(R.id.longitutde_val);
        mCountryText = (TextView) findViewById(R.id.country_val);
        mCountryCodeText = (TextView) findViewById(R.id.ccode_val);
        mPostalCodeText = (TextView) findViewById(R.id.pcode_val);
        mLocalityText = (TextView) findViewById(R.id.locality_val);
        mAreaText = (TextView) findViewById(R.id.area_val);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Button showoMapsBtn = (Button) findViewById(R.id.show_map);
        showoMapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GPSTrackerActivity.this,MapsActivity.class));
            }
        });


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
//                Intent intent = new Intent();
//                intent.putExtra("Longitude", mLastLocation.getLongitude());
//                intent.putExtra("Latitude", mLastLocation.getLatitude());
//                setResult(1,intent);
//                finish();
                double currenntLatitude = mLastLocation.getLatitude();
                double currentLongitude = mLastLocation.getLongitude();
                sharedEditor.putString("PREF_KEY_LATITUDE",String.valueOf(currenntLatitude));
                sharedEditor.putString("PREF_KEY_LONGITUDE",String.valueOf(currentLongitude));
                sharedEditor.apply();
                mLatitudeText.setText(String.valueOf(currenntLatitude));
                mLongitudeText.setText(String.valueOf(currentLongitude));
                getAddress(currenntLatitude,currentLongitude);


            }
        } catch (SecurityException e) {

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed to fetch your location ", Toast.LENGTH_SHORT).show();
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(GPSTrackerActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses.size()>0)
            {
                Address obj = addresses.get(0);
                String area = obj.getAddressLine(0);

                String temp = obj.getCountryName();
                if( temp != null)
                {
                    mCountryText.setText(temp);
                }
                mAreaText.setText(area);
                temp = obj.getCountryCode();
                if( temp != null)
                {
                    mCountryCodeText.setText(temp);
                }
                temp= obj.getPostalCode();
                if( temp != null)
                {
                    mPostalCodeText.setText(temp);
                }
                temp = obj.getLocality();
                if( temp != null)
                {
                    mLocalityText.setText(temp);
                }
            }
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





}
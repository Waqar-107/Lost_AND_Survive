package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.wordpress.farhantanvirutshaw.lostsurvive.police.PoliceListActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by utshaw on 6/25/17.
 */

public class GPSTrackerActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private AlertDialog mInternetDialog;
    Location mLastLocation;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mCountryText;
    protected TextView mCountryCodeText;
    protected TextView mPostalCodeText;
    protected TextView mLocalityText;
    protected TextView mAreaText;
    LocationManager mLocationManager;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor sharedEditor;

    double currenntLatitude=0.0;
    double currentLongitude=0.0;

    private static final int WIFI_ENABLE_REQUEST = 0x1006;
    ConnectivityManager connMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.gps_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Location Info");

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

        Button showPoliceButton = (Button)findViewById(R.id.show_police);
        showPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDeviceOnline())
                {
                    showNoInternetDialog();
                }
                else
                {
                    Utils.setCurrentLatitude(currenntLatitude);
                    Utils.setCurrentLongitude(currentLongitude);
                    startActivity(new Intent(GPSTrackerActivity.this, PoliceListActivity.class));
                }
            }
        });


    }

    private void showNoInternetDialog() {

        if (mInternetDialog != null && mInternetDialog.isShowing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Internet Disabled!");
        builder.setMessage("No active Internet connection found.");
        builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                startActivityForResult(gpsOptionsIntent, WIFI_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mInternetDialog = builder.create();
        mInternetDialog.show();
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==WIFI_ENABLE_REQUEST) {

            if(connMgr == null)
            {
                connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if(networkInfo==null || !networkInfo.isConnected())
            {
                showNoInternetDialog();
            }
            else
            {
                startActivity(new Intent(GPSTrackerActivity.this, PoliceListActivity.class));
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                currenntLatitude = mLastLocation.getLatitude();
                currentLongitude = mLastLocation.getLongitude();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gps_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.refresh:
                View view = getWindow().getDecorView().findViewById(android.R.id.content);
                if(!isDeviceOnline() && !isGPSEnabled())
                {
                    final Snackbar snackbar = Snackbar.make(view,"Requires Internet & GPS",Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else if(!isDeviceOnline())
                {
                    final Snackbar snackbar = Snackbar.make(view,"Check Internet Connection",Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else if(!isGPSEnabled())
                {
                    final Snackbar snackbar = Snackbar.make(view,"Enable GPS",Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else
                {
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
        }
        return super.onOptionsItemSelected(item);
    }


    private boolean isGPSEnabled()
    {

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return  true;
        }else{
            return false;
        }
    }
}
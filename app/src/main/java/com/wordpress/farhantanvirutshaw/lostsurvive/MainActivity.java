package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    LocationManager mLocationManager;
    ConnectivityManager connMgr;

    private static final int GPS_ENABLE_REQUEST = 0x1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;


    final private int REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        toolbar = (Toolbar) findViewById(R.id.main_activity_appbar);
        setSupportActionBar(toolbar);

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_email:
                startActivity(new Intent(MainActivity.this,StartGmail.class));
                break;
            case R.id.menu_location:
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                {
                    int permission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                    if(permission != PackageManager.PERMISSION_GRANTED)
                    {
                        if(!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION))
                        {
                            showMessageOKCancel("You need to allow access to your Location",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS);
                                            }
                                        }
                                    });
                            break;
                        }
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS);

                        break;
                    }

                }
                if (!isGooglePlayServicesAvailable()) {
                    acquireGooglePlayServices();
                }
                if(!isGPSEnabled())
                {
                    showGPSDisabledDialog();
                }
                if(isGPSEnabled() && isGooglePlayServicesAvailable())
                {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (!isGooglePlayServicesAvailable()) {
                        acquireGooglePlayServices();
                    }
                    if(!isGPSEnabled())
                    {
                        showGPSDisabledDialog();
                    }
                    if( isGPSEnabled() && isGooglePlayServicesAvailable())
                    {
                        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                        startActivity(intent);

                    }
                }
                else
                {
                    Toast.makeText(this, "You need to enable GPS to use this feature", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_GOOGLE_PLAY_SERVICES) {
            if (resultCode != RESULT_OK) {

                final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Requires Google Play Services", Snackbar.LENGTH_LONG);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();

            } else {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        }
        if (requestCode == GPS_ENABLE_REQUEST ) {

            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }

            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
            {
                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
            else if( !(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
            {
                showGPSDisabledDialog();
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private boolean isGPSEnabled()
    {

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return  true;
        }else{
            return false;
        }
    }



    public void showGPSDisabledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Disabled");
        builder.setMessage("Gps is disabled, in order to use the application properly you need to enable GPS of your device");
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_REQUEST);
            }
        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog mGPSDialog = builder.create();
        mGPSDialog.show();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }




}



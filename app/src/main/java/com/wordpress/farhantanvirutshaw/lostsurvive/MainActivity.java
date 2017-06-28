package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.*;
import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    private AlertDialog mInternetDialog;
    LocationManager mLocationManager;
    ConnectivityManager connMgr;
    private AlertDialog mGPSDialog;
    private static final int WIFI_ENABLE_REQUEST = 0x1006;
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





//        seUpViews();
    }


//    private void seUpViews() {
//        TextView mountainTextView = (TextView) findViewById(R.id.text_view_mountain);
//        TextView jungleTextView  = (TextView) findViewById(R.id.text_view_jungle);
//        TextView desertTextView  = (TextView) findViewById(R.id.text_view_desert);
//        TextView islandTextView =  (TextView) findViewById(R.id.text_view_island);
//        TextView iceTextView =  (TextView) findViewById(R.id.text_view_ice);
//        TextView foreignTextView =  (TextView) findViewById(R.id.text_view_fland);
//
//        Typeface mountainFont = Typeface.createFromAsset(getAssets(), "fonts/font_mountain.ttf");
//        Typeface jungleFont = Typeface.createFromAsset(getAssets(),"fonts/font_jungle2.otf");
//        Typeface desertFont = Typeface.createFromAsset(getAssets(),"fonts/font_desert.ttf");
//        Typeface islandFont = Typeface.createFromAsset(getAssets(), "fonts/font_island2.ttf");
//        Typeface iceFont = Typeface.createFromAsset(getAssets(), "fonts/font_ice.ttf");
//        Typeface foreignfont = Typeface.createFromAsset(getAssets(), "fonts/font_foreign.ttf");
//
//        mountainTextView.setTypeface(mountainFont);
//        jungleTextView.setTypeface(jungleFont);
//        desertTextView.setTypeface(desertFont);
//        islandTextView.setTypeface(islandFont);
//        iceTextView.setTypeface(iceFont);
//        foreignTextView.setTypeface(foreignfont);
//    }


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
                if(!isDeviceOnline())
                {
                    showNoInternetDialog();
                }
                if(!isGPSEnabled())
                {
                    showGPSDisabledDialog();
                }
                if(isDeviceOnline() && isGPSEnabled() && isGooglePlayServicesAvailable())
                {
                    Intent intent = new Intent(MainActivity.this, GPSTempActivity.class);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (!isGooglePlayServicesAvailable()) {
                        acquireGooglePlayServices();
                    }
                    if(!isDeviceOnline())
                    {
                        showNoInternetDialog();
                    }
                    if(!isGPSEnabled())
                    {
                        showGPSDisabledDialog();
                    }
                    if(isDeviceOnline() && isGPSEnabled() && isGooglePlayServicesAvailable())
                    {
//                        Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
                        Intent intent = new Intent(MainActivity.this, GPSTempActivity.class);
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
//                Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
                Intent intent = new Intent(MainActivity.this, GPSTempActivity.class);
                startActivity(intent);
            }
        }
        if (requestCode == GPS_ENABLE_REQUEST || requestCode==WIFI_ENABLE_REQUEST) {

            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            }
            if(connMgr == null)
            {
                connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            }
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && networkInfo!= null && networkInfo.isConnected() )
            {
//                Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
                Intent intent = new Intent(MainActivity.this, GPSTempActivity.class);
                startActivity(intent);
                return;
            }
            else if(requestCode == GPS_ENABLE_REQUEST && !(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)))
            {
                showGPSDisabledDialog();
            }
            else if(requestCode == WIFI_ENABLE_REQUEST && (networkInfo==null || !networkInfo.isConnected()) )
            {
                showNoInternetDialog();
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGPSEnabled()
    {

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return  true;
        }else{
            return false;
        }
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
        mGPSDialog = builder.create();
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



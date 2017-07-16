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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    LocationManager mLocationManager;
    ConnectivityManager connMgr;

    private static final int GPS_ENABLE_REQUEST = 0x1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;


    final private int REQUEST_CODE_ASK_ACCESS_FINE_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_old);
        setContentView(R.layout.activity_main_nav);


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,StartGmail.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        FragmentManager manager = getSupportFragmentManager();

        if(getIntent().getStringExtra(Opening_Screen.CALLER_ACTIVITY)!=null)
        {
            switch (getIntent().getStringExtra(Opening_Screen.FRAGMENT_NAME)) {

                case "jungle":
                    JungleFragment jungleFragment = new JungleFragment();
                    manager.beginTransaction().replace(R.id.content_main, jungleFragment, jungleFragment.getTag()).commit();
                    getSupportActionBar().setTitle("Jungle");
                    break;
                case "desert":
                    DesertFragment desertFragment = new DesertFragment();
                    manager.beginTransaction().replace(R.id.content_main, desertFragment, desertFragment.getTag()).commit();
                    getSupportActionBar().setTitle("Desert");
                    break;
                case "mountain":
                    MountainFragment mountainFragment = new MountainFragment();
                    manager.beginTransaction().replace(R.id.content_main, mountainFragment, mountainFragment.getTag()).commit();
                    getSupportActionBar().setTitle("Mountain");
                    break;
                case "abroad":
                    AbroadFragment abrFragment = new AbroadFragment();
                    manager.beginTransaction().replace(R.id.content_main, abrFragment, abrFragment.getTag()).commit();
                    getSupportActionBar().setTitle("Abroad");
                    break;
                case "island":
                    IslandFragment islandFragment = new IslandFragment();
                    manager.beginTransaction().replace(R.id.content_main, islandFragment, islandFragment.getTag()).commit();
                    getSupportActionBar().setTitle("Island");
                    break;
            }
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
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


    //-----------------------------------------------------navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_desert)
        {
            DesertFragment desertFragment = new DesertFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, desertFragment, desertFragment.getTag()).commit();
            getSupportActionBar().setTitle("Desert");

        }

        else if (id == R.id.nav_island)
        {
            IslandFragment islandFragment = new IslandFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, islandFragment, islandFragment.getTag()).commit();
            getSupportActionBar().setTitle("Island");
        }

        else if (id == R.id.nav_mountains)
        {
            MountainFragment mountainFragment = new MountainFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, mountainFragment, mountainFragment.getTag()).commit();
            getSupportActionBar().setTitle("Mountain");
        }

        else if (id == R.id.nav_jungle)
        {
            JungleFragment jungleFragment = new JungleFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, jungleFragment, jungleFragment.getTag()).commit();
            getSupportActionBar().setTitle("Jungle");

        }

        else if(id==R.id.nav_abroad)
        {
            AbroadFragment abrFragment = new AbroadFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, abrFragment, abrFragment.getTag()).commit();
            getSupportActionBar().setTitle("Abroad");
        }

        else if (id == R.id.nav_share)
        {
            startActivity(new Intent(this,Compass.class));
        }

        else if (id == R.id.nav_send)
        {
            startActivity(new Intent(this,FlashlightActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //-----------------------------------------------------navigation drawer



}



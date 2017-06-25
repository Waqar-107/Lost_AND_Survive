package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt;

    private AlertDialog mInternetDialog;
    LocationManager mLocationManager;
    ConnectivityManager connMgr;
    private AlertDialog mGPSDialog;
    private static final int WIFI_ENABLE_REQUEST = 0x1006;
    private static final int GPS_ENABLE_REQUEST = 0x1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);


        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        toolbar = (Toolbar) findViewById(R.id.main_activity_appbar);
        setSupportActionBar(toolbar);

        txt = (TextView) findViewById(R.id.txt);


//        seUpViews();
    }


    private void seUpViews() {
        TextView mountainTextView = (TextView) findViewById(R.id.text_view_mountain);
        TextView jungleTextView  = (TextView) findViewById(R.id.text_view_jungle);
        TextView desertTextView  = (TextView) findViewById(R.id.text_view_desert);
        TextView islandTextView =  (TextView) findViewById(R.id.text_view_island);
        TextView iceTextView =  (TextView) findViewById(R.id.text_view_ice);
        TextView foreignTextView =  (TextView) findViewById(R.id.text_view_fland);

        Typeface mountainFont = Typeface.createFromAsset(getAssets(), "fonts/font_mountain.ttf");
        Typeface jungleFont = Typeface.createFromAsset(getAssets(),"fonts/font_jungle2.otf");
        Typeface desertFont = Typeface.createFromAsset(getAssets(),"fonts/font_desert.ttf");
        Typeface islandFont = Typeface.createFromAsset(getAssets(), "fonts/font_island2.ttf");
        Typeface iceFont = Typeface.createFromAsset(getAssets(), "fonts/font_ice.ttf");
        Typeface foreignfont = Typeface.createFromAsset(getAssets(), "fonts/font_foreign.ttf");

        mountainTextView.setTypeface(mountainFont);
        jungleTextView.setTypeface(jungleFont);
        desertTextView.setTypeface(desertFont);
        islandTextView.setTypeface(islandFont);
        iceTextView.setTypeface(iceFont);
        foreignTextView.setTypeface(foreignfont);
    }


    public void go(View view) {
        startActivity(new Intent(MainActivity.this,StartGmail.class));
    }

    public void go2(View view)
    {
//        startActivity(new Intent(MainActivity.this,BaseActivity.class));

        if(!isDeviceOnline())
        {
            showNoInternetDialog();
        }
        if(!isGPSEnabled())
        {
            showGPSDiabledDialog();
        }

        if(isDeviceOnline() && isGPSEnabled())
        {
            Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
            startActivity(intent);
        }


//        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1){
//            if(data != null)
//            {
//                Bundle extras = data.getExtras();
//                Double longitude = extras.getDouble("Longitude");
//                Double latitude = extras.getDouble("Latitude");
//                txt.setText(String.valueOf(latitude));
//            }
//            else
//            {
//                txt.setText("Location Information Not Available");
//            }
//
//        }
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
                Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
                startActivity(intent);
                return;
            }
            else if(requestCode == GPS_ENABLE_REQUEST)
            {
                showGPSDiabledDialog();
            }
            else if(requestCode == WIFI_ENABLE_REQUEST)
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

    public void showGPSDiabledDialog() {
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


}



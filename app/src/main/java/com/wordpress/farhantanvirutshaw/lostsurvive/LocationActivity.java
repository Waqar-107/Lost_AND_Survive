package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.farhantanvirutshaw.lostsurvive.hospital.HospitalListActivity;
import com.wordpress.farhantanvirutshaw.lostsurvive.police.PoliceListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by utshaw on 6/25/17.
 */

public class LocationActivity extends AppCompatActivity {

    LocationManager mLocationManager;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    protected TextView mLocationText;


    double currenntLatitude = 0.0;
    double currentLongitude = 0.0;

    private static final String LOCATION_REQUEST_URL = "http://maps.googleapis.com/maps/api/geocode/json?";
    private static final String LOG_TAG = LocationActivity.class.getSimpleName();



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setSupportActionBar((Toolbar) findViewById(R.id.gps_app_bar));


        mLatitudeText = (TextView) findViewById(R.id.latitude_val);
        mLongitudeText = (TextView) findViewById(R.id.longitutde_val);
        mLocationText = (TextView) findViewById(R.id.location_val);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setSupportActionBar((Toolbar)findViewById(R.id.gps_app_bar));

//        getSupportActionBar().setTitle("Location");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button showoMapsBtn = (Button) findViewById(R.id.show_map);
        showoMapsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LocationActivity.this,MapsActivity.class));
            }
        });

        Button showPoliceButton = (Button)findViewById(R.id.show_police);
        showPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDeviceOnline())
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
                else
                {
                    startActivity(new Intent(LocationActivity.this, PoliceListActivity.class));
                }
            }
        });

        Button showHotels = (Button) findViewById(R.id.show_hospital);
        showHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDeviceOnline())
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
                else
                {
                    startActivity(new Intent(LocationActivity.this, HospitalListActivity.class));
                }
            }
        });

        BottomNavigationView bt= (BottomNavigationView) findViewById(R.id.botNavBar);
        bt.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item)
            {
                if(item.getItemId()==R.id.btmnav_my_place)
                {
                    startActivity(new Intent(LocationActivity.this,MapsActivity.class));
                }

                if(item.getItemId()==R.id.btmnav_hospital)
                {
                    if(!isDeviceOnline())
                    {
                        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(),"Requires Internet & GPS",Snackbar.LENGTH_LONG);
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
                        startActivity(new Intent(LocationActivity.this, HospitalListActivity.class));
                    }
                }

                if(item.getItemId()==R.id.btmnav_police)
                {
                    if(!isDeviceOnline())
                    {
                        final Snackbar snackbar = Snackbar.make(getWindow().getDecorView(),"Requires Internet & GPS",Snackbar.LENGTH_LONG);
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
                        startActivity(new Intent(LocationActivity.this, PoliceListActivity.class));
                    }
                }

                return  true;

            }
        });



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Uri baseUri = Uri.parse(LOCATION_REQUEST_URL);
        Uri.Builder builder = baseUri.buildUpon();


        if(location != null) {
            currenntLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            mLatitudeText.setText(Double.toString(currenntLatitude));
            mLongitudeText.setText(Double.toString(currentLongitude));
            Utils.setCurrentLatitude(currenntLatitude);
            Utils.setCurrentLongitude(currentLongitude);
            builder.appendQueryParameter("latlng", Double.toString(currenntLatitude) + "," + Double.toString(currentLongitude));
            builder.appendQueryParameter("sensor", "true");
            if (isDeviceOnline())
            {
                new MyAsyncTask().execute(builder.toString());
            }
            else
            {
                final Snackbar snackbar = Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content), "Couldn't retrieve location name\nCheck internet connection", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setLines(2);
                snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }





    private class MyAsyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            URL url=null;
            try {
                url = new URL(strings[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            String jsonResponse = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                if(urlConnection.getResponseCode() == 200)
                {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }
                else
                {
                    Log.e(LOG_TAG,"Error response Code " +  urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
            }
            finally {
                if(urlConnection != null)
                {
                    urlConnection.disconnect();
                }
                if(inputStream != null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {

                if(jsonResponse != null)
                {
                    JSONObject root = new JSONObject(jsonResponse);
                    JSONArray resultsArray = root.getJSONArray("results");
                    JSONObject obj  = resultsArray.getJSONObject(0);
                    String temp = obj.getString("formatted_address");
                    return temp;
                }
                return "";

            } catch (JSONException e) {
                Toast.makeText(LocationActivity.this, "Couldn't load data", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            mLocationText.setText(s);
            super.onPostExecute(s);
        }
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output  = new StringBuilder();

        if(inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null)
            {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
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
                if(!isGPSEnabled())
                {
                    final Snackbar snackbar = Snackbar.make(view,"Requires GPS",Snackbar.LENGTH_LONG);
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


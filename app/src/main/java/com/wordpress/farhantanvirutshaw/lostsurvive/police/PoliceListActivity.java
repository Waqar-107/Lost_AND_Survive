package com.wordpress.farhantanvirutshaw.lostsurvive.police;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wordpress.farhantanvirutshaw.lostsurvive.Constants;
import com.wordpress.farhantanvirutshaw.lostsurvive.PoliceMapsActivity;
import com.wordpress.farhantanvirutshaw.lostsurvive.R;
import com.wordpress.farhantanvirutshaw.lostsurvive.Utils;

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
import java.util.ArrayList;

public class PoliceListActivity extends AppCompatActivity {


    private static final String LOG_TAG = PoliceListActivity.class.getSimpleName();
    private static final String POLICE_REQUEST_URL = "https://maps.googleapis.com/maps/api/place/search/json?";
    private static final String API_KEY = "AIzaSyBqJ1_AhhhS81n1pQ4PtCqm__3MlH2HAOE";

    String temp = "https://maps.googleapis.com/maps/api/place/search/json?location=37.785835,-122.406418&rankby=distance&types=police&sensor=false&key=AIzaSyBqJ1_AhhhS81n1pQ4PtCqm__3MlH2HAOE";

    private PoliceAdapter mAdapter;
    private TextView mEmptyStateTextView;

    private AlertDialog mInternetDialog;

    private static final int WIFI_ENABLE_REQUEST = 0x1006;
    ConnectivityManager connMgr;


    TextView textView;
    private static int selectedItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_list_activity);
        textView = (TextView) findViewById(R.id.empty_view);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);


        mAdapter = new PoliceAdapter(this,new ArrayList<PoliceStation>());

        ListView policeListView = (ListView) findViewById(R.id.list);

        policeListView.setAdapter(mAdapter);

        policeListView.setEmptyView(mEmptyStateTextView);

        policeListView.setEmptyView(mEmptyStateTextView);

        policeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!isDeviceOnline())
                {
                    selectedItem = i;
                    showNoInternetDialog();
                }
                else
                {
                    PoliceStation clickedPoliceStation = mAdapter.getItem(i);
                    Utils.setDesLatitude(clickedPoliceStation.getmLatitude());
                    Utils.setDesLongitude(clickedPoliceStation.getmLongitude());
                    startActivity(new Intent(PoliceListActivity.this, PoliceMapsActivity.class));
                }
            }
        });

        Uri baseUri = Uri.parse(POLICE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("location",Utils.getCurrentLatitude()+ "," +
        Utils.getCurrentLongitude());


        uriBuilder.appendQueryParameter("rankby","distance");
        uriBuilder.appendQueryParameter("types","police");
        uriBuilder.appendQueryParameter("sensor","false");
        uriBuilder.appendQueryParameter("key",API_KEY);

        Log.e("Utshaw",uriBuilder.toString());
        new MyTask().execute(uriBuilder.toString());

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


    class MyTask extends AsyncTask<String,Void,ArrayList<PoliceStation>>
    {


        @Override
        protected ArrayList<PoliceStation> doInBackground(String... strings) {

            ArrayList<PoliceStation> arrayList = new ArrayList<PoliceStation>();
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
            double lat;
            double lng;
            String name="";
            String vicinity = "";

            try {
                JSONObject root = new JSONObject(jsonResponse);
                JSONArray resultsArray = root.getJSONArray("results");
                for(int i=0;i<resultsArray.length();i++)
                {
                    JSONObject currentObject = resultsArray.getJSONObject(i);
                    JSONObject geoObject = currentObject.getJSONObject("geometry");
                    JSONObject locationObject = geoObject.getJSONObject("location");
                    lat = locationObject.getDouble("lat");
                    lng = locationObject.getDouble("lng");
                    name = currentObject.getString("name");
                    vicinity = currentObject.getString("vicinity");
                    double totalDistance = PoliceStation.distance(lat,lng, Utils.getCurrentLatitude(),Utils.getCurrentLongitude());
                    arrayList.add(new PoliceStation(name,lat,lng,vicinity,totalDistance));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return arrayList;

        }

        @Override
        protected void onPostExecute(ArrayList<PoliceStation> policeStations) {
            mEmptyStateTextView.setText("No nearby police stations found");
            super.onPostExecute(policeStations);
            mAdapter.addAll(policeStations);
            ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
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
                PoliceStation clickedPoliceStation = mAdapter.getItem(selectedItem);
                Utils.desLatitude = clickedPoliceStation.getmLatitude();
                Utils.desLongitude = clickedPoliceStation.getmLongitude();
                startActivity(new Intent(PoliceListActivity.this, PoliceMapsActivity.class));
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
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
        Intent intent = new Intent(MainActivity.this, GPSTrackerActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle extras = data.getExtras();
            Double longitude = extras.getDouble("Longitude");
            Double latitude = extras.getDouble("Latitude");
            txt.setText(String.valueOf(latitude));
        }
    }


}

package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_activity_appbar);
        setSupportActionBar(toolbar);
    }



    public void go(View view) {
        startActivity(new Intent(MainActivity.this,StartGmail.class));
    }
}

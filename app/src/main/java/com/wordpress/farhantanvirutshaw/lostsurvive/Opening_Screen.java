package com.wordpress.farhantanvirutshaw.lostsurvive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Opening_Screen extends AppCompatActivity
{

    static String FRAGMENT_NAME = "FRAGMENT_NAME";
    static String CALLER_ACTIVITY = "MAIN";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening__screen);
    }


    //switch to five different categories from the opening screen
    public void switchToCategory(View view)
    {
        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        if(view.getId()==R.id.op_jungle)
        {
            intent.putExtra(FRAGMENT_NAME,"jungle");
        }

        else if(view.getId()==R.id.op_desert)
        {
            intent.putExtra(FRAGMENT_NAME,"desert");
        }

        else if(view.getId()==R.id.op_abroad)
        {
            intent.putExtra(FRAGMENT_NAME,"abroad");
        }

        else if(view.getId()==R.id.op_island)
        {
            intent.putExtra(FRAGMENT_NAME,"island");
        }

        else if(view.getId()==R.id.op_mountain)
        {
            intent.putExtra(FRAGMENT_NAME,"mountain");
        }
        intent.putExtra(CALLER_ACTIVITY,"main");

        startActivity(intent);

    }
}

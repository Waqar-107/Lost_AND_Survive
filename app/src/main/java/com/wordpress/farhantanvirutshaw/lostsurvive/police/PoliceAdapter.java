package com.wordpress.farhantanvirutshaw.lostsurvive.police;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wordpress.farhantanvirutshaw.lostsurvive.R;
import com.wordpress.farhantanvirutshaw.lostsurvive.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by utshaw on 5/15/17.
 */

public class PoliceAdapter extends ArrayAdapter<PoliceStation> {




    public PoliceAdapter(Activity context, ArrayList<PoliceStation> quakeArrayList)
    {
        super(context,0,quakeArrayList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        PoliceStation currEarthQuake = getItem(position);



        TextView stationName = (TextView) listItemView.findViewById(R.id.station_name);
        stationName.setText(currEarthQuake.getmName());

        TextView stationLocation = (TextView)listItemView.findViewById(R.id.station_location);
        stationLocation.setText(currEarthQuake.getVicinity());

        TextView latitudeView = (TextView)listItemView.findViewById(R.id.latitude);
        latitudeView.setText(formatMagnitude(currEarthQuake.getmLatitude()));


        TextView longitudeView = (TextView)listItemView.findViewById(R.id.longitude);
        longitudeView.setText(formatMagnitude(currEarthQuake.getmLongitude()));

        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.police_distance);

        GradientDrawable distanceCircle = (GradientDrawable) distanceTextView.getBackground();

        int magnitudeColor = getMagnitudeColor(currEarthQuake.getDistanceFromCurrentLocation());
        distanceCircle.setColor(magnitudeColor);

        String formattedMagnitude = formatMagnitude(currEarthQuake.getDistanceFromCurrentLocation());
        distanceTextView.setText(formattedMagnitude+"\nKM");


        return listItemView;
    }

    private int getMagnitudeColor(double distance) {

        int policeColorResourceId;
        int distanceFloor = (int) Math.floor(distance);
        switch (distanceFloor)
        {
            case 0:
                policeColorResourceId = R.color.police1;
                break;
            case 1:
                policeColorResourceId = R.color.police2;
                break;
            case 2:
                policeColorResourceId = R.color.police3;
                break;
            case 3:
                policeColorResourceId = R.color.police4;
                break;
            case 4:
                policeColorResourceId = R.color.police5;
                break;
            case 5:
                policeColorResourceId = R.color.police6;
                break;
            case 6:
                policeColorResourceId = R.color.police7;
                break;
            case 7:
                policeColorResourceId = R.color.police8;
                break;
            case 8:
                policeColorResourceId = R.color.police9;
                break;
            default:
                policeColorResourceId = R.color.police10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),policeColorResourceId);
    }



    private String formatMagnitude(double magnitude)
    {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.00");
        return magnitudeFormat.format(magnitude);
    }
}

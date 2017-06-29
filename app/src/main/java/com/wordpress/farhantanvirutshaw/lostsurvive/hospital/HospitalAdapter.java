package com.wordpress.farhantanvirutshaw.lostsurvive.hospital;

import android.app.Activity;
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

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by utshaw on 6/29/17.
 */

public class HospitalAdapter extends ArrayAdapter<Hospital> {

    public HospitalAdapter(Activity context, ArrayList<Hospital> quakeArrayList)
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

        Hospital currentHospital = getItem(position);



        TextView stationName = (TextView) listItemView.findViewById(R.id.station_name);
        stationName.setText(currentHospital.getmName());

        TextView stationLocation = (TextView)listItemView.findViewById(R.id.station_location);
        stationLocation.setText(currentHospital.getVicinity());

        TextView latitudeView = (TextView)listItemView.findViewById(R.id.latitude);
        latitudeView.setText(formatMagnitude(currentHospital.getmLatitude()));


        TextView longitudeView = (TextView)listItemView.findViewById(R.id.longitude);
        longitudeView.setText(formatMagnitude(currentHospital.getmLongitude()));

        TextView distanceTextView = (TextView) listItemView.findViewById(R.id.police_distance);

        GradientDrawable distanceCircle = (GradientDrawable) distanceTextView.getBackground();

        int magnitudeColor = getMagnitudeColor(currentHospital.getDistanceFromCurrentLocation());
        distanceCircle.setColor(magnitudeColor);

        String formattedMagnitude = formatMagnitude(currentHospital.getDistanceFromCurrentLocation());
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

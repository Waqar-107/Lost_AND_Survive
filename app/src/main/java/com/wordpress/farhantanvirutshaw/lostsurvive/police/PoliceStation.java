package com.wordpress.farhantanvirutshaw.lostsurvive.police;

/**
 * Created by utshaw on 6/25/17.
 */

public class PoliceStation {

    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String vicinity;
    private double distanceFromCurrentLocation;

    public PoliceStation(String mName, double mLatitude, double mLongitude, String vicinity,double distanceFromCurrentLocation) {
        this.mName = mName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.vicinity = vicinity;
        this.distanceFromCurrentLocation = distanceFromCurrentLocation;
    }

    public String getmName() {
        return mName;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public String getVicinity() {
        return vicinity;
    }

    public double getDistanceFromCurrentLocation()
    {
        return distanceFromCurrentLocation;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}

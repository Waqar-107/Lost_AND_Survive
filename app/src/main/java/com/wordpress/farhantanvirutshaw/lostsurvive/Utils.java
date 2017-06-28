package com.wordpress.farhantanvirutshaw.lostsurvive;

/**
 * Created by utshaw on 6/26/17.
 */

public class Utils {

    public static double desLatitude;
    public static double desLongitude;

    public static double currentLongitude;
    public static double currentLatitude;

    public static double getCurrentLongitude() {
        return currentLongitude;
    }

    public static double getCurrentLatitude() {
        return currentLatitude;
    }

    public static void setCurrentLongitude(double currentLongitude) {
        Utils.currentLongitude = currentLongitude;
    }

    public static void setCurrentLatitude(double currentLatitude) {
        Utils.currentLatitude = currentLatitude;
    }

    public static double getDesLatitude() {
        return desLatitude;
    }

    public static double getDesLongitude() {
        return desLongitude;
    }

    public static void setDesLatitude(double desLatitude) {
        Utils.desLatitude = desLatitude;
    }

    public static void setDesLongitude(double desLongitude) {
        Utils.desLongitude = desLongitude;
    }
}

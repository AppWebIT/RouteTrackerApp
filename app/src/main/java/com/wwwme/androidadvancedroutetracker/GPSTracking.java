package com.wwwme.androidadvancedroutetracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Manfred Zlamala on 01.11.2015.
 */

public class GPSTracking extends Service implements LocationListener {

    private final Context mContext;

    protected LocationManager locationManager;

    // 01.11.2015 Zlamala: Deklarationen für GPS-Abfrage
    private static final String TAG = "GPSActivity";
    Location mCurrentLocation;
    Location mOldCurrentLocation;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    public JSONObject track = new JSONObject();
    public static JSONArray COORDS_LIST = new JSONArray();

    // 01.11.2015 Zlamala: Deklarationen für Berechnungen
    double distance;
    double distanceSum;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    // 01.11.2015 Zlamala: Deklarationen für BroadcastReceiver
    public static final String
            ACTION_LOCATION_BROADCAST = GPSTracking.class.getName() + "LocationBroadcast",
            EXTRA_LATITUDE = "extra_latitude",
            EXTRA_LONGITUDE = "extra_longitude",
            EXTRA_DISTANCE_SUM = "extra_distance_sum";

    // Flags für Status GPS
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;

    // Flag für Status Netzwerk
    boolean isNetworkEnabled = false;

    // Die minimale Distanz für Updates in Meter
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 5 Meter

    // Die minimale Zeit zwischen den Updates in Millisekunden
    private static final long MIN_TIME_BW_UPDATES = 1000; // 1 Sekunde

    public GPSTracking(Context context) {
        this.mContext = context;

        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Abfrage Status GPS
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Abfrage Status Netzwerk
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // Kein Netzwerkbetreiber
            } else {
                this.canGetLocation = true;
/*                if (isNetworkEnabled) {
                    // 01.11.2015 Zlamala: checkSelfPermission hinzugefügt
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            Log.d(TAG, "----- CheckSelfPermission getLastKnownLocation aufgerufen -----");
                        }
                    }
                    // Ende - checkSelfPermission
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d(TAG, "----- Netzwerk aktiv -----");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
*/                // Wenn GPS aktiviert ist, Abfrage der Koordinaten mit GPS Services
                if (isGPSEnabled) {
                    // 01.11.2015 Zlamala: checkSelfPermission hinzugefügt
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d(TAG, "----- CheckSelfPermission getLastKnownLocation aufgerufen -----");
                        }
                    }
                    // Ende - checkSelfPermission
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d(TAG, "----- GPS aktiviert -----");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    //20.10.2015 Zlamala: GPS-Abfrage mit Daten speichern
    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location l) {
        //    printSaveLocation(l);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    // Ende - GPS-Abfrage mit speichern der Daten


    public void stopUsingGPS(){
        if(locationManager != null){
            // 01.11.2015 Zlamala: checkSelfPermission hinzugefügt
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.removeUpdates(locationListener);
                    Log.d(TAG, "------ CheckSelfPermission removeUpdates aufgerufen -----");
                }
            }
            // Ende - checkSelfPermission
            locationManager.removeUpdates(GPSTracking.this);
            Log.d(TAG, "----- GPS stoppen -----");
            long currentDate = System.currentTimeMillis();
            Log.d(TAG, "----- Datum berechnen -----");
            final Date date = new Date(currentDate);
            Log.d(TAG, "---- Berechnung JSON - currentDate = " + dateFormat.format(date) + " ----");
            try {
                Log.d(TAG, "----- Berechnung JSON - Track mit Daten befüllen (Datum) -----");
                track.put("datum", dateFormat.format(date));
                Log.d(TAG, "----- Berechnung JSON - Track mit Daten befüllen (Distanz) -----");
                track.put("distanz", distanceSum);
                Log.d(TAG, "----- Berechnung JSON - Track mit Daten befüllen (Koordinaten) -----");
                track.put("koordinaten", COORDS_LIST);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "---- Berechnung JSON - Ausgabe Track in Log ----");
            String trackText = track.toString();
            Log.d(TAG, trackText);
        }
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
      /*
       * 01.11.2015 Zlamala: Berechnung Distanz anhand Haversine Formel
       *
       * R = earth’s radius (mean radius = 6,371km)
       * Δlat = lat2− lat1
       * Δlong = long2− long1
       * a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
       * c = 2.atan2(√a, √(1−a))
       * d = R.c
       */

        final double radius = 6372800; // Empfohlener Radius der Erde in m
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.sin (dLat/2)*Math.sin (dLat/2) + Math.cos(lat1)*Math.cos(lat2)*Math.sin(dLng / 2)*Math.sin(dLng/2);
        double ang = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double distance = ang *radius;

        return distance;
    }

    // 01.11.2015 Zlamala: Senden eines Broadcasts an Klasse ShowHome, damit Anzeige aktualisiert wird
    private void sendBroadcastMessage(Location location, double distance) {
        if (location != null) {
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
            intent.putExtra(EXTRA_DISTANCE_SUM, distance);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "---- Location Changed ----");
        if (mOldCurrentLocation == null) {
            mOldCurrentLocation = location;
            distanceSum = 0.0;
            Log.d(TAG, "---- Berechnung JSON - Liste erzeugen ----");

        } else {
            // 01.11.2015 Zlamala: distance muss vor der Änderung von mOldCurrentLocation aufgerufen werden!!!
            distance = getDistance(mOldCurrentLocation.getLatitude(), mOldCurrentLocation.getLongitude(), mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mOldCurrentLocation = mCurrentLocation;
            distanceSum = distanceSum + distance;
        }
        mCurrentLocation = location;

        JSONObject coords = new JSONObject();

        try {
            coords.put("lat", mCurrentLocation.getLatitude());
            coords.put("lng", mCurrentLocation.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "---- Berechnung JSON - Ausgabe JSON Objekt in Log ----");
        String coordsText = coords.toString();
        Log.d(TAG, coordsText);
        COORDS_LIST.put(coords);
        Log.d(TAG, "---- Berechnung JSON - Ausgabe Track in Log ----");
        String trackText = COORDS_LIST.toString();
        Log.d(TAG, trackText);

        sendBroadcastMessage(mCurrentLocation, distanceSum);

        Log.d(TAG, "Old Location: " + mOldCurrentLocation.getLatitude() + " " + mOldCurrentLocation.getLongitude());
        Log.d(TAG, "Current Location: " + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
        Log.d(TAG, "Distance: " + distance + "m");
        Log.d(TAG, "DistanceSum: " + Math.round(distanceSum * 1000) / 1000 + "m");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

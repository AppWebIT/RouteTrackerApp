package com.wwwme.androidadvancedroutetracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by WDEZLA on 20.10.2015.
 */

public class GPSTracker_LocationTrackerGoogleMaps implements LocationListener {

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000; // 1 Sekunde
    private static final long FASTEST_INTERVAL = 1000; // 1 Sekunde
    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    Location mOldCurrentLocation;
    String mLastUpdateTime;
    GoogleMap googleMap;
    // private final Context context = this;

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location Changed!");
        if (mOldCurrentLocation == null) {
            mOldCurrentLocation = location;
        }
        else {
            mOldCurrentLocation = mCurrentLocation;
        }
        mCurrentLocation = location;

        // 16.10.2015 - Zlamala: Test für addLines() - Ausgabe der alten und neuen Koordinaten

        Log.d(TAG, "Old Location: " + mOldCurrentLocation.getLatitude() + " " + mOldCurrentLocation.getLongitude());
        Log.d(TAG, "Current Location: " + mCurrentLocation.getLatitude()+ " " + mCurrentLocation.getLongitude());

        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        // addMarker();
        // addLines ();
    }




    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

}

package com.wwwme.androidadvancedroutetracker;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Manfred Zlamala on 18.11.2015.
 */
public class ShowMap extends Fragment {

    private static final String TAG = "MapActivity";

    private static GoogleMap googleMap;
    Location mCurrentLocation;
    Location mOldCurrentLocation;

    // Für Instanziierung
    public static ShowMap newInstance(String someTitle) {
        ShowMap showMap = new ShowMap();
        Bundle args = new Bundle();
        args.putString("someTitle", someTitle);
        showMap.setArguments(args);
        return showMap;
    }

    private void getJSON() throws JSONException{

        String trackText = GPSTracking.coordsList.toString();
        Log.d(TAG, trackText);

        mOldCurrentLocation = null;

        JSONArray coords = GPSTracking.coordsList;
        for(int i = 0 ; i < coords.length() ; i++){
            if (mOldCurrentLocation != null) {
                mCurrentLocation.setLatitude(coords.getJSONObject(i).getDouble("lat"));
                mCurrentLocation.setLongitude(coords.getJSONObject(i).getDouble("lng"));
                addLines();
                mOldCurrentLocation = mCurrentLocation;
            }
            else {
                mOldCurrentLocation = new Location("dummyprovider");
                mCurrentLocation = new Location("dummyprovider");
                mOldCurrentLocation.setLatitude(coords.getJSONObject(i).getDouble("lat"));
                mOldCurrentLocation.setLongitude(coords.getJSONObject(i).getDouble("lng"));
            }
        }
    }

    private void addLines()  {
        LatLng oldLatLng = new LatLng(mOldCurrentLocation.getLatitude(), mOldCurrentLocation.getLongitude());
        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        googleMap.addPolyline((new PolylineOptions()).add(oldLatLng, currentLatLng).width(5).color(Color.BLUE).geodesic(true));
        // move camera to zoom on map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 13));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {
        Log.d(TAG, "----- onCreateView - View wird initialisiert -----");
        View showMapView = inflater.inflate(R.layout.show_map, container, false);

        Log.d(TAG, "----- Map wird angezeigt -----");

        googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();

        try {
            getJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return showMapView;
    }
}

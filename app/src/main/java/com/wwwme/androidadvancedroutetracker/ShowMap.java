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

        String trackText = GPSTracking.COORDS_LIST.toString();
        Log.d(TAG, trackText);

        mOldCurrentLocation = null;

        // Testdatensatz
//        JSONArray coords = new JSONArray("[{\"lat\":48.23420706,\"lng\":16.35810414},{\"lat\":48.23441139,\"lng\":16.35804606},{\"lat\":48.2344062,\"lng\":16.35819308},{\"lat\":48.2346294,\"lng\":16.35868883},{\"lat\":48.23461259,\"lng\":16.35871193},{\"lat\":48.234735,\"lng\":16.35868587},{\"lat\":48.23468951,\"lng\":16.3588035},{\"lat\":48.23468045,\"lng\":16.35877629},{\"lat\":48.23484135,\"lng\":16.35877498},{\"lat\":48.23502905,\"lng\":16.35865249},{\"lat\":48.23483952,\"lng\":16.35875293},{\"lat\":48.23472715,\"lng\":16.3587504},{\"lat\":48.23464664,\"lng\":16.35868078},{\"lat\":48.23435886,\"lng\":16.35827581},{\"lat\":48.23050697,\"lng\":16.36197625},{\"lat\":48.23028024,\"lng\":16.36205692},{\"lat\":48.23017658,\"lng\":16.36208231},{\"lat\":48.22997664,\"lng\":16.36213119},{\"lat\":48.2298802,\"lng\":16.36211646},{\"lat\":48.22977908,\"lng\":16.36217168},{\"lat\":48.22955216,\"lng\":16.36223485},{\"lat\":48.22941246,\"lng\":16.36232075},{\"lat\":48.22926418,\"lng\":16.36240673},{\"lat\":48.22911738,\"lng\":16.36250639},{\"lat\":48.22896164,\"lng\":16.36259015},{\"lat\":48.22883155,\"lng\":16.36277094},{\"lat\":48.22873155,\"lng\":16.36290691},{\"lat\":48.22869632,\"lng\":16.3629605},{\"lat\":48.22861815,\"lng\":16.36306412},{\"lat\":48.2285517,\"lng\":16.36315365},{\"lat\":48.22843266,\"lng\":16.36325217},{\"lat\":48.22833015,\"lng\":16.3633749},{\"lat\":48.22837138,\"lng\":16.36332842},{\"lat\":48.22836662,\"lng\":16.36334089},{\"lat\":48.22832986,\"lng\":16.36337827},{\"lat\":48.22766728,\"lng\":16.36388237},{\"lat\":48.22761779,\"lng\":16.36395983},{\"lat\":48.22748696,\"lng\":16.36416115},{\"lat\":48.22742561,\"lng\":16.36428369},{\"lat\":48.22735419,\"lng\":16.36442895},{\"lat\":48.22729886,\"lng\":16.36464559},{\"lat\":48.22722788,\"lng\":16.36478515},{\"lat\":48.22717018,\"lng\":16.36490685},{\"lat\":48.22711148,\"lng\":16.36503634},{\"lat\":48.222514,\"lng\":16.36769185},{\"lat\":48.22247784,\"lng\":16.36771652},{\"lat\":48.22264361,\"lng\":16.36761152},{\"lat\":48.22266246,\"lng\":16.36746125},{\"lat\":48.22271249,\"lng\":16.36753431},{\"lat\":48.22273349,\"lng\":16.36747729},{\"lat\":48.22273092,\"lng\":16.36755086},{\"lat\":48.22275167,\"lng\":16.36755002},{\"lat\":48.2227502,\"lng\":16.3675356},{\"lat\":48.22269977,\"lng\":16.3675493},{\"lat\":48.22267727,\"lng\":16.3675827},{\"lat\":48.22266263,\"lng\":16.36759653},{\"lat\":48.22263433,\"lng\":16.36758604},{\"lat\":48.2225415,\"lng\":16.36760576},{\"lat\":48.22235554,\"lng\":16.36766984},{\"lat\":48.22222306,\"lng\":16.3676739},{\"lat\":48.22207517,\"lng\":16.36774356},{\"lat\":48.22199681,\"lng\":16.367754},{\"lat\":48.22190781,\"lng\":16.36777317},{\"lat\":48.22172958,\"lng\":16.36782397},{\"lat\":48.22163052,\"lng\":16.36785326},{\"lat\":48.22153228,\"lng\":16.36788241},{\"lat\":48.22143561,\"lng\":16.36791002},{\"lat\":48.22133874,\"lng\":16.36794754},{\"lat\":48.23843019,\"lng\":16.42510278},{\"lat\":48.23843019,\"lng\":16.42510278},{\"lat\":48.23845049,\"lng\":16.42511702},{\"lat\":48.23845049,\"lng\":16.42511702},{\"lat\":48.23845016,\"lng\":16.42508263},{\"lat\":48.23845016,\"lng\":16.42508263},{\"lat\":48.23847496,\"lng\":16.42505985},{\"lat\":48.23847496,\"lng\":16.42505985},{\"lat\":48.23849868,\"lng\":16.42505611},{\"lat\":48.23849868,\"lng\":16.42505611},{\"lat\":48.23850835,\"lng\":16.42507438},{\"lat\":48.23850835,\"lng\":16.42507438},{\"lat\":48.23852779,\"lng\":16.42512676},{\"lat\":48.23852779,\"lng\":16.42512676},{\"lat\":48.23846465,\"lng\":16.4251519},{\"lat\":48.23846465,\"lng\":16.4251519},{\"lat\":48.23848517,\"lng\":16.42520762},{\"lat\":48.23848517,\"lng\":16.42520762},{\"lat\":48.23850161,\"lng\":16.42512312},{\"lat\":48.23850161,\"lng\":16.42512312},{\"lat\":48.23853446,\"lng\":16.42513285},{\"lat\":48.23853446,\"lng\":16.42513285},{\"lat\":48.23851028,\"lng\":16.42513464},{\"lat\":48.23851028,\"lng\":16.42513464},{\"lat\":48.23853822,\"lng\":16.42513632},{\"lat\":48.23853822,\"lng\":16.42513632},{\"lat\":48.2385478,\"lng\":16.42519047},{\"lat\":48.2385478,\"lng\":16.42519047},{\"lat\":48.23856278,\"lng\":16.42524363},{\"lat\":48.23856278,\"lng\":16.42524363},{\"lat\":48.23855405,\"lng\":16.42527434},{\"lat\":48.23855405,\"lng\":16.42527434},{\"lat\":48.23855578,\"lng\":16.42525822},{\"lat\":48.23855578,\"lng\":16.42525822},{\"lat\":48.23859193,\"lng\":16.42526934},{\"lat\":48.23859193,\"lng\":16.42526934},{\"lat\":48.23860878,\"lng\":16.42527086},{\"lat\":48.23860878,\"lng\":16.42527086},{\"lat\":48.23862477,\"lng\":16.42529657}]");
//        Log.d(TAG, coords.toString());

        JSONArray coords = GPSTracking.COORDS_LIST;

        int length = coords.length();
        Log.d(TAG, "----- JSON Array - Length: "+ String.valueOf(length)+ " -----");

        for(int i = 0 ; i < length ; i++){
            if (mOldCurrentLocation != null) {
                // mOldCurrentLocation = mCurrentLocation;
                mOldCurrentLocation.setLatitude(mCurrentLocation.getLatitude());
                mOldCurrentLocation.setLongitude(mCurrentLocation.getLongitude());
                // mCurrentLocation = new Location("dummyprovider");
                Log.d(TAG, "----- Schleife mOldCurrentLocation is not null - i = " + String.valueOf(i) + " -----");
                mCurrentLocation.setLatitude(coords.getJSONObject(i).getDouble("lat"));
                mCurrentLocation.setLongitude(coords.getJSONObject(i).getDouble("lng"));
                Log.d(TAG, "----- Schleife (nach Zuweisung) - LatLng - Lat: " + mCurrentLocation.getLatitude() + " Lng: " + mCurrentLocation.getLongitude() + " -----" );
                Log.d(TAG, "----- Schleife (nach Zuweisung) - oldLatLng - Lat: " + mOldCurrentLocation.getLatitude() + " Lng: " + mOldCurrentLocation.getLongitude() + " -----");
                addLines();
            }
            else {
                Log.d(TAG, "----- Schleife mOldCurrentLocation is null -----");
                mOldCurrentLocation = new Location("dummyprovider");
                mCurrentLocation = new Location("dummyprovider");
                mCurrentLocation.setLatitude(coords.getJSONObject(i).getDouble("lat"));
                mCurrentLocation.setLongitude(coords.getJSONObject(i).getDouble("lng"));
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

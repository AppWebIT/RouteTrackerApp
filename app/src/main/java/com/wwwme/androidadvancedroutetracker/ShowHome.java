package com.wwwme.androidadvancedroutetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Manfred Zlamala on 19.11.2015.
 * Startscreen in eignes Fragment eingefügt.
 */

public class ShowHome extends Fragment {

    private static final String TAG = "HomeActivity";

    private TextView timeLabel;

    private TextView lat;
    private TextView lng;
    private TextView dist;
    private TextView speed;
    private TextView minkm;

    private Context thiscontext;

    private Button startBtn;

    // 01.11.2015 Klasse GPSTracking
    public static GPSTracking gpsTracking;

    private static long DELAY = 100;
    private static String IN_COUNTING_LABEL = "Stop";
    private static String IN_WAITING_LABEL = "Start/Renew";

    public static String stopWatchStates = StopWatchStates.IN_WAITING;

    private static long startTimePoint;

    private SimpleDateFormat screenFormat = new SimpleDateFormat("HH:mm:ss.S", Locale.GERMAN);

    // 12.12.2015 Zlamala: Floating Action Button ein- und ausblenden
    private void showFAB (){
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.show();
    }

    private void hideFAB (){
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    // 20.10.2015 Zlamala: GPS-Abfrage mit Daten speichern
    // 01.11.2015 Zlamala: wird vorerst nicht benötigt
/*    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location l) {
            printSaveLocation(l);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void printSaveLocation(Location l) {
        ((TextView) tv_longitude).setText(String.valueOf(l.getLatitude()));
        ((TextView) tv_latitude).setText(String.valueOf(l.getLongitude()));
        ((TextView) herkunft).setText("printSaveLocation");
    }
    // Ende - GPS-Abfrage mit speichern der Daten
*/

    public static ShowHome newInstance(String someTitle) {
        ShowHome showHome = new ShowHome();
        Bundle args = new Bundle();
        args.putString("someTitle", someTitle);
        showHome.setArguments(args);
        return showHome;
    }

    private void afterInit() {
        setLabelText("00:00:00.0");
        screenFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        startTimePoint = 0;

        stopCounting();
    }

    private Handler tasksHandler = new Handler();

    public void startCounting() {
        stopWatchStates = StopWatchStates.IN_COUNTING;

        tasksHandler.removeCallbacks(timeTickRunnable);
        tasksHandler.postDelayed(timeTickRunnable, DELAY);

        startTimePoint = System.currentTimeMillis();
    }

    public void stopCounting() {
        stopWatchStates = StopWatchStates.IN_WAITING;
    }

    public void startGPSTracking(){
        gpsTracking = new GPSTracking(thiscontext);
    }

    // Anzeige aktuell abgelaufene Zeit
    public static Date currentTime (){
        long interval = System.currentTimeMillis() - startTimePoint;
        final Date date = new Date(interval);
        return date;
    }

    public String currentTimeString() {
        Date date = currentTime();
        return screenFormat.format(date);
    }

    public void setLabelText(String string) {
        timeLabel.setText(string);
    }

    private Runnable timeTickRunnable = new Runnable() {
        public void run() {
            if (stopWatchStates == StopWatchStates.IN_COUNTING) {
                setLabelText(currentTimeString());
                tasksHandler.postDelayed(timeTickRunnable, DELAY);
            }
        }
    };

    // GPS Tracking starten und stoppen
    public void stopButtonClick (View button) {

        if (stopWatchStates == StopWatchStates.IN_COUNTING) {
            stopCounting();
            gpsTracking.stopUsingGPS();
            if (GPSTracking.coordsList.toString().equals("[]")) {
                hideFAB();
            }
            else {
                showFAB();
            }
            startBtn.setText("Start");
        } else if (stopWatchStates == StopWatchStates.IN_WAITING) {
            hideFAB();
            startCounting();
            startGPSTracking();
            startBtn.setText("Stop");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "----- onCreateView wurde aufgerufen -----");
        View showHomeView = inflater.inflate(R.layout.show_home, null);
        startBtn = (Button) showHomeView.findViewById(R.id.btnStart);
        timeLabel = (TextView) showHomeView.findViewById(R.id.TimeLabel);
        lat = (TextView) showHomeView.findViewById(R.id.latitude);
        lng = (TextView) showHomeView.findViewById(R.id.longitude);
        dist = (TextView) showHomeView.findViewById(R.id.kilometer);
        speed = (TextView) showHomeView.findViewById(R.id.geschwindigkeit);
        minkm = (TextView) showHomeView.findViewById(R.id.MinProKm);
        thiscontext= container.getContext();
        Log.d(TAG, "----- onCreate - View wurde initialisiert -----");
        Log.d(TAG, "----- onCreate - LocalBroadcastManager wird initialisiert -----");

        LocalBroadcastManager.getInstance(thiscontext).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(GPSTracking.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(GPSTracking.EXTRA_LONGITUDE, 0);
                        double extDistance = intent.getDoubleExtra(GPSTracking.EXTRA_DISTANCE_SUM, 0);

                        // Anzeige Koordinaten
                        lat.setText(String.valueOf(latitude));
                        lng.setText(String.valueOf(longitude));

                        // Berechnung und Anzeige Distanz in km
                        double anzDistance = (Math.round(extDistance)) / 1000.0;
                        dist.setText(String.valueOf(anzDistance) + " km");
                        Log.d("Stopuhr", "Distanz: " + anzDistance + " km");

                        // Berechnung Dauer in Sekunden
                        Date date = currentTime();
                        long secs = (date.getTime()) / 1000;
                        Log.d("Stopuhr", "Zeit: " + secs + " Sekunden");

                        // Berechnung und Anzeige Geschwindigkeit in km/h
                        double geschw = extDistance / secs * 3.6;
                        Log.d("Stopuhr", "Geschwindigkeit: " + geschw + " km/h");

                        double anzSpeed = (Math.round(geschw * 100)) / 100.0;
                        speed.setText(String.valueOf(anzSpeed) + " km/h");

                        // Berechnung und Anzeige min + sek für einen km
                        double anzMinKm = Math.floor(3600 / (extDistance / secs * 3.6) / 60);
                        double anzSekKm = Math.floor((3600 / (extDistance / secs * 3.6)) % 60);
                        int min = (int) anzMinKm;
                        int sek = (int) anzSekKm;
                        Log.d("StopuhrOriginal", "min pro km: " + 3600 / (extDistance / secs * 3.6) / 60 + " min " + (3600 / (extDistance / secs * 3.6)) % 60 + " s pro km");
                        Log.d("Stopuhr", "min pro km: " + anzMinKm + " min " + anzSekKm + " s pro km");
                        DecimalFormat df = new DecimalFormat("00");
                        if (min > 0 && min < 100 || sek > 0) {
                            minkm.setText((df.format(min)) + ":" + (df.format(sek)) + " min/km");
                        }
                    }
                }, new IntentFilter(GPSTracking.ACTION_LOCATION_BROADCAST)
        );

        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopButtonClick(v);
            }
        });

        afterInit();
        return showHomeView;
    }

}

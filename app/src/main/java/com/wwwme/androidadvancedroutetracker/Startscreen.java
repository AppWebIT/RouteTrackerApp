package com.wwwme.androidadvancedroutetracker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class Startscreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public TextView timeLabel;

    public ShowMap showMap;

    public FragmentManager fragManager;
    public FragmentTransaction fragTrans;


    // 01.11.2015 Klasse GPSTracking
    GPSTracking gpsTracking;

    // 20.10.2015 Zlamala: Deklarationen für GPS-Abfrage
    // 01.11.2015 Zlamala: In eigene Klasse eingefügt
    // public View tv_longitude;
    // public View tv_latitude;
    // public View herkunft;

    private static long DELAY = 100;
    private static String IN_COUNTING_LABEL = "Stop";
    private static String IN_WAITING_LABEL = "Start/Renew";

    private String applicationState;

    private long startTimePoint;

    private SimpleDateFormat screenFormat = new SimpleDateFormat("HH:mm:ss.S", Locale.GERMAN);

    //20.10.2015 Zlamala: GPS-Abfrage mit Daten speichern
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

    private void afterInit() {
        timeLabel = (TextView) findViewById(R.id.TimeLabel);
        setLabelText("00:00:00.0");
        // Zeitzone setzen, sonst wird die Zeit nicht von 00:00:00 weg gerechnet
        screenFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        startTimePoint = 0;

        stopCounting();
    }

    private Handler tasksHandler = new Handler();

    public void startCounting() {
        applicationState = StopWatchStates.IN_COUNTING;

        tasksHandler.removeCallbacks(timeTickRunnable);
        tasksHandler.postDelayed(timeTickRunnable, DELAY);

        startTimePoint = System.currentTimeMillis();
    }

    public void stopCounting() {
        applicationState = StopWatchStates.IN_WAITING;
    }

    public void startGPSTracking(){
        gpsTracking = new GPSTracking(Startscreen.this);
    }


    // Anzeige aktuell abgelaufene Zeit
    public Date currentTime (){
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
            if (applicationState == StopWatchStates.IN_COUNTING) {
                setLabelText(currentTimeString());
                tasksHandler.postDelayed(timeTickRunnable, DELAY);
            }
        }
    };

    // GPS Tracking starten und stoppen
    public void stopButtonClick(View button) {
        if (applicationState == StopWatchStates.IN_COUNTING) {
            stopCounting();
            gpsTracking.stopUsingGPS();
            Button btnStart = (Button)findViewById(R.id.btnStart);
            btnStart.setText("Start");
        } else if (applicationState == StopWatchStates.IN_WAITING) {
            startCounting();
            startGPSTracking();
            Button btnStop = (Button)findViewById(R.id.btnStart);
            btnStop.setText("Stop");
        }
    }

    //20.10.2015 Zlamala: Abfrage, ob GoolglePlayService verfügbar ist
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra(GPSTracking.EXTRA_LATITUDE, 0);
                        double longitude = intent.getDoubleExtra(GPSTracking.EXTRA_LONGITUDE, 0);
                        double extDistance = intent.getDoubleExtra(GPSTracking.EXTRA_DISTANCE_SUM, 0);

                        // Anzeige Koordinaten
                        TextView lat = (TextView) findViewById(R.id.latitude);
                        lat.setText(String.valueOf(latitude));
                        TextView lng = (TextView) findViewById(R.id.longitude);
                        lng.setText(String.valueOf(longitude));

                        // Berechnung und Anzeige Distanz in km
                        double anzDistance = (Math.round(extDistance))/1000.0;
                        TextView dist = (TextView) findViewById(R.id.kilometer);
                        dist.setText(String.valueOf(anzDistance) + " km");
                        Log.d("Stopuhr", "Distanz: " + anzDistance + " km");

                        // Berechnung Dauer in Sekunden
                        Date date = currentTime();
                        long secs = (date.getTime())/1000;
                        Log.d("Stopuhr", "Zeit: " + secs + " Sekunden");

                        // Berechnung und Anzeige Geschwindigkeit in km/h
                        double geschw = extDistance / secs * 3.6;
                        Log.d("Stopuhr", "Geschwindigkeit: " + geschw + " km/h");

                        double anzSpeed = (Math.round(geschw*100))/100.0;
                        TextView speed = (TextView) findViewById(R.id.geschwindigkeit);
                        speed.setText(String.valueOf(anzSpeed) + " km/h");

                        // Berechnung und Anzeige min + sek für einen km
                        double anzMinKm = Math.floor(3600 / (extDistance / secs * 3.6) / 60);
                        double anzSekKm = Math.floor((3600 / (extDistance / secs * 3.6)) % 60);
                        int min = (int) anzMinKm;
                        int sek = (int) anzSekKm;
                        Log.d("StopuhrOriginal", "min pro km: " + 3600 / (extDistance / secs * 3.6) / 60 + " min " + (3600 / (extDistance / secs * 3.6)) % 60 + " s pro km");
                        Log.d("Stopuhr", "min pro km: " + anzMinKm + " min " + anzSekKm + " s pro km");
                        TextView minKm = (TextView) findViewById(R.id.MinProKm);
                        DecimalFormat df = new DecimalFormat("00");
                        if (min > 0 && min < 100 || sek > 0) {
                            minKm.setText((df.format(min)) +":"+ (df.format(sek))+ " min/km");
                        }

                    }
                }, new IntentFilter(GPSTracking.ACTION_LOCATION_BROADCAST)
        );

        //20.10.2015 Zlamala: Initialisierung Location Manager
        // 01.11.2015 Zlamala: wird vorerst nicht verwendet
/*        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates("gps", 6000, 1, locationListener);
                Log.d(TAG, "Location Updates 1 aufgerufen .................................");
            }
        }
        lm.requestLocationUpdates("gps", 6000, 1, locationListener);
        Log.d(TAG, "Location Updates 2 aufgerufen .................................");

        //20.10.2015 Zlamala: Error, wenn GoolglePlayService nicht verfügbar
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        tv_longitude = this.findViewById(R.id.longitude);
        tv_latitude = this.findViewById(R.id.latitude);
        herkunft = this.findViewById(R.id.herkunft);

        ((TextView) tv_longitude).setText("Längengrad");
        ((TextView) tv_latitude).setText("Breitengrad");
*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        afterInit();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.startscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_track) {

        } else if (id == R.id.nav_archiv) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, GPSTracking.class));
    }

    @Override
    protected void onPause() {
        super.onStop();
        stopService(new Intent(this, GPSTracking.class));
    }
}

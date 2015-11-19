package com.wwwme.androidadvancedroutetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Startscreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "StartscreenActivity";

    private ShowHome showHome;
    private ShowMap showMap;

    public static FragmentManager fragmentManager;

    protected void displayHome() {
        Log.d(TAG, "----- displayHome wurde aufgerufen -----");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.d(TAG, "----- displayHome - FragmentTransaction wir initialisiert -----");

        if (showHome.isAdded()) { // if the fragment is already in container
            Log.d(TAG, "----- displayHome - showHome ist bereits im Container -----");
            ft.show(showHome);
        } else { // fragment needs to be added to frame container
            Log.d(TAG, "----- displayHome - showHome wird zum Container hinzugefügt -----");
            ft.add(R.id.content_startscreen, showHome, "Home");
        }
        // Hide fragment showMap
        if (showMap.isAdded()) {
            Log.d(TAG, "----- displayHome - Wenn showMap im Container, dann soll showMap removed werden -----");
            ft.remove(showMap);
        }
        // Commit changes
        Log.d(TAG, "----- displayHome - commit -----");
        ft.commit();
    }

    protected void displayMap() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (showMap.isAdded()) { // if the fragment is already in container
            ft.show(showMap);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.content_startscreen, showMap, "Map");
        }
        // Hide fragment showMap
        if (showHome.isAdded()) { ft.hide(showHome); }
        // Commit changes
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "----- onCreate wurde aufgerufen -----");

        // Für Anzeige Map
        fragmentManager = getSupportFragmentManager();

        // Fragments instanziieren
        if (savedInstanceState == null) {
            showHome = ShowHome.newInstance("Home");
            showMap = ShowMap.newInstance("Map");
        }

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

        Log.d(TAG, "----- FragmentManager wird aufgerufen -----");

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.add(R.id.content_startscreen, showHome, "Home").commit();
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


        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displayHome();
        } else if (id == R.id.nav_track){
            displayMap();
        } else if (id == R.id.nav_archiv) {

        } else if (id == R.id.nav_logout) {

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

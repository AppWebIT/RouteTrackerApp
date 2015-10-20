package com.wwwme.androidadvancedroutetracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Startscreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnStart;

    public TextView timeLabel;

    private static long DELAY = 100;
    private static String IN_COUNTING_LABEL = "Stop";
    private static String IN_WAITING_LABEL = "Start/Renew";

    private String applicationState;

    private long startTimePoint;

    private SimpleDateFormat screenFormat = new SimpleDateFormat("HH:mm:ss.S", Locale.GERMAN);

    private void afterInit () {
        timeLabel = (TextView) findViewById(R.id.TimeLabel);
        setLabelText("00:00:00.0");
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

    public String currentTimeString() {
        long interval = System.currentTimeMillis() - startTimePoint;
        final Date date = new Date(interval);

        return screenFormat.format(date);
    }

    public void setLabelText (String string) {
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

    public void stopButtonClick (View button) {
        if (applicationState == StopWatchStates.IN_COUNTING) {
            stopCounting();
        } else if (applicationState == StopWatchStates.IN_WAITING) {
            startCounting();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

package com.wwwme.androidadvancedroutetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;

/**
 * Created by Zlamala on 12.12.2015.
 */

public class GetUserID extends AsyncTask<String,Void,String> {

    private String user_id;
    private Context context;

    public GetUserID(Context context) {
        this.context = context;
 //       this.response = response;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {

        try{
            String username = (String) arg0[0];
            Log.d("GetUserID", "----- Username: " + username + " -----");

            String link = "http://disk69700305.dscloud.biz/routetracker/android/getuseridandroid.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            Log.d("GetUserID", "----- Return: " + sb.toString() + " -----");
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        // Rückgabewert - Original
        Log.d("GetUserID", "----- Result: " + result + " -----");
        // GPSTracking.RESPONSE = result;
        user_id = result;
        // if (Integer.parseInt(user_id)>0) {
        new SaveData().execute(user_id, GPSTracking.trackText);
        GPSTracking.coordsList = new JSONArray();
        // }
    }
}

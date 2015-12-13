package com.wwwme.androidadvancedroutetracker;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Zlamala on 12.12.2015.
 */

public class SaveData extends AsyncTask<String,Void,String>{

    private String response;
    private Context context;

    public SaveData() {
        this.context = context;
//        this.response = response;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {



        try{
            String user_id = (String) arg0[0];
            String track = (String) arg0[1];

            String link = "http://disk69700305.dscloud.biz/routetracker/android/savetrackandroid.php";
            String data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("track", "UTF-8") + "=" + URLEncoder.encode(track, "UTF-8");

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
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
         // Rückgabewert - Original
        // this.responseField.setText(result);

        // Rückgabewert für Login = "Success" - Test
        // this.responseField.setText("Success");

    }


}

package com.wwwme.androidadvancedroutetracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by WDEZLA on 09.12.2015.
 */
public class SigninActivity extends AsyncTask<String,Void,String>{

    private TextView statusField,responseField;
    private Context context;
    // private int byGetOrPost = 1;

    //flag 0 means get and 1 means post.(By default it is post.)
    public SigninActivity(Context context, TextView statusField, TextView responseField) {
        this.context = context;
        this.statusField = statusField;
        this.responseField = responseField;
    //    byGetOrPost = flag;
    }

    protected void onPreExecute(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        /**
        if(byGetOrPost == 0){ //means by Get Method

            try{
                String username = (String)arg0[0];
                String password = (String)arg0[1];
                String link = "http://myphpmysqlweb.hostei.com/login.php?username="+username+"& password="+password;

                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            }

            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        */

        // else {
            try{
                String username = (String)arg0[0];
                String password = (String)arg0[1];

                String link="http://disk69700305.dscloud.biz/routetracker/loginandroid.php";
                String data  = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        // Klammer von else Statement
        // }
    }

    @Override
    protected void onPostExecute(String result) {
        // this.statusField.setText("Login Successful");

        // Rückgabewert - Original
        this.responseField.setText(result);

        // Rückgabewert für Login = "Success" - Test
        // this.responseField.setText("Success");

    }


}
package com.example.myweather2;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    private ListView lv;
    String temp2, date2;
    ArrayList<HashMap<String, String>> weatherData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_listview);
        //MainActivity.checkLocationPermission();

        weatherData = new ArrayList<> ();
        lv =(ListView) findViewById (R.id.listview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double latitude2 = extras.getDouble("Latitude");
            double longitude2 = extras.getDouble("Longitude");
        }



        //MainActivity.getInstance().checkLocationPermission();
        //String lat = String.valueOf (lati);
        //String lon = String.valueOf (longi);

        GetData getData = new GetData ();
        getData.execute ();
    }

    public class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            StringBuilder result2 = new StringBuilder ();
            try {
                //URL url = null;
                HttpURLConnection urlConnection = null;

                try {
                    URL url2 = new URL ("https://api.openweathermap.org/data/2.5/onecall?lat=23&lon=90&exclude=hourly,minutely&units=metric&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
                    urlConnection = (HttpURLConnection) url2.openConnection ();
                    InputStream in = urlConnection.getInputStream ();
                    InputStreamReader isr = new InputStreamReader (in);
                    BufferedReader br = new BufferedReader (new InputStreamReader (in));
                    while ((current = br.readLine ()) != null) {
                        result2.append (current);
                    }

                } catch (Exception e) {
                    e.printStackTrace ();
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            return result2.toString ();
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute (s);

            try {
                JSONObject jo = new JSONObject (s);
                JSONArray ja = jo.getJSONArray ("daily");

                for (int i = 1; i < ja.length (); i++) {

                    JSONObject daily = ja.getJSONObject (i);
                    date2 = daily.getString ("dt");

                    JSONObject temperature = daily.getJSONObject("temp");
                    temp2 = temperature.getString ("day");

                    HashMap<String, String> weather = new HashMap<> ();
                    weather.put ("day", temp2);
                    weather.put ("dt", date2);

                    weatherData.add (weather);
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }


            ListAdapter adapter = new SimpleAdapter (
                    SecondActivity.this,
                    weatherData,
                    R.layout.row_layout,
                    new String[]{"day", "dt"},
                    new int[]{R.id.textView20, R.id.textView21});
            lv.setAdapter (adapter);

        }
    }
}

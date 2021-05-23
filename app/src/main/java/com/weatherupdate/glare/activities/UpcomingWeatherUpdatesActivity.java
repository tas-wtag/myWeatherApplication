package com.weatherupdate.glare.activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.weatherupdate.glare.utilities.ConstantData;
import com.weatherupdate.glare.R;
import com.weatherupdate.glare.models.Weatherdata;
import com.weatherupdate.glare.adapters.WeatherDataAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpcomingWeatherUpdatesActivity extends AppCompatActivity {

    public ListView lv;
    public String temp2, date2;
    public String rain2;
    public String img2;
    public String img3;
    public String vv;
    String latitude2;
    String longitude2;

    public List<UpcomingWeatherUpdatesActivity> wdetails=new ArrayList<> ();
    ArrayList<Weatherdata> weatherList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_upcomingforecast);

        weatherList = new ArrayList<> ();
        lv =(ListView) findViewById (R.id.listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude2 =extras.getString ("Latitude");
            longitude2 = extras.getString ("Longitude");
        }

        GetData getData = new GetData (lv);
        getData.execute ();
    }

    public class GetData extends AsyncTask<String, String, String> {
        public GetData(ListView listview) {
            this.listview = listview;
        }

        public ListView listview;
        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            StringBuilder result2 = new StringBuilder ();
            String lat2 = String.valueOf (latitude2);
            String long2 = String.valueOf (longitude2);
            try {
                HttpURLConnection urlConnection = null;
                try {
                    URL url2 = new URL ("https://api.openweathermap.org/data/2.5/onecall?lat="+lat2+"&lon="+long2+"&exclude=hourly,minutely&units=metric&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
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

        @SuppressLint("SimpleDateFormat")
        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jo = new JSONObject (s);
                JSONArray ja = jo.getJSONArray ("daily");

                for (int i = 1; i < ja.length (); i++) {
                    JSONObject daily = ja.getJSONObject (i);

                    JSONArray ja2 = daily.getJSONArray ("weather");
                    JSONObject m = ja2.getJSONObject (0);
                    rain2 = m.getString ("main");

                    JSONObject temperature = daily.getJSONObject("temp");
                    temp2 = temperature.getString ("day");

                    date2 = daily.getString ("dt");
                    long dateParse = Long.parseLong (date2) * 1000;
                    Date dateFormat = new Date (dateParse);
                    vv = new SimpleDateFormat ("dd/MM/yyyy ").format (dateFormat);

                    JSONArray ja3 = daily.getJSONArray ("weather");
                    JSONObject icon = ja3.getJSONObject (0);
                    img2 = icon.getString ("icon");
                    img3= ConstantData.IMG_URL2 + img2 + ".png";

                    Weatherdata weatherdata=new Weatherdata (temp2, rain2,img3,vv);
                    weatherList.add(weatherdata);
                }
            } catch (Exception e) {
                e.printStackTrace ();
            }
            WeatherDataAdapter adapter = new WeatherDataAdapter(UpcomingWeatherUpdatesActivity.this, weatherList);
            listview.setAdapter(adapter);
        }
    }
}
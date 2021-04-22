package com.weatherupdate.glare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SearchClass extends AppCompatActivity {
    protected String data2;
    public String normalDate;
    String findCity;
    String findDate;
    String findSituation;
    String findImage;
    String findTemp;
    String findTimeZone;
    String findLat;
    String findLong;
    String findHum;
    String findWS;
    String findPressure;


    TextView cityName;
    TextView dateTime;
    TextView temp;
    ImageView image;
    TextView situation;
    TextView latitude;
    TextView longitude;
    TextView pressure;
    TextView windSpeed;
    TextView humidity;

    public static final String IMG_URL3 = "https://openweathermap.org/img/w/";

    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoidGFzZmlhc2V1dGkiLCJhIjoiY2tubzd1b3U5MTVjMzJvbW9ybm5laGU4bSJ9.3XIBkPnAK9juMzlx-Rar9A";
    PlaceOptions placeOptions;
    private static final  int REQUEST_CODE_AUTOCOMPLETE=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_third);

        cityName = findViewById (R.id.cityName);
        temp = findViewById (R.id.temperature);
        dateTime = findViewById (R.id.dateTime);
        image=findViewById (R.id.image);
        situation=findViewById (R.id.situation);

        latitude = findViewById (R.id.latitude3);
        longitude = findViewById (R.id.longitude3);
        humidity = findViewById (R.id.Humidity3);
        pressure = findViewById (R.id.pressure3);
        windSpeed = findViewById (R.id.windSpeed3);

        Mapbox.getInstance(this, getString(R.string.MAPBOX_ACCESS_TOKEN));
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(MAPBOX_ACCESS_TOKEN)
                .placeOptions(placeOptions)
                .build(this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            data2=feature.toJson();
            try{
            JSONObject jsonObject = new JSONObject (data2);
            findCity= jsonObject.getString ("text");
            cityName.setText (findCity);

            DataFetch process = new DataFetch ();
            process.execute ();
            } catch (JSONException e) {
                e.printStackTrace ( );
            }
           // Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
        }
    }
    private class DataFetch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String inputLine2;
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?q="+findCity+"&units=metric&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
                httpURLConnection.setRequestMethod ("GET");
                InputStream inputStream = httpURLConnection.getInputStream ();
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
                while ((inputLine2 = bufferedReader.readLine ()) != null) {
                    result.append (inputLine2);
                }
            } catch (ProtocolException e) {
                e.printStackTrace ();
            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            return result.toString ();
        }

        @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
        @Override
        protected void onPostExecute(String aVoid) {
            try {
                JSONObject jsonObject = new JSONObject (aVoid);

                //find temp
                JSONObject object2 = jsonObject.getJSONObject ("main");
                findTemp= object2.getString ("temp");
                temp.setText (findTemp + " °C ");

                //find image icon
                JSONArray jsonArray = jsonObject.getJSONArray ("weather");
                JSONObject object3 = jsonArray.getJSONObject (0);
                findImage = object3.getString ("icon");
                Picasso.get ().load (IMG_URL3 + findImage + ".png").into (image);

                //find situation
                JSONArray object4 = jsonObject.getJSONArray ("weather");
                JSONObject  m= object4.getJSONObject (0);
                findSituation = m.getString ("main");
                situation.setText(findSituation);

                //find latitude
                JSONObject object7 = jsonObject.getJSONObject ("coord");
                findLat = object7.getString ("lat");
                latitude.setText (findLat + "°  N ");

                //find longitude
                JSONObject object5 = jsonObject.getJSONObject ("coord");
                findLong = object5.getString ("lon");
                longitude.setText (findLong + "°  E ");

                //find humidity
                JSONObject object6 = jsonObject.getJSONObject ("main");
                findHum = object6.getString ("humidity");
                humidity.setText (findHum + " %");

                //find pressure
                JSONObject object9 = jsonObject.getJSONObject ("main");
                findPressure = object9.getString ("pressure");
                pressure.setText (findPressure + "  hPa");

                //find windSpeed
                JSONObject object10 = jsonObject.getJSONObject ("wind");
                findWS = object10.getString ("speed");
                windSpeed.setText (findWS + "  km/h");


                //findDate
                findDate = jsonObject.getString ("dt");
                findTimeZone=jsonObject.getString ("timezone");
                Calendar c= Calendar.getInstance(TimeZone.getTimeZone (findTimeZone));
                c.setTimeInMillis (Long.parseLong (findDate));
                DateFormat df=DateFormat.getDateInstance();
                df.setTimeZone(c.getTimeZone());
                normalDate=df.format(c.getTime());
                dateTime.setText (normalDate);

            }   catch (JSONException jsonException) {
                jsonException.printStackTrace ();
            }

        }
    }
}

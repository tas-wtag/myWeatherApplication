package com.weatherupdate.glare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class SearchActivity extends AppCompatActivity {

    Variables findLat=new Variables ();
    Variables findLong=new Variables ();
    StaticVars MAPBOX_ACCESS_TOKEN=new StaticVars ();
    String findCity;

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


    private String data2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_third);

        cityName = findViewById (R.id.cityName);
        temp = findViewById (R.id.temperature);
        dateTime = findViewById (R.id.dateTime2);
        image = findViewById (R.id.image);
        situation = findViewById (R.id.situation);

        latitude = findViewById (R.id.latitude3);
        longitude = findViewById (R.id.longitude3);
        humidity = findViewById (R.id.Humidity3);
        pressure = findViewById (R.id.pressure3);
        windSpeed = findViewById (R.id.windSpeed3);

        Mapbox.getInstance (this, getString (R.string.MAPBOX_ACCESS_TOKEN));
        Intent intent;
        intent = new PlaceAutocomplete.IntentBuilder ( )
                .accessToken (StaticVars.getMapboxAccessToken ())
                .placeOptions (StaticVars.placeOptions)
                .build (this);
        startActivityForResult (intent, StaticVars.REQUEST_CODE_AUTOCOMPLETE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && requestCode == StaticVars.REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            data2 = feature.toJson ( );

            try{
            JSONObject jsonObject = new JSONObject (data2);
            findCity=jsonObject.getString ("text");

            DataFetch process = new DataFetch ();
            process.execute ();

            } catch (JSONException e) {
                e.printStackTrace ( );
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    class DataFetch extends AsyncTask<String, Void, String> {
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
                JSONObject object7 = jsonObject.getJSONObject ("coord");
                findLat.setFindLat (object7.getString ("lat"));

                JSONObject object5 = jsonObject.getJSONObject ("coord");
                findLong.setFindLong (object5.getString ("lon"));

                Intent  i = new Intent(SearchActivity.this, MainActivity.class);
                i.putExtra("latitude3",findLat.getFindLat ());
                i.putExtra("longitude3",findLong.getFindLong ());
                SharedPrefManager sPref=new SharedPrefManager (this);
                sPref.setSearchActivity ("latitude3", findLat.getFindLat ());
                sPref.setSearchActivity ("longitude3", findLong.getFindLong ());
                startActivity (i);

            }   catch (JSONException jsonException) {
                jsonException.printStackTrace ();
            }
        }
    }
}

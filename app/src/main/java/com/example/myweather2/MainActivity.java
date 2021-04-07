package com.example.myweather2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static TextView data;

    ListView simpleList;

    private TextView finalResult;

    //location
    protected LocationManager locationManager ;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String city_find;
    String country_find;
    String temp_find;
    String img;
    double lat_find;
    double lon_find;
    String date;
   int hum_find;
   String sunset_find;
   String sunrise_find;
    String windSpeed_find;
    String pressure_find;
    EditText editText1, editText2;
    double lati;
    double longi;
    TextView TextView3;
    Button button;
    Button next;
    ImageView imageView;
    TextView country, city, temp, time;
    TextView latitude1;
    TextView longitude1;
    TextView pressure;
    TextView humidity;
    TextView sunrise;
    TextView sunset;
    TextView wind_speed;
    //TextView lattt;
   // TextView lonnn;
    public static final String IMG_URL = "https://openweathermap.org/img/w/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        checkLocationPermission();

        button = findViewById (R.id.button);
        next = (Button) findViewById (R.id.next);
        country = findViewById (R.id.country);
        city = findViewById (R.id.city);
        temp = findViewById (R.id.temp);
        time = findViewById (R.id.textView2);

        latitude1 = findViewById (R.id.Latitude);
        longitude1 = findViewById (R.id.Longitude);
        humidity = findViewById (R.id.Humidity);
        sunrise = findViewById (R.id.Sunrise);
        sunset = findViewById (R.id.Sunset);
        pressure = findViewById (R.id.pressure);
        wind_speed = findViewById (R.id.WindSpeed);

      //  lattt=findViewById (R.id.lat);
       // lonnn=findViewById (R.id.lon);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        next.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, SecondActivity.class);
                startActivity (intent);
            }
        });
   button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                fetchData process = new fetchData ();
                process.execute ();

            }
        });


        //simpleList = (ListView) findViewById(R.id.simpleListView);
        //SecondActivity customAdapter = new SecondActivity(getApplicationContext(), countryList, flags);
        //simpleList.setAdapter((ListAdapter) customAdapter);
    }

//location
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission (this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale (this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder (this)
                        .setTitle (R.string.title_location_permission)
                        .setMessage (R.string.text_location_permission)
                        .setPositiveButton (R.string.ok, new DialogInterface.OnClickListener () {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions (MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create ()
                        .show ();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions (this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }return false;
        }else {
            return true;
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return ;
            }

        }
    }
    @Override
    public void onLocationChanged(Location location) {

        lati=location.getLatitude();
        longi=location.getLongitude();
        String lat = String.valueOf (lati);
        String lon = String.valueOf (longi);

        //lattt.setText(lat);
       // lonnn.setText (lon);

    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }


   @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);

    }


   private class fetchData extends AsyncTask<String, String, Void> {
        String inputLine;


        @Override
        protected Void doInBackground(String... params) {
            try {

                URL url = new URL ("http://api.openweathermap.org/data/2.5/weather?units=metric&lat=23&lon=90&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
                httpURLConnection.setRequestMethod ("GET");
                InputStream inputStream = httpURLConnection.getInputStream ();
                BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
                StringBuffer response = new StringBuffer ();
                while ((inputLine = bufferedReader.readLine ()) != null) {
                    response.append (inputLine);
                }


                //find country
                JSONObject jsonObject = new JSONObject (inputLine);
                JSONObject object1 = jsonObject.getJSONObject ("sys");
                country_find = object1.getString ("country");

                //find city
                city_find = jsonObject.getString ("name");


                //find temp
                JSONObject object2 = jsonObject.getJSONObject ("main");
                temp_find = object2.getString ("temp");


                //find image icon
                JSONArray jsonArray = jsonObject.getJSONArray ("weather");
                JSONObject object3 = jsonArray.getJSONObject (0);
                img = object3.getString ("icon");


                //find date
                Calendar calender = Calendar.getInstance ();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat std = new SimpleDateFormat ("dd/MM/yyyy \nHH:mm:ssa");
                //std.setTimeZone(TimeZone.getTimeZone("UTC"));
                date = std.format (calender.getTime ());


                //find latitude
                JSONObject object4 = jsonObject.getJSONObject ("coord");
                lat_find = object4.getDouble ("lat");


                //find longitude
                JSONObject object5 = jsonObject.getJSONObject ("coord");
                lon_find = object5.getDouble ("lon");


                //find humidity
                JSONObject object6 = jsonObject.getJSONObject ("main");
                hum_find = object6.getInt ("humidity");


                //find sunrise
                JSONObject object7 = jsonObject.getJSONObject ("sys");
                sunrise_find = object7.getString ("sunrise");


                //find sunset
                JSONObject object8 = jsonObject.getJSONObject ("sys");
                sunset_find = object8.getString ("sunset");
                //long dv2 = Long.parseLong (sunset_find) * 1000;// it needs to be in milisecond
              //  Date df2 = new Date (dv2);
                //@SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat ("hh:mma").format (df2);


                //find pressure
                JSONObject object9 = jsonObject.getJSONObject ("main");
                pressure_find = object9.getString ("pressure");


                //find windSpeed
                JSONObject object10 = jsonObject.getJSONObject ("wind");
                windSpeed_find = object10.getString ("speed");


            } catch (IOException ioException) {
                ioException.printStackTrace ();
            } catch (JSONException jsonException) {
                jsonException.printStackTrace ();
            }

            return null;}

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute (aVoid);
            country.setText (country_find);
            time.setText (date);
            city.setText (city_find);
            long dv = Long.parseLong (sunrise_find) * 1000;// it needs to be in milisecond
            Date df = new Date (dv);
            @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat ("hh:mma").format (df);
            sunrise.setText (vv);
            long dv2 = Long.parseLong (sunset_find) * 1000;// it needs to be in milisecond
            Date df2 = new Date (dv2);
            @SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat ("hh:mma").format (df2);
            sunset.setText (vv2);
            imageView = (ImageView) findViewById (R.id.imageButton);
            Picasso.get ().load (IMG_URL + img + ".png").into (imageView);
            latitude1.setText (lat_find + "°  N ");
            longitude1.setText (lon_find + "°  E ");
            temp.setText (temp_find + " °C ");
            pressure.setText (pressure_find + "  hPa");
            wind_speed.setText (windSpeed_find + "  km/h");
            humidity.setText (hum_find + " %");

        }
    }
    }
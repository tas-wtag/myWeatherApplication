package com.weatherupdate.glare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity<doTask> extends AppCompatActivity implements LocationListener{

    //location
    protected LocationManager locationManager ;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String city_find;
    String country_find;
    String temp_find;
    String img;
    String date;
    String sunset_find;
    String sunrise_find;
    String windSpeed_find;
    String pressure_find;
    double lat_find;
    double lon_find;
    double lati;
    double longi;
    int hum_find;

    Button searchCity;
    Button next;
    ImageView imageView;
    TextView country, city, temp, dateTime;
    TextView latitude1;
    TextView longitude1;
    TextView pressure;
    TextView humidity;
    TextView sunrise;
    TextView sunset;
    TextView wind_speed;

    public static final String IMG_URL = "https://openweathermap.org/img/w/";

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        checkLocationPermission();

        next =  findViewById (R.id.next);
        searchCity=findViewById (R.id.search);

        country = findViewById (R.id.cityName);
        city = findViewById (R.id.city);
        temp = findViewById (R.id.temperature);
        dateTime = findViewById (R.id.dateTime2);

        latitude1 = findViewById (R.id.latitude3);
        longitude1 = findViewById (R.id.longitude3);
        humidity = findViewById (R.id.Humidity3);
        sunrise = findViewById (R.id.Sunrise);
        sunset = findViewById (R.id.Sunset);
        pressure = findViewById (R.id.pressure3);
        wind_speed = findViewById (R.id.windSpeed3);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        next.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, SecondActivity.class);
                intent.putExtra("Latitude", lati);
                intent.putExtra("Longitude", longi);
                startActivity(intent);
            }
        });

        searchCity.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, SearchClass.class);
                startActivity(intent);
            }
        });

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
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
                    }

                } else {

                }
                return ;
            }

        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lati=location.getLatitude();
        longi=location.getLongitude();
        FetchData process = new FetchData (dateTime);
        process.execute ();
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    protected void onResume() {
        super.onResume();
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.removeUpdates(this);
    }


    private class FetchData extends AsyncTask<String, Void, String> {
      TextView text;
      public FetchData(TextView textView){
          this.text=textView;
      }
       @Override
       protected String doInBackground(String... params) {
           String lat = String.valueOf (lati);
           String lon = String.valueOf (longi);
           String inputLine;
           StringBuilder result = new StringBuilder();
           try {
               URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
               httpURLConnection.setRequestMethod ("GET");
             InputStream inputStream = httpURLConnection.getInputStream ();
                   BufferedReader  bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
               while ((inputLine = bufferedReader.readLine ()) != null) {
                   result.append (inputLine);
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

           @SuppressLint("SetTextI18n")
       @Override
       protected void onPostExecute(String aVoid) {
           try {
               //find country
               JSONObject jsonObject = new JSONObject (aVoid);
               JSONObject object1 = jsonObject.getJSONObject ("sys");
               country_find = object1.getString ("country");
               country.setText (country_find);

               //find city
               city_find = jsonObject.getString ("name");
               city.setText (city_find);

               //add realTime clock
               date = jsonObject.getString ("dt");
               long date2= Long.parseLong (date) * 1000;
               @SuppressLint("SimpleDateFormat") SimpleDateFormat format=new SimpleDateFormat ( "dd-MM-yyyy\nHH:mm:ssa " );
               Calendar cal = Calendar.getInstance();
               cal.setTimeInMillis (date2);
               updateTime (String.valueOf (date2),text);

               //find temp
               JSONObject object2 = jsonObject.getJSONObject ("main");
               temp_find = object2.getString ("temp");
               temp.setText (temp_find + " °C ");

               //find image icon
               JSONArray jsonArray = jsonObject.getJSONArray ("weather");
               JSONObject object3 = jsonArray.getJSONObject (0);
               img = object3.getString ("icon");
               imageView = (ImageView) findViewById (R.id.image);
               Picasso.get ().load (IMG_URL + img + ".png").into (imageView);

               //find latitude
               JSONObject object4 = jsonObject.getJSONObject ("coord");
               lat_find = object4.getDouble ("lat");
               latitude1.setText (lat_find + "°  N ");

               //find longitude
               JSONObject object5 = jsonObject.getJSONObject ("coord");
               lon_find = object5.getDouble ("lon");
               longitude1.setText (lon_find + "°  E ");

               //find humidity
               JSONObject object6 = jsonObject.getJSONObject ("main");
               hum_find = object6.getInt ("humidity");
               humidity.setText (hum_find + " %");

               //find sunrise
               JSONObject object7 = jsonObject.getJSONObject ("sys");
               sunrise_find = object7.getString ("sunrise");
               long dv = Long.parseLong (sunrise_find) * 1000;
               Date df = new Date (dv);
               @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat ("hh:mma").format (df);
               sunrise.setText (vv);

               //find sunset
               JSONObject object8 = jsonObject.getJSONObject ("sys");
               sunset_find = object8.getString ("sunset");
               long dv2 = Long.parseLong (sunset_find) * 1000;
               Date df2 = new Date (dv2);
               @SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat ("hh:mma").format (df2);
               sunset.setText (vv2);

               //find pressure
               JSONObject object9 = jsonObject.getJSONObject ("main");
               pressure_find = object9.getString ("pressure");
               pressure.setText (pressure_find + "  hPa");

               //find windSpeed
               JSONObject object10 = jsonObject.getJSONObject ("wind");
               windSpeed_find = object10.getString ("speed");
               wind_speed.setText (windSpeed_find + "  km/h");

           } catch (JSONException jsonException) {
               jsonException.printStackTrace ();
           }
       }
        Runnable updater;
        void updateTime(final String timeString,TextView dateTime2) {
            final Handler timerHandler = new Handler();
            updater = new Runnable() {
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {
                    Calendar calender = Calendar.getInstance();
                    int day=calender.get(Calendar.DATE);
                    int month=calender.get(Calendar.MONTH)+1;
                    int year=calender.get(Calendar.YEAR);
                    int hour=calender.get (Calendar.HOUR_OF_DAY);
                    int min=calender.get (Calendar.MINUTE);
                    int sec=calender.get(Calendar.SECOND);
                    dateTime2.setText(day+"-"+month+"-"+year+"\n"+hour+":"+min+":"+sec);
                    timerHandler.postDelayed(updater,1000);
                }
            };
            timerHandler.post(updater);
        }

    }

}

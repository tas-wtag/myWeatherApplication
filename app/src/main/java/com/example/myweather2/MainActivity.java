package com.example.myweather2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static TextView data;

    ListView simpleList;

    private TextView finalResult;
    ProgressDialog progressDialog;
    private static MainActivity instance;
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

   /* public static MainActivity getInstance() {
        return instance;
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        checkLocationPermission();

      /*  Intent intent = new Intent(MainActivity.this , SecondActivity.class);
        intent.putExtra("Latitude", latitude);
        intent.putExtra("Longitude", longitude);
        startActivity(intent);*/

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

        instance = this;

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
                FetchData process = new FetchData ();
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

        //lattt.setText(lat);
        //lonnn.setText (lon);

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

   private class FetchData extends AsyncTask<String, Void, String> {

    /*  @Override
       protected void onPreExecute() {
           super.onPreExecute();
           // display a progress dialog for good user experiance
           progressDialog = new ProgressDialog (MainActivity.this);
           progressDialog.setMessage("Please Wait");
           progressDialog.setCancelable(false);
           progressDialog.show();
       }*/
       @Override
       protected String doInBackground(String... params) {
           String lat = String.valueOf (lati);
           String lon = String.valueOf (longi);
           String inputLine;
           StringBuilder result = new StringBuilder();
           try {
               URL url = new URL ("http://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
               httpURLConnection.setRequestMethod ("GET");
               InputStream inputStream = httpURLConnection.getInputStream ();
               //InputStreamReader isw = new InputStreamReader(inputStream);
               BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
               // StringBuffer response = new StringBuffer ();
               while ((inputLine = bufferedReader.readLine ()) != null) {
                   result.append (inputLine);
               }
               Log.d ("***data", result.toString ());
              // progressDialog.dismiss();
           } catch (ProtocolException e) {
               e.printStackTrace ();
           } catch (MalformedURLException e) {
               e.printStackTrace ();
           } catch (IOException e) {
               e.printStackTrace ();
           }
           return result.toString ();
    }

           //@SuppressLint("SetTextI18n")
       @Override
       protected void onPostExecute(String aVoid) {
           //Log.d("data", aVoid.toString());


           try {
               //find country
               JSONObject jsonObject = new JSONObject (aVoid);
               JSONObject object1 = jsonObject.getJSONObject ("sys");
               country_find = object1.getString ("country");
               country.setText (country_find);

               //find city
               city_find = jsonObject.getString ("name");
               city.setText (city_find);

               //find temp
               JSONObject object2 = jsonObject.getJSONObject ("main");
               temp_find = object2.getString ("temp");
               temp.setText (temp_find + " °C ");

               //find image icon
               JSONArray jsonArray = jsonObject.getJSONArray ("weather");
               JSONObject object3 = jsonArray.getJSONObject (0);
               img = object3.getString ("icon");
               imageView = (ImageView) findViewById (R.id.imageButton);
               Picasso.get ().load (IMG_URL + img + ".png").into (imageView);

               //find date
               Calendar calender = Calendar.getInstance ();
               @SuppressLint("SimpleDateFormat") SimpleDateFormat std = new SimpleDateFormat ("dd/MM/yyyy \nHH:mm:ssa");
               //std.setTimeZone(TimeZone.getTimeZone("UTC"));
               date = std.format (calender.getTime ());
               time.setText (date);

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
               long dv = Long.parseLong (sunrise_find) * 1000;// it needs to be in milisecond
               Date df = new Date (dv);
               @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat ("hh:mma").format (df);
               sunrise.setText (vv);

               //find sunset
               JSONObject object8 = jsonObject.getJSONObject ("sys");
               sunset_find = object8.getString ("sunset");
               long dv2 = Long.parseLong (sunset_find) * 1000;// it needs to be in milisecond
               Date df2 = new Date (dv2);
               @SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat ("hh:mma").format (df2);
               sunset.setText (vv2); //long dv2 = Long.parseLong (sunset_find) * 1000;// it needs to be in milisecond
               //  Date df2 = new Date (dv2);
               //@SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat ("hh:mma").format (df2);


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
   }
}

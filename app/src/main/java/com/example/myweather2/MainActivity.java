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

import androidx.annotation.NonNull;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static TextView data;

    ListView simpleList;

    private TextView finalResult;

    //location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
        }else
        {return true;}
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
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, this);

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



    /*  public void findweather(){

        //location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    //String city1=editText.getText().toString();

    // String url="http://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf";


    StringRequest stringRequest=new StringRequest(Request.Method.GET, url, (Response.Listener<String>) response -> {
       //calling api


           try {
               JSONObject jsonObject =new JSONObject(response);
               //find country
               JSONObject object1=jsonObject.getJSONObject("sys");
               String country_find=object1.getString("country");
               country.setText(country_find);

               //find city
               String city_find=jsonObject.getString("name");
               city.setText(city_find);

               //find temp
               JSONObject object2=jsonObject.getJSONObject("main");
               String temp_find=object2.getString("temp");
               temp.setText(temp_find+" °C ");

               //find image icon
               JSONArray jsonArray=jsonObject.getJSONArray("weather");
               JSONObject object3=jsonArray.getJSONObject(0);
               String img=object3.getString("icon");
               imageView=(ImageView) findViewById(R.id.imageButton);
               Picasso.get().load(IMG_URL+img+".png").into(imageView);

               //find date
               Calendar calender=Calendar.getInstance();
               @SuppressLint("SimpleDateFormat") SimpleDateFormat std=new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ssa");
               //std.setTimeZone(TimeZone.getTimeZone("UTC"));
               String date=std.format(calender.getTime());
               time.setText(date);

               //find latitude
               JSONObject object4=jsonObject.getJSONObject("coord");
               double lat_find=object4.getDouble("lat");
               latitude1.setText(lat_find+"°  N ");

               //find longitude
               JSONObject object5=jsonObject.getJSONObject("coord");
               double lon_find=object5.getDouble("lon");
               longitude1.setText(lon_find+"°  E ");

               //find humidity
               JSONObject object6=jsonObject.getJSONObject("main");
               int hum_find=object6.getInt("humidity");
               humidity.setText(hum_find+" %");

               //find sunrise
               JSONObject object7=jsonObject.getJSONObject("sys");
               String sunrise_find=object7.getString("sunrise");
               long dv = Long.parseLong(sunrise_find)*1000;// it needs to be in milisecond
               Date df = new Date(dv);
               @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat("hh:mma").format(df);
               sunrise.setText(vv);

               //find sunset
               JSONObject object8=jsonObject.getJSONObject("sys");
               String sunset_find=object8.getString("sunset");
               long dv2 = Long.parseLong(sunset_find)*1000;// it needs to be in milisecond
               Date df2 = new Date(dv2);
               @SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat("hh:mma").format(df2);
               sunset.setText(vv2);

               //find pressure
               JSONObject object9=jsonObject.getJSONObject("main");
               String pressure_find=object9.getString("pressure");
               pressure.setText(pressure_find+"  hPa");

               //find windSpeed
               JSONObject object10=jsonObject.getJSONObject("wind");
               String windSpeed_find=object10.getString("speed");
               wind_speed.setText(windSpeed_find+"  km/h");

           } catch (JSONException e) {
               e.printStackTrace();
           }
       },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
        //new MyHttpRequestClass().execute();
        }*/





    private class fetchData extends AsyncTask <String, String, String> {
        String  data1;


        @Override
        protected String doInBackground(String... params) {
            try{
                String lat=String.valueOf(lati);
                String lon=String.valueOf(longi);
                URL url=new URL("http://api.openweathermap.org/data/2.5/weather?units=metric&lat=23&lon=90&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
                InputStream inputStream=httpURLConnection.getInputStream ();
                BufferedReader bufferedReader=new BufferedReader (new InputStreamReader (inputStream));
               String line="";
               while (line!=null){
                   line=bufferedReader.readLine ();
                   data1=data1+line;


                   //find country
                   JSONObject jsonObject = new JSONObject (data1);
                   JSONObject object1= jsonObject.getJSONObject("sys");
                   String   country_find = object1.getString("country");
                   country.setText(country_find);
                   //find city
                   String city_find = jsonObject.getString("name");
                   city.setText(city_find);

                   //find temp
                   JSONObject object2=jsonObject.getJSONObject("main");
                   String temp_find = object2.getString("temp");
                   temp.setText(temp_find+" °C ");

                   //find image icon
                   JSONArray jsonArray= null;
                   try {
                       jsonArray = jsonObject.getJSONArray("weather");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   JSONObject object3= null;
                   try {
                       object3 = jsonArray.getJSONObject(0);
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   String img= null;
                   try {
                       img = object3.getString("icon");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   imageView=(ImageView) findViewById(R.id.imageButton);
                   Picasso.get().load(IMG_URL+img+".png").into(imageView);

                   //find date
                   Calendar calender=Calendar.getInstance();
                   @SuppressLint("SimpleDateFormat") SimpleDateFormat std=new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ssa");
                   //std.setTimeZone(TimeZone.getTimeZone("UTC"));
                   String date=std.format(calender.getTime());
                   time.setText(date);

                   //find latitude
                   JSONObject object4= null;
                   try {
                       object4 = jsonObject.getJSONObject("coord");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   double lat_find= 0;
                   try {
                       lat_find = object4.getDouble("lat");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   latitude1.setText(lat_find+"°  N ");

                   //find longitude
                   JSONObject object5= null;
                   try {
                       object5 = jsonObject.getJSONObject("coord");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   double lon_find= 0;
                   try {
                       lon_find = object5.getDouble("lon");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   longitude1.setText(lon_find+"°  E ");

                   //find humidity
                   JSONObject object6= null;
                   try {
                       object6 = jsonObject.getJSONObject("main");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   int     hum_find = object6.getInt("humidity");
                   humidity.setText(hum_find+" %");

                   //find sunrise
                   JSONObject object7= jsonObject.getJSONObject("sys");

                   String sunrise_find=  object7.getString("sunrise");;
                   
                   long dv = Long.parseLong(sunrise_find)*1000;// it needs to be in milisecond
                   Date df = new Date(dv);
                   @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat("hh:mma").format(df);
                   sunrise.setText(vv);

                   //find sunset
                   JSONObject object8= null;
                   try {
                       object8 = jsonObject.getJSONObject("sys");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   String sunset_find= null;
                   try {
                       sunset_find = object8.getString("sunset");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   long dv2 = Long.parseLong(sunset_find)*1000;// it needs to be in milisecond
                   Date df2 = new Date(dv2);
                   @SuppressLint("SimpleDateFormat") String vv2 = new SimpleDateFormat("hh:mma").format(df2);
                   sunset.setText(vv2);

                   //find pressure
                   JSONObject object9= null;
                   try {
                       object9 = jsonObject.getJSONObject("main");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   String pressure_find= null;
                   try {
                       pressure_find = object9.getString("pressure");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   pressure.setText(pressure_find+"  hPa");

                   //find windSpeed
                   JSONObject object10= null;
                   try {
                       object10 = jsonObject.getJSONObject("wind");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   String windSpeed_find= null;
                   try {
                       windSpeed_find = object10.getString("speed");
                   } catch (JSONException e) {
                       e.printStackTrace ();
                   }
                   wind_speed.setText(windSpeed_find+"  km/h");

               }

            }catch (MalformedURLException e){
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            return null;
        }


        @Override
        public void onPostExecute(String aVoid) {
            super.onPostExecute (aVoid);
           // fetchData.execute ();
           // MainActivity.data.setText (this.data);


            // execution of result of Long time consuming operation
        }
    }
}
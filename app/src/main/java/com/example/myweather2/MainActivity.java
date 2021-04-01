package com.example.myweather2;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements LocationListener {

    ListView simpleList;

    private Button abutton;
    private EditText atime;
    private TextView finalResult;

    //location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    public static final int REQUEST_CODE_PERMISSIONS = 101;

    EditText editText1,editText2;
    double lati;
    double longi;
    TextView TextView3;
    Button button;
    Button next;
    ImageView imageView;
    TextView country,city,temp,time;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //editText1=findViewById(R.id.editTextTextPersonName);
        //editText2=findViewById(R.id.editTextTextPersonName2);
        button = findViewById(R.id.button);
        next = (Button) findViewById(R.id.next);
        country=findViewById(R.id.country);
        city=findViewById(R.id.city);
        temp=findViewById(R.id.temp);
        time=findViewById(R.id.textView2);

        latitude1=findViewById(R.id.Latitude);
        longitude1=findViewById(R.id.Longitude);
        humidity=findViewById(R.id.Humidity);
        sunrise=findViewById(R.id.Sunrise);
        sunset=findViewById(R.id.Sunset);
        pressure=findViewById(R.id.pressure);
        wind_speed=findViewById(R.id.WindSpeed);

        atime = (EditText) findViewById(R.id.in_time);
        abutton = (Button) findViewById(R.id.btn_run);
        finalResult = (TextView) findViewById(R.id.tv_result);
        abutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = atime.getText().toString();
                runner.execute(sleepTime);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findweather();
            }
        });
        //simpleList = (ListView) findViewById(R.id.simpleListView);
        //SecondActivity customAdapter = new SecondActivity(getApplicationContext(), countryList, flags);
        //simpleList.setAdapter((ListAdapter) customAdapter);
    }

    public void findweather(){

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
        String lat=String.valueOf(lati);
        String lon=String.valueOf(longi);
        String url="http://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf";


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

    }
    public void sendMessage(View view) {
        // Do something in response to button
    }

    //location
    @Override
    public void onLocationChanged(Location location) {

       lati=location.getLatitude();
       longi=location.getLongitude();
       // TextView3 = (TextView) findViewById(R.id.textView3);
        //TextView3.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());

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

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                int time = Integer.parseInt(params[0])*1000;

                Thread.sleep(time);
                resp = "Slept for " + params[0] + " seconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for "+atime.getText().toString()+ " seconds");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            finalResult.setText(text[0]);

        }
    }
}
//hellooooo
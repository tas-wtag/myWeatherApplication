package com.example.myweather2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    EditText editText1,editText2;
    Button button;
    ImageView imageView;
    TextView country,city,temp,time;
    TextView latitude;
    TextView longitude;
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

        editText1=findViewById(R.id.editTextTextPersonName);
        editText2=findViewById(R.id.editTextTextPersonName2);
        button = findViewById(R.id.button);
        country=findViewById(R.id.country);
        city=findViewById(R.id.city);
        temp=findViewById(R.id.temp);
        time=findViewById(R.id.textView2);

        latitude=findViewById(R.id.Latitude);
        longitude=findViewById(R.id.Longitude);
        humidity=findViewById(R.id.Humidity);
        sunrise=findViewById(R.id.Sunrise);
        sunset=findViewById(R.id.Sunset);
        pressure=findViewById(R.id.pressure);
        wind_speed=findViewById(R.id.WindSpeed);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findweather();
            }
        });

    }



    public void findweather(){
        //String city1=editText.getText().toString();
        String lat=editText1.getText().toString();
        String lon=editText2.getText().toString();
        String url="http://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf";


        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
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
                    SimpleDateFormat std=new SimpleDateFormat("dd/MM/yyyy \nHH:mm:ssa");
                    //std.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String date=std.format(calender.getTime());
                    time.setText(date);

                    //find latitude
                    JSONObject object4=jsonObject.getJSONObject("coord");
                    double lat_find=object4.getDouble("lat");
                    latitude.setText(lat_find+"°  N ");

                    //find longitude
                    JSONObject object5=jsonObject.getJSONObject("coord");
                    double lon_find=object5.getDouble("lon");
                    longitude.setText(lon_find+"°  E ");

                    //find humidity
                    JSONObject object6=jsonObject.getJSONObject("main");
                    int hum_find=object6.getInt("humidity");
                    humidity.setText(hum_find+" %");

                    //find sunrise
                    JSONObject object7=jsonObject.getJSONObject("sys");
                    String sunrise_find=object7.getString("sunrise");
                    long dv = Long.parseLong(sunrise_find)*1000;// it needs to be in milisecond
                    Date df = new java.util.Date(dv);
                    @SuppressLint("SimpleDateFormat") String vv = new SimpleDateFormat("hh:mma").format(df);
                    sunrise.setText(vv);

                    //find sunset
                    JSONObject object8=jsonObject.getJSONObject("sys");
                    String sunset_find=object8.getString("sunset");
                    long dv2 = Long.parseLong(sunset_find)*1000;// it needs to be in milisecond
                    Date df2 = new java.util.Date(dv2);
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
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}
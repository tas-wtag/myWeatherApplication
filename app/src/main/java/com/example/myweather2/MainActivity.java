package com.example.myweather2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.io.StringReader;

public class MainActivity extends AppCompatActivity {


    EditText editText1,editText2;
    Button button;
    ImageView imageView;
    TextView country,city,temp;

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
package com.weatherupdate.glare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
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
import java.util.TimeZone;

import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity implements LocationListener {


     String latitude3;
     String longitude3;
     String lat;
     String lon;
     Variables country_find=new Variables ();
     Variables temp_find=new Variables ();
     Variables img=new Variables ();
     Variables date=new Variables ();
     Variables sunset_find=new Variables ();
     Variables sunrise_find=new Variables ();
     Variables windSpeed_find=new Variables ();
     Variables pressure_find=new Variables ();
     Variables findTimeZone=new Variables ();
     Variables date2=new Variables ();
     Variables timePause=new Variables ();
     Variables lat_find=new Variables ();
     Variables lon_find=new Variables ();
     Variables lati=new Variables ();
     Variables longi=new Variables ();
     Variables hum_find=new Variables ();
     Variables dateInPause=new Variables ();
     Variables dateInResume=new Variables ();
    Variables dateInPause2=new Variables ();

    ImageView imageView;
    TextView country, city, temp, dateTime;
    TextView latitude1;
    TextView longitude1;
    TextView pressure;
    TextView humidity;
    TextView sunrise;
    TextView sunset;
    TextView wind_speed;

    SharedPrefManager sharedPrefManager=new SharedPrefManager (this);
    SharedPrefManager sharedPrefManager2=new SharedPrefManager (this);
    SharedPrefManager sharedPrefManager3=new SharedPrefManager (this);
    protected LocationManager locationManager;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent (MainActivity.this, SearchActivity.class);
                startActivity (intent);

                return true;

            case R.id.action_upcoming:
                Intent intent2 = new Intent (MainActivity.this, UpcomingUpdatesActivity.class);
                intent2.putExtra ("Latitude",lat);
                intent2.putExtra ("Longitude",lon);
                startActivity (intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        checkLocationPermission ( );

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF6200EE"));

        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);
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
        locationManager = (LocationManager) getApplicationContext ( ).getSystemService (LOCATION_SERVICE);
    }
    //location
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission (this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale (this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder (this)
                        .setTitle (R.string.title_location_permission)
                        .setMessage (R.string.text_location_permission)
                        .setPositiveButton (R.string.ok, (dialogInterface, i) -> {
                            ActivityCompat.requestPermissions (MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    StaticVars.MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create ( )
                        .show ( );
            } else {
                ActivityCompat.requestPermissions (this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        StaticVars.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String @NotNull [] permissions, int @NotNull [] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == StaticVars.MY_PERMISSIONS_REQUEST_LOCATION) {
            if ((grantResults.length > 0)
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                if (ContextCompat.checkSelfPermission (this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 400, 1, this);
                }
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        lati.setLati ((location.getLatitude ( )));
        longi.setLongi ((location.getLongitude ( )));
        FetchData process = new FetchData (dateTime);
        process.execute ( );
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
        super.onResume ( );
         sharedPrefManager.getSearchActivity ("MySP");
        if(sharedPrefManager.getSearchActivity ("MySP")!=null)
        {
            latitude3=sharedPrefManager2.getSearchActivity ("latitude3");
            longitude3=sharedPrefManager2.getSearchActivity ("longitude3");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 400, 1, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Calendar calendar = Calendar.getInstance ();
        dateInPause.setDateInPause ((calendar.getTimeInMillis ()));
        timePause.setTimePause (Long.toString (dateInPause.getDateInPause ()));

        sharedPrefManager.setPauseTime ("timePause", timePause.getTimePause ());
        if (ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onStop() {
        super.onStop ( );
    }
    @Override
    protected void onStart() {
        super.onStart ( );
    }

    @Override
    protected void onRestart() {
        super.onRestart ( );
        Calendar calendar2 = Calendar.getInstance ( );
        dateInResume.setDateInResume ((calendar2.getTimeInMillis ()));
        sharedPrefManager3.getPauseTime ("MySharedPref", timePause.getTimePause ()) ;
        if(sharedPrefManager3.getPauseTime ("MySharedPref", timePause.getTimePause ()) !=null) {
            String timePause2 = sharedPrefManager3.getPauseTime ("timePause", timePause.getTimePause ());
            if(timePause2!=null) {
                dateInPause2.setDateInPause2 (parseLong (timePause2));
                long d = dateInResume.getDateInResume () - dateInPause2.getDateInPause2 ();
                if (d > 300000) {
                    Intent intent = new Intent (MainActivity.this, MainActivity.class);
                    finish ( );
                    startActivity (intent);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchData extends AsyncTask<String, Void, String> {
      TextView text;
      public FetchData(TextView textView){
          this.text=textView;
      }
       @Override
       protected String doInBackground(String... params) {
           Bundle extras = getIntent().getExtras();
           if (extras != null) {
               lat=(extras.getString ("latitude3"));
               lon=(extras.getString ("longitude3"));
           }
           else if (!latitude3.equals ("")) {
               if (!longitude3.equals ("")) {
                   lat= (latitude3);
                   lon=(longitude3);
               } else {
                   lat=(String.valueOf (lati.getLati ()));
                   lon=(String.valueOf (longi.getLongi ()));
               }
           } else {
               lat=(String.valueOf (lati.getLati ()));
               lon= (String.valueOf (longi.getLongi ()));
           }
           String inputLine;
           StringBuilder result = new StringBuilder();
           try {
               URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?units=metric&lat="+lat+"&lon="+lon+"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
               HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection ();
               httpURLConnection.setRequestMethod ("GET");
               httpURLConnection.setDoOutput(false);
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
               JSONObject jsonObject = new JSONObject (aVoid);
               findTimeZone.setFindTimeZone (jsonObject.getString ("timezone"));

               JSONObject object1 = jsonObject.getJSONObject ("sys");
               country_find.setCountry_find (object1.getString ("country"));
               country.setText (country_find.getCountry_find ());

               StaticVars.city_find = jsonObject.getString ("name");
               city.setText (StaticVars.city_find);

               JSONObject object2 = jsonObject.getJSONObject ("main");
               temp_find.setTemp_find (object2.getString ("temp"));
               temp.setText (temp_find.getTemp_find () + " °C ");

               JSONArray jsonArray = jsonObject.getJSONArray ("weather");
               JSONObject object3 = jsonArray.getJSONObject (0);
               img.setImg (object3.getString ("icon"));
               imageView = findViewById (R.id.image);
               Picasso.get ().load (StaticVars.IMG_URL + img.getImg () + ".png").into (imageView);

               JSONObject object4 = jsonObject.getJSONObject ("coord");
               lat_find.setLat_find (object4.getDouble ("lat"));
               latitude1.setText (lat_find.getLat_find () + "°  N ");

               JSONObject object5 = jsonObject.getJSONObject ("coord");
               lon_find.setLon_find ( object5.getDouble ("lon"));
               longitude1.setText (lon_find.getLon_find () + "°  E ");

               JSONObject object6 = jsonObject.getJSONObject ("main");
               hum_find.setHum_find (object6.getInt ("humidity"));
               humidity.setText (hum_find.getHum_find () + " %");

               JSONObject object7 = jsonObject.getJSONObject ("sys");
               sunrise_find.setSunrise_find (object7.getString ("sunrise"));
               int findTimeZoneInt=Integer.parseInt (findTimeZone.getFindTimeZone ());
               int findSunriseInt=Integer.parseInt (sunrise_find.getSunrise_find ());
               int sunriseToShowInt=findSunriseInt+findTimeZoneInt;
               String sunriseToShow=Integer.toString(sunriseToShowInt);
               long sunriseLong= Long.parseLong (sunriseToShow) * 1000;
               Date sunriseFind = new Date(sunriseLong);
               @SuppressLint("SimpleDateFormat") SimpleDateFormat format= new SimpleDateFormat ("hh:mma");
               format.setTimeZone (TimeZone.getTimeZone ("GMT"));
               sunrise.setText (format.format(sunriseFind));

               JSONObject object8 = jsonObject.getJSONObject ("sys");
               sunset_find.setSunset_find (object8.getString ("sunset"));
               int findSunsetInt=Integer.parseInt (sunset_find.getSunset_find ());
               int sunsetToShowInt=findSunsetInt+findTimeZoneInt;
               String sunsetToShow=Integer.toString(sunsetToShowInt);
               long sunsetLong = Long.parseLong (sunsetToShow) * 1000;
               Date sunsetFind = new Date(sunsetLong);
               format.setTimeZone (TimeZone.getTimeZone ("GMT"));
               sunset.setText (format.format(sunsetFind));

               JSONObject object9 = jsonObject.getJSONObject ("main");
               pressure_find.setPressure_find (object9.getString ("pressure"));
               pressure.setText (pressure_find.getPressure_find () + "  hPa");

               JSONObject object10 = jsonObject.getJSONObject ("wind");
               windSpeed_find.setWindSpeed_find (object10.getString ("speed"));
               wind_speed.setText (windSpeed_find.getWindSpeed_find () + "  km/h");

               date.setDate (jsonObject.getString ("dt"));
               date2.setDate2 (parseLong (date.getDate ())*1000);
               updateTime (text);

           } catch (JSONException  jsonException) {
               jsonException.printStackTrace ();
           }
       }
       Runnable updater;
        @SuppressLint("SetTextI18n")
        void updateTime(TextView dateTime2) {
            final Handler timerHandler = new Handler();
            updater = () -> {
               Calendar calender = Calendar.getInstance();
               int day=calender.get(Calendar.DATE);
               int month=calender.get(Calendar.MONTH)+1;
               int year=calender.get(Calendar.YEAR);
               int hour=calender.get (Calendar.HOUR_OF_DAY);
               int min=calender.get (Calendar.MINUTE);
               int sec=calender.get(Calendar.SECOND);
               dateTime2.setText(day+"-"+month+"-"+year+getString(R.string.newLine)+hour+":"+min+":"+sec);
               timerHandler.postDelayed(updater,1000);
            };
            timerHandler.post(updater);
        }
    }
}

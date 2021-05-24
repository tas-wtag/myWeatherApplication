package com.weatherupdate.glare.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.weatherupdate.glare.utilities.ConstantData;
import com.weatherupdate.glare.R;
import com.weatherupdate.glare.models.MyWeatherData;
import com.weatherupdate.glare.utilities.SharedPrefManager;

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
    String latitudeOfSearchActivity;
    String longitudeOfSearchactivity;
    String latitudeOfCurrentLocation;
    String longitudeOfCurrentLocation;
    MyWeatherData weatherData = new MyWeatherData ();

    private String timePause;
    public long dateInPause;
    public long dateInResume;
    public long dateInPause2;
    public String city_name;

    ImageView imageView;
    TextView country, city, temp, dateTime;
    TextView latitude;
    TextView longitude;
    TextView pressure;
    TextView humidity;
    TextView sunrise;
    TextView sunset;
    TextView wind_speed;

    SharedPrefManager sharedPrefManager=new SharedPrefManager (this);
    SharedPrefManager sharedPrefManager2=new SharedPrefManager (this);
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
                Intent intent2 = new Intent (MainActivity.this, UpcomingWeatherUpdatesActivity.class);
                intent2.putExtra ("Latitude", latitudeOfCurrentLocation);
                intent2.putExtra ("Longitude", longitudeOfCurrentLocation);
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
        imageView = findViewById (R.id.image);
        latitude = findViewById (R.id.latitude3);
        longitude = findViewById (R.id.longitude3);
        humidity = findViewById (R.id.Humidity3);
        sunrise = findViewById (R.id.Sunrise);
        sunset = findViewById (R.id.Sunset);
        pressure = findViewById (R.id.pressure3);
        wind_speed = findViewById (R.id.windSpeed3);
        locationManager = (LocationManager) getApplicationContext ( ).getSystemService (LOCATION_SERVICE);
    }

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
                                    ConstantData.MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create ( )
                        .show ( );
            } else {
                ActivityCompat.requestPermissions (this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ConstantData.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String @NotNull [] permissions, int @NotNull [] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == ConstantData.MY_PERMISSIONS_REQUEST_LOCATION) {
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
        weatherData.setLatitudeCurrentLocation ((location.getLatitude ( )));
        weatherData.setLongitudeCurrentLocation ((location.getLongitude ( )));
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
        String mySp = sharedPrefManager.getSearchActivity ("MySP");
        if(sharedPrefManager.getSearchActivity ("MySP")!=null)
        {
            latitudeOfSearchActivity =sharedPrefManager2.getSearchActivity ("latitude3");
            longitudeOfSearchactivity =sharedPrefManager2.getSearchActivity ("longitude3");
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
        dateInPause=calendar.getTimeInMillis ();
        timePause=Long.toString (dateInPause);

        sharedPrefManager.setPauseTime ("timePause", timePause);
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
        dateInResume=calendar2.getTimeInMillis ();
        SharedPrefManager.getPauseTime ("MySharedPref", timePause) ;
        if(SharedPrefManager.getPauseTime ("MySharedPref", timePause) !=null) {
            String timePause2 = SharedPrefManager.getPauseTime ("timePause", timePause);
            if(timePause2!=null) {
                dateInPause2=parseLong (timePause2);
                long d = dateInResume- dateInPause2;
                if (d > 300000) {
                    Intent intent = new Intent (MainActivity.this, MainActivity.class);
                    finish ( );
                    startActivity (intent);
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateUI (MyWeatherData wd){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format= new SimpleDateFormat ("hh:mma");
        int findTimeZoneInt=Integer.parseInt (weatherData.getFindTimeZone ());

        country.setText (weatherData.getCountry ());
        city.setText (city_name);


        Picasso.get ().load (ConstantData.IMG_URL + weatherData.getImg () + ".png").into (imageView);

        latitude.setText (weatherData.getLatitude () + "°  N ");
        longitude.setText (weatherData.getLongitude () + "°  E ");
        humidity.setText (weatherData.getHumidity () + " %");


        int findSunriseInt=Integer.parseInt (weatherData.getSunrise ());
        int sunriseToShowInt=findSunriseInt+findTimeZoneInt;
        String sunriseToShow=Integer.toString(sunriseToShowInt);
        long sunriseLong= Long.parseLong (sunriseToShow) * 1000;
        Date sunriseFind = new Date(sunriseLong);
        format.setTimeZone (TimeZone.getTimeZone ("GMT"));
        sunrise.setText (format.format(sunriseFind));


        int findSunsetInt=Integer.parseInt (weatherData.getSunset ());
        int sunsetToShowInt=findSunsetInt+findTimeZoneInt;
        temp.setText (weatherData.getTemprature () + " °C ");
        String sunsetToShow=Integer.toString(sunsetToShowInt);
        long sunsetLong = Long.parseLong (sunsetToShow) * 1000;
        Date sunsetFind = new Date(sunsetLong);
        format.setTimeZone (TimeZone.getTimeZone ("GMT"));
        sunset.setText (format.format(sunsetFind));

        pressure.setText (weatherData.getPressure () + "  hPa");
        wind_speed.setText (weatherData.getWindSpeed () + "  km/h");
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
                latitudeOfCurrentLocation =(extras.getString ("latitude3"));
                longitudeOfCurrentLocation =(extras.getString ("longitude3"));
            }
            else if (!latitudeOfSearchActivity.equals ("")) {
                if (!longitudeOfSearchactivity.equals ("")) {
                    latitudeOfCurrentLocation = (latitudeOfSearchActivity);
                    longitudeOfCurrentLocation =(longitudeOfSearchactivity);
                } else {
                    latitudeOfCurrentLocation =(String.valueOf (weatherData.getLatitudeCurrentLocation ()));
                    longitudeOfCurrentLocation =(String.valueOf (weatherData.getLongitudeCurrentLocation ()));
                }
            } else {
                latitudeOfCurrentLocation =(String.valueOf (weatherData.getLatitudeCurrentLocation ()));
                longitudeOfCurrentLocation = (String.valueOf (weatherData.getLongitudeCurrentLocation ()));
            }
            String inputLine;
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL ("https://api.openweathermap.org/data/2.5/weather?units=metric&lat="+ latitudeOfCurrentLocation +"&lon="+ longitudeOfCurrentLocation +"&appid=1ccb72c16c65d0f4afbfbb0c64313fbf");
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
                weatherData.setFindTimeZone (jsonObject.getString ("timezone"));

                JSONObject object1 = jsonObject.getJSONObject ("sys");
                weatherData.setCountry (object1.getString ("country"));
                city_name = jsonObject.getString ("name");

                JSONObject object2 = jsonObject.getJSONObject ("main");
                weatherData.setTemprature (object2.getString ("temp"));

                JSONArray jsonArray = jsonObject.getJSONArray ("weather");
                JSONObject weather = jsonArray.getJSONObject (0);
                weatherData.setImg (weather.getString ("icon"));

                JSONObject object4 = jsonObject.getJSONObject ("coord");
                weatherData.setLatitude (object4.getDouble ("lat"));

                JSONObject object5 = jsonObject.getJSONObject ("coord");
                weatherData.setLongitude ( object5.getDouble ("lon"));

                JSONObject object6 = jsonObject.getJSONObject ("main");
                weatherData.setHumidity (object6.getInt ("humidity"));

                JSONObject object7 = jsonObject.getJSONObject ("sys");
                weatherData.setSunrise (object7.getString ("sunrise"));

                JSONObject object8 = jsonObject.getJSONObject ("sys");
                weatherData.setSunset (object8.getString ("sunset"));

                JSONObject object9 = jsonObject.getJSONObject ("main");
                weatherData.setPressure (object9.getString ("pressure"));


                JSONObject object10 = jsonObject.getJSONObject ("wind");
                weatherData.setWindSpeed (object10.getString ("speed"));

                updateTime (text);

                updateUI (weatherData);

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

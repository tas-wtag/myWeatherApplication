package com.weatherupdate.glare.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.weatherupdate.glare.models.MyWeatherData
import android.widget.TextView
import com.weatherupdate.glare.utilities.SharedPrefManager
import android.location.LocationManager
import android.view.MenuInflater
import com.weatherupdate.glare.R
import android.annotation.SuppressLint
import android.content.Intent
import com.weatherupdate.glare.activities.SearchActivity
import com.weatherupdate.glare.activities.UpcomingWeatherUpdatesActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.content.DialogInterface
import android.location.Location
import android.location.LocationListener
import com.weatherupdate.glare.utilities.OnlyConstants
import com.weatherupdate.glare.activities.MainActivity.FetchData
import com.weatherupdate.glare.activities.MainActivity
import com.squareup.picasso.Picasso
import android.os.AsyncTask
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {
    var latitudeOfSearchActivity: String? = null
    var longitudeOfSearchactivity: String? = null
    var latitudeOfCurrentLocation: String? = null
    var longitudeOfCurrentLocation: String? = null
    var weatherData = MyWeatherData()
    private var timePause: String? = null
    var dateInPause: Long = 0
    var dateInResume: Long = 0
    var dateInPause2: Long = 0
    var city_name: String? = null
    var imageView: ImageView? = null
    var country: TextView? = null
    var city: TextView? = null
    var temp: TextView? = null
    var dateTime: TextView? = null
    var latitude: TextView? = null
    var longitude: TextView? = null
    var pressure: TextView? = null
    var humidity: TextView? = null
    var sunrise: TextView? = null
    var sunset: TextView? = null
    var wind_speed: TextView? = null
    var sharedPrefManager = SharedPrefManager(this)
    var sharedPrefManager2 = SharedPrefManager(this)
    protected var locationManager: LocationManager? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_upcoming -> {
                val intent2 = Intent(this@MainActivity, UpcomingWeatherUpdatesActivity::class.java)
                intent2.putExtra("Latitude", latitudeOfCurrentLocation)
                intent2.putExtra("Longitude", longitudeOfCurrentLocation)
                startActivity(intent2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLocationPermission()
        country = findViewById(R.id.cityName)
        city = findViewById(R.id.city)
        temp = findViewById(R.id.temperature)
        dateTime = findViewById(R.id.dateTime2)
        imageView = findViewById(R.id.image)
        latitude = findViewById(R.id.latitude3)
        longitude = findViewById(R.id.longitude3)
        humidity = findViewById(R.id.Humidity3)
        sunrise = findViewById(R.id.Sunrise)
        sunset = findViewById(R.id.Sunset)
        pressure = findViewById(R.id.pressure3)
        wind_speed = findViewById(R.id.windSpeed3)
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok) { dialogInterface: DialogInterface?, i: Int ->
                        ActivityCompat.requestPermissions(
                            this@MainActivity, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ),
                            OnlyConstants.MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    OnlyConstants.MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == OnlyConstants.MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        400,
                        1f,
                        this
                    )
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        weatherData.latitudeCurrentLocation = location.latitude
        weatherData.longitudeCurrentLocation = location.longitude
        val process = FetchData(dateTime)
        process.execute()
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onResume() {
        super.onResume()
        if (SharedPrefManager.getSearchActivity("MySP") != null) {
            latitudeOfSearchActivity = SharedPrefManager.getSearchActivity("latitude3")
            longitudeOfSearchactivity = SharedPrefManager.getSearchActivity("longitude3")
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1f, this)
    }

    override fun onPause() {
        super.onPause()
        val calendar = Calendar.getInstance()
        dateInPause = calendar.timeInMillis
        timePause = java.lang.Long.toString(dateInPause)
        SharedPrefManager.setPauseTime("timePause", timePause)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        locationManager!!.removeUpdates(this)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        val calendar2 = Calendar.getInstance()
        dateInResume = calendar2.timeInMillis
        SharedPrefManager.getPauseTime("MySharedPref", timePause)
        if (SharedPrefManager.getPauseTime("MySharedPref", timePause) != null) {
            val timePause2 = SharedPrefManager.getPauseTime("timePause", timePause)
            if (timePause2 != null) {
                dateInPause2 = timePause2.toLong()
                val d = dateInResume - dateInPause2
                if (d > 300000) {
                    val intent = Intent(this@MainActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateUI(wd: MyWeatherData) {
        @SuppressLint("SimpleDateFormat") val format = SimpleDateFormat("hh:mma")
        val findTimeZoneInt = weatherData.findTimeZone.toInt()
        country!!.text = weatherData.country
        city!!.text = city_name
        Picasso.get().load(OnlyConstants.IMG_URL + weatherData.img + ".png").into(imageView)
        latitude!!.text = wd.latitude.toString() + "°  N "
        longitude!!.text = wd.longitude.toString() + "°  E "
        humidity!!.text = wd.humidity.toString() + " %"
        val findSunriseInt = wd.sunrise.toInt()
        val sunriseToShowInt = findSunriseInt + findTimeZoneInt
        val sunriseToShow = Integer.toString(sunriseToShowInt)
        val sunriseLong = sunriseToShow.toLong() * 1000
        val sunriseFind = Date(sunriseLong)
        format.timeZone = TimeZone.getTimeZone("GMT")
        sunrise!!.text = format.format(sunriseFind)
        val findSunsetInt = wd.sunset.toInt()
        val sunsetToShowInt = findSunsetInt + findTimeZoneInt
        temp!!.text = wd.temperature + " °C "
        val sunsetToShow = Integer.toString(sunsetToShowInt)
        val sunsetLong = sunsetToShow.toLong() * 1000
        val sunsetFind = Date(sunsetLong)
        format.timeZone = TimeZone.getTimeZone("GMT")
        sunset!!.text = format.format(sunsetFind)
        pressure!!.text = wd.pressure + "  hPa"
        wind_speed!!.text = wd.windSpeed + "  km/h"
    }

   inner class FetchData(var date_time: TextView?) : AsyncTask<String?, Void?, String>() {
       override fun doInBackground(vararg params: String?): String {
           val extras = intent.extras
           if (extras != null) {
               latitudeOfCurrentLocation = extras.getString("latitude3")
               longitudeOfCurrentLocation = extras.getString("longitude3")
           } else if (latitudeOfSearchActivity != "") {
               if (longitudeOfSearchactivity != "") {
                   latitudeOfCurrentLocation = latitudeOfSearchActivity
                   longitudeOfCurrentLocation = longitudeOfSearchactivity
               } else {
                   latitudeOfCurrentLocation = weatherData.latitudeCurrentLocation.toString()
                   longitudeOfCurrentLocation = weatherData.longitudeCurrentLocation.toString()
               }
           } else {
               latitudeOfCurrentLocation = weatherData.latitudeCurrentLocation.toString()
               longitudeOfCurrentLocation = weatherData.longitudeCurrentLocation.toString()
           }
           var inputLine: String?
           val result = StringBuilder()
           try {
               val url =
                   URL("https://api.openweathermap.org/data/2.5/weather?units=metric&lat=$latitudeOfCurrentLocation&lon=$longitudeOfCurrentLocation&appid=1ccb72c16c65d0f4afbfbb0c64313fbf")
               val httpURLConnection = url.openConnection() as HttpURLConnection
               httpURLConnection.requestMethod = "GET"
               httpURLConnection.doOutput = false
               val inputStream = httpURLConnection.inputStream
               val bufferedReader = BufferedReader(InputStreamReader(inputStream))
               while (bufferedReader.readLine().also { inputLine = it } != null) {
                   result.append(inputLine)
               }
           } catch (e: ProtocolException) {
               e.printStackTrace()
           } catch (e: MalformedURLException) {
               e.printStackTrace()
           } catch (e: IOException) {
               e.printStackTrace()
           }
           return result.toString()
       }


        @SuppressLint("SetTextI18n")
        override fun onPostExecute(aVoid: String) {
            try {
                val jsonObject = JSONObject(aVoid)
                weatherData.findTimeZone = jsonObject.getString("timezone")
                val object1 = jsonObject.getJSONObject("sys")
                weatherData.country = object1.getString("country")
                city_name = jsonObject.getString("name")
                val object2 = jsonObject.getJSONObject("main")
                weatherData.temperature = object2.getString("temp")
                val jsonArray = jsonObject.getJSONArray("weather")
                val weather = jsonArray.getJSONObject(0)
                weatherData.img = weather.getString("icon")
                val object4 = jsonObject.getJSONObject("coord")
                weatherData.latitude = object4.getDouble("lat")
                val object5 = jsonObject.getJSONObject("coord")
                weatherData.longitude = object5.getDouble("lon")
                val object6 = jsonObject.getJSONObject("main")
                weatherData.humidity = object6.getInt("humidity")
                val object7 = jsonObject.getJSONObject("sys")
                weatherData.sunrise = object7.getString("sunrise")
                val object8 = jsonObject.getJSONObject("sys")
                weatherData.sunset = object8.getString("sunset")
                val object9 = jsonObject.getJSONObject("main")
                weatherData.pressure = object9.getString("pressure")
                val object10 = jsonObject.getJSONObject("wind")
                weatherData.windSpeed = object10.getString("speed")
                updateTime(date_time)
                updateUI(weatherData)
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        }

        var updater: Runnable? = null
        @SuppressLint("SetTextI18n")
        fun updateTime(dateTime2: TextView?) {
            val timerHandler = Handler()
            updater = Runnable {
                val calender = Calendar.getInstance()
                val day = calender[Calendar.DATE]
                val month = calender[Calendar.MONTH] + 1
                val year = calender[Calendar.YEAR]
                val hour = calender[Calendar.HOUR_OF_DAY]
                val min = calender[Calendar.MINUTE]
                val sec = calender[Calendar.SECOND]
                dateTime2!!.text =
                    day.toString() + "-" + month + "-" + year + getString(R.string.newLine) + hour + ":" + min + ":" + sec
                timerHandler.postDelayed(updater!!, 1000)
            }
            timerHandler.post(updater!!)
        }
    }
}
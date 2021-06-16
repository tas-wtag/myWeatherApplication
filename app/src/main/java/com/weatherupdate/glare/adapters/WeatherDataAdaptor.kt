package com.weatherupdate.glare.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.weatherupdate.glare.R
import com.weatherupdate.glare.models.UpcomingWeatherData
import java.util.*

class WeatherDataAdaptor(
    context: Context,
    weatherdata: ArrayList<UpcomingWeatherData>
) : ArrayAdapter<UpcomingWeatherData?>(
    context, R.layout.row_layout, weatherdata as List<UpcomingWeatherData?>
) {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflater = LayoutInflater.from(getContext())
        convertView = inflater.inflate(R.layout.row_layout, null, true)
        val temp2 = convertView.findViewById<View>(R.id.textView20) as TextView
        val date2 = convertView.findViewById<View>(R.id.textView21) as TextView
        val rain = convertView.findViewById<View>(R.id.rain) as TextView
        val imageView = convertView.findViewById<View>(R.id.imageView2) as ImageView
        val temperature = getItem(position)
        temp2.text = temperature!!.tempOfUpcomingWeather
        rain.text = temperature.upcomingWeatherSituation
        date2.text = temperature.dateInExpectedFormat
        Picasso.get().load(temperature.upcomingWeatherImage).placeholder(R.drawable.hello)
            .into(imageView)
        return convertView
    }
}
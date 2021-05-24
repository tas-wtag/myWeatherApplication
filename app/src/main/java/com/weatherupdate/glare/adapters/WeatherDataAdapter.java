package com.weatherupdate.glare.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.weatherupdate.glare.R;
import com.weatherupdate.glare.models.UpcomingWeatherData;

public class WeatherDataAdapter extends ArrayAdapter<UpcomingWeatherData> {

    ArrayList<UpcomingWeatherData> weatherdata;
    public WeatherDataAdapter(@NonNull Context context, ArrayList<UpcomingWeatherData> weatherdata) {
        super(context, R.layout.row_layout, weatherdata);
        this.weatherdata= weatherdata;
    }
    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from (getContext ());
        convertView= inflater.inflate(R.layout.row_layout, null, true);
        TextView temp2 = (TextView) convertView.findViewById(R.id.textView20);
        TextView date2 = (TextView) convertView.findViewById(R.id.textView21);
        TextView rain = (TextView) convertView.findViewById(R.id.rain);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);

        UpcomingWeatherData temperature=getItem (position);
        temp2.setText (temperature.getTempOfUpcomingWeather ());
        rain.setText (temperature.getUpcomingWeatherSituation ());
        date2.setText (temperature.getDateInExpectedFormat ());
        Picasso.get().load (temperature.getUpcomingWeatherImage ()).placeholder(R.drawable.hello).into (imageView);

        return convertView;
    }

}
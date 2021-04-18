package com.weatherupdate.glare;

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
import java.util.HashMap;

public class Customlist extends ArrayAdapter<Weatherdata> {
    private Context context;
     ArrayList<Weatherdata> weatherdata;
    public Customlist(@NonNull Context context, ArrayList<Weatherdata> weatherdata) {
        //super (context, resource);
        super(context, R.layout.row_layout, weatherdata);
        this.context = context;
        this.weatherdata= weatherdata;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from (getContext ());
        convertView= inflater.inflate(R.layout.row_layout, null, true);
        TextView temp2 = (TextView) convertView.findViewById(R.id.textView20);
        TextView date2 = (TextView) convertView.findViewById(R.id.textView21);
        TextView rain = (TextView) convertView.findViewById(R.id.rain);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView2);


        Weatherdata temperature=getItem (position);
        temp2.setText (temperature.getTemp2 ());
        rain.setText (temperature.getRain2 ());
        date2.setText (temperature.getVv ());
        Picasso.get ().load (temperature.getImg3 ()).into (imageView);

        // Contact
      /*  HashMap<String, String> weather = weatherdata.get(position);

        String temp3 = weather.get ("day");
        String date3 = weather.get("vv");
        String rain3 = weather.get("main");
        String img = weather.get(Picasso.get ().load ("img"));
        weatherdata.add (weather);*/

        // Do something with value id, name and imageurl

        return convertView;
    }

}

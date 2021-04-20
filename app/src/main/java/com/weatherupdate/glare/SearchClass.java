package com.weatherupdate.glare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;


public class SearchClass extends AppCompatActivity {
    private static final String MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoidGFzZmlhc2V1dGkiLCJhIjoiY2tubzd1b3U5MTVjMzJvbW9ybm5laGU4bSJ9.3XIBkPnAK9juMzlx-Rar9A";
    PlaceOptions placeOptions;
    private static final  int REQUEST_CODE_AUTOCOMPLETE=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_third);


        Mapbox.getInstance(this, getString(R.string.MAPBOX_ACCESS_TOKEN));
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(MAPBOX_ACCESS_TOKEN)
                .placeOptions(placeOptions)
                .build(this);
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            Toast.makeText(this, feature.text(), Toast.LENGTH_LONG).show();
        }
    }
}

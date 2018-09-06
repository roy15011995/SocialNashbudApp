package com.estar.nashbud.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.estar.nashbud.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Google_Search_places extends AppCompatActivity {
    String Place,Address;
    SharedPreferences sharedPreferences,sharedPreferences1;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google__search_places);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e("Place", "Place: " + place.getName());
                Place=place.getName().toString();
                Address=place.getAddress().toString();
                /*Toast.makeText(getApplicationContext(),"Place Name :"+Place+"\n"+"Place Address :"+Address,Toast.LENGTH_SHORT).show();
                */

                sharedPreferences=getSharedPreferences("PREF_KEY_LOCATION",context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Address",Address);
                editor.putString("Place",Place);
                editor.commit();
                editor.clear();

                Log.e("SharePreferenceClear",""+sharedPreferences);

               /* sharedPreferences1=getSharedPreferences("PREF_KEY_LOCATION_POST",context.MODE_PRIVATE);
                SharedPreferences.Editor editor1=sharedPreferences.edit();
                editor1.putString("Address",Address);
                editor1.putString("Place",Place);
                editor1.commit();*/

                Intent intent=new Intent(Google_Search_places.this,Post.class);
                intent.putExtra("Place",Place);
                intent.putExtra("Address",Address);
                startActivity(intent);
                finish();

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e("Error_Place", "An error occurred: " + status);
            }
        });
    }
}

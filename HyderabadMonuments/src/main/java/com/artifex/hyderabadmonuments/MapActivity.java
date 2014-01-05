package com.artifex.hyderabadmonuments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle args = getIntent().getExtras();
        final Monument monument = (Monument) args.getParcelable("monument");

        Log.d("MapActivity", monument.getLatitude() + " " + monument.getLongitude());
        //ADD THE MARKER IN MAP
        GoogleMap mMap;
        MapFragment map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mMap = map.getMap();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
//                View customView = new View(getBaseContext());
                ImageView imageView = new ImageView(getBaseContext());
                imageView.setImageResource(monument.getDrawable());
//                TextView textView = new TextView(getBaseContext());
//                textView.setText(monument.getName());
                return imageView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
        mMap.addMarker(new MarkerOptions().position(
                new LatLng(monument.getLatitude(), monument.getLongitude())).title(monument.getName())
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapsmarker)));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(monument.getLatitude(), monument.getLongitude()),12);
        mMap.animateCamera(update);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}

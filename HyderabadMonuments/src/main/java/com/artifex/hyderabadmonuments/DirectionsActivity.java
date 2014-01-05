package com.artifex.hyderabadmonuments;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Vector;

public class DirectionsActivity extends Activity {

    GoogleMap map;
    static TextView locationBox;

        private class MyGeoPoint {
        String location;
        double latitude;
        double longitude;

        public MyGeoPoint(String loc, double lat, double longi) {
            this.location = loc;
            this.latitude = lat;
            this.longitude = longi;
        }

        public LatLng getLatLng() {
            return new LatLng(latitude, longitude);
        }
    }
    Vector<MyGeoPoint> myGeoPoints = new Vector<MyGeoPoint>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, Fragment.instantiate(this, PlaceholderFragment.class.getName(),bundle) )
                    .commit();
        }
        //addLatLong();
//        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//        map.setMyLocationEnabled(true);


//        map.addMarker(new MarkerOptions().position(myGeoPoints.get(0).getLatLng()).title("Hyderabad"));

    }



    private void addLatLong() {
        myGeoPoints.add(new MyGeoPoint("Hyderabad", 17.38504, 78.48667));
        myGeoPoints.add(new MyGeoPoint("Srinagar", 34.08366, 74.79737));
        myGeoPoints.add(new MyGeoPoint("Delhi", 28.63531, 77.22496));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        Monument monument;
        LocationClient client;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_directions, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            locationBox = (TextView) view.findViewById(R.id.latlongBox);
            monument = getArguments().getParcelable("monument");
            getLocation();
        }

        public void getLocation(){

            client = new LocationClient(getActivity(),
                    new GooglePlayServicesClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Location location = updateMap();
                            if(location != null){
                            String queryString = "http://maps.googleapis.com/maps/api/directions/json?"
                                    +"origin="+roundThreeDigits(location.getLatitude())+","+roundThreeDigits(location.getLongitude())
                                    +"&destination="+roundThreeDigits((monument.getLatitude()))+","+roundThreeDigits(monument.getLongitude())
                                    +"&sensor=false&units=metric&mode=driving";

                            new PlacesTask().execute(queryString);
                                //CALL ANOTHER METHOD TO CONVERT DATA AND SHOW MAPS
                            }
                        }
                        @Override
                        public void onDisconnected() {

                        }
                    }, new GooglePlayServicesClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {}
            });
            client.connect();
        }

        private double roundThreeDigits(double number) {
            DecimalFormat twoDForm = new DecimalFormat("#.###");
            return Double.valueOf(twoDForm.format(number));
        }


        private class PlacesTask extends AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Getting data, please wait..");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpContext localContext = new BasicHttpContext();
                    HttpPost httpPost = new HttpPost(strings[0]);
                    HttpResponse response = httpClient.execute(httpPost, localContext);

                    BufferedReader in = new BufferedReader(new InputStreamReader(response
                            .getEntity().getContent()));

                    //SB to make a string out of the inputstream
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    String NL = System.getProperty("line.separator");
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    in.close();

                    //the json string is stored here
                    String  result = sb.toString();
                    return result;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                locationBox.setText(s);
                Log.d("TAG", s);
            }
        }

        public Location updateMap(){
            Location loc = null;
            Log.d("TAG", client.getLastLocation() + "");
            boolean isLocationAvailable = checkLocationAvailability();

            if(isLocationAvailable){
                loc = client.getLastLocation();
                if(loc != null)
                {
                    locationBox.setText(loc.getLatitude()+","+loc.getLongitude());
                }
                else
                {
                    locationBox.setText("Location data unavailable");
                }
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Location access not enabled");
                builder.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub
                        Intent myIntent = new Intent(Settings.ACTION_SETTINGS );
                        startActivity(myIntent);
                        //get gps
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                builder.create().show();
            }
            return loc;
        }

        public boolean checkLocationAvailability(){

            LocationManager lm = null;

            boolean gps_enabled = false,
                    network_enabled = false;
            if(lm==null)
                lm = (LocationManager) getActivity().getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            try{
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            catch(Exception ex){}

            try{
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
            catch(Exception ex){}

            if(!gps_enabled && !network_enabled){
                return false;
            }
            else
                return true;
        }
    }

}

package com.artifex.hyderabadmonuments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;

public class DirectionsActivity extends Activity {

    Context context;
    Monument monument;
    LocationClient client;
    LatLng currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);
        Bundle bundle = getIntent().getExtras();
        context = getApplicationContext();
        monument = bundle.getParcelable("monument");
        getLocation();
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


        public void getLocation(){

            client = new LocationClient(DirectionsActivity.this,
                    new GooglePlayServicesClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                                Location location = updateMap();
                                if(location != null){
                                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                String queryString = "http://maps.googleapis.com/maps/api/directions/json?"
                                    +"origin="+roundThreeDigits(location.getLatitude())+","+roundThreeDigits(location.getLongitude())
                                    +"&destination="+roundThreeDigits((monument.getLatitude()))+","+roundThreeDigits(monument.getLongitude())
                                    +"&sensor=false&units=metric&mode=driving";

                                new PlacesTask().execute(queryString);

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

    public Location updateMap(){
        Location loc = null;
        Log.d("TAG", client.getLastLocation() + "");
        boolean isLocationAvailable = checkLocationAvailability();

        if(isLocationAvailable){
            loc = client.getLastLocation();
            if(loc == null)
            {
                //SHOW A POPUP THAT SAYS NO LOCATION DATA AVAILABLE
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(DirectionsActivity.this);
                errorDialog.setMessage("Location data unavailable");
                errorDialog.create().show();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(DirectionsActivity.this);
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
            lm = (LocationManager) DirectionsActivity.this.getBaseContext().getSystemService(Context.LOCATION_SERVICE);
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

        private double roundThreeDigits(double number) {
            DecimalFormat twoDForm = new DecimalFormat("#.###");
            return Double.valueOf(twoDForm.format(number));
        }


        private class PlacesTask extends AsyncTask<String, Void, String>{

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DirectionsActivity.this);
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
                new JsonConversionTask().execute(s);
            }
        }

        private class JsonConversionTask extends AsyncTask<String, Void, GoogleDirections>{
            GoogleDirections directions;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected GoogleDirections doInBackground(String... strings) {
                String jsonData = strings[0];
                try {
                    directions = (GoogleDirections) DirectionsMapper.fromJson(jsonData, GoogleDirections.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return directions;
            }

            @Override
            protected void onPostExecute(GoogleDirections s) {
                super.onPostExecute(s);
                ShowDirections(s);
            }
        }

        private void ShowDirections(GoogleDirections directions) {
            GoogleMap mMap;
            MapFragment map = ((MapFragment) getFragmentManager().findFragmentById(R.id.directionsMap));
            mMap = map.getMap();

            List<LatLng> directionPoint = directions.getRoutes();

            PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
             for (int i = 0; i < directionPoint.size(); i++){
                rectLine.add(directionPoint.get(i));
            }
            Polyline polyLine = mMap.addPolyline(rectLine);
            LatLng source = new LatLng(monument.getLatitude(), monument.getLongitude());
            LatLng destination = currentLocation;
        }


}

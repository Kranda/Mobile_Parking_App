package abayomi.code.parking;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LatLngBounds pv;
    ArrayList<Parking> parkings;
    static String[] jsonresult;
    private static final String Maps_URL = "http://10.124.4.141/androidtest/maps.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parkings = new ArrayList<Parking>();
        JSONTask parseData = new JSONTask();
        parseData.execute();
        setUpMap();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng current = new LatLng(30.093316, -95.991004);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((current), 11.0f));
        Marker currentMarker = mMap.addMarker(new MarkerOptions().position((current)).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        currentMarker.showInfoWindow();

        //mMap.addMarker(new MarkerOptions().position(current).title("Current Location").alpha(3.8f));

        pv = new LatLngBounds(new LatLng(30, -100), new LatLng(32, -93));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pv.getCenter(), 10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            //to fix
            @Override
            public boolean onMarkerClick(Marker marker) {

                String markerTitle = marker.getTitle();
                int lotID = Integer.parseInt(markerTitle.substring(4));

                if (markerTitle.equals("Lot "+lotID)) {// if marker source is clicked
                    Log.d("OnClickListener", "Marker1 Clicked");
                    Parking p = parkings.get(lotID-1);
                    Intent goToSpecificPark = new Intent(getApplicationContext(), SpecificParkingActivity.class);
                    goToSpecificPark.putExtra("Object", (Serializable) p);
                    startActivity(goToSpecificPark);
                } else {
                    //do nothing
                }
                return false;


            }
        });
    }




    public GoogleMap getMap() {
        return mMap;
    }









/*
    void createMarkersFromJson(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            map.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                    .snippet(Integer.toString(jsonObj.getInt("population")))
                    .position(new LatLng(
                            jsonObj.getJSONArray("latlng").getDouble(0),
                            jsonObj.getJSONArray("latlng").getDouble(1)
                    ))
            );
        }
    }

 */


    private void setUpMap() {
        // Retrieve the city data from the web service
        // In a worker thread since it's a network operation.
        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveParking();
                } catch (IOException e) {
                    //Log.e(LOG_TAG, "Cannot retrive cities", e);
                    return;
                }
            }
        }).start();
    }


    protected void retrieveParking() throws IOException {

        //connection
        //retrieve Json
        //String js = "{'id':24,'capacity':10,'status':'full','parkingName':'Lot 1', 'lat':'30.094043', 'lng':'-96.200413'}" ;
        //retrieve parking

        String js = "{'result1': {'id':1,'capacity':7,'status':'full','parkingName':'Lot 1', 'lat':'30.094043', 'lng':'-96.200413'}," +
                "'result2': {'id':2,'capacity':10,'status':'full','parkingName':'Lot 2', 'lat':'30.138080', 'lng':'-95.788551'}," +
                "'result3': {'id':3,'capacity':13,'status':'full','parkingName':'Lot 3', 'lat':'30.155739', 'lng':'-96.067116'}," +
                "'result4': {'id':4,'capacity':9,'status':'full','parkingName':'Lot 4', 'lat':'30.056611', 'lng':'-95.948948'}," +
                "'result5': {'id':5,'capacity':8,'status':'full','parkingName':'Lot 5', 'lat':'30.093007', 'lng':'-95.892919'}}" ;
        //textResult = js;

        parkings.clear();

        Gson gson = new Gson();
        try {
            JSONObject jsonObj = new JSONObject(js);
            Iterator<String> keys = jsonObj.keys();
            int i = 0;
            while (keys.hasNext()) {
                String key = keys.next();
                Log.d("json", key);
                JSONObject jobj2 = jsonObj.getJSONObject(key);
                parkings.add(i++, gson.fromJson(jobj2.toString(), Parking.class));
            }
        } catch(Exception ee){

        }



        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson(parkings);
                } catch (JSONException e) {
                    //Log.e(LOG_TAG, "Error processing JSON", e);
                }
            }
        });

    }



    void createMarkersFromJson(ArrayList<Parking> pks) throws JSONException {
        // De-serialize the Parking objects
        //JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < pks.size(); i++) {
            // Create a marker for each city in the JSON data.
            Parking indP = pks.get(i);
            LatLng markerPos = new LatLng(Double.parseDouble(indP.lat), Double.parseDouble(indP.lng));
            mMap.addMarker(new MarkerOptions().position(markerPos).title(indP.parkingName).snippet(String.valueOf(indP.capacity)));
        }
    }





    /**
     * AssyncTask to parse JSON.
     */
    private class JSONTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            parseJSON(parkings, Maps_URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //adapter.notifyDataSetChanged();
        }

    }

        public static void parseJSON(ArrayList<Parking> parkings, String ipToParse) {
            URL textUrl;
            String textResult;
            try {
                textUrl = new URL(ipToParse);
                BufferedReader bufferReader
                        = new BufferedReader(new InputStreamReader(textUrl.openStream()));
                String StringBuffer;
                String stringText = "";
                while ((StringBuffer = bufferReader.readLine()) != null) {
                    stringText += StringBuffer;
                }
                bufferReader.close();
                textResult = stringText;
                Log.d("jsondata", textResult);
                JSONObject jsonObj = new JSONObject(textResult);
                JSONArray lots = jsonObj.getJSONArray("result");
                Log.d("jsonlength", ""+lots.length());

                jsonresult = new String[lots.length()];
                for(int i=0;i<lots.length();i++){

                    JSONObject obj2 = lots.getJSONObject(i);
                    jsonresult[i] = obj2.toString();
                    //Log.d("jsonlength",obj2.toString());
                    //parkings.add(i++, gson.fromJson(jobj2.toString(), Parking.class));
                }
            /*JSON parser*/
                //parkings.clear();
                //GsonBuilder builder = new GsonBuilder();
                //Gson gson = builder.create();
                //Parking[] murzik = gson.fromJson(textResult, Parking[].class);
                //for(Parking a : murzik)
                //    parkings.add(a);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                textResult = e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                textResult = e.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }









}






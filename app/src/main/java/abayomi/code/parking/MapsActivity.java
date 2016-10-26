package abayomi.code.parking;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LatLngBounds pv;
    MyItem mitem;
    private ClusterManager<MyItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location").alpha(3.8f));

        pv = new LatLngBounds(new LatLng(30, -100), new LatLng(32, -93));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pv.getCenter(), 10));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        LatLng Lot1 = new LatLng(30.094043, -96.200413);
        mMap.addMarker(new MarkerOptions().position(Lot1).title("Lot 1").snippet("5 Available"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Lot1));

        LatLng Lot2 = new LatLng(30.138080, -95.788551);
        mMap.addMarker(new MarkerOptions().position(Lot2).title("Lot 2").snippet("3 Available"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Lot2));

        LatLng Lot3 = new LatLng(30.155739, -96.067116);
        mMap.addMarker(new MarkerOptions().position(Lot3).title("Lot 3").snippet("7 Available"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Lot3));

        LatLng Lot4 = new LatLng(30.056611, -95.948948);
        mMap.addMarker(new MarkerOptions().position(Lot4).title("Lot 4").snippet("2 Available"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Lot4));

        LatLng Lot5 = new LatLng(30.093007, -95.892919);
        mMap.addMarker(new MarkerOptions().position(Lot5).title("Lot 5").snippet("1 Available"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Lot5));

        //setUpClusterer();



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

//to fix
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTitle().equals("Lot 1")) // if marker source is clicked
                    Log.d("OnClickListener", "Marker Clicked");
                Intent goToSpecificPark = new Intent(getApplicationContext(), SpecificParkingActivity.class);
                startActivity(goToSpecificPark);

                return false;




            }
        });
    }



    private void setUpClusterer() {
        // Declare a variable for the cluster manager.
       // private ClusterManager<MyItem> mClusterManager;

        // Position the map.
        LatLng current = new LatLng(30.093316, -95.991004);
        pv = new LatLngBounds(new LatLng(30, -100), new LatLng(30, -93));
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(pv.getCenter(), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(this, getMap());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    public GoogleMap getMap() {
        return mMap;
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = 29.094043;
        double lng = -96.000413;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            MyItem offsetItem = new MyItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }
    }
}

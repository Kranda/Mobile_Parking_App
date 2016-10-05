package abayomi.code.parking;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        LatLng Lot1 = new LatLng(30.094043, -96.000413);
        mMap.addMarker(new MarkerOptions().position(current).title("Lot 1").snippet("5 Available"));

        LatLng Lot2 = new LatLng(30.138080, -95.988551);
        mMap.addMarker(new MarkerOptions().position(current).title("Lot 2").snippet("3 Available"));

        LatLng Lot3 = new LatLng(30.155739, -96.067116);
        mMap.addMarker(new MarkerOptions().position(current).title("Lot 3").snippet("7 Available"));

        LatLng Lot4 = new LatLng(30.056611, -95.948948);
        mMap.addMarker(new MarkerOptions().position(current).title("Lot 4").snippet("2 Available"));

        LatLng Lot5 = new LatLng(30.093007, -95.992919);
        mMap.addMarker(new MarkerOptions().position(current).title("Lot 5").snippet("1 Available"));
    }
}

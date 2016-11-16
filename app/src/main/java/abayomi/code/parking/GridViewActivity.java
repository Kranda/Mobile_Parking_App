package abayomi.code.parking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {

    GridView gridViewParkings;
    ArrayList<Parking> parkings;
    ParkingListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        gridViewParkings = (GridView) findViewById(R.id.simpleGridView);

        parkings = new ArrayList<Parking>();

        //MainActivity.JSONTask parseData = new MainActivity.JSONTask();
        //parseData.execute();
        adapter = new ParkingListViewAdapter(this, parkings); //whats doing
        Log.d("Adapt", "Adapter");
        gridViewParkings.setAdapter(adapter); //whats doing

        //CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags);
        //simpleGrid.setAdapter(customAdapter);
    }
}

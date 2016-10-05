package abayomi.code.parking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import abayomi.code.parking.R;

public class MainActivity extends AppCompatActivity {
    /*Declaration*/
    ListView listViewParkings;
    ArrayList<Parking> parkings;
    ParkingListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Assigning views to objects.*/
        listViewParkings = (ListView) findViewById(R.id.listView_MainActivity_Parkings);

        parkings = new ArrayList<Parking>();

        JSONTask parseData = new JSONTask();
        parseData.execute();
        adapter = new ParkingListViewAdapter(this, parkings);
        listViewParkings.setAdapter(adapter);
    }
    /**
     * AssyncTask to parse JSON.
     */
    private class JSONTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            parseJSON(parkings, "/showParkings");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
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
            /*JSON parser*/
            parkings.clear();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Parking[] murzik = gson.fromJson(textResult, Parking[].class);
            for(Parking a : murzik)
                parkings.add(a);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            textResult = e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            textResult = e.toString();
        }

    }
}

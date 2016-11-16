package abayomi.code.parking;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

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
        adapter = new ParkingListViewAdapter(this, parkings); //whats doing
        Log.d("Adapt", "Adapter");
        listViewParkings.setAdapter(adapter); //whats doing
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
           /* textUrl = new URL(ipToParse);
            BufferedReader bufferReader
                    = new BufferedReader(new InputStreamReader(textUrl.openStream()));
            String StringBuffer;
            String stringText = "";
            while ((StringBuffer = bufferReader.readLine()) != null) {
                stringText += StringBuffer;
            }
            bufferReader.close();
            textResult = stringText;
            */
            //String js = "{'id':24,'capacity':7,'status':'full','parkingName':'Lot1', 'lat':'60', 'lng':'11'}";
            String js = "{'result1': {'id':24,'capacity':7,'status':'full','parkingName':'Lot1', 'lat':'60', 'lng':'11'},'result2': {'id':24,'capacity':10,'status':'full','parkingName':'Lot2', 'lat':'60', 'lng':'11'}}" ;
                    //textResult = js;


            //insert JSon file..
            /*

            {
                “Field” : “value”,
                “Field” : “value”,
                “Field” : “value”,
                “Field” : “value”,
            }



            String coordinates;
            int id;
            int capacity;
            String status;
            String parkingName;
             */

            /*JSON parser*/
            parkings.clear();

            Gson gson = new Gson();

            JSONObject jsonObj = new JSONObject(js);
            Iterator<String> keys = jsonObj.keys();
            int i = 0;
            while( keys.hasNext() ) {
                String key = keys.next();
                Log.d("json", key);
                JSONObject jobj2 = jsonObj.getJSONObject(key);
                parkings.add(i++, gson.fromJson(jobj2.toString(), Parking.class));
            }


        //} catch (MalformedURLException e) {
            // TODO Auto-generated catch block
        //    e.printStackTrace();
        //    textResult = e.toString();
        } catch (Exception e) {//(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            textResult = e.toString();
        }

    }
}

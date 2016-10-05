package abayomi.code.parking;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import abayomi.code.parking.R;

public class SpecificParkingActivity extends AppCompatActivity {
    /*Declaration*/
    TextView textViewName, textViewAvaliableLot, textViewTimer;
    Button buttonReserve, buttonPark, buttonAbort;
    CountDownTimer countDownTimer;
    Parking parking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_parking);
         countDownTimer = new CountDownTimer(600000, 1000) {
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("Time rem.: " + millisUntilFinished / 1000 + "sec.");
            }

            public void onFinish() {
                buttonAbort.performClick();
            }
        };
        Intent intent = getIntent();
        parking = (Parking) intent.getSerializableExtra("Object");
        /*Assigning views to objects.*/
        textViewName = (TextView) findViewById(R.id.textView_Specific_ParkingName);
        textViewAvaliableLot = (TextView) findViewById(R.id.textView_Specific_AvaliableLotNum);
        buttonReserve = (Button) findViewById(R.id.button_Specific_Reserve);
        buttonPark = (Button) findViewById(R.id.button_Specific_Park);
        buttonAbort = (Button) findViewById(R.id.button_Specific_Abort);
        textViewTimer = (TextView) findViewById(R.id.textView_Specific_Timer);

        /*Set OnClickListener on buttons.*/
        buttonReserve.setOnClickListener(buttonOnClickListener);
        buttonPark.setOnClickListener(buttonOnClickListener);
        buttonAbort.setOnClickListener(buttonOnClickListener);

        textViewName.setText("Parking: " + parking.parkingName);
        textViewAvaliableLot.setText("Avaliable lot: "+String.valueOf(parking.capacity));
    }

    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_Specific_Reserve:
                    countDownTimer.start();
                    parking.status = "Reserved";
                    SenderTask task = new SenderTask();
                    task.execute();
                    break;
                case R.id.button_Specific_Park:
                    countDownTimer.cancel();
                    parking.status = "Free";
                    textViewTimer.setText("Time remaining: 10 min" );
                    break;
                case R.id.button_Specific_Abort:
                    countDownTimer.cancel();
                    parking.status = "Free";
                    textViewTimer.setText("Time remaining: 10 min");
                    break;
            }

        }
    };

    private class SenderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("/changeState");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("parkingName", parking.parkingName )
                        .appendQueryParameter("status", parking.status );
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}

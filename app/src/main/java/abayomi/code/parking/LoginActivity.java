package abayomi.code.parking;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import abayomi.code.parking.Interfaces.*;
import abayomi.code.parking.R;

public class LoginActivity extends AppCompatActivity implements Login {
    /*Declaration*/
    Button buttonLogin, buttonSignUp;
    EditText editTextLogin, editTextPassword;
    String login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Assigning views to objects.*/
        buttonLogin = (Button) findViewById(R.id.button_LoginActivity_Loin);
        buttonSignUp = (Button) findViewById(R.id.button_LoginActivity_SignUp);
        editTextLogin = (EditText) findViewById(R.id.editText_LoginActivity_Login);
        editTextPassword = (EditText) findViewById(R.id.editText_LoginActivity_Password);

        /*Adding OnClickListener to Buttons.*/
        buttonLogin.setOnClickListener(buttonOnClickListener);
        buttonSignUp.setOnClickListener(buttonOnClickListener);
    }

    /*OnClickListener for Buttons.*/
    View.OnClickListener buttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_LoginActivity_Loin:
                    Log.d("OnClickListener", "Login button pressed");
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    if(String.valueOf(editTextLogin.getText()).equals("testuser") && String.valueOf(editTextPassword.getText()).equals("testpassword"))
                        startActivity(intent);
                    break;
                case R.id.button_LoginActivity_SignUp:
                    Log.d("OnClickListener", "Login button pressed");
                    Intent goToSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(goToSignUp);
                    break;
            }
        }
    };
    private class SenderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            login = editTextLogin.getText().toString();
            password = editTextPassword.getText().toString();


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", login)
                        .appendQueryParameter("password", password);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                Log.d("YAP", "1");
                InputStream is = conn.getInputStream();
                BufferedReader reader;
                if (conn.getResponseCode() == 200)
                    reader = new BufferedReader(new InputStreamReader(is));
                else
                    reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                StringBuilder sb = new StringBuilder();
                Log.d("YAP", "2");
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                is.close();
                Log.d("YAP", "3");


               // responceObject = new JSONObject(sb.toString());
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

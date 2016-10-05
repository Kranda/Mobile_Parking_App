package abayomi.code.parking;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

import abayomi.code.parking.R;

public class SignUpActivity extends AppCompatActivity {

    /*Declaration*/
    EditText editTextPassword1, editTextPassword2, editTextMail, editTextPhone;
    ImageView imageViewCheck;
    Button buttonSignUp;
    String name, passw1, passw2, mail, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        /*Assigning views to objects.*/
        editTextPassword1 = (EditText) findViewById(R.id.editText_SignUpActivity_Password1);
        editTextPassword2 = (EditText) findViewById(R.id.editText_SignUpActivity_Password2);
        editTextMail = (EditText) findViewById(R.id.editText_SignUpActivity_Mail);
        editTextPhone = (EditText) findViewById(R.id.editText_SignUpActivity_Phone);
        imageViewCheck = (ImageView) findViewById(R.id.imageView_SignUpActivity_ImageViewCorrect);
        buttonSignUp = (Button) findViewById(R.id.button_SignUpActivity_SignUp);

        /*Do passwords match?*/
        editTextPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                compareTextViews();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                compareTextViews();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*Adding OnClickListener to buttons*/
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SenderTask task = new SenderTask();
                task.execute();
            }
        });
    }

    void compareTextViews() {
        if (String.valueOf(editTextPassword1.getText()).equals(String.valueOf(editTextPassword2.getText())))
            imageViewCheck.setImageResource(R.drawable.greentick);
        else imageViewCheck.setImageResource(R.drawable.redcross);
    }

    private class SenderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //name = String.valueOf(editTextName.getText());
            passw1 = String.valueOf(editTextPassword1.getText());
            mail = String.valueOf(editTextMail.getText());
            phone = String.valueOf(editTextPhone.getText());

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("/signUp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("password", passw1)
                        .appendQueryParameter("email", mail)
                        .appendQueryParameter("phone", phone)
                        .appendQueryParameter("source", "tablet");
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

            Intent goBackToLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(goBackToLogin);
        }
    }
}

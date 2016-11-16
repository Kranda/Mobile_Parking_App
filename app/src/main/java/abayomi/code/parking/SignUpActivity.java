package abayomi.code.parking;

import android.app.ProgressDialog;
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
import android.widget.Toast;

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
    private static final String REGISTER_URL = "http://10.124.4.141/androidtest/register.php";
    int responsecode = 0;

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
                //SenderTask task = new SenderTask();
                //task.execute();
                registerUser();
            }
        });
    }

    void compareTextViews() {
        if (String.valueOf(editTextPassword1.getText()).equals(String.valueOf(editTextPassword2.getText())))
            imageViewCheck.setImageResource(R.drawable.greentick);
        else imageViewCheck.setImageResource(R.drawable.redcross);
    }

   /* private class SenderTask extends AsyncTask<Void, Void, Void> {
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
                URL url = new URL(REGISTER_URL);
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
    }*/


    private void registerUser() {
        //String name = "Abayomi"; //editTextName.getText().toString().trim().toLowerCase();
        String phone = String.valueOf(editTextPhone.getText()); //editTextUsername.getText().toString().trim().toLowerCase();
        String password = String.valueOf(editTextPassword1.getText());;//editTextPassword.getText().toString().trim().toLowerCase();
        String email = String.valueOf(editTextMail.getText());;//editTextEmail.getText().toString().trim().toLowerCase();

        register(email,phone,password);
    }

    private void register(String email, String phone, String password) {
        String urlSuffix = "?email="+email+"&phone="+phone+"&password="+password;
        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUpActivity.this, "Please Wait", null, true, true);

                //Intent goBackToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                //startActivity(goBackToLogin);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if (responsecode== 200 && !(s.equals("username or email already exist")||s.equals("please fill all values"))) {
                    Intent goBackToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(goBackToLogin);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();
                    responsecode = con.getResponseCode();

                    return result;
                } catch (Exception e) {
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
}
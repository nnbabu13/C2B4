package com.koiy.playercompanion;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ActivationActivity extends AppCompatActivity {
    SharedPreferences settings;

    SharedPreferences.Editor editore;

    EditText txtActivationCode, txtActivationEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        txtActivationCode = (EditText) findViewById(R.id.txtActivationCode);
        txtActivationEmail = (EditText) findViewById(R.id.txtActivationEmail);

        settings = this.getSharedPreferences("prefs", 0);


        String inputCode = settings.getString("myKey", "");
        String code = utils.getAndroidID(this);

        if (inputCode.equals(code)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {

        }


    }

    public boolean isValidEmail(String email) {
        // Email pattern regular expression
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check if the email matches the pattern
        return email.matches(emailPattern);
    }


    public void getCodeClicked(View view) {


        String email = txtActivationEmail.getText().toString();
        boolean isValid = isValidEmail(email);


        if (!isValid) {
            Toast.makeText(getApplicationContext(), "Enter Email   Address", Toast.LENGTH_SHORT).show();
        } else {

            String message = "---REQUEST ACCESS---\n"+email+"\n" + utils.getAndroidID(this);

            String url = "https://discord.com/api/webhooks/964045836109627402/Zns6KkcdW6sG8IP_vKdTIQKK7WvACP34stfkXlnp-23X6VA8DManINVcFBbMnDecggd3";
            Map<String, String> parameters = new HashMap<>();
            parameters.put("username", "Access");
            parameters.put("content", message);


            HttpRequestHelper.HttpRequestListener listener = new HttpRequestHelper.HttpRequestListener() {
                @Override
                public void onRequestComplete(byte[] response) {
                    if (response != null) {
                        String responseString = new String(response, StandardCharsets.UTF_8);
                        Log.d(TAG, "Response: " + responseString);
                        Toast.makeText(ActivationActivity.this, "Please wait the code from the developer", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            HttpRequestHelper httpRequest = new HttpRequestHelper(url, parameters, listener);
            httpRequest.execute();
        }
    }

    public void activateClicked(View view) {


        String inputCode = txtActivationCode.getText().toString();
        String code = utils.getAndroidID(this);

        if (inputCode.equals(code)) {
            editore = settings.edit();
            editore.putString("myKey", code);
            editore.apply();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Toast.makeText(this, "InvalidCode", Toast.LENGTH_SHORT).show();
        }


    }
}
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

    EditText txtActivationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        txtActivationCode = (EditText)findViewById(R.id.txtActivationCode);

        settings = this.getSharedPreferences("prefs", 0);





        String inputCode  = settings.getString("myKey","");
        String code  = utils.getAndroidID(this);

        if(inputCode.equals(code)){
            startActivity(new Intent(this,MainActivity.class));
        }else{

        }




    }

    public void getCodeClicked(View view) {

        String message = "---REQUEST ACCESS---\n" + utils.getAndroidID(this);


        String url = "https://discord.com/api/webhooks/964045836109627402/Zns6KkcdW6sG8IP_vKdTIQKK7WvACP34stfkXlnp-23X6VA8DManINVcFBbMnDecggd3";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username","Access" );
        parameters.put("content", message);



        HttpRequestHelper.HttpRequestListener listener = new HttpRequestHelper.HttpRequestListener() {
            @Override
            public void onRequestComplete(byte[] response) {
                if (response != null) {
                    String responseString = new String(response, StandardCharsets.UTF_8);
                    Log.d(TAG, "Response: " + responseString);
                }
            }
        };

        HttpRequestHelper httpRequest = new HttpRequestHelper(url, parameters, listener);
        httpRequest.execute();

    }

    public void activateClicked(View view) {

        String inputCode  = txtActivationCode.getText().toString();
        String code  = utils.getAndroidID(this);

        if(inputCode.equals(code)){
            editore = settings.edit();
            editore.putString("myKey", code);
            editore.apply();
            startActivity(new Intent(this,MainActivity.class));
        }else{
            Toast.makeText(this, "InvalidCode",Toast.LENGTH_SHORT).show();
        }








    }
}
package com.koiy.playercompanion;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getSuperSicBoResult  extends AsyncTask<Void, Void, String> {



        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.tracksino.com/sicbo_history?sort_by=&sort_desc=false&page_num=1&per_page=25&period=24hours&table_id=149")
                    .addHeader("authority", "api.tracksino.com")
                    .addHeader("accept", "application/json, text/plain, */*")
                    .addHeader("accept-language", "en-US,en;q=0.9,en-AU;q=0.8")
                    .addHeader("authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjo3MTc3LCJlbWFpbCI6ImtvaXk2MTg2NEBnbWFpbC5jb20iLCJpYXQiOjE2ODEzOTMwMTgsImV4cCI6MTY4MTk5NzgxOH0.cADLIayFwxCXe8bQlF6X9WaOGOTkBU1F2DDyidGPIfA")
                    .addHeader("origin", "https://tracksino.com")
                    .addHeader("referer", "https://tracksino.com/")
                    .addHeader("sec-ch-ua", "\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-site")
                    .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    variables.RESULT =  responseBody;
//                    Log.e("HEreHere",responseBody);
                    // Process the response body here
//                    Gson gson = new Gson();
//                    SicBoResponse sicBoResponse = gson.fromJson(responseBody, SicBoResponse.class);
                    //Log.e("HereHere",sicBoResponse.getData()[0].getWhen()+"");

                    return responseBody;
                } else {
                    // Handle non-successful response here
                    Log.e("HTTP Request", "Request failed with status code: " + response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                // Use the result here

            } else {
                // Handle the case where the result is null
            }
        }





}

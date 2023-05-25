package com.koiy.playercompanion;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHelper extends AsyncTask<Void, Void, byte[]> {

    private static final String TAG = "HttpRequestHelper";
    private String url;
    private Map<String, String> parameters;
    private HttpRequestListener listener;

    public HttpRequestHelper(String url, Map<String, String> parameters, HttpRequestListener listener) {
        this.url = url;
        this.parameters = parameters;
        this.listener = listener;
    }

    @Override
    protected byte[] doInBackground(Void... voids) {
        try {
            URL postUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Prepare the request parameters
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (postData.length() > 0) {
                    postData.append("&");
                }
                postData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()));
                postData.append("=");
                postData.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            // Set the Content-Type header
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            try (OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream())) {
                // Send the POST data
                outputStream.write(postDataBytes);
                outputStream.flush();
            }

            // Read the response from the server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                inputStream.close();
                return response.toString().getBytes(StandardCharsets.UTF_8);
            } else {
                Log.e(TAG, "POST request failed with response code: " + responseCode);
            }
            connection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "Exception while performing POST request: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] response) {
        if (listener != null) {
            listener.onRequestComplete(response);
        }
    }

    public interface HttpRequestListener {
        void onRequestComplete(byte[] response);
    }
}

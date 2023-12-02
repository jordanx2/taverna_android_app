package com.example.project.model;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class RetrieveEstablishments extends Thread{
    private String response;
    private RetrieveEstablishmentsCallback callback;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String requestString;

    public RetrieveEstablishments(String requestString, RetrieveEstablishmentsCallback callback) {
        this.callback = callback;
        this.requestString = requestString;

    }

    public void run(){
        makeRequest();
        JSONObject parsedJSON;
        try {
            parsedJSON = (JSONObject) (new JSONParser().parse(getResponse()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        handler.post(() -> callback.onResult(parsedJSON));
    }

    public void makeRequest() {
        try {
            URL url = new URL(this.requestString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            setResponse(response.toString());
            Log.d("reponseSet", "ResponseSet: " + response.toString());
            reader.close();
            stream.close();

        } catch (IOException e) {
            Log.d("reponseError", "Error: " + e.toString());
        }

    }

    public String getResponse(){
        return response;
    }

    private void setResponse(String response){
        this.response = response;
    }

}




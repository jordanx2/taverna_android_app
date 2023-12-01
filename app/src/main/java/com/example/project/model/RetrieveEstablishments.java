package com.example.project.model;
import android.os.Handler;
import android.os.Looper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class RetrieveEstablishments extends Thread{
    private String response;
    private JSONArray responseJSON;
    private final String API_KEY;
    private RetrieveEstablishmentsCallback callback;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int radius;

    public RetrieveEstablishments(String API_KEY, int radius, RetrieveEstablishmentsCallback callback) {
        this.API_KEY = API_KEY;
        this.radius = radius;
        this.callback = callback;

    }

    public void run(){
        makeRequest();
        handler.post(() -> callback.onResult(getResponseJSON()));
    }

    public void makeRequest() {
        try {
            String request = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.354440,-6.278720&radius=" + this.radius + "&type=bar&key=" + this.API_KEY;
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            setResponse(response.toString());
            setResponseJSON(parseToJSONArray(getResponse()));
            reader.close();
            stream.close();

        } catch (IOException e) {}

    }

    public JSONArray parseToJSONArray(String res) {
        JSONArray array = null;
        try {
            JSONParser parser = new JSONParser();
            JSONObject parsedJSON = (JSONObject) parser.parse(res);
            array = (JSONArray) parsedJSON.get("results");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return array;
    }

    public JSONArray getResponseJSON(){
        return this.responseJSON;
    }

    public void setResponseJSON(JSONArray responseJSON){
        this.responseJSON = responseJSON;
    }

    public String getResponse(){
        return response;
    }

    private void setResponse(String response){
        this.response = response;
    }

}




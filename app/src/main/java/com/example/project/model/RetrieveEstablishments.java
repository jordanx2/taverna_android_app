package com.example.project.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Customer thread class for making HTTP requests
public class RetrieveEstablishments extends Thread{
    // String representation for HTTP response
    private String response;

    // Callback interface for handling result of HTTP request
    private RetrieveEstablishmentsCallback callback;

    // Handler to post results back to main thread
    private Handler handler = new Handler(Looper.getMainLooper());

    // Represents URL for HTTP request
    private String requestString;

    // Constructor with two parameters
    public RetrieveEstablishments(String requestString, RetrieveEstablishmentsCallback callback) {
        this.callback = callback;
        this.requestString = requestString;

    }

    /*
        - Entry point for thread
        - Where the HTTP request is made
        - Response is also processed
     */
    public void run(){
        // Perform HTTP request
        makeRequest();
        JSONObject parsedJSON;

        // Parse the received JSON response
        try {
            parsedJSON = (JSONObject) (new JSONParser().parse(getResponse()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Post the parsed JSON back to the main thread using callback
        handler.post(() -> callback.onResult(parsedJSON));
    }

    // Method to make an HTTP request and store response
    public void makeRequest() {
        try {
            // Create the URL object from the request string
            URL url = new URL(this.requestString);

            // Open the connection to the URL and obtain InputStream from the response
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream stream = conn.getInputStream();

            // Read InputStream and build response
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Set the response string
            setResponse(response.toString());

            // Close the reader and the stream
            reader.close();
            stream.close();

        } catch (IOException e) {
            Log.d("responseError", "Error: " + e.toString());
        }

    }

    // Getter and Setter for response field
    public String getResponse(){
        return response;
    }

    private void setResponse(String response){
        this.response = response;
    }

}




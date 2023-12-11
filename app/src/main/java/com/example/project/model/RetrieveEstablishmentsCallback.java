package com.example.project.model;

// Callback interface for handling the result of the RetrieveEstablishments thread
public interface RetrieveEstablishmentsCallback {
    // Callback method invoked when the result of HTTP request is available
    void onResult(org.json.simple.JSONObject response);
}

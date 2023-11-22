package com.example.project;

import org.json.JSONArray;
import org.json.simple.JSONObject;

public interface RetrieveEstablishmentsCallback {
    void onResult(org.json.simple.JSONArray response);
}

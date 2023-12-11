package com.example.project.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Class for reading images from a URL and setting them to an ImageView
public class ReadImage {

    // Method to read image from a given URL, return as Bitmap
    public Bitmap readImage(String address){
        Bitmap bitmapImage = null;
        try{
            // Open InputStream from the URL and decode into Bitmap
            InputStream inputStream = new URL(address).openStream();
            bitmapImage = BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
            e.printStackTrace();
        }

        // Log a message if the image was not fetched successfully
        if(bitmapImage == null){
            Log.d("Message", "The image was not fetched");
        }
        return bitmapImage;
    }

    // method to set an image to an ImageView using a background thread
    // Reference: https://developer.android.com/guide/components/processes-and-threads#java
    public void setImage(ImageView img, String photoReference, String API_KEY){
        // Creates a service to execute threads
        Executor service = Executors.newSingleThreadExecutor();

        // Creates a handle to recover the result
        Handler handler = new Handler(Looper.getMainLooper());

        // Execute the image retrieval
        service.execute(new Runnable() {
            @Override
            public void run() {
                // Construct the URL
                String URI = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + API_KEY;
                ReadImage readImage = new ReadImage();

                // Create a new instance of ReadImage and retrieve the image as a Bitmap
                Bitmap bitmap = Bitmap.createBitmap(readImage.readImage(URI));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        img.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
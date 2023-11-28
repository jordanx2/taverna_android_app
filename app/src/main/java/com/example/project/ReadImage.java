package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReadImage {
    public Bitmap readImage(String address){
        Bitmap bitmapImage = null;
        try{
            InputStream inputStream = new URL(address).openStream();
            bitmapImage = BitmapFactory.decodeStream(inputStream);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(bitmapImage == null){
            Log.d("Message", "The image was not fetched");
        }
        return bitmapImage;
    }

    public void setImage(ImageView img, String photoReference, String API_KEY){
        // Creates a service to execute threads
        Executor service = Executors.newSingleThreadExecutor();

        // Creates a handle to recover the result
        Handler handler = new Handler(Looper.getMainLooper());
        service.execute(new Runnable() {
            @Override
            public void run() {
                String URI = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + "&key=" + API_KEY;
                ReadImage readImage = new ReadImage();
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
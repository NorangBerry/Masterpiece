package com.example.masterpiece.masterpiece;


import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class LearningActivity extends AppCompatActivity {
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        final Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d("countkkk", Integer.toString(count));
                Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(StringRequest.Method.GET,
                        "http://143.248.38.75:8080/test?num=" + Integer.toString(count),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("count", Integer.toString(count));
                                    Log.d("test", response);
                                    if (response.equals("yes")) {
                                        if (count == 10){
                                            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                                            timer.cancel();
                                            startActivity(intent);

                                        }
                                        load_one_by_one(load_img(count));
                                        count += 1;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());
                    }
                }));
            }
        }, 5000,5000);

    }


    public void load_one_by_one(final ImageView imageView) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                "http://143.248.38.75:8080/output?num="+Integer.toString(count), // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {


                        // Do something with response
                        imageView.setImageBitmap(response);

                        // Save this downloaded bitmap to internal storage
                        //Uri uri = saveImageToInternalStorage(response);

                        // Display the internal storage saved image to image view
                        //imageView.setImageURI(uri);
                    }
                },
                400, // Image width
                400, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);
        Log.d("tag","emd");
    }

    public ImageView load_img(int num) {
        ImageView img1 = findViewById(R.id.learning1);
        ImageView img2 = findViewById(R.id.learning2);
        ImageView img3 = findViewById(R.id.learning3);
        ImageView img4 = findViewById(R.id.learning4);
        ImageView img5 = findViewById(R.id.learning5);
        ImageView img6 = findViewById(R.id.learning6);
        ImageView img7 = findViewById(R.id.learning7);
        ImageView img8 = findViewById(R.id.learning8);
        ImageView img9 = findViewById(R.id.learning9);

        ImageView[] imageViewArr = {img1, img2, img3, img4, img5, img6, img7, img8, img9};

        return imageViewArr[num-1];
    }

    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images", MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, "UniqueFileName" + ".jpg");

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }

}


package com.example.masterpiece.masterpiece;


import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

public class LearningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
    }


    public void load_one_by_one(final ImageView[] imageViewArr, final int index) {
        if(index==imageViewArr.length)
            return;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(
                "http://143.248.38.75:8080/output", // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        imageViewArr[index].setImageBitmap(response);

                        // Save this downloaded bitmap to internal storage
                        Uri uri = saveImageToInternalStorage(response);

                        // Display the internal storage saved image to image view
                        imageViewArr[index].setImageURI(uri);
                        load_one_by_one(imageViewArr, index+1);
                    }
                },
                100, // Image width
                100, // Image height
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
    }

    public void load_img() {
        final ImageView[] imageViewArr = new ImageView[9];
        final ImageView img1 = findViewById(R.id.learning1);
        ImageView img2 = findViewById(R.id.learning2);
        ImageView img3 = findViewById(R.id.learning3);
        ImageView img4 = findViewById(R.id.learning4);
        ImageView img5 = findViewById(R.id.learning5);
        ImageView img6 = findViewById(R.id.learning6);
        ImageView img7 = findViewById(R.id.learning7);
        ImageView img8 = findViewById(R.id.learning8);
        ImageView img9 = findViewById(R.id.learning9);

        load_one_by_one(imageViewArr, 0);
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


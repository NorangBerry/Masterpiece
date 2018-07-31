package com.example.masterpiece.masterpiece;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResultActivity extends AppCompatActivity {
    public RequestQueue queue;
    Bitmap image;
    String image_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        queue = Volley.newRequestQueue(this);
        image = intent.getParcelableExtra("image");
        final ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
        image_name = "k";
        setResultImage();

//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        // Initialize a new ImageRequest
//        ImageRequest imageRequest = new ImageRequest(
//                "http://143.248.38.75:8080/output", // Image URL
//                new Response.Listener<Bitmap>() { // Bitmap listener
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        // Do something with response
//                        imageView.setImageBitmap(response);
//
//                        // Save this downloaded bitmap to internal storage
//                        Uri uri = saveImageToInternalStorage(response);
//
//                        // Display the internal storage saved image to image view
//                        imageView.setImageURI(uri);
//                    }
//                },
//                400, // Image width
//                400, // Image height
//                ImageView.ScaleType.CENTER_CROP, // Image scale type
//                Bitmap.Config.RGB_565, //Image decode configuration
//                new Response.ErrorListener() { // Error listener
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Do something with error response
//                        error.printStackTrace();
//                    }
//                }
//        );
//
//        // Add ImageRequest to the RequestQueue
//        requestQueue.add(imageRequest);
//    }
//
//    // Custom method to save a bitmap into internal storage
//    protected Uri saveImageToInternalStorage(Bitmap bitmap){
//        // Initialize ContextWrapper
//        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
//
//        // Initializing a new file
//        // The bellow line return a directory in internal storage
//        File file = wrapper.getDir("Images",MODE_PRIVATE);
//
//        // Create a file to save the image
//        file = new File(file, "UniqueFileName"+".jpg");
//
//        try{
//            // Initialize a new OutputStream
//            OutputStream stream = null;
//
//            // If the output file exists, it can be replaced or appended to it
//            stream = new FileOutputStream(file);
//
//            // Compress the bitmap
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//
//            // Flushes the stream
//            stream.flush();
//
//            // Closes the stream
//            stream.close();
//
//        }catch (IOException e) // Catch the exception
//        {
//            e.printStackTrace();
//        }
//
//        // Parse the gallery image url to uri
//        Uri savedImageURI = Uri.parse(file.getAbsolutePath());
//
//        // Return the saved image Uri
//        return savedImageURI;
    }

    public void setResultImage() {
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, "http://143.248.38.75:8080/output?num=10",
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response != null) {
                                Log.d("ResponseLog", response.toString());
                                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeByteArray(response, 0, response.length));
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
            }
        }, null);
        queue.add(request);
    }

    public void saveImage(View view) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, image_name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shareImage(View view) {
        try {
            File file = new File(this.getExternalCacheDir(), image_name + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/jpeg");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

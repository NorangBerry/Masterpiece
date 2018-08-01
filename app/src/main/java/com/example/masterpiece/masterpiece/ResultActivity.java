package com.example.masterpiece.masterpiece;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        final ImageView imageView = findViewById(R.id.imageView);
        image_name = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());
        setResultImage();




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
                                image = BitmapFactory.decodeByteArray(response, 0, response.length);
                                setImage(image);
                                Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(StringRequest.Method.GET,
                                        "http://143.248.38.75:8080/done?done=" + "True",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                System.out.println(response);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                System.out.println(error.getMessage());
                                            }
                                        })
                                );
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

    public void setImage(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        View button = findViewById(R.id.button);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 40;
        int height = size.y - 20 - button.getHeight();

        if ((width * 1.0 / height) > (bitmap.getWidth() * 1.0 / bitmap.getHeight())) {
            bitmap = getResizedBitmap(bitmap,
                    height,
                    (int) (height * (bitmap.getWidth() * 1.0 / bitmap.getHeight())));
            imageView.setImageBitmap(bitmap);
        } else {
            bitmap = getResizedBitmap(bitmap,
                    (int) (width * (bitmap.getHeight() * 1.0 / bitmap.getWidth())),
                    width);
            imageView.setImageBitmap(bitmap);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    public void saveImage(View view) {
        File root = Environment.getExternalStorageDirectory();
        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/"+image_name+".jpg");
        if(!cachePath.mkdirs())
            cachePath.mkdir();

        // Create imageDir
        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this,"save",Toast.LENGTH_SHORT).show();
    }

    public void shareImage(View view) {
        Bitmap bitmap = image;
        File root = Environment.getExternalStorageDirectory();
        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/"+image_name+".jpg");
        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".com.example.masterpiece.masterpiece", cachePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setDataAndType(photoURI, "image/*");
        share.putExtra(Intent.EXTRA_STREAM, photoURI);

        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(share,"Share via"));
    }
}

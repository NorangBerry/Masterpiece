package com.example.masterpiece.masterpiece;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FilterSelectActivity extends AppCompatActivity {
    Bitmap image;
    public RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_select);
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        Uri imageUri = intent.getParcelableExtra("image");
        try {

            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            image = BitmapFactory.decodeStream(imageStream);
            ImageView imageView = findViewById(R.id.origin_image);
            imageView.setImageBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFilter(final View view) {
        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(StringRequest.Method.GET,
                "http://143.248.38.75:8080/input/filter?filter=" + view.getTag().toString(),
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
        sendImage(view);
        Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
        startActivity(intent);
    }

    public void sendImage(final View view) {
        AndroidMultiPartEntity volleyMultipartRequest = new AndroidMultiPartEntity(Request.Method.POST, "http://143.248.38.75:8080/input/img",
                new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.d("return value", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("return value error", error.getMessage());
                    }
                }) {

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, AndroidMultiPartEntity.DataPart> getByteData() {
                Map<String, AndroidMultiPartEntity.DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new AndroidMultiPartEntity.DataPart(imagename + ".jpg",
                        getFileDataFromDrawable(image)));
                return params;
            }
        };
        queue.add(volleyMultipartRequest);

        Intent intent = new Intent(getApplication(),LearningActivity.class);
        startActivity(intent);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}

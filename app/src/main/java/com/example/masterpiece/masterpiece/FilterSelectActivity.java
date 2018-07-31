package com.example.masterpiece.masterpiece;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FilterSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_select);

        Intent intent = getIntent();
        ImageView imageView = findViewById(R.id.origin_image);
        imageView.setImageBitmap((Bitmap) intent.getParcelableExtra("image"));
    }

    public void sendFilter(final View view) {
        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(StringRequest.Method.POST,
                "http://0.0.0.0:8080/input/filter",
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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("filter", view.getTag().toString());
                return params;
            }
        });

//        sendImage(view);
    }

//    public void sendImage(final View view) {
//        Volley.newRequestQueue(getApplicationContext()).add(new StringRequest(StringRequest.Method.POST,
//                "http://0.0.0.0:8080/input/img",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error.getMessage());
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("img", img);
//                return params;
//            }
//        });
//    }
}

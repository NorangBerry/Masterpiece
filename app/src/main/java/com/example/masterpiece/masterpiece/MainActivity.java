package com.example.masterpiece.masterpiece;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static int REQUEST_SELECT_IMAGE = 3, REQUEST_IMAGE_CAPTURE = 4;
    Bitmap image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_SELECT_IMAGE){
                //이미지 데이터를 비트맵으로 받아온다.
                try {
                    Uri imageUri = data.getData();

                    System.out.println("===========================");
                    System.out.println(image);
                    Intent intent = new Intent(getApplicationContext(),FilterSelectActivity.class);
                    intent.putExtra("image",imageUri);
                    startActivity(intent);
                    }catch(Exception e){e.printStackTrace();}

            }
            else if(requestCode == REQUEST_IMAGE_CAPTURE){
                Uri u = data.getData();
                Intent intent = new Intent(getApplicationContext(),FilterSelectActivity.class);
                intent.putExtra("image",u);
                startActivity(intent);
            }
        }
    }
    public String getImageNameToUri(Uri data) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }
    public void onClickGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }
    public void onClickCamera(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }

        try{
            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, 4);

        } catch(ActivityNotFoundException e){
            e.printStackTrace();
        }


    }
}
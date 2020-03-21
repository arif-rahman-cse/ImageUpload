package com.smarifrahman.imageupload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int CAMERA_PERMISSION_CODE = 121;
    private static final int PICK_FROM_CAMERA = 122;
    private static final int GALLERY_REQUEST_CODE = 123;
    ImageView img;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        submit = findViewById(R.id.add_img);

        img.setOnClickListener(v -> popUpOptionDialog());
        submit.setOnClickListener(v -> {
            submitData();
        });

    }

    private void submitData() {
        Call<ImageResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .postBazar("test","20", "20","Arif","12120","null");

        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {

                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: Success"+ response.body().getMessage());
                }else {
                    Log.d(TAG, "onResponse: "+ response.errorBody());

                }


            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void popUpOptionDialog() {
        final String[] option = {"Take Picture From Gallery", "Take Picture by Camera "};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setSingleChoiceItems(option, -1, (dialog, which) -> {

            if (which == 0) {
                pickFromGallery();
                dialog.dismiss();
            }
            if (which == 1) {
                Log.d(TAG, "onClick: Camera");
                //openCamera();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, CAMERA_PERMISSION_CODE);

                    } else {
                        //Permission already granted
                        openCamera();
                    }
                } else {
                    //System OS < Marshmallow
                    openCamera();
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            Toast.makeText(this, "Camera is Calling", Toast.LENGTH_SHORT).show();
        }

    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
            // convertToJpg(imageBitmap);

        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            Log.d(TAG, "onActivityResult: Pick form gallery");

            if (data != null) {
                Uri contentURI = data.getData();
                Log.d(TAG, "onActivityResult: URI: "+ contentURI);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    img.setImageBitmap(bitmap);
                    Log.d(TAG, "onActivityResult: Bitmap: "+ bitmap);
                    //  convertToJpg(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from pop is granted
                    openCamera();
                } else {
                    //Permission from pop is denied
                    Toast.makeText(this, "Permission from pop is denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

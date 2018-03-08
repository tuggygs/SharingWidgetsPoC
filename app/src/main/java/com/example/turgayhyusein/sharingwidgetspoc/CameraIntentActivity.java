package com.example.turgayhyusein.sharingwidgetspoc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by turgay.hyusein on 2/26/2018.
 */

public class CameraIntentActivity extends AppCompatActivity {

    public static final String TAG = CameraIntentActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private static final int READ_EXTERNAL_STORAGE = 1;

    private File captureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA); //requestCode
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }else {
            ImagePicker imagePicker = new ImagePicker(CameraIntentActivity.this);
            captureFile = imagePicker.pickImageFromCamera(CameraIntentActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG,"REQ CODE = " + requestCode + " / " + grantResults[0]);

        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            Log.e(TAG,"BUILS VERSION GREATER THEN 23");
            ImagePicker imagePicker = new ImagePicker(CameraIntentActivity.this);
            captureFile = imagePicker.pickImageFromCamera(CameraIntentActivity.this);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
        }

        /*   if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Log.e(TAG,"BUILS VERSION GREATER THEN 23");
                ImagePicker imagePicker = new ImagePicker(CameraIntentActivity.this);
                captureFile = imagePicker.pickImageFromCamera(CameraIntentActivity.this);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.ACTION_PICK_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            if (captureFile != null) {
                Log.e(TAG, "CAPTURED FILE = " + captureFile);
                shareCapturedImage(captureFile);
            }
        } else if (requestCode == ImagePicker.ACTION_PICK_IMAGE_FROM_GALLERY) {
            //Log.e(TAG, "GALLERY RESULT DATA = " + data);
            //shareGalleryImage(data);
        }
    }

    private void shareCapturedImage(File file) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.e(TAG, "CAPTURED FILE 4 " + file);
            final long token = Binder.clearCallingIdentity();
            try {
                Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
                share.putExtra(Intent.EXTRA_STREAM, photoURI);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }else {
            Log.e(TAG, "CAPTURED FILE 3 " + file);
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        }

        startActivity(Intent.createChooser(share, "Share Image!"));
    }
}
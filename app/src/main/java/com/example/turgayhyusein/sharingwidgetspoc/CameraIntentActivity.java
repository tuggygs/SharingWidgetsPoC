package com.example.turgayhyusein.sharingwidgetspoc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by turgay.hyusein on 2/26/2018.
 */

public class CameraIntentActivity extends AppCompatActivity {

    public static final String TAG = CameraIntentActivity.class.getSimpleName();

    private File captureFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImagePicker imagePicker = new ImagePicker(CameraIntentActivity.this);

        captureFile = imagePicker.pickImageFromCamera(CameraIntentActivity.this);
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
            Log.e(TAG, "GALLERY RESULT DATA = " + data);
            //shareGalleryImage(data);
        }
    }

    private void shareCapturedImage(File file) {

        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        final long token = Binder.clearCallingIdentity();
        try {
            Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
            Log.e("TAG", "THE URI = " + photoURI);
            share.putExtra(Intent.EXTRA_STREAM, photoURI);
        } finally {
            Binder.restoreCallingIdentity(token);
        }

        startActivity(Intent.createChooser(share, "Share Image!"));
    }

}

package com.example.turgayhyusein.sharingwidgetspoc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

/**
 * Created by turgay.hyusein on 3/9/2018.
 */

public class TempClass extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String filepath = intent.getStringExtra("audiofile");
        shareAudioFile(filepath);
        finish();
    }

    private void shareAudioFile(String mFileName) {
        File f = new File(mFileName);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", f);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("audio/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intent.createChooser(share, "Share audio File"));
    }
}
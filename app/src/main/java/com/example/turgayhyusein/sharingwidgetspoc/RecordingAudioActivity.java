package com.example.turgayhyusein.sharingwidgetspoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by turgay.hyusein on 2/26/2018.
 */

public class RecordingAudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,	"TEST ...", Toast.LENGTH_LONG).show();
    }
}

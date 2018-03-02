package com.example.turgayhyusein.sharingwidgetspoc;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by turgay.hyusein on 3/1/2018.
 */

public class RecordingAudioService extends Service {

    public static final String TAG = RecordingAudioService.class.getSimpleName();

    private MediaRecorder mRecorder = null;
    private static String mFileName = null;
    boolean mStartRecording = true;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    public RecordingAudioService() {
    }

    public RecordingAudioService(Context applicationContext) {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordsharing.3gpp";

        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if(intent.getAction().equals("START")) {
            Log.e("--SERVICE----- ", "--STARTED--" + intent.getAction());
            mStartRecording = true;

        } else if(intent.getAction().equals("STOP")) {
            Log.e("--SERVICE----- ", "--STARTED--" + intent.getAction());
            mStartRecording = false;
        }

        onRecord(mStartRecording);

        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    /*private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())) {
                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.e ("isMyServiceRunning?", false + "");
        return false;
    }*/

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG,"prepare() failed = " + e);
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        shareAudioFile();
    }

    private void shareAudioFile() {
        File f =new File(mFileName);
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", f);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType("audio/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share audio File"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("EXIT", "onDestroy!!!!!");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
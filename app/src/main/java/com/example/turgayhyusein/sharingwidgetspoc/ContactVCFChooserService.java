package com.example.turgayhyusein.sharingwidgetspoc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by turgay.hyusein on 3/2/2018.
 */

public class ContactVCFChooserService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //Toast.makeText(this, "CONTACT SHARING", Toast.LENGTH_SHORT).show();

        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

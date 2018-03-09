package com.example.turgayhyusein.sharingwidgetspoc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by turgay.hyusein on 3/2/2018.
 */

public class ContactVCFChooserActivity extends AppCompatActivity {

    private static final String TAG = ContactVCFChooserActivity.class.getSimpleName();

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Uri uriContact;
    private static String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/contactsharing.vcf";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }else {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {

            uriContact = data.getData();

            getContactVCF(uriContact);
            shareContact(mFileName);
        }
    }

    public void getContactVCF(Uri urii) {

        Cursor phones = getBaseContext().getContentResolver().query(
                urii, null, null, null, null);

        phones.moveToFirst();
        for (int i = 0; i < 1; i++) { // phones.getCount()

            String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);

            AssetFileDescriptor fd;
            try {
                fd = getBaseContext().getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String VCard = new String(buf);
                FileOutputStream mFileOutputStream = new FileOutputStream(mFileName,true);
                mFileOutputStream.write(VCard.toString().getBytes());
                phones.moveToNext();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void shareContact(String contactUri) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/x-vcard");
        final long token = Binder.clearCallingIdentity();
        try {
            Uri contactURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(contactUri));
            intent.putExtra(Intent.EXTRA_STREAM, contactURI);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
        startActivity(Intent.createChooser(intent, "Share Contact"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
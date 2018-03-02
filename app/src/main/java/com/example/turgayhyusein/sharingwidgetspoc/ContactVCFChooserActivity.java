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
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by turgay.hyusein on 3/2/2018.
 */

public class ContactVCFChooserActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    private static final String VCF_DIRECTORY = "/vcf_demonuts";
    private File vcfFile;
    private static String mFileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* // File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
        File vdfdirectory = new File(
                Environment.getExternalStorageDirectory() + VCF_DIRECTORY);
// have the object build the directory structure, if needed.
        if (!vdfdirectory.exists()) {
            vdfdirectory.mkdirs();
        }
*/
        mFileName = getExternalCacheDir().getAbsolutePath();
        mFileName += "/contactsharing.vcf";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Toast.makeText(this, "CONTACT-----", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {

            uriContact = data.getData();

            Log.e(TAG, "Response: " + data.toString() + "URI --> " +uriContact);

           /* retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();*/

            getContactVCF(uriContact);

            shareContact(mFileName);
        }
    }

    public void getContactVCF(Uri urii) {
        final String vfile = "Contacts.vcf";

      /*  Cursor phones = getBaseContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);*/

        Cursor phones = getBaseContext().getContentResolver().query(
                urii, null,
                null, null, null);

        phones.moveToFirst();
        for (int i = 0; i < 1; i++) { // phones.getCount()

            String lookupKey = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey); //"1486i31040964042965120.2578i101"
            Log.e(TAG,"getVCF = " + uri + " /// Look up key = " + lookupKey);

            AssetFileDescriptor fd;
            try {
                fd = getBaseContext().getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String VCard = new String(buf);
                //String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
                FileOutputStream mFileOutputStream = new FileOutputStream(mFileName,true);
                mFileOutputStream.write(VCard.toString().getBytes());
                phones.moveToNext();

                Log.e("Vcard", VCard);

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        //return uri;
    }

    private void shareContact(String contactUri) {
        Log.e(TAG,"CONTACT = " + contactUri + " / " + vcfFile);
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
        //intent.putExtra(Intent.EXTRA_STREAM, contactUri); //Uri.parse("storage_path")
        startActivity(Intent.createChooser(intent, "Share Contact"));

      /*  //String sharePath = Environment.getExternalStorageDirectory().getPath() + "/Soundboard/Ringtones/sample.m4a";
        //Log.e(TAG,"AUDIO PATH = " + sharePath);
        //Uri uri = Uri.parse(sharePath);
        Intent share = new Intent(Intent.ACTION_SEND);
        //share.setType("text/x-vcard");
        //share.putExtra(Intent.EXTRA_STREAM, contactUri);
        share.setDataAndType(contactUri, "text/x-vcard");
        startActivity(Intent.createChooser(share, "Share Contact"));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

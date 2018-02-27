package com.example.turgayhyusein.sharingwidgetspoc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by turgay.hyusein on 2/26/2018.
 */

public class ImagePicker {

    public static final String TAG = ImagePicker.class.getSimpleName();

    public static final int ACTION_PICK_IMAGE_FROM_CAMERA = 0;
    public static final int ACTION_PICK_IMAGE_FROM_GALLERY = 1;
    private Context mContext;
    private String mImageFromCameraPath = null;

    public ImagePicker(Context context)
    {
        mContext = context;
    }

    /*public ImagePicker(Context context, int imageSize)
    {
        mContext = context;
        mImageSize = imageSize;
    }*/

    public File pickImageFromCamera(Activity fragment) {

        File file = null;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            try {
                file = createImageFile(fragment.getString(R.string.app_name));
                mImageFromCameraPath = file.getAbsolutePath();
                addImageToGallery(mImageFromCameraPath);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            } catch(IOException e) {
                e.printStackTrace();
                mImageFromCameraPath = null;
            }
        } else {
            try {
                file = createImageFile(fragment.getString(R.string.app_name));
                mImageFromCameraPath = file.getAbsolutePath();
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

                file = createImageFile(fragment.getString(R.string.app_name));
                mImageFromCameraPath = file.getAbsolutePath();

                addImageToGallery(mImageFromCameraPath);

                Uri imageURI = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageURI);

            } catch(IOException e) {
                e.printStackTrace();
                mImageFromCameraPath = null;
            }
        }

        fragment.startActivityForResult(intent, ACTION_PICK_IMAGE_FROM_CAMERA);
        return file;
    }

    public File createImageFile(String albumDirectoryName) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";

        File albumFile = getAlbumDirectory(albumDirectoryName);

        File imageFile = File.createTempFile(imageFileName, ".jpg", albumFile);

        if(imageFile.exists())
            Log.e(TAG, "THE FILE EXIST = " + imageFile.getAbsolutePath());
        else
            Log.e(TAG, "THE FILE NOT EXIST = " + imageFile.getAbsolutePath());

        return imageFile;
    }

    public File getAlbumDirectory(String albumDirectoryName) {

        File storageDirectory = null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumDirectoryName);
            //storageDirectoryf = new File(Environment.get);

            if(storageDirectory != null) {
                if(!storageDirectory.mkdirs()) {
                    if(!storageDirectory.exists()) {
                        return null;
                    }
                }
            }
        }else {
            Log.e(TAG,"ImagePicker.getAlbumDirectory(): external storage is not mounted");
        }
        return storageDirectory;
    }

    public void addImageToGallery(String imageFromCameraPath) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File file = new File(imageFromCameraPath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        if(mContext!=null) mContext.sendBroadcast(intent);
    }
}

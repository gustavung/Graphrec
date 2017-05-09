package com.app.graphrec.graphrec;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.content.ContentValues.TAG;

/**
 * The main activity class
 */

public class MainActivity extends AppCompatActivity {

    // Intent handles
    static final int CAMERA_ACTIVITY = 1;

    /**
     * Initialize activity, set up layout and UI.
     * @param savedInstanceState If activity is re-initialized then this contains data it supplied to onSaveInstanceState, otherwise null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * This is fired when startActivityForResult(Intent, int) is done.
     * @param requestCode Specifies who the result came from
     * @param resultCode Specifies the status of its child activity through its setResult
     * @param data An intent with returned data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(data.getStringExtra("picture"));
            Bitmap imageBitmap = getBitmapFromUri(imageUri);
            ImageView view = (ImageView) findViewById(R.id.imageView);
            view.setImageBitmap(imageBitmap);
            sendRequest(imageUri);
        }
    }

    /**
     * Tries to extract a bitmap from a given Uri and returns null if an error occurred.
     * @param uri The Uri to be opened
     * @return The resulting bitmap
     */
    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(uri.getPath()));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error trying to open temp file " + e.getMessage());
        }

        return bitmap;
    }

    /**
     * Created a new camera activity and starts it.
     * @param view THe current view
     */
    public void invokeCameraIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_ACTIVITY);
    }

    /**
     * Sends a HTTP multiform request with payload etracted from Uri.
     * @param uri The Uri to be used
     */
    public void sendRequest(Uri uri) {
        new ImageUploadTask().execute(uri);
    }

}

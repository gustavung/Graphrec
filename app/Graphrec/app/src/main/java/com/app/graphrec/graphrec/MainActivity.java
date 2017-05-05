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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static android.content.ContentValues.TAG;

/**
 * The main activity class
 */

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(data.getStringExtra("picture"));
            Bitmap imageBitmap = getBitmapFromUri(imageUri);
            ImageView view = (ImageView) findViewById(R.id.imageView);
            view.setImageBitmap(imageBitmap);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            FileInputStream stream = new FileInputStream(uri.getPath());
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error trying to open temp file " + e.getMessage());
        }

        // cleanup
        File tempfile = new File(uri.getPath());
        if (!tempfile.delete()) {
            Log.d(TAG, "Error trying to delete temp file ");
        }
        return bitmap;
    }

    public void invokeCameraIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_ACTIVITY);
    }

    public void sendRequest() {
        new ImageUploadTask().execute();
    }

}

package com.app.graphrec.graphrec;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * A custom camera activity using the old and deprecated camera API.
 * Should be rewritten for the new API if we target newer (android 5+, API 21+)
 * phones exclusively.
 */

public class CameraActivity extends Activity {

    private Camera cam;
    private CameraPreview camPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // get the camera and relay it to the preview service
        cam = getCameraInstance();
        camPreview = new CameraPreview(this, cam);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(camPreview);

    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "Error getting the camera object: " + e.getMessage());
        }
        return camera;
    }

    public void invokeTakePicture(View view) {
        cam.takePicture(null, null, pictureCallback);
    }

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Intent intent = new Intent();
            File image = createImageFile(data);
            intent.putExtra("picture", image.toURI().toString());
            setResult(RESULT_OK, intent);
            camera.release();
            finish();
        }
    };

    private File createImageFile(byte[] data) {

        // Right now we create a secret temp file to transfer img to main activity
        // can this be done in a better way?
        File tempFile = new File(getExternalFilesDir(null), "tempImg.png");

        try {
            FileOutputStream output = new FileOutputStream(tempFile.getAbsoluteFile());
            output.write(data);
            output.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error while opening temp file");
        } catch (IOException e) {
            Log.d(TAG, "Error while writing to temp file");
        }

        return tempFile;
    }

}


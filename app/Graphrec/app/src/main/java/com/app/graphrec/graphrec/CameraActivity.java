package com.app.graphrec.graphrec;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;


/**
 * A custom camera activity using the old and deprecated camera API.
 * Should be rewritten for the new API if we target newer (android 5+, API 21+)
 * phones exclusively.
 * @author gustav
 */

public class CameraActivity extends Activity {

    private Camera cam;
    private CameraPreview camPreview;

    /**
     * Initialize activity, set up layout and UI.
     * @param savedInstanceState If activity is re-initialized then this contains data it supplied to onSaveInstanceState, otherwise null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // get the camera and relay it to the preview service
        cam = getCameraInstance();

        camPreview = new CameraPreview(this, cam);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(camPreview);

        final SurfaceOverlay overlay = (SurfaceOverlay) findViewById(R.id.overlay);
        overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (overlay.didButtonClick(event)) {
                    invokeTakePicture();
                }
                return true;
            }
        });

    }

    /**
     * Tries to create a new camera instance, will fail if denied by the android system
     * @return The new camera instance
     */
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.d(TAG, "Error getting the camera object: " + e.getMessage());
        }
        return camera;
    }

    /**
     * Takes a picture from the camera preview.
     */
    public void invokeTakePicture() {
        // according to the api this invocation will return a jpeg file and NOT a png file.
        cam.takePicture(null, null, pictureCallback);
    }

    /**
     * Creates a new intent and a new temporary image file and return to the previous activity.
     */
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
    private byte[] proccessData(byte[] data) {

        SurfaceOverlay overlay = (SurfaceOverlay) findViewById(R.id.overlay);
        int width = overlay.getWidth()/2;
        int indicatorOffset = 200;

        int leftCornerX = width-indicatorOffset;
        int leftCornerY = indicatorOffset;
        int indicatorHeight = width;
        int indicatorWidth = width;

        Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap dst = Bitmap.createBitmap(bmp, leftCornerX, leftCornerY, indicatorWidth, indicatorHeight);
        dst = Bitmap.createScaledBitmap(dst, 28, 28, false);
        dst.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();

    }

    /**
     * Tries to create a new image file, which is stored in the private app storage, from a byte array.
     * @param data the image in byte array form
     * @return The newly created image, which might be empty
     */
    private File createImageFile(byte[] data) {

        // Right now we create a secret temp file to transfer img to main activity
        // can this be done in a better way?
        File tempFile = new File(getExternalFilesDir(null), "tempImg.png");

        try {
            FileOutputStream output = new FileOutputStream(tempFile.getAbsoluteFile());
            output.write(proccessData(data));
            output.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error while opening temp file");
        } catch (IOException e) {
            Log.d(TAG, "Error while writing to temp file");
        }

        return tempFile;
    }

}


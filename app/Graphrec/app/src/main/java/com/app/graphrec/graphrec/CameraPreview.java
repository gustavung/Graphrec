package com.app.graphrec.graphrec;

/**
 * Created by gustav on 2017-05-04.
 */

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/** A basic Camera preview class which is based on the drawable SurfaceView class*/

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private Camera cam;

    /**
     * Class constructor, initialises internal class fields.
     * @param context The current context
     * @param camera THe camera to be used for previewing
     */
    public CameraPreview(Context context, Camera camera) {
        super(context);
        cam = camera;

        // Use Surface view for handling surface view changes
        holder = getHolder();
        holder.addCallback(this);
    }

    /**
     * Run after the surface is created.
     * Should contain initial rendering code if drawing on this thread.
     * @param holder The internal surface holder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // We have a preview surface and now we can start the camera
        try {
            cam.setPreviewDisplay(holder);
            cam.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    /**
     * Called immediately after surface is destroyed.
     * Cleanup rendering threads still using the surface.
     * @param holder The internal surface holder
     */
    public void surfaceDestroyed(SurfaceHolder holder) {}

    /**
     * This is called after any format or size changes have been made ot the surface.
     * @param holder The internal surface holder
     * @param format The new pixelformat of the surface
     * @param w The new width of the device
     * @param h The new height of the device
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        if (holder.getSurface() == null){
            return;
        }

        // We must have this code so that we can (eventually) draw an overlay.

        try {
            cam.stopPreview();
        } catch (Exception e){
        }

        try {
            cam.setPreviewDisplay(holder);
            cam.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
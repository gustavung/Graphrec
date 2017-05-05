package com.app.graphrec.graphrec;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * The main activity class
 */

public class MainActivity extends AppCompatActivity {

    static final int CAMERA_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            byte[] byteArray = extras.getByteArray("picture");
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            ImageView view = (ImageView) findViewById(R.id.imageView);
            view.setImageBitmap(imageBitmap);
        }
    }

    public void invokeCameraIntent(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_ACTIVITY);
    }

    public void sendRequest() {
        new ImageUploadTask().execute();
    }

}

package com.app.graphrec.graphrec;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * The activity which represents the result view.
 * @author gustav
 */

public class ResultActivity extends Activity {

    // Intent handles
    static final int CAMERA_ACTIVITY = 1;

    /**
     * Initialize activity, set up layout and UI.
     * @param savedInstanceState If activity is re-initialized then this contains data it supplied to onSaveInstanceState, otherwise null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageButton imageBtn = (ImageButton) findViewById(R.id.launchCameraButton);
        imageBtn.setImageResource(R.drawable.camera_button);

        // because of activity lifecycles...
        if (savedInstanceState == null) {
            sendRequestFromIntent(getIntent());
        }

        TextView view = (TextView) findViewById(R.id.textView3);
        view.setText("Oh snap...");
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
            sendRequestFromIntent(data);
        }
    }

    /**
     * Sends a HTTP request with payload extracted from intent
     * @param intent The intent to extract data from
     */
    private void sendRequestFromIntent(Intent intent) {
        Uri imageUri = Uri.parse(intent.getStringExtra("picture"));
        sendRequest(imageUri);
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
     * Sends a HTTP multiform request with payload extracted from Uri.
     * @param uri The Uri to be used
     */
    public void sendRequest(Uri uri) {
        // TODO: does not return anything on main view -> camera view -> result view, why?

        // I can imagine that passing an entire activity to the async task is good...
        new ImageUploadTask(getResources().getString(R.string.URL), this)
                .execute(uri);
    }

}

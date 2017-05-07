package com.app.graphrec.graphrec;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * A class which is used to send HTTP requests on a different thread.
 *
 */

public class ImageUploadTask extends AsyncTask<Uri, Void, Void> {

    protected Void doInBackground(Uri ... uri) {
        sendRequest(uri[0]);
        return null;
    }

    private void sendRequest(Uri uri) {

        File file = new File(uri.getPath());
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "picture.png",
                        RequestBody.create(MediaType.parse("image/png"),
                                file))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.0.114:5000/upload")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // cleanup
        if (!file.delete()) {
            Log.d(TAG, "Error trying to delete temp file ");
        }

    }

}

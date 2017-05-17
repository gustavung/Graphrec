package com.app.graphrec.graphrec;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

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
 * @author gustav
 */

public class ImageUploadTask extends AsyncTask<Uri, Void, String> {

    private String url;
    private ResultActivity activity;

    /**
     * Constructor used to pass auxiliary variables
     * @param url The url to be posted to
     */
    ImageUploadTask(String url, ResultActivity activity) {
        this.activity = activity;
        this.url = url;
    }

    /**
     * This is called after the execute method is called.
     * @param uri a variadic list of Uris
     * @return nothing
     */
    protected String doInBackground(Uri ... uri) {
        return sendRequest(uri[0]);
    }

    /**
     * Sends a HTTP multiform request to a fixed server.
     * @param uri THe uri of the file to be sent
     */
    private String sendRequest(Uri uri) {
        String result = null;
        File file = new File(uri.getPath());
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "picture.png",
                        RequestBody.create(MediaType.parse("image/png"),
                                file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // cleanup
        if (!file.delete()) {
            Log.d(TAG, "Error trying to delete temp file ");
        }

        return result;
    }

    @Override
    protected void onPostExecute(final String result) {
        TextView view = (TextView) activity.findViewById(R.id.resultTextView);
        view.setText(result);
    }

}

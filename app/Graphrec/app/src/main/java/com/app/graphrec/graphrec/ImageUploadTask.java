package com.app.graphrec.graphrec;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class which is used to send HTTP requests on a different thread.
 *
 */

public class ImageUploadTask extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {
            sendRequest();
            return Long.valueOf(0);
        }

    private void sendRequest() {
        InputStreamReader input = null;
        try {
            // we don't want to hardcode the server, find config file
            URL url = new URL("http",  "192.168.0.114", R.integer.PORT, "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoInput(true);

                input = new InputStreamReader(conn.getInputStream());
                readStream(input);
            } finally {
                conn.disconnect();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void readStream(InputStreamReader stream) throws IOException {
        BufferedReader reader = new BufferedReader(stream);
        String output;
        while ((output = reader.readLine()) != null) {
            System.out.println(output);
        }

    }

}

package com.example.jyn.hearhere;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JYN on 2017-07-04.
 */

public class AsyncTask_get_unreceived_msg extends AsyncTask<String, Void, String> {

    private String SERVER_URL_GET_UNRECEIVED_MSG = Static.SERVER_URL_GET_UNRECEIVED_MSG;

    AsyncTask_get_unreceived_msg() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String last_received_msg_no = params[0];
        String broadCast_no = params[1];
        String user_no = params[2];

        String REQUEST = "unreceived_msg";

        String postParams = "request=" + REQUEST +
                "&last_received_msg_no="+ last_received_msg_no +
                "&broadCast_no="+ broadCast_no +
                "&user_no=" + user_no;

        try {
            URL url = new URL(SERVER_URL_GET_UNRECEIVED_MSG);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setReadTimeout(Static.HTTP_READTIMEOUT);
            httpURLConnection.setConnectTimeout(Static.HTTP_CONNECTTIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParams.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            InputStream inputStream;
            int responseStatusCode = httpURLConnection.getResponseCode();
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.d("AsyncTask_get_cast", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}

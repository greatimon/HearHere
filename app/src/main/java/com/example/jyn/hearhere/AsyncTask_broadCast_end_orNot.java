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

public class AsyncTask_broadCast_end_orNot extends AsyncTask<String, Void, String> {

    private String broadCast_no, user_no;
    private String SERVER_URL_broadCast_end_orNot = Static.SERVER_URL_BROADCAST_END_ORNOT;

    AsyncTask_broadCast_end_orNot() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        broadCast_no = params[0];
        user_no = params[1];
        Log.d("AsyncTask_broadCast_end", broadCast_no);

        String postPrams = "broadCast_no=" + broadCast_no + "&user_no=" + user_no;

        try {
            URL url = new URL(SERVER_URL_broadCast_end_orNot);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

            httpURLConnection.setReadTimeout(Static.HTTP_READTIMEOUT);
            httpURLConnection.setConnectTimeout(Static.HTTP_CONNECTTIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postPrams.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("AsyncTask_create_room", "POST response code - " + responseStatusCode);

            InputStream inputStream;
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

            while((line = bufferedReader.readLine()) != null ) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();

        }catch (Exception e) {
            Log.d("AsyncTask_create_room", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

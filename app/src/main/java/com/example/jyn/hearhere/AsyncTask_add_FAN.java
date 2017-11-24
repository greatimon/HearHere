package com.example.jyn.hearhere;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class AsyncTask_add_FAN extends AsyncTask<String, Void, String> {

    private String SERVER_URL_ADD_FAN = Static.SERVER_URL_ADD_FAN;
    private String my_user_no;
    private String clicked_user_no;

    AsyncTask_add_FAN() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        my_user_no = params[0];
        clicked_user_no = params[1];

        String postParams = "my_user_no=" + my_user_no + "&clicked_user_no=" + clicked_user_no;

        try {
            URL url = new URL(SERVER_URL_ADD_FAN);
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

            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d("AsyncTask_add_FAN", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_add_FAN", "Error: " + e);
            return "error";
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}

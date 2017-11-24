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
 * Created by JYN on 2017-07-28.
 */

public class AsyncTask_push_notification extends AsyncTask<String, Void, String> {

    private String SERVER_URL_PUSH_NOTIFICATION = Static.SERVER_URL_PUSH_NOTIFICATION;
    private String user_no;
    private String title;
    private String noti_type;
    private String noti_user_no;

    String postParams;

    AsyncTask_push_notification() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        noti_type = params[0];
        user_no = params[1];

        if(noti_type.equals("broadCast")) {
            title = params[2];
            postParams = "user_no=" + user_no + "&title=" + title + "&noti_type=" + noti_type;
        }
        if(noti_type.equals("fan")) {
            noti_user_no = params[2];
            postParams = "user_no=" + user_no + "&noti_user_no=" + noti_user_no + "&noti_type=" + noti_type;
        }


        try {
            URL url = new URL(SERVER_URL_PUSH_NOTIFICATION);
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
            Log.d("AsyncTask_push_noti", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_push_noti", "Error: " + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

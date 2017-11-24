package com.example.jyn.hearhere;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_get_channel_list extends AsyncTask<String, Void, String> {

    private Context context;
    private String SERVER_URL_GET_CHANNEL_LIST;
    private String REQUEST;

    DataClass_room_list channel_info;
    private String received_channel_list;

//    AsyncTask_get_channel_list(Context context) {
    AsyncTask_get_channel_list() {
//        this.context = context;
        this.SERVER_URL_GET_CHANNEL_LIST = Static.SERVER_URL_GET_CHANNEL_LIST;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        REQUEST = "channel_list";

        String postParams = "request=" + REQUEST;

        try {
            URL url = new URL(SERVER_URL_GET_CHANNEL_LIST);
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

        }catch (Exception e) {
            Log.d("AsyncTask_channel_list", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {}




}

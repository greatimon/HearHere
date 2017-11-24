package com.example.jyn.hearhere;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_modi_nickName extends AsyncTask<String, Void, String> {

    public AsyncTask_modi_nickName() {}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String user_no = params[0];
        String user_nickName = params[1];

        String postParams = "user_no=" + user_no + "&user_nickName=" + user_nickName;

        try {
            URL url = new URL(Static.SERVER_URL_MODIFY_NICKNAME);
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
            Log.d("AsyncTask_modi_nickName", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_modi_nickName", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}

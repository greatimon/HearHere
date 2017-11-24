package com.example.jyn.hearhere;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_get_cast_list extends AsyncTask<String, Void, String> {

    private String SERVER_URL_GET_CAST_LIST = Static.SERVER_URL_GET_CAST_LIST;
    private String request;
    private String user_no;

    AsyncTask_get_cast_list() {}

    @Override
    protected void onPreExecute() {super.onPreExecute();}

    @Override
    protected String doInBackground(String... params) {
        request = params[0];
        String postParams = "";

        if(request.equals("profile")) {
            user_no = params[1];
            postParams = "request=" + request + "&user_no=" + user_no;
        }
        if(request.equals("main")) {
            postParams = "request=" + request;
        }

//        String REQUEST = "cast_list";
//        String postParams = "request=" + REQUEST;

        try {
            URL url = new URL(SERVER_URL_GET_CAST_LIST);
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
            Log.d("AsyncTask_get_cast_list", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {super.onPostExecute(s);}

}

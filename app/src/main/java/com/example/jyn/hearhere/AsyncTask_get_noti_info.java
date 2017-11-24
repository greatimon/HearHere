package com.example.jyn.hearhere;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JYN on 2017-08-01.
 */

public class AsyncTask_get_noti_info extends AsyncTask<String, Void, String> {

    private String SERVER_URL_GET_NOTI_INFO = Static.SERVER_URL_GET_NOTI_INFO;
    private String user_no;
    private ProgressDialog progressDialog;
    private Context context;


    AsyncTask_get_noti_info(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "알림 정보를 가져오는 중입니다.", null, true,true);
    }

    @Override
    protected String doInBackground(String... params) {
        user_no = params[0];

        String postParams = "user_no=" + user_no;

        try {
            URL url = new URL(SERVER_URL_GET_NOTI_INFO);
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
            Log.d("AsyncTask_get_noti_info", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_get_noti_info", "Error: " + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
    }
}

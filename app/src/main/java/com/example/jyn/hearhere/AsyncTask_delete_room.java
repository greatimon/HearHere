package com.example.jyn.hearhere;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_delete_room extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context context;
    private String SERVER_URL_DELETE_ROOM;
    private String broadCast_no, save, user_no;

    AsyncTask_delete_room(Context context) {
        this.context = context;
        this.SERVER_URL_DELETE_ROOM = Static.SERVER_URL_DELETE_ROOM;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "방송 종료 중입니다", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        broadCast_no = params[0];
        save = params[1];
        user_no = params[2];

        // 포스트 이름 + broadCast_no
        String postPrams = "delete_room=" + broadCast_no + "&save=" + save + "&user_no" + user_no;

        try {
            URL url = new URL(SERVER_URL_DELETE_ROOM);
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
            Log.d("AsyncTask_delete_room", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_delete_room", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressDialog.dismiss();
        String delete_target_broadCast_no;

        if(result.equals("success")) {
            delete_target_broadCast_no = result;
            Log.d("AsyncTask_delete_room", "delete_target_broadCast_no: " + broadCast_no);
        }
        else if(result.equals("fail")) {
            Toast.makeText(context, "방송종료가 정상적으로 이루어지지 않았습니다", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
        }
    }
}

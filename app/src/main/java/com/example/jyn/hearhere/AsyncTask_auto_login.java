package com.example.jyn.hearhere;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_auto_login extends AsyncTask<String, Void, String>{

    private ProgressDialog progressDialog;
    private Context context;
    private String SERVER_URL_LOGIN;
    private String email_str;
    private String pw_str;
    private String fcm_orNot;
    private String fcm_user_no;
    private String fcm_broadCast_no;
    private String fcm_type;
    private String fcm_no;
    private AsyncTask_login task;

    public AsyncTask_auto_login(Context context) {
        this.context = context;
        this.SERVER_URL_LOGIN = Static.SERVER_URL_LOGIN;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "자동 로그인 중입니다.", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        email_str = params[0];
        pw_str = params[1];
        fcm_orNot = params[2];
        fcm_user_no = params[3];
        fcm_broadCast_no = params[4];
        fcm_type = params[5];
        fcm_no = params[6];
        Log.d("AsyncTask_auto_login", email_str);
        Log.d("AsyncTask_auto_login", pw_str);
        Log.d("AsyncTask_auto_login", fcm_orNot);
        Log.d("AsyncTask_auto_login", fcm_user_no);
        Log.d("AsyncTask_auto_login", fcm_broadCast_no);
        Log.d("AsyncTask_auto_login", fcm_type);
        Log.d("AsyncTask_auto_login", fcm_no);

        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 로그인 정보를 JSON 객체화 (JSON 객체 만들기)
        JSONObject jObject_login = new JSONObject();

        try {
            jObject_login.put("EMAIL", email_str);
            jObject_login.put("PW", pw_str);
            Log.d("AsyncTask_login", email_str);
            Log.d("AsyncTask_login", pw_str);
        } catch (JSONException e) {
            Log.d("AsyncTask_login", "JSONException: Error" + e);
        }

        // JSON_Object to JSON_String
        String Json_str = jObject_login.toString();
        // 포스트 이름 + Json_str
        String postParams = "login=" + Json_str;

        try {
            URL url = new URL(SERVER_URL_LOGIN);
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
            Log.d("AsyncTask_login", "POST response code - " + responseStatusCode);

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

            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();

        } catch (Exception e) {
            Log.d("AsyncTask_login", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressDialog.dismiss();

        String before_split = result;
        String[] after_split = before_split.split("&");

        String user_no = after_split[0];
        String login_path = after_split[1];
        String user_email = after_split[2];

        Intent intent = new Intent(context, a_main_after_login.class);
        intent.putExtra("email", user_email);
        intent.putExtra("user_no", user_no);
        intent.putExtra("login_path", login_path);
        if(fcm_orNot.equals("true")) {
            intent.putExtra("fcm_orNot", "true");
            intent.putExtra("fcm_user_no", fcm_user_no);
            intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
            intent.putExtra("type", fcm_type);
            intent.putExtra("fcm_no", fcm_no);
        }
        if(fcm_orNot.equals("false")) {
            intent.putExtra("fcm_orNot", "false");
        }
        context.startActivity(intent);
    }

}

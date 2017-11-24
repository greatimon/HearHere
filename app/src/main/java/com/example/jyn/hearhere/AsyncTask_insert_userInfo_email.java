package com.example.jyn.hearhere;

import android.app.ProgressDialog;
import android.content.Context;
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


public class AsyncTask_insert_userInfo_email extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
//    private Gson gson;
    private Context context;
    private String SERVER_URL_INSERT_USERINFO_EMAIL;

    AsyncTask_insert_userInfo_email(Context context) {
        this.context = context;
        this.SERVER_URL_INSERT_USERINFO_EMAIL = Static.SERVER_URL_INSERT_USERINFO_EMAIL;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        progressDialog = ProgressDialog.show(context,
//                "회원가입 중입니다", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
//        gson = new Gson();

        String email_str = params[0];
        String pw2_str = params[1];
        String nickName_str = params[2];
        String join_path = params[3];
        String imagePath ="";

        if(join_path.equals("kakao") || join_path.equals("facebook")) {
            imagePath = params[4];
        }

        // 회원 정보를 JSON 객체화 (JSON 객체만들기)
        JSONObject jObject_Join = new JSONObject();

        try {
            jObject_Join.put("EMAIL", email_str);
            jObject_Join.put("PW", pw2_str);
            jObject_Join.put("NICkNAME", nickName_str);
            jObject_Join.put("IMAGEPATH", imagePath);

            Log.d("AsyncTask_ins_userInfo", email_str);
            Log.d("AsyncTask_ins_userInfo", pw2_str);
            Log.d("AsyncTask_ins_userInfo", nickName_str);
            Log.d("AsyncTask_ins_userInfo", imagePath);
        } catch (JSONException e) {
            Log.d("AsyncTask_ins_userInfo", "JSONException: Error" + e);
        }

        // JSON_Object to JSON_String
        String Json_str = jObject_Join.toString();
        // 포스트 이름  + Json_str
        String postPrams = "userInfo=" + Json_str + "&join_path=" + join_path;

        try {
            URL url = new URL(SERVER_URL_INSERT_USERINFO_EMAIL);
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
            Log.d("AsyncTask_ins_userInfo", "POST response code - " + responseStatusCode);

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
            Log.d("AsyncTask_ins_userInfo", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

//        progressDialog.dismiss();
    }
}

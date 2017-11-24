package com.example.jyn.hearhere;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_create_room extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context context;
    private String SERVER_URL_CREATE_ROOM;
    private String user_no;
    private String title;
    private String comment;
    private String save;
    private String broadCast_img_fileName;
    private String request_from;

    AsyncTask_create_room(Context context) {
        this.context = context;
        this.SERVER_URL_CREATE_ROOM = Static.SERVER_URL_CREATE_ROOM;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "방송 채널 생성 중입니다", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        user_no = params[0];
        title = params[1];
        comment = params[2];
        save = params[3];
        broadCast_img_fileName = params[4];
        Log.d("AsyncTask_create_room",user_no);
        Log.d("AsyncTask_create_room",title);
        Log.d("AsyncTask_create_room",comment);
        Log.d("AsyncTask_create_room",save);
        Log.d("AsyncTask_create_room", broadCast_img_fileName);

        // 방송 채널 정보를 JSON 객체화 (JSON 객체 만들기)
        JSONObject jsonObject_create_room = new JSONObject();

        try {
            jsonObject_create_room.put("USER_NO", user_no);
            jsonObject_create_room.put("TITLE", title);
            jsonObject_create_room.put("COMMENT", comment);
            jsonObject_create_room.put("SAVE", save);
            jsonObject_create_room.put("BROADCAST_IMG_FILENAME", broadCast_img_fileName);
        } catch(JSONException e) {
            Log.d("AsyncTask_create_room", "JSONException: Error" + e);
        }

        //JSON_Object to JSON_String
        String Json_str = jsonObject_create_room.toString();
        Log.d("AsyncTask_create_room",Json_str);
        // 포스트 이름 + Json_str
        String postPrams = "create_room=" + Json_str;

        try {
            URL url = new URL(SERVER_URL_CREATE_ROOM);
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
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressDialog.dismiss();
        String result_before_split;
        String broadCast_no;
        String nickName;
        String BJ_img_fileName;
        String get_from_server_broadCast_img_fileName;

        if(!result.equals("fail")) {
//            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//            ((Activity)context).finish();

            // 스플릿하기
            result_before_split = result;
            String[] result_after_split =  result_before_split.split("[&]");

            broadCast_no = result_after_split[0];
            nickName = result_after_split[1];
            BJ_img_fileName = result_after_split[2];
            get_from_server_broadCast_img_fileName = result_after_split[3];


            Intent intent = new Intent(context, a_room.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("HOST_ORNOT", "true");
            intent.putExtra("BROADCAST_NO", broadCast_no);
            intent.putExtra("USER_NO", user_no);
            intent.putExtra("NICKNAME", nickName);
            intent.putExtra("BJ_IMG_FILENAME", BJ_img_fileName);
            intent.putExtra("MY_IMG_FILENAME", BJ_img_fileName);
            intent.putExtra("BROADCAST_IMG_FILENAME", get_from_server_broadCast_img_fileName);
            intent.putExtra("FROM", "live");

            intent.putExtra("TITLE", title);
            intent.putExtra("COMMENT", comment);
            intent.putExtra("SAVE", save);
            context.startActivity(intent);


        }
        else if(result.equals("fail")) {
            Toast.makeText(context, "방송 채널 생성에 실패하였습니다", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
        }
    }















}

package com.example.jyn.hearhere;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTask_enter_room extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context context;
    private String SERVER_URL_ENTER_ROOM;
    private String user_no, broadCast_no, request_from;

    AsyncTask_enter_room(Context context) {
        this.context = context;
        this.SERVER_URL_ENTER_ROOM = Static.SERVER_URL_ENTER_ROOM;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context,
                "입장 중입니다", null, true, true);
    }

    @Override
    protected String doInBackground(String... params) {
        user_no = params[0];
        broadCast_no = params[1];
        request_from = params[2];
        Log.d("AsyncTask_enter_room", "user_no: " + user_no);
        Log.d("AsyncTask_enter_room", "broadCast_no: " + broadCast_no);
        Log.d("AsyncTask_enter_room", "request_from: " + request_from);

        // 포스트 이름 + Json_str
        String postPrams = "user_no=" + user_no + "&broadCast_no=" + broadCast_no + "&request_from=" + request_from;

        try {
            URL url = new URL(SERVER_URL_ENTER_ROOM);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

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
            Log.d("AsyncTask_enter_room", "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();

        } catch (Exception e) {
            Log.d("AsyncTask_enter_room", "InsertData: Error" + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressDialog.dismiss();

        if(result.equals("error")) {
            Toast.makeText(context, "입장에 실패하였습니다,\n인터넷 환경을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
        else if(!result.equals("error")) {
            String result_before_split;
            String user_nickName;
            String broadCast_title;
            String BJ_nickName;
            String BJ_img_fileName;
            String my_img_fileName;
            String broadCast_img_fileName;
            String welcome_word;
            String like_count;
            String present_guest_count;
            String total_guest_count;
            String didn_i_click_like;
            String broadCast_start_time="none";
            String total_broadCast_time="none";

            if (!result.equals("fail")) {
//            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//            ((Activity)context).finish();
                Log.d("AsyncTask_enter_room", "result_nickName: "+result);

                // 스플릿하기
                result_before_split = result;
                String[] result_after_split =  result_before_split.split("[&]");

                user_nickName = result_after_split[0];
                broadCast_title = result_after_split[1];
                BJ_nickName = result_after_split[2];
                BJ_img_fileName = result_after_split[3];
                my_img_fileName = result_after_split[4];
                broadCast_img_fileName = result_after_split[5];
                welcome_word = result_after_split[6];
                like_count = result_after_split[7];
                present_guest_count = result_after_split[8];
                total_guest_count = result_after_split[9];
                didn_i_click_like = result_after_split[10];
                if(request_from.equals("cast")) {
                    broadCast_start_time = result_after_split[11];
                    total_broadCast_time = result_after_split[12];
                }

                Intent intent = new Intent(context, a_room.class);
                intent.putExtra("HOST_ORNOT", "false");
                intent.putExtra("BROADCAST_NO", broadCast_no);
                intent.putExtra("USER_NO", user_no);
                intent.putExtra("NICKNAME", user_nickName);
                intent.putExtra("BJ_IMG_FILENAME", BJ_img_fileName);
                intent.putExtra("MY_IMG_FILENAME", my_img_fileName);
                intent.putExtra("BROADCAST_IMG_FILENAME", broadCast_img_fileName);
                intent.putExtra("BROADCAST_TITLE", broadCast_title);
                intent.putExtra("BJ_NICKNAME", BJ_nickName);
                intent.putExtra("WELCOME_WORD", welcome_word);
                intent.putExtra("LIKE_COUNT", like_count);
                intent.putExtra("PRESENT_GUEST_COUNT", present_guest_count);
                intent.putExtra("TOTAL_GUEST_COUNT", total_guest_count);
                intent.putExtra("DIDN_I_CLICK_LIKE", didn_i_click_like);
                intent.putExtra("BROADCAST_START_TIME", broadCast_start_time);
                intent.putExtra("TOTAL_BROADCAST_TIME", total_broadCast_time);

                if(request_from.equals("live")) {
                    intent.putExtra("FROM", "live");
                }
                if(request_from.equals("cast")) {
                    intent.putExtra("FROM", "cast");
                }
                context.startActivity(intent);

            } else if (result.equals("fail")) {
                Toast.makeText(context, "방송 입장에 실패하였습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "예외발생: " + result, Toast.LENGTH_SHORT).show();
            }
        }


    }
}

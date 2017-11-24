package com.example.jyn.hearhere;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JYN on 2017-08-07.
 */

public class AsyncTask_img_download extends AsyncTask<String, Void, String> {

    private Context context;
    private String fileName;
    private final String SAVE_FOLDER = "/HearHere";


    AsyncTask_img_download(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        //웹 서버 쪽 파일이 있는 경로
        String fileUrl = params[0];

        //다운로드 경로를 지정
        String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
//        String savePath = "/storage/emulated/0/DCIM" + SAVE_FOLDER;
        Log.d("download_img", "savePath: " + savePath);

        File dir = new File(savePath);

        //상위 디렉토리가 존재하지 않을 경우 생성
        if (!dir.exists()) {
            dir.mkdirs();
        }


        //파일 이름 :날짜_시간
        Date day = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
        fileName = String.valueOf(sdf.format(day));

        String localPath = savePath + "/" + fileName + ".jpg";

        try {

            URL imgUrl = new URL(fileUrl);
            //서버와 접속하는 클라이언트 객체 생성
            HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();

            int responseStatusCode = conn.getResponseCode();
            Log.d("AsyncTask_img_download", "POST response code - " + responseStatusCode);

            int len = conn.getContentLength();
            byte[] tmpByte = new byte[len];

            //입력 스트림을 구한다
            InputStream is = conn.getInputStream();
            File file = new File(localPath);
            //파일 저장 스트림 생성
            FileOutputStream fos = new FileOutputStream(file);

            int read;
            //입력 스트림을 파일로 저장
            for (;;) {
                read = is.read(tmpByte);
                if (read <= 0) {
                    break;
                }
                fos.write(tmpByte, 0, read); //file 생성
            }

            is.close();
            fos.close();
            conn.disconnect();

            Log.d("AsyncTask_img_download", "file.exists(): " + file.exists());

            if(file.exists()) {

            }

            return String.valueOf(file.exists());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("AsyncTask_img_download", "Error: " + e);
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if(result.equals("true")) {
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
        }


        String targetDir = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;
        File file = new File(targetDir + "/" + fileName + ".jpg");

        //이미지 스캔해서 갤러리 업데이트
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}

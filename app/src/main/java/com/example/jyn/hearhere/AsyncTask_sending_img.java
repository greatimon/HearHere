package com.example.jyn.hearhere;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTask_sending_img extends AsyncTask<String, Void, String> {


    private ProgressDialog progressDialog;

    private String file_uri;
    private String user_no;
    private String request_activity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("send_img_time_check", "onPreExecute: " + System.currentTimeMillis());
    }

    @Override
    protected String doInBackground(String... params) {
        file_uri = params[0];
        user_no = params[1];
        request_activity = params[2]; //profile, broadCast, room
        Log.d("photo", "file_uri: " + file_uri);
        Log.d("photo", "user_no: " + user_no);
        Log.d("photo", "request_activity: " + request_activity);


        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

        File sourceFile = new File(file_uri);

        /** 기기에 해당 파일이 존재 하지 않을 때 */
        if(!sourceFile.isFile() && !file_uri.equals("none")) {
            return "file does not exist!";
        }

        /** 프로필 사진을 삭제하였을 때 */
        else if(file_uri.equals("none")) {

            String postPrams = "user_no=" + user_no + "&delete_profile_img=" + file_uri + "&request_activity=" + request_activity;

            try {
                URL url = null;
                if(request_activity.equals("room")) {
                    url = new URL(Static.SERVER_URL_UPLOAD_IMG_FOR_CHATROOM);
                }
                else {
                    url = new URL(Static.SERVER_URL_UPLOAD_IMG);
                }

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

            } catch (Exception e) {
                Log.d("AsyncTask_drawer_header", "InsertData: Error" + e);
                return "error";
            }
        }

        /** 프로필 사진을 변경하였을 때 */
        else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);

                URL url = null;
                if(request_activity.equals("room")) {
                    url = new URL(Static.SERVER_URL_UPLOAD_IMG_FOR_CHATROOM);
                }
                else {
                    url = new URL(Static.SERVER_URL_UPLOAD_IMG);
                }

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.setRequestProperty("uploaded_file", file_uri);

                dos = new DataOutputStream(conn.getOutputStream());

                // add parameters_1
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_no\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // assign value_1
                dos.writeBytes(user_no);
                Log.d("photo", "user_no: "+user_no);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // add parameters_2
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"request_activity\"" + lineEnd);
                dos.writeBytes(lineEnd);
                // assign value_2
                dos.writeBytes(request_activity);
                Log.d("photo", "request_activity: "+request_activity);
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                // send image
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+file_uri +"\""+lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

                /** response 받기 */
                int responseStatusCode = conn.getResponseCode();
                Log.d("AsyncTask_sending_img", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                }
                else {
                    inputStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                Log.d("send_img_time_check", "doInBackground: " + System.currentTimeMillis());
                return stringBuilder.toString();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();;
                return "MalformedURLException";
            } catch (Exception e) {
                e.printStackTrace();
                return "Got Exception";
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("send_img_time_check", "onPostExecute: " + System.currentTimeMillis());
    }


}

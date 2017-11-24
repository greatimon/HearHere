package com.example.jyn.hearhere;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class a_camera_opencv extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String TAG = "a_camera_opencv";
    JavaCameraView javaCameraView;
    Mat mRgba;
    int back_camera_on = 0;

    String filename;
    static String saveFolder = Environment.getExternalStorageDirectory() + "/DCIM/HearHere/";

    PermissionListener permissionListener;

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    /**---------------------------------------------------------------------------
     메소드 ==> 퍼미션 체크
     ---------------------------------------------------------------------------*/
    public void permission_check() {
        // 퍼미션 확인(테드_ 라이브러리)
        new TedPermission(this)
                .setPermissionListener(permissionListener)
//                .setRationaleMessage("다음 작업을 허용하시겠습니까? 기기 사진, 미디어, 파일 액세스")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다")
                .setGotoSettingButton(true)
                .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
                .check();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.a_camera_opencv);

        // 퍼미션 리스너(테드_ 라이브러리)
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(a_profile.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(a_profile.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        // 퍼미션 체크
        permission_check();

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        // front-camera(1), back-camera(0)
        javaCameraView.setCameraIndex(0);
        back_camera_on = 0;
//        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV load 성공");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.d(TAG, "OpenCV load 실패");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
        }
    }

    private void saveImage(Mat subImg){
        Bitmap bmp=null;

        try {
            bmp = Bitmap.createBitmap(subImg.cols(), subImg.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(subImg, bmp);
        } catch (CvException e) {
            Log.d(TAG, e.getMessage());
        }

        subImg.release();

        FileOutputStream out = null;

        // 현재시간 기준으로 파일명 만들기
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date nowdate = new Date();
        String dateString = formatter.format(nowdate);
        Log.d("fileName_create", "dateString: " + dateString);

        // 난수 생성
        int num = 0;
        String num_str = "";
        for(int i=0; i<10; i++) {
            num = (int)(Math.random()*10) + 1;
            num_str += String.valueOf(num);
        }
        Log.d("fileName_create", "num_str_result: " + num_str);

        filename =  dateString + "_" + num_str + ".png";
        Log.d("fileName_create", "filename: " + filename);

        File sd = new File(saveFolder);
        boolean success = true;
        if (!sd.exists()) {
            success = sd.mkdir();
        }
        if (success) {
            File dest = new File(sd, filename);
            //이미지 스캔해서 갤러리 업데이트
            a_camera_opencv.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dest)));

            try {
                out = new FileOutputStream(dest);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, e.getMessage());
            } finally {
                try {
                    if (out != null) {
                        out.close();
                        Log.d(TAG, "OK!!");
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.getMessage() + "Error");
                    e.printStackTrace();
                }
            }
        }
    }


    public void onClickGo(View view) {
        saveImage(mRgba);
        Intent intent = new Intent(this, a_face_check.class);
        intent.putExtra("photoPath", saveFolder + filename);
        intent.putExtra("request", "camera");
        Log.d("photoPath_onClickGo", "photoPath_putExtra:: " + saveFolder + filename);
        startActivityForResult(intent, a_profile.REQUEST_TAKE_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("a_camera_opencv", "onActivityResult");

        if(requestCode==a_profile.REQUEST_TAKE_PHOTO && resultCode==a_profile.REQUEST_TAKE_PHOTO) {
            Intent intent = new Intent();
            intent.putExtra("savePath", saveFolder + filename);
            setResult(a_profile.REQUEST_TAKE_PHOTO, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }



    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        return mRgba;
    }

    public void camera_flip(View view) {
        switch (back_camera_on) {
            case 0: // back-camera
                javaCameraView.disableView();
                javaCameraView.setCameraIndex(1);
                javaCameraView.enableView();
                back_camera_on = 1;
                break;
            case 1: // front-camera
                javaCameraView.disableView();
                javaCameraView.setCameraIndex(0);
                javaCameraView.enableView();
                back_camera_on = 0;
                break;
        }
    }
}

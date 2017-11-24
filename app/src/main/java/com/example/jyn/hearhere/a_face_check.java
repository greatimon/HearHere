package com.example.jyn.hearhere;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;

import static org.opencv.imgproc.Imgproc.cvtColor;


public class a_face_check extends AppCompatActivity {

    ImageView imageView;
    Bitmap bmpInput, bmpOutput;
    Mat matInput, matOutput;

    String filename;
    static String saveFolder = Environment.getExternalStorageDirectory() + "/DCIM/HearHere/";

    View progressWheel_cover_V;
    private ProgressWheel progressWheel;
    RelativeLayout face_checked_img_Rel;
    LinearLayout go_check_face_Lin, face_checked_notice_Lin;

    String photoPath="";
    String request="";

    int width;
    int height;

    /** openCV 라이브러리 로드 */
    static {
        System.loadLibrary("MyLibs");
    }


    /**---------------------------------------------------------------------------
     콜벡메소드 ==> OpenCV 로드 or ETC
     ---------------------------------------------------------------------------*/
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    //convert bitmap to mat for native function
                    matInput = convertBitMap2Mat(bmpInput);
                    matOutput = new Mat(matInput.rows(), matInput.cols(), CvType.CV_8UC3);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_face_check);

        /** 디바이스 화면사이즈 가져오기 */
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        Log.d("device", "width: " + width);
        Log.d("device", "height: " + height);
        if(width > 1080) {
            width = 1080;
        }

        //get image view
        imageView = (ImageView)findViewById(R.id.imageView);
        go_check_face_Lin = (LinearLayout) findViewById(R.id.go_check_face_Lin);
        face_checked_img_Rel = (RelativeLayout)findViewById(R.id.face_checked_img_Rel);
        face_checked_notice_Lin = (LinearLayout)findViewById(R.id.face_checked_notice);
        progressWheel_cover_V = (View)findViewById(R.id.progressWheel_cover);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        progressWheel.setSpinSpeed(0.7f);
        progressWheel.setBarWidth(10);

        progressWheel.setVisibility(View.GONE);
        go_check_face_Lin.setVisibility(View.VISIBLE);
        face_checked_img_Rel.setVisibility(View.GONE);
        face_checked_notice_Lin.setVisibility(View.GONE);

        Intent intent = getIntent();
//        filename = intent.getExtras().getString("filename");
//        Log.d("fileName_create", "filename_getIntent_DetailActivity:: " + filename);

        //get the frame's path
//        String photoPath = saveFolder + filename;
        photoPath = intent.getExtras().getString("photoPath");
        request = intent.getExtras().getString("request");
        Log.d("photoPath_create", "photoPath_getIntent_photoPath:: " + photoPath);
        Log.d("photoPath_create", "photoPath_getIntent_request:: " + request);

        //get the bitmap frame
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmpInput = BitmapFactory.decodeFile(photoPath, options);

        /** set img 방법.1 */
//        imageView.setImageBitmap(bmpInput);

        /** set img 방법.2 - 글라이드 절대 경로 셋팅 */
        Glide
            .with(this)
            .load(photoPath)
            .override(width, height)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .bitmapTransform(new CropCircleTransformation(this))
            .into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    /**---------------------------------------------------------------------------
     메소드 ==> Mat to Bitmap
     ---------------------------------------------------------------------------*/
    Bitmap convertMat2Bitmap(Mat img){
        int width = img.width();
        int height = img.height();


        Bitmap bmp;
        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Mat tmp;
        tmp = img.channels()==1? new Mat (width,height, CvType.CV_8UC1,new Scalar(1)):new Mat (width,height, CvType.CV_8UC4,new Scalar(4));
        try {
            if (img.channels()==3)
                cvtColor(img, tmp, Imgproc.COLOR_RGB2BGRA);
            else if (img.channels()==1)
                cvtColor(img, tmp, Imgproc.COLOR_GRAY2RGBA);
            Utils.matToBitmap(tmp, bmp);
        }
        catch (CvException e){
            Log.d("Exception",e.getMessage());
        }
        return bmp;
    }


    /**---------------------------------------------------------------------------
     메소드 ==> Bitmap to Mat
     ---------------------------------------------------------------------------*/
    Mat convertBitMap2Mat(Bitmap rbgaImage){
        // convert Java Bitmap into Opencv Mat
        Mat rgbaMat = new Mat (rbgaImage.getHeight(), rbgaImage.getWidth(), CvType.CV_8UC4);
        Bitmap bmp32 = rbgaImage.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, rgbaMat);

        Mat rgbMat = new Mat(rbgaImage.getHeight(), rbgaImage.getWidth(), CvType.CV_8UC3); // 8 bits per component, 3 channels
        cvtColor(rgbaMat, rgbMat, Imgproc.COLOR_RGBA2BGR, 3);
        return rgbMat;
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 얼굴 인식
     ---------------------------------------------------------------------------*/
    public void check_face(View view) {

        new Asynctask_face_recognition(a_face_check.this).execute();
//        long temp = NativeClass.LandmarkDetection(matInput.getNativeObjAddr(), matOutput.getNativeObjAddr());
//        Log.d("face.size", "face.size: " + temp);
//
//        //convert back mat to bitmap for displaying in ImageView
//        bmpOutput = convertMat2Bitmap(matOutput);
//        imageView.setImageBitmap(bmpOutput);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 사진 선택 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void select_this_img(View view) {
        if(request.equals("gallery")) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        if(request.equals("camera")) {
            Intent intent = new Intent();
            setResult(a_profile.REQUEST_TAKE_PHOTO, intent);
            finish();
        }

    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 사진 취소 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void cancel_select(View view) {
        if(request.equals("gallery")) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        if(request.equals("camera")) {
            finish();
        }

    }


    /**---------------------------------------------------------------------------
     오버라이드 ==> 사진 취소 로직 적용
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(request.equals("gallery")) {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        }
        if(request.equals("camera")) {
            finish();
        }
    }

    /**---------------------------------------------------------------------------
     클래스 ==> AsyncTask -- 얼굴 인식
     ---------------------------------------------------------------------------*/
    public class Asynctask_face_recognition extends AsyncTask<String, Void, Long> {

        private Context context;


        Asynctask_face_recognition(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressWheel_cover_V.setVisibility(View.VISIBLE);
            progressWheel.setVisibility(View.VISIBLE);
        }

        @Override
        protected Long doInBackground(String... params) {
            long temp = NativeClass.LandmarkDetection(matInput.getNativeObjAddr(), matOutput.getNativeObjAddr());
            Log.d("face.size", "face.size: " + temp);
            return temp;
        }

        @Override
        protected void onPostExecute(Long face_count) {
            super.onPostExecute(face_count);
            progressWheel_cover_V.setVisibility(View.GONE);
            progressWheel.setVisibility(View.GONE);

            if(face_count == 0) {
                Toast.makeText(a_face_check.this, "얼굴 인식에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
            if(face_count >= 1) {
                //convert back mat to bitmap for displaying in ImageView
                bmpOutput = convertMat2Bitmap(matOutput);

                /** set img 방법.1 */
//                imageView.setImageBitmap(bmpOutput);

                /** set img 방법.2 - 비트맵 글라이드 셋팅 */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmpOutput.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Glide.with(context)
                        .load(stream.toByteArray())
                        .asBitmap()
                        .override(width, height)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);

                go_check_face_Lin.setVisibility(View.GONE);
                face_checked_img_Rel.setVisibility(View.VISIBLE);
                face_checked_notice_Lin.setVisibility(View.VISIBLE);
            }

        }


    }


}

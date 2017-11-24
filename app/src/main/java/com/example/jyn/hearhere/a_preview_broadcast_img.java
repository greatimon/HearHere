package com.example.jyn.hearhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class a_preview_broadcast_img extends AppCompatActivity {

    ImageView preview_img;
    String photoPath_on_myPhone;
    String request_activity;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_preview_broadcast_img);

        /** 디바이스 화면사이즈 가져오기 */
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.d("device", "width: " + width);
        Log.d("device", "height: " + height);
        if(width > 1080) {
            width = 1080;
        }

        Intent intent = getIntent();
        photoPath_on_myPhone = intent.getExtras().getString("preview_intent");
        request_activity = intent.getExtras().getString("request_activity");
        preview_img = (ImageView)findViewById(R.id.preview_img);

        
        if(request_activity.equals("room")) {
            preview_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        Glide
            .with(this)
            .load(photoPath_on_myPhone)
            .override(width, height)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(preview_img);

    }

    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 사진 선택 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void select_this_img(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 사진 선택 취소 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void cancel_select(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}

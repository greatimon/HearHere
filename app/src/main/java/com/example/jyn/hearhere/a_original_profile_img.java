package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class a_original_profile_img extends Activity {

    TextView nickName_TV;
    ImageView profile_IV;

    String user_nickName;
    String user_img_fileName;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_original_profile_img);

        /** 디바이스 화면사이즈 가져오기 */
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        Log.d("device", "width: " + width);
        Log.d("device", "height: " + height);
        if(width > 1080) {
            width = 1080;
        }

        // 뷰 find
        nickName_TV = (TextView)findViewById(R.id.nickName);
        profile_IV = (ImageView)findViewById(R.id.profile_img);

        // 인텐트, 변수 받기
        Intent intent = getIntent();
        user_nickName = intent.getExtras().getString("USER_NICKNAME");
        user_img_fileName = intent.getExtras().getString("USER_IMG_FILENAME");

        // 유저 닉네임 넣기
        nickName_TV.setText(user_nickName);

        // 유저 프로필 사진 넣기
        // 유저 프로필 사진이 없을 때
        if(user_img_fileName.equals("none")) {
            Glide
                .with(this)
                .load(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(profile_IV);
        }
        // 유저 프로필 사진이 있을 때
        if(!user_img_fileName.equals("none")) {
            // 카카오톡|페이스북 프사 URL일 때
            if(user_img_fileName.contains("http")) {
                Glide
                    .with(this)
                    .load(user_img_fileName)
                    .override(width, height)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profile_IV);
            }

            // 카카오톡|페이스북 프사 URL이 아닐 때
            if(!user_img_fileName.contains("http")) {
                // 웹서버로의 이미지 url로 이미지뷰에 이미지 넣기
                Glide
                    .with(this)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                    .override(width, height)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profile_IV);
            }
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 뒤로가기 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
//        finish();
    }


}

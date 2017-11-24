package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class a_send_img extends Activity implements PhotoViewAttacher.OnViewTapListener {

    String send_img="";
    String send_user_nickName="";
    String send_user_profile_img_url="none";

    LinearLayout actionBar_LIN;
    PhotoView send_img_PV;
    ImageView sender_profile_img_IV, download_IV;
    TextView nickName_TV;

    boolean layout_show = true;

    Animation popup_ctl_layout_emerge;
    Animation popup_download_layout_emerge;

    AsyncTask_img_download task;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_send_img);

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
        send_img = intent.getStringExtra("send_img");
        send_user_nickName = intent.getStringExtra("send_user_nickName");
        send_user_profile_img_url = intent.getStringExtra("send_user_profile_img_url");

        actionBar_LIN = (LinearLayout)findViewById(R.id.actionBar);
        send_img_PV = (PhotoView)findViewById(R.id.send_img);
        sender_profile_img_IV = (ImageView)findViewById(R.id.sender_profile_img);
        download_IV = (ImageView)findViewById(R.id.download);
        nickName_TV = (TextView)findViewById(R.id.nickName);

        /** send_user_profile_img_url 셋팅 */
        // 유저 프로필 사진이 없을 때
        if(send_user_profile_img_url.equals("none")) {
            Glide
                .with(this)
                .load(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(sender_profile_img_IV);
        }
        // 유저 프로필 사진이 있을 때
        if(!send_user_profile_img_url.equals("none")) {
            // 카카오톡|페이스북 프사 URL일 때
            if(send_user_profile_img_url.contains("http")) {
                Glide
                    .with(this)
                    .load(send_user_profile_img_url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(sender_profile_img_IV);
            }

            // 카카오톡|페이스북 프사 URL이 아닐 때
            if(!send_user_profile_img_url.contains("http")) {
                // 웹서버로의 이미지 url로 이미지뷰에 이미지 넣기
                Glide
                    .with(this)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + send_user_profile_img_url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(sender_profile_img_IV);
            }
        }

        /** send_user_nickName 셋팅 */
        nickName_TV.setText(send_user_nickName);

        /**  send_img 셋팅 - [ 디바이스 화면에 꽉차게 넣기 위해서, 한참 검색함... ]
         * override 했을 때, 이미지의 width와 height가 디바이스의 width와 height보다 작다면,
         * 화면에 꽉차게 나타나지 않는데, 이때 fitCenter를 해주면 화면에 꽉차게 이미지가 늘어난다.
         * 당연히 이미지는 확장되므로 늘어난 비율에 비례하여 깨져 보인다
         */
        Glide
            .with(this)
            .load(Static.SERVER_URL_ROOM_SENDING_IMG_FOLDER + send_img)
            .override(width, height)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(send_img_PV);

        /** PhotoView 탭(클릭) 리스너, 스케일체인지 리너스 등록 */
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(send_img_PV);
        mAttacher.setScale(1.0f);
        mAttacher.setOnViewTapListener(this);
        mAttacher.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
            @Override
            public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                if(scaleFactor >= 1.0f) {
                    mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        });

        /** 애니메이션 load */
        popup_ctl_layout_emerge = AnimationUtils.loadAnimation(this, R.anim.popup_ctl_layout_emerge);
        popup_download_layout_emerge = AnimationUtils.loadAnimation(this, R.anim.popup_download_layout_emerge);
    }

    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 뒤로가기 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 이미지 다운로드 - 로컬 사진첩에
     ---------------------------------------------------------------------------*/
    public void clicked_download(View view) {
        Log.d("clicked_download", "다운로드 클릭");

        task = new AsyncTask_img_download(a_send_img.this);
        task.execute(Static.SERVER_URL_ROOM_SENDING_IMG_FOLDER + send_img);
        Log.d("download_img", "url: " + Static.SERVER_URL_ROOM_SENDING_IMG_FOLDER + send_user_profile_img_url);
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 클릭이벤트 -- 일부 레이아웃 보이기/안보이기 토글
     ---------------------------------------------------------------------------*/
    @Override
    public void onViewTap(View view, float x, float y) {
        int i=0;

        if(layout_show) {
            Log.d("layout_show", "layout_show was true");
            actionBar_LIN.clearAnimation();
            actionBar_LIN.setVisibility(View.GONE);
            download_IV.clearAnimation();
            download_IV.setVisibility(View.GONE);
            layout_show = false;
            i = 1;
        }

        if(!layout_show && i==0) {
            Log.d("layout_show", "layout_show was false");
            actionBar_LIN.setVisibility(View.VISIBLE);
            actionBar_LIN.clearAnimation();
            actionBar_LIN.startAnimation(popup_ctl_layout_emerge);

            download_IV.setVisibility(View.VISIBLE);
            download_IV.clearAnimation();
            download_IV.startAnimation(popup_download_layout_emerge);

            layout_show = true;
        }
    }

    /**---------------------------------------------------------------------------
     콜백메소드 ==> 애니메이션 -- 미사용
     ---------------------------------------------------------------------------*/
    Animation.AnimationListener myAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}

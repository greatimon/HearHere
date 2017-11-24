package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class v_profile extends Activity {

    final int CONFIRM_SUBTRACT = 1000;
    final int FORCE_TO_OUT = 3000;

    ImageView profile_background_IV, profile_IV, report_IV;
    TextView email_TV, nickName_TV, my_Fans_TV, my_BJ_TV;
    Button add_FAN_btn, subtract_FAN_btn;
    LinearLayout red_card_LIN;

    String clicked_user_no;
    String my_user_no;
    String request_from;
    String profile_img_fileName;
    String Email;
    String nickName;
    String my_Fans;
    String my_BJ;
    String am_i_host_orNot;
    String broadCast_no;

    Handler handler;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_profile);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
//                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(true);

        profile_background_IV = (ImageView)findViewById(R.id.profile_background_img);
        profile_IV = (ImageView)findViewById(R.id.profile_img);
        email_TV = (TextView)findViewById(R.id.email);
        nickName_TV = (TextView)findViewById(R.id.nickName);
        my_Fans_TV = (TextView)findViewById(R.id.my_Fans);
        my_BJ_TV = (TextView)findViewById(R.id.my_BJ);
        add_FAN_btn = (Button)findViewById(R.id.add_FAN);
        subtract_FAN_btn = (Button)findViewById(R.id.subtract_FAN);
        report_IV = (ImageView)findViewById(R.id.report);
        red_card_LIN = (LinearLayout)findViewById(R.id.red_card_layout);

        Intent intent = getIntent();
        clicked_user_no = intent.getExtras().getString("CLICKED_USER_NO");
        my_user_no = intent.getExtras().getString("MY_USER_NO");
        request_from = intent.getExtras().getString("REQUEST_FROM");

        // 회원 정보 가져오기
        // 팬인지 아닌지 확인하기
        try {
            String before_split = new AsyncTask_get_drawer_header_info().execute(clicked_user_no, "profile").get();
            get_user_info(before_split, "onCreate");

            // 본인 프로필이 아니라면
            if(!clicked_user_no.equals(my_user_no)) {
                String check_FAN_orNot = check_FAN_orNot(clicked_user_no, my_user_no);
                if(check_FAN_orNot.equals("true")) {
                    add_FAN_btn.setVisibility(View.GONE);
                    subtract_FAN_btn.setVisibility(View.VISIBLE);
                }
                if(check_FAN_orNot.equals("false")) {
                    add_FAN_btn.setVisibility(View.VISIBLE);
                    subtract_FAN_btn.setVisibility(View.GONE);
                }
            }
            // 본인 프로필이라면
            if(clicked_user_no.equals(my_user_no)) {
                add_FAN_btn.setVisibility(View.GONE);
                subtract_FAN_btn.setVisibility(View.GONE);
            }

        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}

        // 'f_my_fans' OR 'f_my_BJ' 으로부터 온 요청일 때, 버튼 아예 안 보이게(너무 중복되어 버튼이 노출됨)
        // report 버튼도 안 보이게(리스트에서 일괄적으로 악의성 신고를 할 수가 있음)
        if(request_from.equals("f_my_fans") || request_from.equals("f_my_BJ")) {
            add_FAN_btn.setVisibility(View.GONE);
            subtract_FAN_btn.setVisibility(View.GONE);
            report_IV.setVisibility(View.GONE);
            red_card_LIN.setVisibility(View.GONE);
        }

        // a_noti, a_search 에서 온 요청일 때
        // report 버튼 안 보이게, 팬 기능 버튼은 보이게
        if(request_from.equals("a_noti") || request_from.equals("a_search")) {
            report_IV.setVisibility(View.GONE);
            red_card_LIN.setVisibility(View.GONE);
        }

        // a_room 에서 온 요청일 때
        if(request_from.equals("a_room")) {
            am_i_host_orNot = intent.getExtras().getString("HOST_ORNOT");
            broadCast_no = intent.getExtras().getString("BROADCAST_NO");
            Log.d("강퇴버튼", "get intent_ am_i_host_orNot: " + am_i_host_orNot);
            Log.d("강퇴버튼", "get intent_ broadCast_no: " + broadCast_no);
            Log.d("강퇴버튼", "get intent_ clicked_user_no: " + clicked_user_no);
            Log.d("강퇴버튼", "get intent_ my_user_no: " + my_user_no);

            // * 강퇴 버튼이 보여야 하는 조건
            // 1. 방송을 듣고 있는 사람이어야함
            // 2. BJ한테만 보여야함, but 본인 프로필에서는 보일필요 없음

            // 조건1 먼저 체크
            in_broadCast_orNot(clicked_user_no);
        }

        /** 프로필 사진 클릭 이벤트 리스너 등록 */ //
        profile_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_img_intent = new Intent(v_profile.this, a_original_profile_img.class);
                profile_img_intent.putExtra("USER_NICKNAME", nickName);
                profile_img_intent.putExtra("USER_IMG_FILENAME", profile_img_fileName);
                startActivity(profile_img_intent);
            }
        });

        /** 핸들러 등록 */
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 조건1. 아직 방에 있는 사람일 때
                if(msg.what == 0) {
                    //  조건2. BJ한테만 보여야함, but 본인 프로필에서는 보일필요 없음
                    if(am_i_host_orNot.equals("true") && !clicked_user_no.equals(my_user_no)) {
                        Log.d("강퇴버튼", "강퇴버튼을 보여줘!");
                        red_card_LIN.setVisibility(View.VISIBLE);
                    }
                    else {
                        red_card_LIN.setVisibility(View.GONE);
                    }
                    handler.removeMessages(0);
                }
                // 이 방송에 있지 않음
                if(msg.what == 1) {
                    Log.d("강퇴버튼", "해당 방송에 있지 않아, 버튼 안 보임");
                    handler.removeMessages(1);
                    red_card_LIN.setVisibility(View.GONE);
                }
                // Exception error
                if(msg.what == 2) {
                    Log.d("강퇴버튼", "통신중 Exception error로, 버튼 안 보임");
                    handler.removeMessages(2);
                    red_card_LIN.setVisibility(View.GONE);
                }
                // retrofit 통신실패
                if(msg.what == 3) {
                    Log.d("강퇴버튼", "Retrofit onFailure로, 버튼 안 보임");
                    handler.removeMessages(3);
                    red_card_LIN.setVisibility(View.GONE);
                }
            }
        };
    }


    /**---------------------------------------------------------------------------
     메소드 ==> get_user_info -- 유저정보 가져와서 뷰에 적용하기
     ---------------------------------------------------------------------------*/
    public void get_user_info(String before_split, String request_from) {
        Log.d("profile", "before_split: "+ before_split);
        String[] after_split = before_split.split("&");

        nickName = after_split[0];
        profile_img_fileName = after_split[1];
//        join_path = after_split[2];
        Email = after_split[3];
        String[] after_split_Email = Email.split("@");
        my_Fans = after_split[4];
        my_BJ = after_split[5];
//        fan_board_content = after_split[6];

        email_TV.setText(after_split_Email[0]);
        nickName_TV.setText(nickName);
        my_Fans_TV.setText(my_Fans);
        my_BJ_TV.setText(my_BJ);

        // 이미지 파일 이름이 'none'일 떄
        if(profile_img_fileName.equals("none")) {
            Glide
                .with(this)
//                .load(intent.getData())
                .load(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(profile_IV);
            Glide
                .with(this)
//                .load(intent.getData())
                .load(R.drawable.headphone2)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new BlurTransformation(this, 4))
                .into(profile_background_IV);
        }

        // 이미지 파일이 있을 때
        if(!profile_img_fileName.equals("none")) {

            // 카카오톡|페이스북 프사 URL일 때
            if(profile_img_fileName.contains("http")) {
                Glide
                    .with(this)
                    .load(profile_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profile_IV);

                Glide
                    .with(this)
                    .load(profile_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new BlurTransformation(this, 4))
                    .into(profile_background_IV);
            }

            // 카카오톡|페이스북 프사 URL이 아닐 때
            if(!profile_img_fileName.contains("http")) {
                // 웹서버로의 이미지 url로 이미지뷰에 이미지 넣기
                Glide
                    .with(this)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + profile_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profile_IV);

                Glide
                    .with(this)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + profile_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new BlurTransformation(this, 4))
                    .into(profile_background_IV);
            }
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 신고하기
     ---------------------------------------------------------------------------*/
    public void report_clicked(View view) {
        Intent intent = new Intent(v_profile.this, v_report.class);
        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 팬 추가
     ---------------------------------------------------------------------------*/
    public void add_FAN(View view) {
        try {
            String add_FAN_result = new AsyncTask_add_FAN().execute(my_user_no, clicked_user_no).get();
            Log.d("fan", "add_FAN_result: " + add_FAN_result);
            String[] after_split_result = add_FAN_result.split("&");

            if(after_split_result[0].equals("success")) {
                // after_split_result[1] => 클릭한 대상의 fan_count
                my_Fans_TV.setText(after_split_result[1]);
                add_FAN_btn.setVisibility(View.GONE);
                subtract_FAN_btn.setVisibility(View.VISIBLE);

                // 노티피케이션 날리기
                AsyncTask_push_notification task_2 = new AsyncTask_push_notification();
                String temp = task_2.execute("fan", my_user_no, clicked_user_no).get();
                Log.d("MyFirebase", "result: " + temp);
            }
            if(after_split_result[0].equals("fail")) {
                Log.d("fan", "add_FAN_fail: " + add_FAN_result);
            }
            else {
                Log.d("fan", "add_FAN_result: " + add_FAN_result);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 팬 삭제 - 삭제 확인 다이얼로그 팝업
     ---------------------------------------------------------------------------*/
    public void subtract_FAN(View view) {
        Intent subtract_FAN_confirm_intent = new Intent(v_profile.this, v_subtract_FAN_confirm.class);
        subtract_FAN_confirm_intent.putExtra("REQUEST_FROM", "a_room");
        startActivityForResult(subtract_FAN_confirm_intent, CONFIRM_SUBTRACT);
    }

    /**---------------------------------------------------------------------------
     메소드 ==> about_FAN -- FAN 여부 확인하기
     ---------------------------------------------------------------------------*/
    public String check_FAN_orNot(String clicked_user_no, String my_user_no) throws ExecutionException, InterruptedException {

        String result = new AsyncTask_about_FAN().execute("fan_orNot", clicked_user_no, my_user_no).get();
        Log.d("fan", "result: " + result);

        if(!result.equals("fail")) {
            return result;
        }
        if(result.equals("fail")) {
            return "error";
        }
        return null;
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> onActivityResult
     ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CONFIRM_SUBTRACT && resultCode == RESULT_OK) {
            try {
                String subtract_FAN_result = new AsyncTask_subtract_FAN().execute(my_user_no, clicked_user_no).get();
                Log.d("fan", "subtract_FAN_result: " + subtract_FAN_result);
                String[] after_split_result = subtract_FAN_result.split("&");

                if (after_split_result[0].equals("success")) {
                    // after_split_result[1] => 클릭한 대상의 fan_count
                    my_Fans_TV.setText(after_split_result[1]);
                    add_FAN_btn.setVisibility(View.VISIBLE);
                    subtract_FAN_btn.setVisibility(View.GONE);
                }
                if(after_split_result[0].equals("fail")) {
                    Log.d("fan", "subtract_FAN_fail: " + subtract_FAN_result);
                }
                else {
                    Log.d("fan", "subtract_FAN_result: " + subtract_FAN_result);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CONFIRM_SUBTRACT && resultCode == RESULT_CANCELED) {
            // FAN 삭제 하지 않음
        }

        // 강퇴 다이얼로그 결과
        if(requestCode == FORCE_TO_OUT && resultCode == RESULT_OK) {
            Log.d("강퇴", "onActivityResult_RESULT_OK");
            // 다이얼로그 닫기
            finish();

            // a_room 액티비티에 핸들러로 메시지 전달하여, 해당 유저 강퇴시키기기
            String red_card_user_no = data.getExtras().getString("user_no");
            String red_card_user_nickName = data.getExtras().getString("nickName");
            Log.d("강퇴", "red_card_user_no: " + red_card_user_no);
            Log.d("강퇴", "red_card_user_nickName: " + red_card_user_nickName);

            Message msg = a_room.force_to_out_handler.obtainMessage();
            msg.what = 0;
            msg.obj = red_card_user_nickName;
            msg.arg1 = Integer.parseInt(red_card_user_no);
            a_room.force_to_out_handler.sendMessage(msg);
        }
        if(requestCode == FORCE_TO_OUT && resultCode == RESULT_CANCELED) {
            Log.d("강퇴", "onActivityResult_RESULT_CANCELED");
            // 아무것도 하지 않음
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 강퇴시키기 -- 방장만 가능
     ---------------------------------------------------------------------------*/
    public void You_Get_Out(View view) {
        Intent red_card_intent = new Intent(v_profile.this, v_red_card_confirm.class);
        red_card_intent.putExtra("REQUEST_FROM", "v_show_guests");
        red_card_intent.putExtra("NICKNAME", nickName);
        red_card_intent.putExtra("USER_NO", clicked_user_no);
        startActivityForResult(red_card_intent, FORCE_TO_OUT);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 현재 방송에 참여하고 있는지 아닌지 확인하기
     ---------------------------------------------------------------------------*/
    public void in_broadCast_orNot(String user_no) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<ResponseBody> result = retrofitService.in_broadCast_orNot(Static.IN_BROADCAST_ORNOT, user_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("강퇴버튼", "레트로핏 통신 결과: " + result);
                    if(result.equals(broadCast_no)) {
                        handler.sendEmptyMessage(0);
                        Log.d("강퇴버튼", "sendEmptyMessage(0)");
                    }
                    else {
                        handler.sendEmptyMessage(1);
                        Log.d("강퇴버튼", "sendEmptyMessage(1)");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                    Log.d("강퇴버튼", "sendEmptyMessage(2)");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
                handler.sendEmptyMessage(3);
                Log.d("강퇴버튼", "sendEmptyMessage(3)");
            }
        });
    }

}

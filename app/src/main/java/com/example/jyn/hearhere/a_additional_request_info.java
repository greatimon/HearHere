package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by JYN on 2017-07-25.
 */

public class a_additional_request_info extends Activity {

    EditText email_edit;
    String facebook_ID;
    String facebook_NAME;
    String facebook_USER_PROFILEIMAGEURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_additional_request_info);

        email_edit = (EditText)findViewById(R.id.input_email);

        // editText 밑줄 색 커스텀
        int color = Color.parseColor("#fbfbfb");
        email_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        Intent intent = getIntent();
        try {
            facebook_ID = intent.getExtras().getString("ID");
            facebook_NAME = intent.getExtras().getString("NAME");
            facebook_USER_PROFILEIMAGEURL = intent.getExtras().getString("USER_PROFILEIMAGEURL");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(a_additional_request_info.this, "이메일(필수정보) 입력을 위해 이동합니다.", Toast.LENGTH_SHORT).show();

        // 페이스북 로그아웃
        LoginManager.getInstance().logOut();
        Log.d("facebook", "페이스북 로그인 상태: " + a_main_before_login.isLogin());
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 이메일 정규식 -- 영문, 숫자 조합 + '@' + 뒤에 '.' 포함
     ---------------------------------------------------------------------------*/
    public static boolean email_check(String email) {
        String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Boolean b = email.matches(EMAIL_REGEX);
        return b;
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 이메일 회원가입
     ---------------------------------------------------------------------------*/
    public void join_clicked(View view) {

        String email_str = email_edit.getText().toString();

        // 각 폼 입력확인
        if(email_str.length()==0) {
            Toast.makeText(a_additional_request_info.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 정규식 확인
        if(!email_check(email_str)) {
            Toast.makeText(a_additional_request_info.this, "형식에 맞지 않는 이메일입니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 회원가입 정보 서버 전달
        /** method 1. */
//        try {
//            String result = new AsyncTask_insert_userInfo_email(a_additional_request_info.this).execute(email_str, facebook_ID, facebook_NAME, "facebook", facebook_USER_PROFILEIMAGEURL).get();
//            String[] after_split = result.split("&");
//
//            if(after_split[0].equals("success")) {
//                finish();
//                Toast.makeText(a_additional_request_info.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
//            }
//            else if(after_split[0].equals("overlap")) {
//                Toast.makeText(a_additional_request_info.this, "이미 사용중인 이메일입니다", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Log.d("AsyncTask_ins_userInfo", "result:" + result);
//                Toast.makeText(a_additional_request_info.this, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /** method 2. */
        try {
            String result = new AsyncTask_insert_userInfo_email(a_additional_request_info.this).execute(email_str, facebook_ID, facebook_NAME, "facebook", facebook_USER_PROFILEIMAGEURL).get();
            String[] after_split = result.split("&");
            Log.d("facebook", "session_check/result: " + result);
            Log.d("facebook", "session_check/회원가입 시도 결과: " + after_split[0]);

            // 회원가입 성공
            if(after_split[0].equals("success")) {
                Toast.makeText(a_additional_request_info.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                // 1. 자동 로그인 정보 쉐어드에 저장 -- facebook 로그인
                SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                Auto_login_edit.putString("login_path", "facebook").apply();

                // 2. 로그인하기
                Intent intent = new Intent(a_additional_request_info.this, a_main_after_login.class);
                intent.putExtra("email", email_str);
                intent.putExtra("user_no", after_split[1]);
                intent.putExtra("login_path", "facebook");
                startActivity(intent);
            }

            // 이메일 중복
            else if(after_split[0].equals("overlap")) {

                // 이미 가입되어 있는 사람 로그인
                if(after_split[1].equals("match")) {
                    // after_split[2] => user_no
                    Log.d("facebook", "match: " + after_split[1]);
                    Log.d("facebook", "user_no: " + after_split[2]);

                    // 1. 자동 로그인 정보 쉐어드에 저장 -- facebook 로그인
                    SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                    SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                    Auto_login_edit.putString("login_path", "facebook").apply();

                    // 2. 로그인하기
                    Intent intent = new Intent(a_additional_request_info.this, a_main_after_login.class);
                    intent.putExtra("email", email_str);
                    intent.putExtra("user_no", after_split[2]);
                    intent.putExtra("login_path", "facebook");
                    startActivity(intent);
                }

                // 본인이 아닌 다른 사람이 이메일 주소를 사용하고 있는 것이므로 로그인을 허가 하지 않고
                // 다른 로그인 수단이 필요함을 알린다 - 토스트
                if(after_split[1].equals("not_match")) {
                    Log.d("facebook", "not_match: " + after_split[1]);

                    Toast.makeText(a_additional_request_info.this, "이미 사용중인 이메일입니다", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Log.d("AsyncTask_ins_userInfo", "result:" + result);
                Toast.makeText(a_additional_request_info.this, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

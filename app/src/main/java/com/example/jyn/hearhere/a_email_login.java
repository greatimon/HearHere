package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class a_email_login extends Activity {

    EditText email_edit, pw_edit;
    AsyncTask_login task;
    String fcm_orNot;
    String fcm_user_no="";
    String fcm_broadCast_no="";
    String fcm_type="";
    String fcm_no="";

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_email_login);

        email_edit = (EditText)findViewById(R.id.input_email);
        pw_edit = (EditText)findViewById(R.id.input_pw);

        // editText 밑줄 색 커스텀
        int color = Color.parseColor("#fbfbfb");
        email_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pw_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        Intent intent = getIntent();
        fcm_orNot = intent.getExtras().getString("fcm_orNot");
        if(fcm_orNot.equals("true")) {
            fcm_user_no = intent.getExtras().getString("fcm_user_no");
            fcm_broadCast_no = intent.getExtras().getString("fcm_broadCast_no");
            fcm_type = intent.getExtras().getString("fcm_type");
            fcm_no = intent.getExtras().getString("fcm_no");
        }

    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 로그인
     ---------------------------------------------------------------------------*/
    public void login_clicked(View view) {
        /** 실제 코드!!!!!!!!!!!!!!!!!!!!! */
        String email_str = email_edit.getText().toString();
        String pw_str = pw_edit.getText().toString();

        // 각 폼 입력확인
        if(email_str.length()==0 || pw_str.length()==0) {
            Toast.makeText(a_email_login.this, "로그인 정보를 모두 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 정규식 확인
        if(!a_join.email_check(email_str)) {
            Toast.makeText(a_email_login.this, "이메일이 형식에 맞지않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 로그인시도 정보 서버 전달
        task = new AsyncTask_login(a_email_login.this);
        task.execute(email_str, pw_str, fcm_orNot, fcm_user_no, fcm_broadCast_no, fcm_type, fcm_no);

        /** 테스트 코드!!!!!!!!!!!!!!!!!!!!! -- 통신없이 이메일값만 넘겨주기*/
//        Intent intent = new Intent(getBaseContext(), a_main_after_login.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra("email", "greatimon@naver.com");
//        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 비밀번호 찾기
     ---------------------------------------------------------------------------*/
    public void find_pw_clicked(View view) {
        Intent intent = new Intent(getBaseContext(), a_find_pw.class);
        startActivity(intent);
    }














}
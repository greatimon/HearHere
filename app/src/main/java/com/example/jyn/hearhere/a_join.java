package com.example.jyn.hearhere;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class a_join extends Activity{

    EditText email_edit, pw_edit, pw2_edit, nickName_edit;
    AsyncTask_insert_userInfo_email task;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_join);

        email_edit = (EditText)findViewById(R.id.input_email);
        pw_edit = (EditText)findViewById(R.id.input_pw);
        pw2_edit = (EditText)findViewById(R.id.input_pw2);
        nickName_edit = (EditText)findViewById(R.id.input_nickName);

        // editText 밑줄 색 커스텀
        int color = Color.parseColor("#fbfbfb");
        email_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pw_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        pw2_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        nickName_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 이메일 회원가입
     ---------------------------------------------------------------------------*/
    public void join_clicked(View view) {

        String email_str = email_edit.getText().toString();
        String pw_str = pw_edit.getText().toString();
        String pw2_str = pw2_edit.getText().toString();
        String nickName_str = nickName_edit.getText().toString();

        // 각 폼 입력확인
        if(email_str.length()==0 || pw_str.length()==0 || pw2_str.length()==0 || nickName_str.length()==0) {
            Toast.makeText(a_join.this, "양식이 모두 작성되지 않았습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 정규식 확인
        if(!email_check(email_str)) {
            Toast.makeText(a_join.this, "이메일이 형식에 맞지않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 비밀번호 정규식 확인
        if(!pw_check(pw_str)) {
            Toast.makeText(a_join.this, "비밀번호가 형식에 맞지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 비밀번호 재입력 일치 확인
        if(!pw_str.equals(pw2_str)) {
            Toast.makeText(a_join.this, "비밀번호 재입력값이 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 닉네임 형식 확인
        if(!nickName_check(nickName_str)) {
            Toast.makeText(a_join.this, "닉네임이 형식에 맞지 않습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 회원가입 정보 서버 전달
//        task = new AsyncTask_insert_userInfo_email(a_join.this);
//        task.execute(email_str, pw2_str, nickName_str);

        // 회원가입 정보 서버 전달
        try {
            String result = new AsyncTask_insert_userInfo_email(a_join.this).execute(email_str, pw2_str, nickName_str, "email").get();
            String[] after_split = result.split("&");

            if(after_split[0].equals("success")) {
                finish();
                Toast.makeText(a_join.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
            }
            else if(after_split[0].equals("overlap")) {
                Toast.makeText(a_join.this, "이미 사용중인 이메일입니다", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("AsyncTask_ins_userInfo", "result:" + result);
                Toast.makeText(a_join.this, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


//        // AsyncTask result 값을 받는 또다른 방법
//        try {
//            String result = new AsyncTask_insert_userInfo_email(a_join.this).execute(email_str, pw2_str, nickName_str).get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


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
     메소드 ==> 비밀번호 정규식 -- 영문, 숫자, 특수문자 조합 / 8자리이상 16자이하
     ---------------------------------------------------------------------------*/
    public static boolean pw_check(String pw) {
        String PASSWORD_REGEX = "^(?=.*[a-zA-Z]+)(?=.*[0-9]+)(?=.*[!@#$%^&*?_~]+).{8,16}$";
        Boolean b = pw.matches(PASSWORD_REGEX);
        return b;
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 닉네임 정규식 -- 모든 문자열 / 2자리이상 20자이하
     ---------------------------------------------------------------------------*/
    public static boolean nickName_check(String nickName) {
        String NICKNAME_REGEX = "^[\\w\\Wㄱ-ㅎㅏ-ㅣ가-힣]{2,20}$";
        Boolean b = nickName.matches(NICKNAME_REGEX);
        return b;
    }
}

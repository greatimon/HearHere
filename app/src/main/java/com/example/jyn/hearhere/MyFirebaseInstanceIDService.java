package com.example.jyn.hearhere;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by JYN on 2017-07-28.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static String TAG = "MyFirebaseIIDService";
    String refreshedToken;
    String previousToken;
    String split_token;

    @Override
    public void onTokenRefresh() {
        // 앱 설치 시, 여기서 토큰을 자동으로 만들어 준다
        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // 생성한 토큰을 내 서버로 날려서 저장하기 위한 메소드 (서버로 날리는건 나중에)
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // AsyncTask로 웹 서버에 저장하기
        // 키: user_no
        // 밸류: token
        // 근데 user_no를 앱 설치시에 알수가 없으므로, 일단 쉐어드에 저장한다
        // 그리고 추후에 로그인 후 user_no를 받아오면 그때 웹서버에 저장한다

        // Token SharedPreference
        // 이전 저장 토큰값 가져오기
        SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
        SharedPreferences.Editor fireBase_token_edit = fireBase_token_shared.edit();
        previousToken = fireBase_token_shared.getString("token", "");

        // 발급받은 token이 존재하고, 쉐어드에 저장되어 있는 token 값이 "" 일때
        // ex> 앱을 최초 설치하여, 해당 앱이 token을 처음 발급 받았을 때
        if(refreshedToken != null && previousToken.equals("")) {
            // 새로 발급받은 토큰과, 토큰 변경이 있음(신규임)을 쉐어드에 저장한다
            fireBase_token_edit.putString("token", refreshedToken).apply();
            fireBase_token_edit.putString("token_modified", "true").apply();
            Log.d("MyFirebaseIIDService", "토큰 신규 발급");
        }
        // 발급받은 token이 존재하고, 쉐어드에 저장되어 있는 token 값이 ""이 아닐 때
        // 즉 이전에 token을 발급받은 이력이 있을 때
        if(refreshedToken != null && !previousToken.equals("")) {
            // 이전 토큰과 새로 발급받은 토큰이 같을 때
            // 토큰 변경이 없음을 쉐어드에 저장한다
            if(refreshedToken.equals(previousToken)) {
                fireBase_token_edit.putString("token_modified", "false").apply();
                Log.d("MyFirebaseIIDService", "토큰 변동 없음");
            }
            // 이전 토큰과 새로 발급받은 토큰이 다를 때
            // 새로 발급받은 토큰과, 토큰 변경이 있음을 쉐어드에 저장한다
            if(!refreshedToken.equals(previousToken)) {
                fireBase_token_edit.putString("token", refreshedToken).apply();
                fireBase_token_edit.putString("token_modified", "true").apply();
                Log.d("MyFirebaseIIDService", "토큰 변동 있음");
            }
        }
    }
}

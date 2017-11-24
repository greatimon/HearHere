package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class a_main_before_login extends Activity {

    TextView email_login_txt, join_txt, main_script_txt;
    LinearLayout kakao_login_layout, facebook_login_layout, background_img_parent_layout;
    AsyncTask_auto_login task_2;
    ImageView logo_IV, background_IV;

    String Auto_login_eamil;
    String Auto_login_pw;
    String Auto_login_path;
    String lifeCycle;


    String from = "false";
    String fcm_user_no="";
    String fcm_broadCast_no="";
    String fcm_type="";
    String fcm_no="";

    boolean kakao_already_joined_So_Auto_login = false;
    boolean facebook_already_joined_So_Auto_login = false;

    BackPressCloseHandler backPressCloseHandler;

    /** 페이스북 로그인 관련 */
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    /** 카카오톡 로그인 - 콜백 선언 */
    private SessionCallback callback;

    // 애니메이션
    Animation logo_slide_up;
    Animation main_script_emerge;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main_before_login);
        Log.d("생명주기", "a_main_before_onCreate");

        // 최근 실행중인 앱 리스트 타이틀의 타이틀, 앱아이콘, 타이틀 배경색을 바꿀 수 있는 코드
//        ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription("HearHere", null, Color.parseColor("#FFFFFF"));
//        a_main_before_login.this.setTaskDescription(tDesc);

//        // 쉐어드의 from_logout_clicked 값을 확인해서 true라면 초기화시키기
//        SharedPreferences from_logout_clicked = getSharedPreferences("from_logout_clicked", MODE_PRIVATE);
//        String from_logout_clicked_str = from_logout_clicked.getString("from_logout_clicked", "");
//        if(from_logout_clicked_str.equals("true")) {
//            SharedPreferences.Editor from_logout_clicked_edit = from_logout_clicked.edit();
//            from_logout_clicked_edit.putString("from_logout_clicked", "").apply();
//        }

        // 테스트 코드 -- 페이스북 강제 로그아웃
//        LoginManager.getInstance().logOut();

        /** 자동 로그인 정보 쉐어드 초기화 - 테스트용
         * 실제 시연할때는 주석처리 */
//        onShared_Initializing();



        /** 카카오톡 로그인 - 콜백 */
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        // 뷰 찾기
        kakao_login_layout = (LinearLayout)findViewById(R.id.kakao_login_layout);
        facebook_login_layout = (LinearLayout)findViewById(R.id.facebook_login_layout);
        background_img_parent_layout = (LinearLayout)findViewById(R.id.background_img_parent);
        email_login_txt = (TextView)findViewById(R.id.email_login);
        join_txt = (TextView)findViewById(R.id.join);
        logo_IV = (ImageView)findViewById(R.id.logo);
        main_script_txt = (TextView)findViewById(R.id.main_script);
        background_IV = (ImageView)findViewById(R.id.background_img);

        // setText
        String email_login_str = email_login_txt.getText().toString();
        String join_str = join_txt.getText().toString();
        email_login_txt.setText(Html.fromHtml("<u>" + email_login_str + "</u>"));
        join_txt.setText(Html.fromHtml("<u>" + join_str + "</u>"));

//        /** 디바이스 화면사이즈 가져오기 */
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//        Log.d("device", "width: " + width);
//        Log.d("device", "height: " + height);
//        if(width > 1080) {
//            width = 1080;
//        }
////        ViewGroup.LayoutParams params = background_img_parent_layout.getLayoutParams();
////        params.width = width;
////        params.height = height;
//
//        /** 배경화면 flow 조건 셋팅 */
//        Resources res = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.main_img);
//
////        int bitmapWidth = bitmap.getWidth();
////        int layoutHeight = background_img_parent_layout.getHeight();
////
////        ViewGroup.LayoutParams params_for_img = background_IV.getLayoutParams();
////        params_for_img.width = bitmapWidth;
////        params_for_img.height = layoutHeight;
//
//        ViewGroup.LayoutParams params = background_img_parent_layout.getLayoutParams();
//        params.width = bitmap.getWidth();
//        params.height = bitmap.getHeight();
//
//        ViewGroup.LayoutParams params_for_img = background_IV.getLayoutParams();
//        params_for_img.width = width;
//        params_for_img.height = height;
//
//        background_IV.setImageBitmap(bitmap);
//        background_IV.setScaleType(ImageView.ScaleType.MATRIX);

        /** 애니메이션 load */
        // 로고 올라가는 anim
        logo_slide_up = AnimationUtils.loadAnimation(this, R.anim.logo_slide_up);
        logo_slide_up.setAnimationListener(Logo_and_main_script_anim);
        // 로고위에 글자 올라가는 anim
        main_script_emerge = AnimationUtils.loadAnimation(this, R.anim.main_script_emerge);
        // 로고랑 로고위에 글자 일단은 안 보이게 설정 => 애니메이션 시작되면 VISIBLE로 바꿈
        logo_IV.setVisibility(View.GONE);
        main_script_txt.setVisibility(View.GONE);

        // 뒤로 두번 누르면 종료되게
        backPressCloseHandler = new BackPressCloseHandler(this);

        // 생명주기 확인
        lifeCycle = "onCreate";
        Log.d("생명주기", "onCreate_lifeCycle: " + lifeCycle);

        // fcm으로 부터 호출 된건지 확인
        Intent intent = getIntent();
        try {
            from = intent.getExtras().getString("fcm_orNot");
            fcm_user_no = intent.getExtras().getString("fcm_user_no");
            fcm_broadCast_no = intent.getExtras().getString("fcm_broadCast_no");
            fcm_type = intent.getExtras().getString("type");
            fcm_no = intent.getExtras().getString("fcm_no");
        } catch (Exception e) {
            Log.d("생명주기", "intent.getExtras().getString() Exception: " + e.getMessage());
            from = "false";
        }
        if(from == null) {
            from = "false";
            fcm_user_no = "none";
            fcm_broadCast_no = "none";
            fcm_type = "none";
            fcm_no = "none";
            Log.d("생명주기", "from 값 none으로 바꿈. null로 찍혔었음, 이상해..");
        }
        Log.d("생명주기", "a_main_before_login_onCreate_fcm: " + from);
        Log.d("생명주기", "a_main_before_login_onCreate_fcm_user_no: " + fcm_user_no);
        Log.d("생명주기", "a_main_before_login_onCreate_fcm_broadCast_no: " + fcm_broadCast_no);
        Log.d("생명주기", "a_main_before_login_onCreate_fcm_type: " + fcm_type);

        // fcm으로 부터 왔다면, SNS 로그인 확인해서 로그아웃
        if(from.equals("true")) {
            if(!Session.getCurrentSession().isClosed()) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        boolean session_check = Session.getCurrentSession().isClosed();
                        Log.d("kakao", "session_check_카카오톡 API 로그아웃: " + session_check);
                    }
                });
            }

            if(a_main_before_login.isLogin()) {
                LoginManager.getInstance().logOut();
                Log.d("facebook", "페이스북 로그인 상태: " + a_main_before_login.isLogin());
            }
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 오버라이드 -- onBackPressed
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        backPressCloseHandler.onBackPressed();
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onStart
     ---------------------------------------------------------------------------*/
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("생명주기", "onStart");
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onNewIntent
     ---------------------------------------------------------------------------*/
    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
        Log.d("생명주기", "a_main_before_onNewIntent");

        from = "none";
        try {
            from = intent.getExtras().getString("fcm_orNot");
            fcm_user_no = intent.getExtras().getString("fcm_user_no");
            fcm_broadCast_no = intent.getExtras().getString("fcm_broadCast_no");
            fcm_type = intent.getExtras().getString("type");
            fcm_no = intent.getExtras().getString("fcm_no");
        } catch (Exception e) {
            Log.d("생명주기", "intent.getExtras().getString() Exception: " + e.getMessage());
        }
        Log.d("생명주기", "a_main_before_login_onNewIntent_from: " + from);
        Log.d("생명주기", "a_main_before_login_onNewIntent_fcm_user_no: " + fcm_user_no);
        Log.d("생명주기", "a_main_before_login_onNewIntent_fcm_broadCast_no: " + fcm_broadCast_no);
        Log.d("생명주기", "a_main_before_login_onNewIntent_fcm_type: " + fcm_type);

        // a_main_after_login의 로그아웃으로 부터 왔을 때를 체크하기 위함
        if(from.equals("none")) {
            lifeCycle = "onNewIntent";
            Log.d("생명주기", "onNewIntent_lifeCycle: " + lifeCycle);

            boolean session_check = Session.getCurrentSession().isClosed();
            Log.d("kakao", "session_check_onNewIntent: " + session_check);

            if(a_main_before_login.isLogin()) {
                LoginManager.getInstance().logOut();
                Log.d("facebook", "페이스북 로그인 상태: " + a_main_before_login.isLogin());
            }
        }

        if(from.equals("true")) {
            lifeCycle = "onCreate"; // onResume을 속임! 실제 lifeCycle값은 'ini'
            Log.d("생명주기", "onNewIntent_lifeCycle: " + lifeCycle);
            if(!Session.getCurrentSession().isClosed()) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        boolean session_check = Session.getCurrentSession().isClosed();
                        Log.d("kakao", "session_check_카카오톡 API 로그아웃: " + session_check);
                    }
                });
            }

            if(a_main_before_login.isLogin()) {
                LoginManager.getInstance().logOut();
                Log.d("facebook", "페이스북 로그인 상태: " + a_main_before_login.isLogin());
            }
        }



    }

    /**---------------------------------------------------------------------------
     생명주기 ==> onResume
     ---------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("생명주기", "onResume_lifeCycle: " + lifeCycle);

        // 애니메이션 start
        logo_IV.clearAnimation();
        logo_IV.startAnimation(logo_slide_up);
        main_script_txt.clearAnimation();
        main_script_txt.startAnimation(main_script_emerge);


        // onCreate를 거쳐서 옴
        if(lifeCycle.equals("onCreate")) {
            // 이전 로그인 정보 쉐어드를 통해 불러오기
            // 자동로그인 저장 정보의 login_path가 email인지 확인하기
            SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
            Auto_login_path = Auto_login.getString("login_path", "");

            if(isNetWork()) {
                // 자동로그인 쉐어드에 등록된 계정이 email로 가입된 계정일 때
                if(Auto_login_path.equals("email")) {
                    Auto_login_eamil = Auto_login.getString("email", "");
                    Auto_login_pw = Auto_login.getString("pw", "");

                    if(!Auto_login_eamil.equals("") && !Auto_login_pw.equals("")) {
                        // 애니메이션 중단
                        stopAnimation();
                        task_2 = new AsyncTask_auto_login(a_main_before_login.this);
                        task_2.execute(Auto_login_eamil, Auto_login_pw, from, fcm_user_no, fcm_broadCast_no, fcm_type, fcm_no);
                        Log.d("AsyncTask_auto_login", "Auto_login_eamil: " + Auto_login_eamil);
                        Log.d("AsyncTask_auto_login", "Auto_login_pw: " + Auto_login_pw);
                        Log.d("AsyncTask_auto_login", "from: " + from);
                        Log.d("Auto_login", "Auto_login_email");
                    }
                }
                // 자동로그인 쉐어드에 등록된 계정이 kakao로 가입된 계정일 때
                if(Auto_login_path.equals("kakao")) {
                    kakao_login_layout.performClick();
                    kakao_already_joined_So_Auto_login = true;
                    Log.d("Auto_login", "Auto_login_kakao");
                }
                // 자동로그인 쉐어드에 등록된 계정이 facebook으로 가입된 계정일 때
                if(Auto_login_path.equals("facebook")) {
                    facebook_login_layout.performClick();
                    facebook_already_joined_So_Auto_login = true;
                    Log.d("Auto_login", "Auto_login_facebook");
                }
            }
            if(!isNetWork()) {
                Toast.makeText(a_main_before_login.this, "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_SHORT).show();
            }

        }

        // onNewIntent를 거쳐서 옴
        if(lifeCycle.equals("onNewIntent")) {
//            Toast.makeText(a_main_before_login.this, "로그아웃해서 옴!!!", Toast.LENGTH_SHORT).show();
        }

        boolean session_check = Session.getCurrentSession().isClosed();
        Log.d("kakao", "session_check_onResume: " + session_check);

        Log.d("kakao", "session_check_onResume/kakao_already_joined_So_Auto_login: " + kakao_already_joined_So_Auto_login);
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onPause
     ---------------------------------------------------------------------------*/
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("생명주기", "onPause");
        if(lifeCycle.equals("onNewIntent")) {
            lifeCycle = "ini";
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 이메일 로그인
     ---------------------------------------------------------------------------*/
    public void email_login_clicked(View view) {
        Intent intent = new Intent(getBaseContext(), a_email_login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        if(from.equals("true")) {
            intent.putExtra("fcm_orNot", "true");
            intent.putExtra("fcm_user_no", fcm_user_no);
            intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
            intent.putExtra("fcm_type", fcm_type);
            intent.putExtra("fcm_no", fcm_no);
            Log.d("생명주기", "이메일 로그인 액티비티 전달 인텐트_from: " + from);
            Log.d("생명주기", "이메일 로그인 액티비티 전달 인텐트_ser_no: " + fcm_user_no);
            Log.d("생명주기", "이메일 로그인 액티비티 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
            Log.d("생명주기", "이메일 로그인 액티비티 전달 인텐트_fcm_type: " + fcm_type);
        }
        if(!from.equals("true")) {
            intent.putExtra("fcm_orNot", "false");
        }
        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 회원가입
     ---------------------------------------------------------------------------*/
    public void join_clicked(View view) {
        Intent intent = new Intent(getBaseContext(), a_join.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 페이스북 로그인
     ---------------------------------------------------------------------------*/
    public void facebook_login_clicked(View view) {
        // 애니메이션 중단
        stopAnimation();
        // 페이스북 SDK 초기화
        FacebookSdk.sdkInitialize(getApplicationContext());
        // 페이스북 로그인 응답을 처리할 콜백 관리자
        callbackManager = CallbackManager.Factory.create();

        // email 정보 제공을 거절한 적이 있는지 확인
        // 제공 거절 유무에 따라 물어보는 logInWithReadPermissions이 달라짐
        SharedPreferences Facebook_doNot_ask_email = getSharedPreferences("facebook_doNot_ask_email", MODE_PRIVATE);
        String email_ask_orNot = Facebook_doNot_ask_email.getString("ask_orNot", "");

        if(!email_ask_orNot.equals("no")) {
            LoginManager.getInstance().logInWithReadPermissions(a_main_before_login.this,
                    Arrays.asList("public_profile","email"));
        }
        if(email_ask_orNot.equals("no")) {
            LoginManager.getInstance().logInWithReadPermissions(a_main_before_login.this,
                    Arrays.asList("public_profile"));
        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("facebook", "onSuccess");

//                // 쉐어드의 from_logout_clicked 값을 확인해서 true라면 초기화시키기
//                SharedPreferences from_logout_clicked = getSharedPreferences("from_logout_clicked", MODE_PRIVATE);
//                final String from_logout_clicked_str = from_logout_clicked.getString("from_logout_clicked", "");


                GraphRequest request;
                request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if(response.getError() != null) {

                        }

//                        else if(!from_logout_clicked_str.equals("true")) {
                        else {

                            Log.d("facebook", "user: " + user.toString());
                            Log.d("facebook", "AccessToken: " + loginResult.getAccessToken().getToken());
                            setResult(RESULT_OK);
                            // {"id":"1451352461623799","name":"Yongnam  Jeon","email":"timon11@naver.com","gender":"male"}

                            String id = "";                     // 기본제공
                            String name = "";                   // 기본제공
                            String gender = "";                 // 기본제공
                            String email = "none";              // 선택제공
                            String user_profileImageUrl = "";   // make

                            try {
                                id = user.getString("id");
                                name = user.getString("name").replace("  ", " ");
                                gender = user.getString("gender");

                                String endPoint_imageUrl = "http://graph.facebook.com/";
                                // 프로필 사진 type_value 종류 : large, normal, small, square
                                String imageSize_url = "/picture?type=large";
                                user_profileImageUrl = endPoint_imageUrl + id + imageSize_url;

                                // 제공된 정보에 이메일이 포함되어 있다면
                                if(user.has("email")) {
                                    email = user.getString("email");
                                }

                                Log.d("facebook", "id: "+ id);
                                Log.d("facebook", "name: "+ name);
                                Log.d("facebook", "gender: "+ gender);
                                Log.d("facebook", "email: "+ email);
                                Log.d("facebook", "user_profileImageUrl: "+ user_profileImageUrl);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /** 이메일 정보를 받아 왔다면 */
                            if(!email.equals("none")) {
                                // 페이스북 자동로그인을 거치지 않았었다면
                                if(!facebook_already_joined_So_Auto_login) {
                                    String[] after_split;
                                    try {
                                        String result = new AsyncTask_insert_userInfo_email(a_main_before_login.this).execute(email, id, name, "facebook", user_profileImageUrl).get();
                                        after_split = result.split("&");
                                        Log.d("facebook", "session_check/result: " + result);
                                        Log.d("facebook", "session_check/회원가입 시도 결과: " + after_split[0]);

                                        // 회원가입 성공
                                        if(after_split[0].equals("success")) {
                                            Toast.makeText(a_main_before_login.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                                            // 1. 자동 로그인 정보 쉐어드에 저장 -- facebook 로그인
                                            SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                                            SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                                            Auto_login_edit.putString("login_path", "facebook").apply();


                                            // 2. 로그인하기
                                            Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);

                                            intent.putExtra("email", email);
                                            intent.putExtra("user_no", after_split[1]);
                                            intent.putExtra("login_path", "facebook");
                                            if(from.equals("true")) {
                                                intent.putExtra("fcm_orNot", "true");
                                                intent.putExtra("fcm_user_no", fcm_user_no);
                                                intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                                intent.putExtra("type", fcm_type);
                                                intent.putExtra("fcm_no", fcm_no);
                                                Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_from: " + from);
                                                Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                                Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                                Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                            }
                                            if(!from.equals("true")) {
                                                intent.putExtra("fcm_orNot", "false");
                                            }
                                            startActivity(intent);
                                        }

                                        // 이미 가입되어 있는 사람 로그인 or 회원 가입 시도했으나 이메일이 중복
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
                                                Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                                                intent.putExtra("email", email);
                                                intent.putExtra("user_no", after_split[2]);
                                                intent.putExtra("login_path", "facebook");
                                                if(from.equals("true")) {
                                                    intent.putExtra("fcm_orNot", "true");
                                                    intent.putExtra("fcm_user_no", fcm_user_no);
                                                    intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                                    intent.putExtra("type", fcm_type);
                                                    intent.putExtra("fcm_no", fcm_no);
                                                    Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_from: " + from);
                                                    Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                                    Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                                    Log.d("생명주기", "페이스북 그냥 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                                }
                                                if(!from.equals("true")) {
                                                    intent.putExtra("fcm_orNot", "false");
                                                }
                                                startActivity(intent);
                                            }

                                            // 본인이 아닌 다른 사람이 이메일 주소를 사용하고 있는 것이므로 로그인을 허가 하지 않고
                                            // 다른 로그인 수단이 필요함을 알린다 - 토스트
                                            if(after_split[1].equals("not_match")) {
                                                Log.d("facebook", "not_match: " + after_split[1]);

                                                // 페이스북 로그아웃
                                                LoginManager.getInstance().logOut();
                                                Log.d("facebook", "페이스북 로그인 상태: " + isLogin());

                                                Toast.makeText(a_main_before_login.this, "이미 사용중인 이메일입니다\n다른 방법으로 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Log.d("AsyncTask_ins_userInfo", "result:" + result);
                                            Toast.makeText(a_main_before_login.this, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // 페이스북으로 자동로그인으로 로그인할 때
                                if(facebook_already_joined_So_Auto_login) {
                                    // 1. 자동 로그인 정보 쉐어드에 저장 -- facebook 로그인인 것만 알게, login_path만 올바르게 저장
                                    SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                                    SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                                    Auto_login_edit.putString("login_path", "facebook").apply();

                                    // 2. user_no 가져오기
                                    String user_no = null;
                                    try {
                                        user_no = new AsyncTask_get_user_no().execute("facebook", email, id).get();
                                        Log.d("AsyncTask_get_user_no", "user_no:" + user_no);
                                    } catch (InterruptedException | ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                    if(!user_no.equals("fail")) {
                                        // 3. 로그인하기
                                        Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                                        intent.putExtra("email", email);
                                        intent.putExtra("user_no", user_no);
                                        intent.putExtra("login_path", "facebook");
                                        if(from.equals("true")) {
                                            intent.putExtra("fcm_orNot", "true");
                                            intent.putExtra("fcm_user_no", fcm_user_no);
                                            intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                            intent.putExtra("type", fcm_type);
                                            intent.putExtra("fcm_no", fcm_no);
                                            Log.d("생명주기", "페이스북 자동 로그인 전달 인텐트_from: " + from);
                                            Log.d("생명주기", "페이스북 자동 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                            Log.d("생명주기", "페이스북 자동 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                            Log.d("생명주기", "페이스북 자동 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                        }
                                        if(!from.equals("true")) {
                                            intent.putExtra("fcm_orNot", "false");
                                        }
                                        startActivity(intent);
                                    }
                                    if(user_no.equals("fail")) {
                                        // 페이스북 로그아웃
                                        LoginManager.getInstance().logOut();
                                        Log.d("facebook", "페이스북 로그인 상태: " + isLogin());

                                        Toast.makeText(a_main_before_login.this, "이미 사용중인 이메일입니다\n다른 방법으로 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            /** 이메일 정보를 받아오지 못했다면 */
                            // 이메일을 추가로 받는 액티비티로 이동
                            if(email.equals("none")) {
                                // 쉐어드 프리프런스에 다음에는 email을 빼고 정보동의 여부를 확인하게끔 한다
                                SharedPreferences Facebook_doNot_ask_email = getSharedPreferences("facebook_doNot_ask_email", MODE_PRIVATE);
                                SharedPreferences.Editor Facebook_doNot_ask_email_edit = Facebook_doNot_ask_email.edit();
                                Facebook_doNot_ask_email_edit.putString("ask_orNot", "no").apply();

                                // 해당 페이스북의 ID를 가지고 있는 계정이 DB에 있는지 확인하여,
                                // 만약 있다면, 사용자에겐 이미 가입된 페이스북 계정임을 알리고, 해당 계정의 정보를 가져와서 로그인 처리한다
                                String result = "";
                                try {
                                    result = new AsyncTask_check_facebook_ID().execute(id).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                Log.d("facebook", "result: " + result);

                                // 가입한 이력이 있는 페이스북 계정, 로그인 처리
                                if(!result.equals("fail")) {
//                                    Toast.makeText(a_main_before_login.this, "기존에 가입한 계정으로 로그인합니다", Toast.LENGTH_SHORT).show();
                                    String[] after_split = result.split("&");

                                    String user_no = after_split[0];
                                    String user_email = after_split[1];

                                    // 1. 자동 로그인 정보 쉐어드에 저장 -- facebook 로그인
                                    SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                                    SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                                    Auto_login_edit.putString("login_path", "facebook").apply();

                                    // 2. 로그인하기
                                    Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                                    intent.putExtra("email", user_email);
                                    intent.putExtra("user_no", user_no);
                                    intent.putExtra("login_path", "facebook");
                                    if(from.equals("true")) {
                                        intent.putExtra("fcm_orNot", "true");
                                        intent.putExtra("fcm_user_no", fcm_user_no);
                                        intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                        intent.putExtra("type", fcm_type);
                                        intent.putExtra("fcm_no", fcm_no);
                                        Log.d("생명주기", "페이스북 추가이메일 로그인 전달 인텐트_from: " + from);
                                        Log.d("생명주기", "페이스북 추가이메일 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                        Log.d("생명주기", "페이스북 추가이메일 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                        Log.d("생명주기", "페이스북 추가이메일 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                    }
                                    if(!from.equals("true")) {
                                        intent.putExtra("fcm_orNot", "false");
                                    }
                                    startActivity(intent);
                                }

                                // 가입한 이력이 없는 페이스북 계정, 로그인 시도 - 필수 정보인 이메일 정보를 받기 위해 액티비티 이동
                                if(result.equals("fail")) {
                                    Intent intent = new Intent(a_main_before_login.this, a_additional_request_info.class);
                                    intent.putExtra("ID", id);
                                    intent.putExtra("NAME", name);
                                    intent.putExtra("USER_PROFILEIMAGEURL", user_profileImageUrl);
                                    startActivity(intent);
                                }
                            }
                        }

//                        // 한번 onSuccess를 들어온 이후에는 재로그인 시, 정상적으로 로그인할 수 있도록 from_logout_clicked 초기화
//                        if(from_logout_clicked_str.equals("true")) {
//                            SharedPreferences from_logout_clicked = getSharedPreferences("from_logout_clicked", MODE_PRIVATE);
//                            String from_logout_clicked_str = from_logout_clicked.getString("from_logout_clicked", "");
//                            if(from_logout_clicked_str.equals("true")) {
//                                SharedPreferences.Editor from_logout_clicked_edit = from_logout_clicked.edit();
//                                from_logout_clicked_edit.putString("from_logout_clicked", "").apply();
//                            }
//                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("facebook", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "onError: " + error.getLocalizedMessage());
            }
        });
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 페이스북 토큰 상태 확인
     ---------------------------------------------------------------------------*/
    public static boolean isLogin() {
        com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();
        Log.d("facebook", "json control(olg token): " + token);
        return token != null; // 로그인 구분
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 카카오톡 로그인
     ---------------------------------------------------------------------------*/
    public void kakao_login_clicked(View view) {
        // 애니메이션 중단
        stopAnimation();
        boolean session_check = Session.getCurrentSession().isClosed();
        Log.d("kakao", "session_check_beforeClicked_kakao_login: " + session_check);
        new Kakao_LoginControl(a_main_before_login.this).call();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 네트워크 상태 확인 -- 연결 가능한 상태인지 확인
     ---------------------------------------------------------------------------*/
    private Boolean isNetWork(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ((isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect)){
            return true;
        }else{
            return false;
        }
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> onActivityResult
     ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 카카오톡 간편로그인시 호출, 없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        // 페이스북 로그인 콜백매니저
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    /**---------------------------------------------------------------------------
     클래스 ==> 카카오톡 SessionCallback
     ---------------------------------------------------------------------------*/
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Log.d("kakao", "errorResult: "+message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if(result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    }
                    else {
                        // redirectMainActivity();
                        Log.d("kakao", "onFailure_ else");
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {}

                @Override
                public void onNotSignedUp() {}

                // 로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지URL 등을 리턴
                // 사용자 ID는 보안상의 문제로 제공하지 않으며, 일련번호는 제공함
                @Override
                public void onSuccess(UserProfile userProfile) {

                    Log.d("kakao", "UserProfile: " + userProfile.toString());
                    Log.d("kakao", "ID: " + userProfile.getId());
                    Log.d("kakao", "ServiceUserId: " + userProfile.getServiceUserId());
                    Log.d("kakao", "Email: " + userProfile.getEmail());
                    Log.d("kakao", "EmailVerified: " + userProfile.getEmailVerified());
                    Log.d("kakao", "NickName: " + userProfile.getNickname());
                    Log.d("kakao", "ImagePath: " + userProfile.getProfileImagePath());
                    Log.d("kakao", "ThumbnailImagePath: " + userProfile.getThumbnailImagePath());
                    Log.d("kakao", "UUID: " + userProfile.getUUID());
                    Log.d("kakao", "Properties: " + userProfile.getProperties());
                    Log.d("kakao", "RemainingGroupMsgCount: " + userProfile.getRemainingGroupMsgCount());
                    Log.d("kakao", "RemainingInviteCount(): " + userProfile.getRemainingInviteCount());
                    Log.d("kakao", "ServiceUserId: " + userProfile.getServiceUserId());

                    String kakao_ID = String.valueOf(userProfile.getId());
                    String kakao_imagePath = userProfile.getProfileImagePath();
                    if(kakao_imagePath.length()==0) {
                        kakao_imagePath = "none";
                    }
                    String kakao_email = userProfile.getEmail();
                    String[] split_email = kakao_email.split("@");
                    String email_id = split_email[0];
                    Log.d("kakao", "email_id: "+email_id);

                    // 회원가입 정보 서버 전달
                    // kakao_email: 이메일주소
                    // kakao_ID: 원래 비번 자리W
                    // email_id: 원래 닉네임 자리

                    // 카카오톡 자동 로그인을 거치지 않을 때
                    if(!kakao_already_joined_So_Auto_login) {
                        String[] after_split;
                        try {
                            String result = new AsyncTask_insert_userInfo_email(a_main_before_login.this).execute(kakao_email, kakao_ID, email_id, "kakao", kakao_imagePath).get();
                            after_split = result.split("&");
                            Log.d("kakao", "session_check/result: " + result);
                            Log.d("kakao", "session_check/회원가입 시도 결과: " + after_split[0]);

                            // 회원가입 성공
                            if(after_split[0].equals("success")) {
                                Toast.makeText(a_main_before_login.this, "가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                                // 2. 자동 로그인 정보 쉐어드에 저장 -- kakao 로그인
                                SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                                SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                                Auto_login_edit.putString("login_path", "kakao").apply();

                                // 3. 로그인하기
                                Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                                intent.putExtra("email", kakao_email);
                                intent.putExtra("user_no", after_split[1]);
                                intent.putExtra("login_path", "kakao");
                                if(from.equals("true")) {
                                    intent.putExtra("fcm_orNot", "true");
                                    intent.putExtra("fcm_user_no", fcm_user_no);
                                    intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                    intent.putExtra("type", fcm_type);
                                    intent.putExtra("fcm_no", fcm_no);
                                    Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_from: " + from);
                                    Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                    Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                    Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                }
                                if(!from.equals("true")) {
                                    intent.putExtra("fcm_orNot", "false");
                                }
                                startActivity(intent);
                            }

                            // 이미 가입되어 있는 사람 로그인 or 회원 가입 시도했으나 이메일이 중복
                            else if(after_split[0].equals("overlap")) {

                                boolean session_check = Session.getCurrentSession().isClosed();
                                Log.d("kakao", "session_check_onSuccess: " + session_check);

                                // 이미 가입되어 있는 사람 로그인
                                if(after_split[1].equals("match")) {
                                    // after_split[2] => user_no
                                    Log.d("kakao", "match: " + after_split[1]);
                                    Log.d("kakao", "user_no: " + after_split[2]);

                                    // 1. 자동 로그인 정보 쉐어드에 저장 -- kakao 로그인
                                    SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                                    SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                                    Auto_login_edit.putString("login_path", "kakao").apply();

                                    // 2. 로그인하기
                                    Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                                    intent.putExtra("email", kakao_email);
                                    intent.putExtra("user_no", after_split[2]);
                                    intent.putExtra("login_path", "kakao");
                                    if(from.equals("true")) {
                                        intent.putExtra("fcm_orNot", "true");
                                        intent.putExtra("fcm_user_no", fcm_user_no);
                                        intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                                        intent.putExtra("type", fcm_type);
                                        intent.putExtra("fcm_no", fcm_no);
                                        Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_from: " + from);
                                        Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                                        Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                                        Log.d("생명주기", "카카오톡 그냥 로그인 전달 인텐트_fcm_type: " + fcm_type);
                                    }
                                    if(!from.equals("true")) {
                                        intent.putExtra("fcm_orNot", "false");
                                    }
                                    startActivity(intent);
                                }

                                // 본인이 아닌 다른 사람이 이메일 주소를 사용하고 있는 것이므로 로그인을 허가 하지 않고
                                // 다른 로그인 수단이 필요함을 알린다 - 토스트
                                if(after_split[1].equals("not_match")) {
                                    Log.d("kakao", "not_match: " + after_split[1]);

                                    // 카카오톡 세션 로그아웃
                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
                                            boolean session_check = Session.getCurrentSession().isClosed();
                                            Log.d("kakao", "session_check_onCompleteLogout_사용중인 이메일 => 카카오톡 API 로그아웃: " + session_check);
                                        }
                                    });
                                    Toast.makeText(a_main_before_login.this, "이미 사용중인 이메일입니다\n다른 방법으로 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Log.d("AsyncTask_ins_userInfo", "result:" + result);
                                Toast.makeText(a_main_before_login.this, "예외발생: "+ result, Toast.LENGTH_SHORT).show();
                            }

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }

                    // 카카오톡으로 자동로그인으로 로그인할 때
                    if(kakao_already_joined_So_Auto_login) {
                        // 1. 자동 로그인 정보 쉐어드에 저장 -- kakao 로그인인 것만 알게, login_path만 올바르게 저장
                        SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
                        SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
                        Auto_login_edit.putString("login_path", "kakao").apply();

                        // 2. user_no 가져오기
                        String user_no = null;
                        try {
                            user_no = new AsyncTask_get_user_no().execute("kakao", kakao_email).get();
                            Log.d("AsyncTask_get_user_no", "user_no:" + user_no);
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }

                        // 3. 로그인하기
                        Intent intent = new Intent(a_main_before_login.this, a_main_after_login.class);
                        intent.putExtra("email", kakao_email);
                        intent.putExtra("user_no", user_no);
                        intent.putExtra("login_path", "kakao");
                        if(from.equals("true")) {
                            intent.putExtra("fcm_orNot", "true");
                            intent.putExtra("fcm_user_no", fcm_user_no);
                            intent.putExtra("fcm_broadCast_no", fcm_broadCast_no);
                            intent.putExtra("type", fcm_type);
                            intent.putExtra("fcm_no", fcm_no);
                            Log.d("생명주기", "카카오톡 자동 로그인 전달 인텐트_from: " + from);
                            Log.d("생명주기", "카카오톡 자동 로그인 전달 인텐트_ser_no: " + fcm_user_no);
                            Log.d("생명주기", "카카오톡 자동 로그인 전달 인텐트_broadCast_no: " + fcm_broadCast_no);
                            Log.d("생명주기", "카카오톡 자동 로그인 전달 인텐트_fcm_type: " + fcm_type);
                            startActivity(intent);
                        }
                        if(!from.equals("true")) {
                            intent.putExtra("fcm_orNot", "false");
                            startActivity(intent);
                        }
//                        startActivity(intent);
                    }
                }
            });
            Log.d("kakao", "session_check_onSessionOpened/kakao_already_joined_So_Auto_login: " + kakao_already_joined_So_Auto_login);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.d("kakao", "세션 연결 실패");
            Log.d("kakao", getKeyHash(a_main_before_login.this));
        }
    }

    /**---------------------------------------------------------------------------
     메소드 ==> 카카오톡 keyHash 추출
     ---------------------------------------------------------------------------*/
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (android.content.pm.Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("kakao", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    //쉐어드 초기화_ 테스트용
    public void onShared_Initializing() {
        /** 자동로그인 */
        SharedPreferences Auto_login = getSharedPreferences("Auto_login", MODE_PRIVATE);
        SharedPreferences.Editor Auto_login_edit = Auto_login.edit();
        Auto_login_edit.clear().apply();
//
//        /** 페이스북 email 정보 제공 거절 여부 */
//        SharedPreferences Facebook_doNot_ask_email = getSharedPreferences("facebook_doNot_ask_email", MODE_PRIVATE);
//        SharedPreferences.Editor Facebook_doNot_ask_email_edit = Facebook_doNot_ask_email.edit();
//        Facebook_doNot_ask_email_edit.clear().apply();

//        /** fireBase Token */
//        SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
//        SharedPreferences.Editor fireBase_token_edit = fireBase_token_shared.edit();
//        fireBase_token_edit.clear().apply();
    }

    /**---------------------------------------------------------------------------
     콜백메소드 ==> 로고 and 로고위에 글자 애니메이션
     ---------------------------------------------------------------------------*/
    Animation.AnimationListener Logo_and_main_script_anim = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            logo_IV.setVisibility(View.VISIBLE);
            main_script_txt.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };


    /**---------------------------------------------------------------------------
     메소드 ==> 애니메이션 중지
     ---------------------------------------------------------------------------*/
    public void stopAnimation() {
        logo_IV.setVisibility(View.VISIBLE);
        main_script_txt.setVisibility(View.VISIBLE);

        logo_IV.clearAnimation();
        if (canCancelAnimation()) {
            logo_IV.animate().cancel();
        }
        main_script_txt.clearAnimation();
        if (canCancelAnimation()) {
            main_script_txt.animate().cancel();
        }
    }
    public boolean canCancelAnimation() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }


}

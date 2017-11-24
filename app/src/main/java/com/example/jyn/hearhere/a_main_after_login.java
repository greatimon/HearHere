package com.example.jyn.hearhere;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class a_main_after_login extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    TextView user_ID_TV;
    ImageView user_profile_IV, profile_background_IV;
    ActionBarDrawerToggle toggle;

    TabLayout tabLayout;
    ViewPager viewPager;
    Adapter_view_pager_main adapter;

    String email="";
    String facebook="";
    String kakao="";
    String fcm_orNot="";
    String fcm_user_no;
    String fcm_broadCast_no;
    String fcm_no="";
    static String login_path="";
    static String user_no="";

    BackPressCloseHandler backPressCloseHandler;

    String refreshedToken;

    boolean have_unread_msg = false;

    String user_nickName;
    String user_img_fileName;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main_after_login);
        Log.d("생명주기", "a_main_after_onCreate");

        Intent intent = getIntent();

        if(isNetWork()) {
            // intent 정보 넘겨 받기

            email = intent.getExtras().getString("email");
            facebook = intent.getExtras().getString("facebook");
            kakao = intent.getExtras().getString("kakao");
            user_no = intent.getExtras().getString("user_no");
            login_path = intent.getExtras().getString("login_path");
            fcm_orNot = intent.getExtras().getString("fcm_orNot");

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setNavigationIcon(R.drawable.nav_menu);
            setSupportActionBar(toolbar);

            fab = (FloatingActionButton) findViewById(R.id.fab);

            // 쉐어드에 user_no 저장
            SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
            SharedPreferences.Editor fireBase_token_edit = fireBase_token_shared.edit();
            fireBase_token_edit.putString("user_no", user_no).apply();

            /**---------------------------------------------------------------------------
             액티비티 이동 ==> 방송 준비
             ---------------------------------------------------------------------------*/
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(a_main_after_login.this, "방송이벤트 달아야함", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), a_create_room.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("USER_NO", user_no);
                    startActivity(intent);
                }
            });

            // 네비게이션 드로어
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            // 네비게이션 드로어 아이템 중, '홈' 비활성화 시키기
            Menu menuNav = navigationView.getMenu();
            MenuItem nav_item_1 = menuNav.findItem(R.id.nav_item_home);
            nav_item_1.setEnabled(true);
            nav_item_1.setChecked(true);

            // 네비게이션 헤더
            View nav_header_view = navigationView.getHeaderView(0);
            user_ID_TV = (TextView) nav_header_view.findViewById(R.id.user_ID);
            user_profile_IV = (ImageView) nav_header_view.findViewById(R.id.user_profile_img);
            profile_background_IV = (ImageView) nav_header_view.findViewById(R.id.profile_background_img);
//        user_ID_TV.setText(email);

            /** 네비게이션 헤더 중, 프로필 사진 클릭 이벤트 리스너 등록 */
            user_profile_IV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profile_img_intent = new Intent(a_main_after_login.this, a_original_profile_img.class);
                    profile_img_intent.putExtra("USER_NICKNAME", user_nickName);
                    profile_img_intent.putExtra("USER_IMG_FILENAME", user_img_fileName);
                    startActivity(profile_img_intent);
                }
            });

            // 탭레이아웃, 뷰페이져
            tabLayout = (TabLayout) findViewById(R.id.tab);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            adapter = new Adapter_view_pager_main(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            // 뒤로 두번 누르면 종료되게
            backPressCloseHandler = new BackPressCloseHandler(this);

        }

        /** fireBase 토큰 확인 */
        check_fireBase_token();

        /** fcm 클릭해서 들어왔을 때 처리 */
        if(fcm_orNot.equals("true")) {

            fcm_no = intent.getExtras().getString("fcm_no");

            String fcm_type="";
            try {
                fcm_type = intent.getExtras().getString("type");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(fcm_type.equals("live")) {
                fcm_user_no = intent.getExtras().getString("fcm_user_no");
                fcm_broadCast_no = intent.getExtras().getString("fcm_broadCast_no");

                AsyncTask_enter_room task = new AsyncTask_enter_room(a_main_after_login.this);
                task.execute(fcm_user_no, fcm_broadCast_no, "live");
            }

            if(fcm_type.equals("noti")) {
                String noti_fcm_user_no = intent.getExtras().getString("fcm_user_no");

                Intent noti_intent = new Intent(a_main_after_login.this, a_noti.class);
                noti_intent.putExtra("USER_NO", noti_fcm_user_no);
                startActivity(noti_intent);
            }

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

        }

        Log.d("kakao", "login_path: " + login_path);
        Log.d("facebook", "login_path: " + login_path);
        Log.d("MyFirebaseMsgService", "a_main_after_login_onCreate_fcm_orNot: " + fcm_orNot);
        Log.d("MyFirebaseMsgService", "a_main_after_login_onCreate_fcm_user_no: " + fcm_user_no);
        Log.d("MyFirebaseMsgService", "a_main_after_login_onCreate_fcm_broadCast_no: " + fcm_broadCast_no);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("생명주기", "a_main_after_onResume");

        if(!isNetWork()) {
            Toast.makeText(a_main_after_login.this, "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_SHORT).show();
        }

        if(isNetWork()) {
            // 네비게이션 드로어가 열려있다면 닫기
            if(drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }

            // 네비게이션 드로어 헤더 정보 가져오기
            try {
                String before_split = new AsyncTask_get_drawer_header_info().execute(user_no, "drawer").get();
                Log.d("drawer", "before_split: "+before_split);
                String[] after_split = before_split.split("&");

                user_nickName = after_split[0];
                user_img_fileName = after_split[1];
                Log.d("drawer", "user_nickName: "+ user_nickName);
                Log.d("drawer", "BJ_img_fileName: " + user_img_fileName);

                // 유저 닉네임 넣기
                user_ID_TV.setText(user_nickName);

                // 유저 프로필 사진 넣기
                // 유저 프로필 사진이 없을 때
                if(user_img_fileName.equals("none")) {
                    user_profile_IV.setImageResource(R.drawable.default_profile);
                    // 백그라운드 색 넣기, 닉네임 글자색 바꾸기
                    user_profile_IV.setBackgroundColor(Color.parseColor("#dff1f1"));
                    user_ID_TV.setBackgroundColor(Color.parseColor("#eef8f7"));
                    user_ID_TV.setTextColor(Color.parseColor("#525859"));
                }
                // 유저 프로필 사진이 있을 때
                if(!user_img_fileName.equals("none")) {

                    // 카카오톡|페이스북 프사 URL일 때
                    if(user_img_fileName.contains("http")) {
                        Glide
                                .with(this)
                                .load(user_img_fileName)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .bitmapTransform(new CropCircleTransformation(this))
                                .into(user_profile_IV);
                        Glide
                                .with(this)
                                .load(user_img_fileName)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .bitmapTransform(new BlurTransformation(this))
                                .into(profile_background_IV);
                    }

                    // 카카오톡|페이스북 프사 URL이 아닐 때
                    if(!user_img_fileName.contains("http")) {
                        // 웹서버로의 이미지 url로 이미지뷰에 이미지 넣기
                        Glide
                                .with(this)
                                .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .bitmapTransform(new CropCircleTransformation(this))
                                .into(user_profile_IV);
                        Glide
                                .with(this)
                                .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .bitmapTransform(new BlurTransformation(this))
                                .into(profile_background_IV);
                    }


                    // 백그라운드 색 지우기, 닉네임 글자색 바꾸기
                    user_profile_IV.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    user_ID_TV.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    user_ID_TV.setTextColor(Color.parseColor("#FFFFFF"));
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // 뷰페이저 첫번째 아이템에 포커스 주기
            viewPager.setCurrentItem(0);
        }

//        // 쉐어드의 from_logout_clicked 값을 확인해서 true라면 초기화시키기
//        SharedPreferences from_logout_clicked = getSharedPreferences("from_logout_clicked", MODE_PRIVATE);
//        String from_logout_clicked_str = from_logout_clicked.getString("from_logout_clicked", "");
//        if(from_logout_clicked_str.equals("true")) {
//            SharedPreferences.Editor from_logout_clicked_edit = from_logout_clicked.edit();
//            from_logout_clicked_edit.putString("from_logout_clicked", "").apply();
//        }

        // 읽지 않은 fcm 메시지가 있는지 확인하여, 인플레이팅 되는 메뉴 아이템 레이아웃을 다르게 한다
        try {
            String check_unRead_fcm = new Asynctask_check_unRead_fcm().execute(user_no).get();
            Log.d("check_unRead_fcm", "check_unRead_fcm_result: " + check_unRead_fcm);

            if(check_unRead_fcm.equals("true")) {
                have_unread_msg = true;
            }
            if(check_unRead_fcm.equals("false")) {
                have_unread_msg = false;
            }

            invalidateOptionsMenu();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("A_Check_onPause", "a_main_after_login");
        // 네비게이션 드로어가 열려있다면 닫기
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**---------------------------------------------------------------------------
     네비게이션 드로어 ==> 메뉴 아이템 인플레이트 - VER.1 : 메뉴생성 최초에만 호출되는 콜백메소드
     ---------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        if(have_unread_msg) {
//            getMenuInflater().inflate(R.menu.main_menu_2, menu);
//        }
//        else if(!have_unread_msg) {
//            getMenuInflater().inflate(R.menu.main_menu, menu);
//        }
        return true;
    }


    /**---------------------------------------------------------------------------
     네비게이션 드로어 ==> 메뉴 아이템 인플레이트 - VER.2 : invalidateOptionsMenu() 호출시 콜백되는 메소드
     ---------------------------------------------------------------------------*/
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("navi_item", "onPrepareOptionsMenu");
        Log.d("navi_item", "onPrepareOptionsMenu_have_unread_msg: " + have_unread_msg);
        if(have_unread_msg) {
            getMenuInflater().inflate(R.menu.main_menu_2, menu);
            Log.d("navi_item", "main_menu_2");
        }
        else if(!have_unread_msg) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            Log.d("navi_item", "main_menu");
        }
        return true;
    }

    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 툴바 아이콘들 클릭이벤트
     ---------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("navi_item", "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_search) {
//            Toast.makeText(a_main_after_login.this, "검색하기 액티비티로 이동", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(a_main_after_login.this, a_search.class);
            intent.putExtra("USER_NO", user_no);
            startActivity(intent);
            return true;
        }

        else if (id == R.id.action_notification) {
//            Toast.makeText(a_main_after_login.this, "알림 액티비티로 이동", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(a_main_after_login.this, a_noti.class);
            intent.putExtra("USER_NO", user_no);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 / 액티비티 이동 ==> 소프트 키보드 백버튼 오버라이드 -- 드로어 열려 있으면 닫기, 아니면 원래 기능
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
//        else if(drawer.isDrawerOpen(GravityCompat.END)) {
//            drawer.closeDrawer(GravityCompat.END);
//        }
        else {
            backPressCloseHandler.onBackPressed();
        }

    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 네비게이션 드로어 아이템들 클릭 이벤트
     ---------------------------------------------------------------------------*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_item_home) {}
        // 프로필 액티비티
        else if(id == R.id.nav_item_Profile) {
            Intent intent = new Intent(getBaseContext(), a_profile.class);
            intent.putExtra("USER_NO", user_no);
            startActivity(intent);
        }

        // 알람설정 액티비티
        else if(id == R.id.nav_item_setting_noti) {
            Intent intent = new Intent(getBaseContext(), a_setting_noti.class);
            intent.putExtra("USER_NO", user_no);
            startActivity(intent);
        }

        // 로그아웃 다이얼로그
        else if(id == R.id.nav_item_logout) {
            drawer.closeDrawer(GravityCompat.START);
            logout_dialog();
        }

        // 공지사항 액티비티
        else if(id == R.id.nav_item_notice) {
            Intent intent = new Intent(a_main_after_login.this, a_notice.class);
            startActivity(intent);
        }

        // 이메일 발송 인텐트
        else if(id == R.id.nav_item_ask) {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+09:00"));
            String today_string = sdf.format(date);

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            Uri uri = Uri.parse("mailto:timon11@naver.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "문의합니다.");
            intent.putExtra(Intent.EXTRA_TEXT, "\n" + "문의내용: " + "\n\n" + "작성일: " + today_string);
            intent.setData(uri);
//            Log.i("메소드","("+category_string+")"+title_string);
//            Log.i("메소드",content_string+"\n\n"+"작성자: "+writer_string+"\n"+"작성일: "+writing_date_string);

            if(intent.resolveActivity(getPackageManager()) !=null) {
                startActivity(Intent.createChooser(intent, "고객문의"));
            }
        }

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        return false;
    }


    /**---------------------------------------------------------------------------
     테스트버튼 ==> 서버로부터 가져온 방송 채널 정보 Toast
     ---------------------------------------------------------------------------*/
    public void get_channel_list(View view) throws ExecutionException, InterruptedException {
        // 방송 채널 정보 가져오기
//        String get_channel_result = new AsyncTask_get_channel_list().execute().get();
//        Toast.makeText(a_main_after_login.this, get_channel_result, Toast.LENGTH_SHORT).show();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 다이얼로그 -- 로그아웃 여부 확인
     ---------------------------------------------------------------------------*/
    private void logout_dialog() {
        AlertDialog.Builder logout_dialog = new AlertDialog.Builder(this);

        logout_dialog
                .setMessage("로그아웃 하시겠습니까?")
                .setCancelable(true)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //'네'를 선택했을 때 실행되는 로직

                                // 카카오톡 세션 로그아웃
                                if(login_path.equals("kakao")) {
                                    UserManagement.requestLogout(new LogoutResponseCallback() {
                                        @Override
                                        public void onCompleteLogout() {
//                                            Toast.makeText(a_main_after_login.this, "카카오톡 로그아웃", Toast.LENGTH_SHORT).show();
                                            boolean session_check = Session.getCurrentSession().isClosed();
                                            Log.d("kakao", "session_check_카카오톡 API 로그아웃: " + session_check);
                                        }
                                    });
                                }

                                // 페이스북 로그아웃
                                if(login_path.equals("facebook")) {
                                    LoginManager.getInstance().logOut();
                                    Log.d("facebook", "페이스북 로그인 상태: " + a_main_before_login.isLogin());
                                }

                                // 로그아웃 버튼을 통해서 온것임을 쉐어드에 저장
                                SharedPreferences from_logout_clicked = getSharedPreferences("from_logout_clicked", MODE_PRIVATE);
                                SharedPreferences.Editor from_logout_clicked_edit = from_logout_clicked.edit();
                                from_logout_clicked_edit.putString("from_logout_clicked", "true").apply();

                                Intent intent = new Intent(a_main_after_login.this, a_main_before_login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //'아니오'를 선택했을 때 실행되는 로직

                            }
                        });
        AlertDialog alert = logout_dialog.create();
        alert.setTitle("로그아웃");
        alert.show();

        // 다이얼로그 Positive/Negative 버튼 텍스트 색상 변경
        Button buttonPositive = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(Color.parseColor("#FFB300"));
        Button buttonNegative = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(Color.parseColor("#FFB300"));
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onNewIntent - fcm으로 부터 호출될 때
     ---------------------------------------------------------------------------*/
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("생명주기", "a_main_after_onNewIntent");

        if(intent != null) {
            String fcm_type="";

            try {
                fcm_type = intent.getExtras().getString("type");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 생방송 알림 노티일 때
            if(fcm_type.equals("live")) {
                try {
                    fcm_user_no = intent.getExtras().getString("fcm_user_no");
                    fcm_broadCast_no = intent.getExtras().getString("fcm_broadCast_no");

//            fcm_user_no = MyFirebaseMessagingService.fcm_user_no;
//            fcm_broadCast_no = MyFirebaseMessagingService.broadCast_no;

                    String fcm_orNot = intent.getExtras().getString("fcm_orNot");
                    Log.d("MyFirebaseMsgService", "onNewIntent_fcm_user_no: " + fcm_user_no);
                    Log.d("MyFirebaseMsgService", "onNewIntent_broadCast_no: " + fcm_broadCast_no);
                    Log.d("MyFirebaseMsgService", "onNewIntent_live: " + "live");
                    Log.d("MyFirebaseMsgService", "onNewIntent_fcm_orNot: " + fcm_orNot);

                    AsyncTask_enter_room task = new AsyncTask_enter_room(a_main_after_login.this);
                    task.execute(fcm_user_no, fcm_broadCast_no, "live");
                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("MyFirebaseMsgService", "intent.getExtras().getString() Exception: " + e.getMessage());
                }
            }

            // fan 알림 노티일 때
            if(fcm_type.equals("noti")) {
                String noti_fcm_user_no = intent.getExtras().getString("fcm_user_no");

                Intent noti_intent = new Intent(a_main_after_login.this, a_noti.class);
                noti_intent.putExtra("USER_NO", noti_fcm_user_no);
                startActivity(noti_intent);
            }

            NotificationManager notificationManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        }

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
     메소드 ==> 토큰 확인
     ---------------------------------------------------------------------------*/
    public void check_fireBase_token() {
        // fireBase 토큰 확인
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("MyFirebaseIIDService", "a_main_ getToken(): " + refreshedToken);

        if(refreshedToken == null) {

            // fireBase 쉐어드 초기화
            SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
            SharedPreferences.Editor fireBase_token_edit = fireBase_token_shared.edit();
            fireBase_token_edit.clear().apply();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(4000);
                        register_firebase_token("was null", "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        if(refreshedToken != null) {
            register_firebase_token("normal", refreshedToken);
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 토큰 신규, 변경 여부 확인 및 웹서버 저장
     ---------------------------------------------------------------------------*/
    public void register_firebase_token(String type, String token) {
        // fireBase 토큰 확인
        String FCM_Token="";
        if(type.equals("was null")) {
            FCM_Token = FirebaseInstanceId.getInstance().getToken();
            Log.d("MyFirebaseIIDService", "a_main_ getToken(was null): " + FCM_Token);
        }
        if(type.equals("normal")) {
            FCM_Token = token;
            Log.d("MyFirebaseIIDService", "a_main_ getToken(was normal): " + FCM_Token);
        }

        // Shared에 저장되어 있는 token_modified, user_no값을 가져온다
        SharedPreferences fireBase_token_shared = getSharedPreferences("fireBase_token", MODE_PRIVATE);
        SharedPreferences.Editor fireBase_token_edit = fireBase_token_shared.edit();
        String token_modified = fireBase_token_shared.getString("token_modified", "");
        String token_user_no = fireBase_token_shared.getString("token_user_no", "");
        Log.d("MyFirebaseIIDService", "token_modified: " + token_modified);
        Log.d("MyFirebaseIIDService", "token_user_no: " + token_user_no);
        Log.d("MyFirebaseIIDService", "login_user_no: " + user_no);

        // 토큰이 변경(발급)됨 or user_no가 다르다면 - 웹서버에 user_no와 함께 저장
        if(token_modified.equals("true") || !token_user_no.equals(user_no)) {
            try {
                String register_token_result = new AsyncTask_register_token().execute(user_no, FCM_Token).get();
                Log.d("MyFirebaseIIDService", "register_token_result: " + register_token_result);

                // insert(혹은 update)가 성공했다면, token_modified을 다시 false로 초기화
                if(register_token_result.equals("success")) {
                    fireBase_token_edit.putString("token_modified", "false").apply();
                    Log.d("MyFirebaseIIDService", "토큰 등록 성공: token_modified false로 초기화");

                    // Shared에 user_no도 같이 저장한다
                    fireBase_token_edit.putString("token_user_no", user_no).apply();
                    Log.d("MyFirebaseIIDService", "token_user_no 저장: " + user_no);

                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // 토큰 변경 없음
        if(token_modified.equals("false") && token_user_no.equals(user_no)) {
            // 암것도 안함
            Log.d("MyFirebaseIIDService", "a_main_after_login: 토큰 변경 없음");
        }
    }


}
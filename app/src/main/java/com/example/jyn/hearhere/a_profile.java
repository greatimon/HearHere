package com.example.jyn.hearhere;

import android.Manifest;
import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class a_profile extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;
    Adapter_view_pager_profile adapter;

    RelativeLayout modi_profile_img_Rel;
    ImageView profile_IV, profile_background_IV, select_IV, modi_cancel_IV, back_IV, delete_profile_IV;
    TextView modify_profile_TV, modify_profile_complete_TV, nickName_TV, join_path_TV, email_address_TV, my_Fans_TV, my_BJ_TV;
    EditText modi_nickName_EDIT;

    PermissionListener permissionListener;

    int REQUEST_SELECT_METHOD_FOR_GET_PHOTO = 2000;
    int REQUEST_GET_PHOTO_FROM_ALBUM = 2003;
    static int REQUEST_TAKE_PHOTO = 2004;
    int REQUEST_CHECK_FACE = 2010;
    static int SELECT_TAKE_PHOTO = 2001;
    static int SELECT_PICK_FROM_ALBUM = 2002;

    String user_no;
    String photoPath_on_myPhone;
    boolean is_photoPath_on_myPhone;
    boolean delete_profile_img;
    String ori_nickName;
    String temp_nickName;

    String user_nickName;
    String user_img_fileName;
    String join_path;
    String user_email;
    String my_fan_count;
    String my_BJ_count;
    String fan_board_content;


    AsyncTask_sending_img task;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_profile);

        Intent intent = getIntent();
        user_no = intent.getExtras().getString("USER_NO");

        profile_background_IV = (ImageView)findViewById(R.id.profile_background_img);
        profile_IV = (ImageView)findViewById(R.id.profile_img);
        select_IV = (ImageView)findViewById(R.id.select_img);
        modify_profile_TV = (TextView)findViewById(R.id.modify_profile);
        modify_profile_complete_TV = (TextView)findViewById(R.id.modify_profile_complete);
        modi_profile_img_Rel = (RelativeLayout)findViewById(R.id.modi_profile_img_layout);
        nickName_TV = (TextView)findViewById(R.id.nickName);
        join_path_TV = (TextView)findViewById(R.id.join_path);
        email_address_TV = (TextView)findViewById(R.id.email_address);
        my_Fans_TV = (TextView)findViewById(R.id.my_Fans);
        my_BJ_TV = (TextView)findViewById(R.id.my_BJ);
        modi_nickName_EDIT = (EditText)findViewById(R.id.modi_nickName);
        modi_cancel_IV = (ImageView)findViewById(R.id.modi_cancel);
        back_IV = (ImageView)findViewById(R.id.back);
        delete_profile_IV = (ImageView)findViewById(R.id.delete_profile_img);


        // 탭레이아웃, 뷰페이저
        tabLayout = (TabLayout)findViewById(R.id.tab);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapter = new Adapter_view_pager_profile(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // 탭레이아웃, 클릭리스너
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabsFromPagerAdapter(adapter);
                tabLayout.setOnTabSelectedListener(a_profile.this);
            }
        });
//        viewPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)this);

        // 뷰페이져, 페이징 관련 리스너
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("viewPager", "onPageScrolled position: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("viewPager", "onPageSelected: " + position);
//                adapter.getItem(position);
                update_user_profile();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    Log.d("viewPager", "onPageScrollStateChanged: SCROLL_STATE_DRAGGING");
                }
                if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    Log.d("viewPager", "onPageScrollStateChanged: SCROLL_STATE_SETTLING");
                }
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    Log.d("viewPager", "onPageScrollStateChanged: SCROLL_STATE_IDLE");
                }
            }
        });


        // 이미지 셋팅_ 글라이드
        Glide.with(this).load(R.drawable.modi_photo3).bitmapTransform(new CropCircleTransformation(this)).into(select_IV);
        Glide.with(this).load(R.drawable.delete_profile_img).bitmapTransform(new CropCircleTransformation(this)).into(delete_profile_IV);

        // Visibility 처리
        modify_profile_TV.setVisibility(View.VISIBLE);
        modify_profile_complete_TV.setVisibility(View.GONE);

        profile_IV.setVisibility(View.VISIBLE);
        select_IV.setVisibility(View.GONE);
        delete_profile_IV.setVisibility(View.GONE);

        nickName_TV.setVisibility(View.VISIBLE);
        modi_nickName_EDIT.setVisibility(View.GONE);


        // 퍼미션 리스너(테드_ 라이브러리)
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(a_profile.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(a_profile.this, "권한 거부", Toast.LENGTH_SHORT).show();
            }
        };

        // 이미지 변경 클릭 비활성화
//        modi_profile_img_Rel.setClickable(false);

        // 서버로부터 회원 정보 가져오기
        update_user_profile();

        /** 프로필 사진 클릭 이벤트 리스너 등록 */ //
        profile_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_img_intent = new Intent(a_profile.this, a_original_profile_img.class);
                profile_img_intent.putExtra("USER_NICKNAME", user_nickName);
                profile_img_intent.putExtra("USER_IMG_FILENAME", user_img_fileName);
                startActivity(profile_img_intent);
            }
        });
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 서버로부터 회원 정보 가져오기
     ---------------------------------------------------------------------------*/
    public void update_user_profile() {
        Log.d("photo", "update_user_profile()");
        try {
            String before_split = new AsyncTask_get_drawer_header_info().execute(user_no, "profile").get();
            Log.d("photo", "AsyncTask_get_drawer_header_info_result: " + before_split);
            get_user_info(before_split, "onCreate");
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 회원 정보 가져오기
     ---------------------------------------------------------------------------*/
    public void get_user_info(String before_split, String request_from) {
        Log.d("profile", "before_split: "+ before_split);
        String[] after_split = before_split.split("&");

        user_nickName = after_split[0];
        user_img_fileName = after_split[1];
        join_path = after_split[2];
        user_email = after_split[3];
        my_fan_count = after_split[4];
        my_BJ_count = after_split[5];
        fan_board_content = after_split[6];

        if(request_from.equals("onCreate") || request_from.equals("modi_cancel")) {
            ori_nickName = user_nickName;
            temp_nickName = user_nickName;
        }
        nickName_TV.setText(user_nickName);
        if(join_path.equals("email")) {
            join_path_TV.setText("이메일");
        }
        if(join_path.equals("kakao")) {
            join_path_TV.setText("카카오톡");
        }
        if(join_path.equals("facebook")) {
            join_path_TV.setText("페이스북");
        }
        email_address_TV.setText(user_email);
        my_Fans_TV.setText(my_fan_count);
        my_BJ_TV.setText(my_BJ_count);
        modi_nickName_EDIT.setText(temp_nickName);

        // 이미지 파일 이름이 'none'일 떄
        if(user_img_fileName.equals("none")) {
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
                .bitmapTransform(new BlurTransformation(this))
                .into(profile_background_IV);
        }

        // 이미지 파일이 있을 때
        if(!user_img_fileName.equals("none")) {

            // 카카오톡|페이스북 프사 URL일 때
            if(user_img_fileName.contains("http")) {
                Glide
                        .with(this)
                        .load(user_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(profile_IV);

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
                        .into(profile_IV);

                Glide
                        .with(this)
                        .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new BlurTransformation(this))
                        .into(profile_background_IV);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("photo", "onResume");
//        // 서버로부터 회원 정보 가져오기
//        update_user_profile();

        // onActivityResult부터의 사진 정보가 있을 때, 계속 수정중 화면을 유지하게 함
        if(is_photoPath_on_myPhone) {
            select_IV.setVisibility(View.VISIBLE);
            delete_profile_IV.setVisibility(View.VISIBLE);
//            modi_profile_img_Rel.setClickable(true);

            modify_profile_TV.setVisibility(View.GONE);
            modify_profile_complete_TV.setVisibility(View.VISIBLE);

            back_IV.setVisibility(View.GONE);
            modi_cancel_IV.setVisibility(View.VISIBLE);

//            ori_nickName = nickName_TV.getText().toString();
            nickName_TV.setVisibility(View.GONE);
            modi_nickName_EDIT.setVisibility(View.VISIBLE);
            modi_nickName_EDIT.setText(temp_nickName);
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 수정하기
     ---------------------------------------------------------------------------*/
    public void modi_clicked(View view) {

        select_IV.setVisibility(View.VISIBLE);
        if(user_img_fileName.equals("none")) {
            delete_profile_IV.setVisibility(View.GONE);
        }
        if(!user_img_fileName.equals("none")) {
            delete_profile_IV.setVisibility(View.VISIBLE);
        }
//        modi_profile_img_Rel.setClickable(true);

        modify_profile_TV.setVisibility(View.GONE);
        modify_profile_complete_TV.setVisibility(View.VISIBLE);

        back_IV.setVisibility(View.GONE);
        modi_cancel_IV.setVisibility(View.VISIBLE);

        nickName_TV.setVisibility(View.GONE);
        modi_nickName_EDIT.setVisibility(View.VISIBLE);
        modi_nickName_EDIT.setText(ori_nickName);

    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 수정완료
     ---------------------------------------------------------------------------*/
    public void modi_complete_clicked(View view) {

        // 닉네임 형식 확인
        String modi_nickname = modi_nickName_EDIT.getText().toString();
        if(modi_nickname.length()<2 || modi_nickname.length()>15) {
            Toast.makeText(getBaseContext(), "닉네임은 2~15자리수 이내로 지어주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 변경된 닉네임(비교 안하고 그냥 무조건 변경함) 서버 전송
        try {
            String nickName_from_server = new AsyncTask_modi_nickName().execute(user_no, modi_nickname).get();
            nickName_TV.setText(nickName_from_server);
            ori_nickName = nickName_from_server;
        } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}

        // onActivityResult부터의 사진 정보가 있을 때
        if(is_photoPath_on_myPhone) {
            // 웹서버 업로드
            try {
                // 선택한 이미지의 기기상 절대 경로를 매개변수로 넣어줌
                String result = new AsyncTask_sending_img().execute(photoPath_on_myPhone, user_no, "profile").get();

                if(result.equals("fail")) {
                    Toast.makeText(getBaseContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                }
                if(result.equals("file does not exist!")) {
                    Toast.makeText(getBaseContext(), "file does not exist!", Toast.LENGTH_SHORT).show();
                }
                if(result.equals("MalformedURLException")) {
                    Toast.makeText(getBaseContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
                }
                if(result.equals("Got Exception")) {
                    Toast.makeText(getBaseContext(), "Got Exception", Toast.LENGTH_SHORT).show();
                }
                else {
                    user_img_fileName = result;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            is_photoPath_on_myPhone = false;
            Log.d("photo", "is_PhotoPath: "+ is_photoPath_on_myPhone);
        }

        // 프로필 사진을 삭제 했을 때
        if(delete_profile_img) {
            Log.d("photo", "사진 삭제 로직 들어옴~!!");
            // 'none' 값 서버 전송
            try {
                String result = new AsyncTask_sending_img().execute("none", user_no, "profile").get();
                Log.d("photo", "사진 삭제 result 값: " + result);
                if(result.equals("success")) {
                    user_img_fileName = "none";
                    Log.d("photo", "BJ_img_fileName: "+ user_img_fileName);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        select_IV.setVisibility(View.GONE);
        delete_profile_IV.setVisibility(View.GONE);
//        modi_profile_img_Rel.setClickable(false);

        modify_profile_TV.setVisibility(View.VISIBLE);
        modify_profile_complete_TV.setVisibility(View.GONE);

        back_IV.setVisibility(View.VISIBLE);
        modi_cancel_IV.setVisibility(View.GONE);

        nickName_TV.setVisibility(View.VISIBLE);
        nickName_TV.setText(modi_nickname);
        modi_nickName_EDIT.setVisibility(View.GONE);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 수정취소
     ---------------------------------------------------------------------------*/
    public void modi_cancel_clicked(View view) {

        Log.d("photo", "is_PhotoPath: "+ is_photoPath_on_myPhone);

        // onActivityResult부터의 사진 정보가 있더라도, 수정을 취소 했으므로 다시 서버로부터 원래 회원 정보를 가져온다
        // 사진을 삭제했더라도, 수정을 취소 했으므로 다시 서버로부터 원래 회원 정보를 가져온다
        if(is_photoPath_on_myPhone || delete_profile_img) {
            try {
                String before_split = new AsyncTask_get_drawer_header_info().execute(user_no, "profile").get();
                get_user_info(before_split, "modi_cancel");
            } catch (InterruptedException | ExecutionException e) {e.printStackTrace();}

            is_photoPath_on_myPhone = false;
        }

        select_IV.setVisibility(View.GONE);
        delete_profile_IV.setVisibility(View.GONE);
//        modi_profile_img_Rel.setClickable(false);

        modify_profile_TV.setVisibility(View.VISIBLE);
        modify_profile_complete_TV.setVisibility(View.GONE);

        back_IV.setVisibility(View.VISIBLE);
        modi_cancel_IV.setVisibility(View.GONE);

        nickName_TV.setVisibility(View.VISIBLE);
        nickName_TV.setText(ori_nickName);
        modi_nickName_EDIT.setVisibility(View.GONE);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> onBackPressed 오버라이드 -- 수정중일때는 수정취소 로직을 적용
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        if(modi_cancel_IV.getVisibility()==View.VISIBLE) {
            modi_cancel_IV.performClick();
        }
        else {
            super.onBackPressed();
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 퍼미션 체크
     ---------------------------------------------------------------------------*/
    public void permission_check() {
        // 퍼미션 확인(테드_ 라이브러리)
        new TedPermission(this)
                .setPermissionListener(permissionListener)
//                .setRationaleMessage("다음 작업을 허용하시겠습니까? 기기 사진, 미디어, 파일 액세스")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다")
                .setGotoSettingButton(true)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 프로필 사진 가져오기
     ---------------------------------------------------------------------------*/
    public void select_img_clicked(View view) {
        permission_check();

//        Intent get_photo_intent = new Intent(Intent.ACTION_PICK);
//        get_photo_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        get_photo_intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(get_photo_intent, REQUEST_GET_PHOTO_FROM_ALBUM);

        temp_nickName = modi_nickName_EDIT.getText().toString();

        Intent intent = new Intent(getBaseContext(), v_select_pick_img_mothod.class);
        startActivityForResult(intent, REQUEST_SELECT_METHOD_FOR_GET_PHOTO);
    }

    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 프로필 사진 삭제 -- 다이얼로그 팝업
     ---------------------------------------------------------------------------*/
    public void delete_profile_img_clicked(View view) {
        delete_item_confirm();
    }


    /**---------------------------------------------------------------------------
     다이얼로그 ==> 프로필 사진 삭제확인
     ---------------------------------------------------------------------------*/
    // 내가 쓴 글 삭제 확인 dialog
    public void delete_item_confirm() {

        AlertDialog.Builder delete_item_confirm = new AlertDialog.Builder(this);

        delete_item_confirm
                .setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //'네'를 선택했을 때 실행되는 로직
                                Glide
                                    .with(getBaseContext())
                                    .load(R.drawable.default_profile)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .bitmapTransform(new CropCircleTransformation(getBaseContext()))
                                    .into(profile_IV);
                                Glide
                                    .with(getBaseContext())
                                    .load(R.drawable.headphone2)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .bitmapTransform(new BlurTransformation(getBaseContext()))
                                    .into(profile_background_IV);
                                delete_profile_IV.setVisibility(View.GONE);
                                delete_profile_img = true;
                            }
                        })
                .setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //'아니오'를 선택했을 때 실행되는 로직
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = delete_item_confirm.create();
        alert.setTitle("프로필 사진 삭제");
        alert.show();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> onActivityResult -- 프로필 사진, 앨범에서 선택
     ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("photo", "onActivityResult");

        // 사진 가져오는 방법 선택 - 갤러리(앨범)
        if(requestCode==REQUEST_SELECT_METHOD_FOR_GET_PHOTO && resultCode==SELECT_PICK_FROM_ALBUM) {
            Intent get_photo_intent = new Intent(Intent.ACTION_PICK);
            get_photo_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            get_photo_intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(get_photo_intent, REQUEST_GET_PHOTO_FROM_ALBUM);
        }
        // 사진 가져오는 방법 선택 - 카메라(javaCameraView)
        if(requestCode==REQUEST_SELECT_METHOD_FOR_GET_PHOTO && resultCode==SELECT_TAKE_PHOTO) {
            Intent take_photo_intent = new Intent(a_profile.this, a_camera_opencv.class);
            startActivityForResult(take_photo_intent, REQUEST_TAKE_PHOTO);
        }


        // 앨범에서 사진 선택하여 이미지 절대경로를 가져와서, 얼굴인식 액티비티로 넘기기
        if(requestCode==REQUEST_GET_PHOTO_FROM_ALBUM && resultCode==RESULT_OK) {

            delete_profile_img = false;

            // 이미지의 절대경로 취득
            photoPath_on_myPhone = getPath(intent.getData());
            Log.d("photo", "photoPath_on_myPhone: "+ photoPath_on_myPhone);

            is_photoPath_on_myPhone = true;
            Log.d("photo", "is_PhotoPath: true");

            // 추가 코드
            Intent face_activity_intent = new Intent(a_profile.this, a_face_check.class);
            face_activity_intent.putExtra("photoPath", photoPath_on_myPhone);
            face_activity_intent.putExtra("request", "gallery");
            startActivityForResult(face_activity_intent, REQUEST_CHECK_FACE);
            // 추가 코드

            // 원래 코드
//            delete_profile_img = false;
//
//            // 이미지의 절대경로 취득
//            photoPath_on_myPhone = getPath(intent.getData());
//
//            is_photoPath_on_myPhone = true;
//            Log.d("photo", "is_PhotoPath: true");
//
//            // 웹서버로의 이미지 url로 이미지뷰에 이미지 넣기
//            Glide
//                .with(this)
//                .load(intent.getData())
////                .load(photoPath_on_myPhone)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .bitmapTransform(new CropCircleTransformation(this))
//                .into(profile_IV);
//
//            Glide
//                .with(this)
//                .load(intent.getData())
////                .load(photoPath_on_myPhone)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .bitmapTransform(new BlurTransformation(this))
//                .into(profile_background_IV);
//            Log.d("photo", "glide_intent.getData(): " + intent.getData());
//            Log.d("photo", "glide_getPath(intent.getData()): " + getPath(intent.getData()));
            // 원래 코드
        }

        // 얼굴인식이 완료되어 해당 사진을 선택 -- from gallery
        if(requestCode==REQUEST_CHECK_FACE && resultCode==RESULT_OK) {
            delete_profile_img = false;

            Log.d("photo", "photoPath_on_myPhone: "+ photoPath_on_myPhone);

            is_photoPath_on_myPhone = true;
            Log.d("photo", "is_PhotoPath: true");

            Glide
                    .with(this)
                    .load(photoPath_on_myPhone)
//                    .load(photoPath_on_myPhone)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(profile_IV);
            Glide
                    .with(this)
                    .load(photoPath_on_myPhone)
//                    .load(photoPath_on_myPhone)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new BlurTransformation(this))
                    .into(profile_background_IV);
        }
        // 해당 사진 선택을 취소 or 얼굴 인식 실패 -- from gallery
        if(requestCode==REQUEST_CHECK_FACE && resultCode==RESULT_CANCELED) {
            is_photoPath_on_myPhone = false;
        }



        // 얼굴인식이 완료되어 해당 사진을 선택 -- from camera
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode==REQUEST_TAKE_PHOTO) {

            String savePath = intent.getExtras().getString("savePath");
            photoPath_on_myPhone = savePath;

            delete_profile_img = false;

            Log.d("photo", "photoPath_on_myPhone: "+ savePath);

            is_photoPath_on_myPhone = true;
            Log.d("photo", "is_PhotoPath: true");

            Glide
                .with(this)
                .load(savePath)
//                    .load(photoPath_on_myPhone)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(profile_IV);
            Glide
                .with(this)
                .load(savePath)
//                    .load(photoPath_on_myPhone)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new BlurTransformation(this))
                .into(profile_background_IV);
        }
        // a_camera_opencv에서 되돌아옴(백키) -- from camera
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode==RESULT_CANCELED) {
            is_photoPath_on_myPhone = false;
        }


//        // 프로필 사진 변경 방법 - 앨범
//        if(requestCode==REQUEST_SELECT_METHOD_FOR_GET_PHOTO && resultCode==SELECT_PICK_FROM_ALBUM) {
//            Intent get_photo_intent = new Intent(Intent.ACTION_PICK);
//            get_photo_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//            get_photo_intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(get_photo_intent, REQUEST_GET_PHOTO_FROM_ALBUM);
//        }
//        // 프로필 사진 변경 방법 - 사진 촬영
//        if(requestCode==REQUEST_SELECT_METHOD_FOR_GET_PHOTO && resultCode==SELECT_TAKE_PHOTO) {
//
//        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 선택된 Uri의 사진 `절대경로`를 가져온다
     ---------------------------------------------------------------------------*/
    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 탭레이아웃의 탭 클릭관련 콜백
     ---------------------------------------------------------------------------*/
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.d("to_the_top_handler", "TabSelected: " + tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.d("to_the_top_handler", "TabUnselected: " + tab.getPosition());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.d("to_the_top_handler", "TabReselected: " + tab.getPosition());
        if(tab.getPosition() == 0) {
            f_cast.to_the_top_handler.sendEmptyMessage(0);
        }
    }
}

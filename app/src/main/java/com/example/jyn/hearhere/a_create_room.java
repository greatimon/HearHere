package com.example.jyn.hearhere;

import android.Manifest;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class a_create_room extends Activity {

    int save = 0;
    ImageView broadCast_img, delete_img;
    Switch choose_save_switch;
    TextView save_txt, title_length_txt, comment_length_txt;
    EditText title_edit, comment_edit;

    String user_no="";
    String previous_photoPath="none";

    AsyncTask_create_room task;

    PermissionListener permissionListener;
    int REQUEST_GET_PHOTO_FROM_ALBUM = 3000;
    int REQUEST_SELECT_PHOTO_FROM_PREVIEW = 3001;
    String photoPath_on_myPhone;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_create_room);

        // intent 정보 넘겨 받기
        Intent intent = getIntent();
        user_no = intent.getExtras().getString("USER_NO");

        save_txt = (TextView)findViewById(R.id.choose_save_txt);
        title_edit = (EditText)findViewById(R.id.title);
        comment_edit = (EditText)findViewById(R.id.comment);
        title_length_txt = (TextView)findViewById(R.id.title_length);
        comment_length_txt = (TextView)findViewById(R.id.comment_length);
        broadCast_img = (ImageView)findViewById(R.id.broadCast_img);
        choose_save_switch = (Switch)findViewById(R.id.choose_save_switch);
        delete_img = (ImageView)findViewById(R.id.delete_img);

        // editText 소프트 키보드 각각 설정
        title_edit.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        title_edit.setInputType(InputType.TYPE_CLASS_TEXT);
        comment_edit.setImeOptions(EditorInfo.IME_ACTION_UNSPECIFIED);

        // title_edit 실시간 길이 보여주기
        title_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tmp_length = title_edit.getText().length();
                title_length_txt.setText(String.valueOf(tmp_length));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // comment_edit 실시간 길이 보여주기
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tmp_length = comment_edit.getText().length();
                comment_length_txt.setText(String.valueOf(tmp_length));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

        // 스위치 리스너
        choose_save_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Toast.makeText(a_create_room.this, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                if(isChecked) {
                    save_txt.setTextColor(Color.parseColor("#FFA000"));
                }
                if(!isChecked) {
                    save_txt.setTextColor(Color.parseColor("#898989"));
                }
            }

        });

        // 디폴트 설정
        delete_img.setVisibility(View.GONE);
//        Glide
//            .with(this)
//            .load(R.drawable.headphone2)
//            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//            .into(broadCast_img);

        /******************************************************************************/
        permission_check();
        /******************************************************************************/
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 생방송 시작
     ---------------------------------------------------------------------------*/
    public void on_air_clicked(View view) {

        String title = title_edit.getText().toString();
        String comment = comment_edit.getText().toString();
        if(comment.length()==0) {
            comment = "none";
        }
        String save="";
        if(choose_save_switch.isChecked()) {
            save = "true";
        }
        if(!choose_save_switch.isChecked()) {
            save = "false";
        }

        // 방송 이름 입력확인
        if(title.length()<3) {
            Toast.makeText(a_create_room.this, "방송 이름을 3자 이상 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 이미지 업로드 처리 - 이후 방송 채널정보 서버 전달 task에 result값 넣어주기
         * */
        // 방송 이미지 정보 있을 때
        if(!previous_photoPath.equals("none")) {
            try {
                String result = new AsyncTask_sending_img().execute(previous_photoPath, user_no, "broadCast").get();
//                Toast.makeText(a_create_room.this, result, Toast.LENGTH_SHORT).show();

                // 방송 채널 정보 서버 전달
                task = new AsyncTask_create_room(a_create_room.this);
                task.execute(user_no, title, comment, save, result);

                // 방송알림 Push_Notification
                AsyncTask_push_notification task_2 = new AsyncTask_push_notification();
                String temp = task_2.execute("broadCast", user_no, title).get();
                Log.d("MyFirebase", "result: " + temp);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        // 방송 이미지 정보 없을 때
        else if(previous_photoPath.equals("none")) {

            // 방송 채널 정보 서버 전달
            task = new AsyncTask_create_room(a_create_room.this);
            task.execute(user_no, title, comment, save, "none");

            try {
                // 방송알림 Push_Notification
                AsyncTask_push_notification task_2 = new AsyncTask_push_notification();
                String temp = task_2.execute("broadCast", user_no, title).get();
                Log.d("MyFirebase", "result: " + temp);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
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
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                .check();
    }

    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송 백그라운드 이미지 변경
     ---------------------------------------------------------------------------*/
    public void modi_broadCast_img(View view) {
        permission_check();

        Intent get_photo_intent = new Intent(Intent.ACTION_PICK);
        get_photo_intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        get_photo_intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(get_photo_intent, REQUEST_GET_PHOTO_FROM_ALBUM);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송 백그라운드 이미지 삭제
     ---------------------------------------------------------------------------*/
    public void delete_img_Clicked(View view) {
        previous_photoPath="none";
        Glide
            .with(this)
            .load(R.drawable.modi_broadcast_img2)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .into(broadCast_img);
        delete_img.setVisibility(View.GONE);

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
     메소드 ==> onActivityResult -- 프로필 사진, 앨범에서 선택
     ---------------------------------------------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("photo", "onActivityResult");

        // 앨범에서 사진 선택하여 이미지 가져옴
        if(requestCode==REQUEST_GET_PHOTO_FROM_ALBUM && resultCode==RESULT_OK) {
            // 이미지의 절대경로 취득
            if(intent!=null) {
                photoPath_on_myPhone = getPath(intent.getData());
            }
            // 선택 이미지 프리뷰 액티비티 띄우기
            Intent preview_intent = new Intent(getBaseContext(), a_preview_broadcast_img.class);
            preview_intent.putExtra("preview_intent", photoPath_on_myPhone);
            preview_intent.putExtra("request_activity", "create_room");
            startActivityForResult(preview_intent, REQUEST_SELECT_PHOTO_FROM_PREVIEW);
        }

        // 이미지 프리뷰 액티비티로 부터의 결과값에 따른 처리
        if(requestCode==REQUEST_SELECT_PHOTO_FROM_PREVIEW && resultCode==RESULT_OK) {
            previous_photoPath = photoPath_on_myPhone;
            Glide
                .with(this)
                .load(photoPath_on_myPhone)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(broadCast_img);
            delete_img.setVisibility(View.VISIBLE);
        }

        if(requestCode==REQUEST_SELECT_PHOTO_FROM_PREVIEW && resultCode==RESULT_CANCELED) {
            if(!previous_photoPath.equals("none")) {
                Glide
                    .with(this)
                    .load(previous_photoPath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(broadCast_img);
                delete_img.setVisibility(View.VISIBLE);
            }
            if(previous_photoPath.equals("none")) {
                Glide
                    .with(this)
                    .load(R.drawable.modi_broadcast_img2)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(broadCast_img);
                    delete_img.setVisibility(View.GONE);
            }
        }


    }


}

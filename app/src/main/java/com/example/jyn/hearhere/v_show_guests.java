package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by JYN on 2017-07-27.
 */

public class v_show_guests extends Activity {

    ListView listView;
    TextView no_guest_TV, red_card_TV;
    ImageView red_card_IV, undo_red_mode_IV;
    Adapter_guest_llist adapter_guest;
    ArrayList<DataClass_guest_list> guest_aryLi;

    static final String TAG_JSON = "guest_list";

    String broadCast_no;
    String BJ_user_no;
    String get_guest_list;

    String clicked_user_no; // 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 user_no
    String position;        // 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 listview position
    String clicked_nicnName;// 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 nickName

    final int CONFIRM_SUBTRACT = 2000;
    final int FORCE_TO_OUT = 3000;

    static boolean force_to_out_mode = false;

    boolean through_onActivityresult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_show_guests);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(true);
        Log.d("강퇴", "onCreate");
        Log.d("강퇴", "force_to_out_mode: " + force_to_out_mode);
        force_to_out_mode = false;

        // Intent 정보 넘겨 받기
        Intent intent = getIntent();
        broadCast_no = intent.getExtras().getString("BROADCAST_NO");
        BJ_user_no = intent.getExtras().getString("USER_NO");
        Log.d("v_show_guests", "broadCast_no: "+ broadCast_no);
        Log.d("v_show_guests", "BJ_user_no: "+ BJ_user_no);

        listView = (ListView)findViewById(R.id.listView);
        no_guest_TV = (TextView)findViewById(R.id.no_guest);
        red_card_TV = (TextView)findViewById(R.id.red_card_tv);
        red_card_IV = (ImageView)findViewById(R.id.red_card_img);
        undo_red_mode_IV = (ImageView)findViewById(R.id.undo_red_mode);

        guest_aryLi = new ArrayList<DataClass_guest_list>();

        /** 리스트뷰의 버튼 클릭리스너 (Add/subtract FAN)*/
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clicked_user_info = v.getTag(R.string.user_no).toString();
                Log.d("listView_clicked", "clicked_user_info: " + clicked_user_info);

                String[] after_split = clicked_user_info.split("&");
                clicked_user_no = after_split[0];
                position = after_split[1];
                clicked_nicnName = after_split[2];

                switch(v.getId()) {
                    case R.id.add_FAN:
                        try {
                            String add_FAN_result = new AsyncTask_add_FAN().execute(BJ_user_no, clicked_user_no).get();
                            Log.d("fan", "add_FAN_result: " + add_FAN_result);
                            String[] after_split_result = add_FAN_result.split("&");

                            if(after_split_result[0].equals("success")) {
                                // 클릭한 대상의 버튼 상태 바꾸기
                                guest_aryLi.get(Integer.parseInt(position)).setFan_orNot("true");
                                adapter_guest.notifyDataSetChanged();
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
                        break;

                    case R.id.subtract_FAN:
                        Intent subtract_FAN_confirm_intent = new Intent(v_show_guests.this, v_subtract_FAN_confirm.class);
                        subtract_FAN_confirm_intent.putExtra("REQUEST_FROM", "v_show_guests");
                        subtract_FAN_confirm_intent.putExtra("NICKNAME", clicked_nicnName);
                        startActivityForResult(subtract_FAN_confirm_intent, CONFIRM_SUBTRACT);
                        break;

                    case R.id.force_to_out:
                        Log.d("강퇴", "force_to_out 클릭 ==================");
                        Log.d("강퇴", "clicked_user_no: " + clicked_user_no);
                        Log.d("강퇴", "position: " + position);
                        Log.d("강퇴", "clicked_nicnName: " + clicked_nicnName);

//                        Intent SST_intent = new Intent(v_show_guests.this, v_SpeechToText.class);
//                        SST_intent.putExtra("clicked_user_no", clicked_user_no);
//                        SST_intent.putExtra("clicked_nicnName", clicked_nicnName);
//                        startActivityForResult(SST_intent, FORCE_TO_OUT);

                        Intent red_card_intent = new Intent(v_show_guests.this, v_red_card_confirm.class);
                        red_card_intent.putExtra("REQUEST_FROM", "v_show_guests");
                        red_card_intent.putExtra("NICKNAME", clicked_nicnName);
                        red_card_intent.putExtra("USER_NO", clicked_user_no);
                        startActivityForResult(red_card_intent, FORCE_TO_OUT);
                        break;
                }
            }
        };

        adapter_guest = new Adapter_guest_llist(v_show_guests.this, guest_aryLi, mOnClickListener);
        adapter_guest.notifyDataSetChanged();
        listView.setAdapter(adapter_guest);

        // set Visibility
        red_card_TV.setVisibility(View.VISIBLE);
        red_card_IV.setVisibility(View.VISIBLE);
        undo_red_mode_IV.setVisibility(View.GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        through_onActivityresult = true;

        // 팬 삭제 다이얼로그 결과
        if (requestCode == CONFIRM_SUBTRACT && resultCode == RESULT_OK) {
            try {
                String subtract_FAN_result = new AsyncTask_subtract_FAN().execute(BJ_user_no, clicked_user_no).get();
                Log.d("fan", "subtract_FAN_result: " + subtract_FAN_result);
                String[] after_split_result = subtract_FAN_result.split("&");

                if (after_split_result[0].equals("success")) {
                    // 클릭한 대상의 버튼 상태 바꾸기
                    guest_aryLi.get(Integer.parseInt(position)).setFan_orNot("false");
                    adapter_guest.notifyDataSetChanged();

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
            // 해당 아이템 삭제하고, 리스트 갱신
            for(int i=guest_aryLi.size()-1; i>=0; i--) {
                // 원래 arryalist에서 내가 쓴글만 삭제
                if(guest_aryLi.get(i).getUser_no() == Integer.parseInt(clicked_user_no)) {
                    guest_aryLi.remove(i);
                    adapter_guest.notifyDataSetChanged();
                    break;
                }
            }

            if(adapter_guest.getCount() == 0) {
                no_guest_TV.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                red_card_TV.setVisibility(View.GONE);
                red_card_IV.setVisibility(View.GONE);
                undo_red_mode_IV.setVisibility(View.GONE);
            }
            if(adapter_guest.getCount() > 0) {
                no_guest_TV.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                red_card_TV.setVisibility(View.VISIBLE);
                red_card_IV.setVisibility(View.VISIBLE);
                undo_red_mode_IV.setVisibility(View.GONE);
            }

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("강퇴", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("강퇴", "onResume");
        // 방송 참여자 리스트뷰 작성
        if(!through_onActivityresult) {
            get_guest_list();
        }
        through_onActivityresult = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("강퇴", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("강퇴", "onDestroy");
    }

    /**---------------------------------------------------------------------------
     메소드 ==> 방송 참여자 리스트 가져오기 -- get_guest_list()
     ---------------------------------------------------------------------------*/
    public void get_guest_list() {
        try {
            // 서버로부터 guest_list 정보를 가져온다
            get_guest_list = new AsyncTask_get_guest_list().execute(broadCast_no, BJ_user_no).get();
            Log.d("AsyncTask_guest_list", "get_guest_list_result: " + get_guest_list);
            try {
                // 읽어온 문자열을 JSONObject화 한다
                JSONObject jObject_guest_info = new JSONObject(get_guest_list);
                Log.d("AsyncTask_guest_list", "jObject_guest_info.length(): " + jObject_guest_info.length());

                // 다시 JSONArray화 한다
                JSONArray jArray_guest_info = jObject_guest_info.getJSONArray(TAG_JSON);
                Log.d("AsyncTask_guest_list", "jArray_guest_info.length(): " + jArray_guest_info.length());

                // 서버로부터 받은 guest_info 데이터가 하나도 없을 때
                // 즉 참여자가 한명도 없을 때
                // BJ 본인은 무조건 있으므로 최소 인원은 1명
                if(jArray_guest_info.length()==1) {
                    listView.setVisibility(View.GONE);
                    no_guest_TV.setVisibility(View.VISIBLE);
                    guest_aryLi.clear();
                    red_card_TV.setVisibility(View.GONE);
                    red_card_IV.setVisibility(View.GONE);
                    undo_red_mode_IV.setVisibility(View.GONE);
                }
                // 참여자가 한명이상 있을 때
                else if(jArray_guest_info.length()>1) {
                    listView.setVisibility(View.VISIBLE);
                    no_guest_TV.setVisibility(View.GONE);
                    red_card_TV.setVisibility(View.VISIBLE);
                    red_card_IV.setVisibility(View.VISIBLE);
                    undo_red_mode_IV.setVisibility(View.GONE);
                    guest_aryLi.clear();

                    /**---------------------------------------------------------------------------
                     jArray_guest_info 담긴 JSONObject를 하나씩 가져와,
                     가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                     그 변수들을 토대로, 초기화한 guest_aryLi에
                     새로운 'DataClass_guest_list' 데이터 객체를 하나씩 생성하여 add 한다
                     ---------------------------------------------------------------------------*/
                    for(int i=0; i<jArray_guest_info.length(); i++) {
                        JSONObject jGroup_guest_info = jArray_guest_info.getJSONObject(i);

                        int user_no = jGroup_guest_info.getInt("user_no");
                        String User_img_file = jGroup_guest_info.getString("user_img_fileName");
                        String User_nicName = jGroup_guest_info.getString("user_nickName");
                        String Fan_orNot = jGroup_guest_info.getString("Fan_orNot");

                        // BJ본인은 참여자 리스트에서 제외
                        if(user_no == Integer.parseInt(BJ_user_no)) {
                            continue;
                        }

                        guest_aryLi.add(new DataClass_guest_list(
                                user_no,
                                User_img_file,
                                User_nicName,
                                Fan_orNot
                        ));
                    }
                    adapter_guest.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("AsyncTask_guest_list", "JSONException: " + e);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 강퇴모드
     ---------------------------------------------------------------------------*/
    public void red_card_mode(View view) {
        red_card_TV.setVisibility(View.GONE);
        red_card_IV.setVisibility(View.GONE);
        undo_red_mode_IV.setVisibility(View.VISIBLE);
        force_to_out_mode = true;
        adapter_guest.notifyDataSetChanged();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 강퇴모드 취소
     ---------------------------------------------------------------------------*/
    public void undo_red_mode(View view) {
        red_card_TV.setVisibility(View.VISIBLE);
        red_card_IV.setVisibility(View.VISIBLE);
        undo_red_mode_IV.setVisibility(View.GONE);
        force_to_out_mode = false;
        adapter_guest.notifyDataSetChanged();
    }
}

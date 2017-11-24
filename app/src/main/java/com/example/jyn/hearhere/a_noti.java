package com.example.jyn.hearhere;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class a_noti extends Activity {

    String user_no;
    String get_noti_info_result;
    final String TAG_JSON = "noti_info";

    ListView listView;
    Adapter_noti_list adapter_noti_list;
    ArrayList<DataClass_noti> noti_aryLi;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_noti);

        Intent intent = getIntent();
        user_no = intent.getExtras().getString("USER_NO");
        Log.d("a_noti", "user_no: "+ user_no);

        // 뷰 ID 참조
        listView = (ListView)findViewById(R.id.listView);

        // 리스트뷰 관련, 객체 생성
        noti_aryLi = new ArrayList<>();
        adapter_noti_list = new Adapter_noti_list(a_noti.this, noti_aryLi);

        // 리스트뷰 어댑터 적용
        listView.setAdapter(adapter_noti_list);
        adapter_noti_list.notifyDataSetChanged();

        /** 리스트뷰 아이템 클릭 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 방송 타입의 알림일 때
                if(noti_aryLi.get(position).getNoti_type().equals("broadCast")) {

                    boolean broadCast_end_orNot = noti_aryLi.get(position).isBroadCast_onAir();

                    // 생방송중인 방송일 때
                    if(!broadCast_end_orNot) {
                        Log.d("a_noti", "생방송중인 방송 알림 클릭_position: " + position);

                        int BroadCast_no = noti_aryLi.get(position).getBroadCast_no();

                        AsyncTask_enter_room task = new AsyncTask_enter_room(a_noti.this);
                        task.execute(user_no, String.valueOf(BroadCast_no), "live");
                    }
                    // 종료된 방송일 때
                    if(broadCast_end_orNot) {
                        Log.d("a_noti", "종료된 방송 알림 클릭_position: " + position);

                        int BroadCast_no = noti_aryLi.get(position).getBroadCast_no();

                        AsyncTask_enter_room task = new AsyncTask_enter_room(a_noti.this);
                        task.execute(user_no, String.valueOf(BroadCast_no), "cast");
                    }
                }
                // FAN 타입의 알림일 때
                if(noti_aryLi.get(position).getNoti_type().equals("fan")) {
                    Log.d("a_noti", "Fan 타입의 알림 클릭_position: " + position);

                    int noti_user_no = noti_aryLi.get(position).getNoti_user_no();

                    Intent v_profile_intent = new Intent(a_noti.this, v_profile.class);
                    v_profile_intent.putExtra("REQUEST_FROM", "a_noti");
                    v_profile_intent.putExtra("CLICKED_USER_NO", String.valueOf(noti_user_no));
                    v_profile_intent.putExtra("MY_USER_NO", user_no);
                    startActivity(v_profile_intent);
                }
            }
        });

    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 소프트 키보드 백버튼 오버라이드
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 소프트 키보드 백버튼
     ---------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onResume
     ---------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        get_noti_info();

        // 모든 notification 없애기
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> get_noti_info -- 알림 정보 가져오기
     ---------------------------------------------------------------------------*/
    public void get_noti_info() {
        try {
            get_noti_info_result = new AsyncTask_get_noti_info(a_noti.this).execute(user_no).get();
            Log.d("AsyncTask_get_noti_info", "A_Check_get_noti_info: " + get_noti_info_result);

            try {
                // 읽어온 문자열을 JSONObject화 한다
                JSONObject jObject_noti_info = new JSONObject(get_noti_info_result);
                // 다시 JSONArray화 한다
                JSONArray jArray_noti_info = jObject_noti_info.getJSONArray(TAG_JSON);

                // 리스트를 초기화한다
                noti_aryLi.clear();

                /**---------------------------------------------------------------------------
                 jArray_noti_info 담긴 JSONObject를 하나씩 가져와,
                 가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                 그 변수들을 토대로, 초기화한 noti_aryLi에
                 새로운 'DataClass_noti' 데이터 객체를 하나씩 생성한다.
                 ---------------------------------------------------------------------------*/
                for(int i=0; i<jArray_noti_info.length(); i++) {
                    JSONObject jGroup_noti_info = jArray_noti_info.getJSONObject(i);

                    String noti_type = jGroup_noti_info.getString("noti_type");
                    String timeStamp = jGroup_noti_info.getString("timeStamp");
                    int noti_user_no = jGroup_noti_info.getInt("noti_user_no");
                    String noti_user_nickName = jGroup_noti_info.getString("noti_user_nickName");
                    String noti_user_img_url = jGroup_noti_info.getString("noti_user_img_url");
                    String read_orNot = jGroup_noti_info.getString("read_orNot");
                    int broadCast_no = jGroup_noti_info.getInt("broadCast_no");
                    String no = jGroup_noti_info.getString("no");

                    String broadCast_title = jGroup_noti_info.getString("broadCast_title");
//                    String  broadCast_onAir = jGroup_noti_info.getString("broadCast_onAir");
                    if(broadCast_title.equals("null")) {
                        continue;
                    }

                    String broadCast_onAir = jGroup_noti_info.getString("broadCast_onAir");
                    if(broadCast_onAir.equals("")) {
                        broadCast_onAir = "false";
                    }

                    String broadCast_save_orNot = jGroup_noti_info.getString("broadCast_save_orNot");
//                    if(broadCast_save_orNot.equals("false")) {
//                        continue;
//                    }

                    Log.d("get_noti_info", "noti_type: " + noti_type);
                    Log.d("get_noti_info", "timeStamp: " + timeStamp);
                    Log.d("get_noti_info", "noti_user_no: " + noti_user_no);
                    Log.d("get_noti_info", "noti_user_nickName: " + noti_user_nickName);
                    Log.d("get_noti_info", "noti_user_img_url: " + noti_user_img_url);
                    Log.d("get_noti_info", "read_orNot: " + read_orNot);
                    Log.d("get_noti_info", "broadCast_no: " + broadCast_no);
                    Log.d("get_noti_info", "broadCast_title: " + broadCast_title);
                    Log.d("get_noti_info", "broadCast_onAir: " + broadCast_onAir);
                    Log.d("get_noti_info", "broadCast_save_orNot: " + broadCast_save_orNot);
                    Log.d("get_noti_info", "no: " + no);
                    Log.d("get_noti_info", "-------------------------------------------------");
                    Log.d("get_noti_info", "-------------------------------------------------");

                    noti_aryLi.add(new DataClass_noti(
                            noti_type,
                            Integer.parseInt(user_no),
                            timeStamp,
                            noti_user_no,
                            noti_user_nickName,
                            noti_user_img_url,
                            Boolean.valueOf(read_orNot),
                            broadCast_no,
                            broadCast_title,
                            Boolean.valueOf(broadCast_onAir),
                            no
                    ));
                }
                adapter_noti_list.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}

package com.example.jyn.hearhere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class f_cast extends Fragment {

    ListView listView;
    Adapter_room_list adapter_room_list;
    ArrayList<DataClass_room_list> roomList_aryLi;
    TextView comment_TV;
    Button create_broadCast_BTN;

    String get_cast_result;
    String requestFrom;
    static final String TAG_JSON = "cast_list";

    LayoutInflater inflater;
    ViewGroup container;

    AsyncTask_enter_room task;

    private static final String ARG_PARAM1 = "param1";
    static Handler to_the_top_handler;

    public f_cast() {
        // Required empty public constructor
    }

    public static f_cast newInstance(String requestFrom) {
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, requestFrom);

        f_cast fragment = new f_cast();
        fragment.setArguments(args);
        return fragment;
    }

    //SwipeRefreshLayout -  당겨서 새로고침
    private SwipeRefreshLayout layout;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreateView
     ---------------------------------------------------------------------------*/
    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("프래그먼트", "f_cast_onCreateView");

        // static 핸들러 객체 생성
        to_the_top_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0) {
                    if(listView != null && adapter_room_list != null && adapter_room_list.getCount() > 0) {
                        Log.d("to_the_top_handler", "handleMessage(0) received");
                        // 스크롤중이면, 스크롤 멈추고 맨 위로 포커스
//                        listView.smoothScrollBy(0, 0);
//                        listView.smoothScrollToPosition(0);
                        listView.smoothScrollToPositionFromTop(0, 0, 0);
//                        listView.setSelection(0);
                    }
                }
            }
        };

        // bundle 객체로 부터 요청 액티비티가 어디인지 전달 받음(intent와 같은 개념)
        requestFrom = getArguments().getString(ARG_PARAM1);

        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;

        roomList_aryLi = new ArrayList<>();

        // 프레그먼트 인플레이팅
//        View view = inflater.inflate(R.layout.f_cast, container, false);
        layout = (SwipeRefreshLayout) inflater.inflate(R.layout.f_cast, container, false);

        // 리스트뷰 어댑터 (아이템, 정보 arr 추가)
        adapter_room_list = new Adapter_room_list(getActivity(), R.layout.i_room_list, roomList_aryLi, requestFrom);

        // 뷰 ID 참조
        listView = (ListView)layout.findViewById(R.id.listView);
        comment_TV = (TextView) layout.findViewById(R.id.comment);
        create_broadCast_BTN = (Button)layout.findViewById(R.id.create_broadCast);

        // create_broadCast_BTN, SET 클릭 리스너
        create_broadCast_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), a_create_room.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra("USER_NO", a_main_after_login.user_no);
                startActivity(intent);
            }
        });

        // 리스트뷰 어댑터 적용
        listView.setAdapter(adapter_room_list);
        adapter_room_list.notifyDataSetChanged();

        // 리스트 클릭- a_room 액티비티로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int BroadCast_no = roomList_aryLi.get(position).getBroadCast_no();
                Log.d("listView_clicked", "BroadCast_no: "+String.valueOf(BroadCast_no));

                Go_cast(String.valueOf(BroadCast_no));
            }
        });

        // 새로고침 아이콘 색깔 변경
        layout.setColorSchemeColors(Color.parseColor("#FFB300"), Color.parseColor("#FF6F00"));
//        layout.setColorSchemeResources();
        //리스너 정의
        layout.setOnRefreshListener(sRefresh);
        layout.setEnabled(true);


        // 리스트뷰가 최상단에 있을때만 pull to refresh enable
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean top_of_listview = false;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0 && view.getChildAt(0) != null) {
                    Log.d("pullToRefresh", "view.getChildAt(0).getTop(): " + view.getChildAt(0).getTop());
                }

                top_of_listview = false;

//                if(firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 30){
                if(firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() <= 30){
                    top_of_listview = true;
                    Log.d("pullToRefresh", "top!!");
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                layout.setEnabled(false);
                Log.d("pullToRefresh", "view.getChildAt(0).getTop(): " + view.getChildAt(0).getTop());
                Log.d("pullToRefresh", "scrolled!!");

                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && top_of_listview) {
                    layout.setEnabled(true);
                }
            }
        });
        return layout;
    }


    /**---------------------------------------------------------------------------
     메소드 ==> pull to refresh 리스너 -- get_cast_list 실행
     ---------------------------------------------------------------------------*/
    private SwipeRefreshLayout.OnRefreshListener sRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            get_cast_list();
            if(requestFrom.equals("profile")) {
                ((a_profile)getActivity()).update_user_profile();
            }
        }
    };


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> a_room
     ---------------------------------------------------------------------------*/
    public void Go_cast(String BroadCast_no) {
        Log.d("A_Check_Go_cast", "Go_cast 메소드 들어옴");
        // 방송 채널 정보 서버 전달
        task = new AsyncTask_enter_room(getActivity());
        task.execute(a_main_after_login.user_no, BroadCast_no, "cast");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("A_Check_onResume", "f_cast");

        get_cast_list();
//        adapter_room_list.notifyDataSetChanged();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 캐스트 리스트 정보 가져오기 -- AsyncTask_get_cast_list
     ---------------------------------------------------------------------------*/
    public void get_cast_list() {

        try {
            if(requestFrom.equals("profile")) {
                get_cast_result = new AsyncTask_get_cast_list().execute("profile", a_main_after_login.user_no).get();
            }
            if(requestFrom.equals("main")) {
                get_cast_result = new AsyncTask_get_cast_list().execute("main").get();
            }
//            Toast.makeText(container.getContext(), get_cast_result, Toast.LENGTH_SHORT).show();
            Log.d("A_Check_get_cast_list", get_cast_result);

            try {
                // 읽어온 문자열을 JSONObject화 한다
                JSONObject jObject_channel_info = new JSONObject(get_cast_result);
                // 다시 JSONArray화 한다
                JSONArray jArray_channel_info = jObject_channel_info.getJSONArray(TAG_JSON);
                Log.d("방송개수", requestFrom + ": " + jArray_channel_info.length());

                // 리스트를 초기화한다
                roomList_aryLi.clear();

                /**---------------------------------------------------------------------------
                 jArray_channel_info 담긴 JSONObject를 하나씩 가져와,
                 가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                 그 변수들을 토대로, 초기화한 roomList_aryLi에
                 새로운 'DataClass_channel_info' 데이터 객체를 하나씩 생성한다.
                 ---------------------------------------------------------------------------*/
                for(int i=0; i<jArray_channel_info.length(); i++) {
                    JSONObject jGoup_channel_info = jArray_channel_info.getJSONObject(i);

                    int BJ_user_no = jGoup_channel_info.getInt("BJ_user_no");

                    // 코드 수정으로, 요청 액티비티가 profile 일때 알아서 내 방송만 반환함
//                    // 요청 액티비티가 profile 일때, 내 방송만 arr에 add 한다
//                    // 내 방송 아니면 continue
//                    if(requestFrom.equals("profile")) {
//                        if(BJ_user_no != Integer.parseInt(a_main_after_login.user_no)) {
//                            Log.d("profile", "NOT_MATCH_BJ_user_no: " + BJ_user_no);
//                            Log.d("profile", "NOT_MATCH_a_main_after_login.user_no: " + a_main_after_login.user_no);
//                            continue;
//                        }
//                        if(BJ_user_no == Integer.parseInt(a_main_after_login.user_no)) {
//                            Log.d("profile", "MATCH_BJ_user_no: " + BJ_user_no);
//                            Log.d("profile", "MATCH_a_main_after_login.user_no: " + a_main_after_login.user_no);
//                        }
//                    }

                    int broadCast_no = jGoup_channel_info.getInt("broadCast_no");
                    String BJ_nickName = jGoup_channel_info.getString("BJ_nickName");
                    String broadCast_title = jGoup_channel_info.getString("broadCast_title");
                    String welcome_word = jGoup_channel_info.getString("welcome_word");
                    String broadCast_img_fileName = jGoup_channel_info.getString("broadCast_img_fileName");
                    String broadCast_save_orNot = jGoup_channel_info.getString("broadCast_save_orNot");
                    int final_guest_count = jGoup_channel_info.getInt("final_guest_count");
                    int like_count = jGoup_channel_info.getInt("like_count");
                    int received_report_count = jGoup_channel_info.getInt("received_report_count");
                    String broadCast_start_time = jGoup_channel_info.getString("broadCast_start_time");
                    String broadCast_end_time = jGoup_channel_info.getString("broadCast_end_time");
                    String total_broadCast_time = jGoup_channel_info.getString("total_broadCast_time");
                    String broadCast_end_orNot =  jGoup_channel_info.getString("broadCast_end_orNot");
                    int total_broadCast_time_mil = jGoup_channel_info.getInt("total_broadCast_time_mil");

                    String temp = total_broadCast_time.substring(0,2);
                    if(temp.equals("00")) {
                        total_broadCast_time = total_broadCast_time.substring(3);
                    }
                    Log.d("substring", "total_broadCast_time: "+total_broadCast_time);

                    roomList_aryLi.add(0, new DataClass_room_list(
                            broadCast_no,
                            BJ_user_no,
                            BJ_nickName,
                            broadCast_title,
                            welcome_word,
                            broadCast_img_fileName,
                            broadCast_save_orNot,
                            final_guest_count,
                            like_count,
                            received_report_count,
                            broadCast_start_time,
                            broadCast_end_time,
                            total_broadCast_time,
                            broadCast_end_orNot,
                            total_broadCast_time_mil
                    ));
                }
                adapter_room_list.notifyDataSetChanged();

                // 요청 액티비티가 profile 이고, roomList_aryLi에 add한 cast가 하나도 없을 때
                if(requestFrom.equals("profile")) {
                    if(roomList_aryLi.size()==0) {
                        listView.setVisibility(View.GONE);
                        comment_TV.setVisibility(View.VISIBLE);
                        create_broadCast_BTN.setVisibility(View.VISIBLE);
                    }
                    if(roomList_aryLi.size()>0) {
                        listView.setVisibility(View.VISIBLE);
                        comment_TV.setVisibility(View.GONE);
                        create_broadCast_BTN.setVisibility(View.GONE);
                    }
                }

                // 요청 액티비티가 main 일때
                if(requestFrom.equals("main")) {
                    listView.setVisibility(View.VISIBLE);
                    comment_TV.setVisibility(View.GONE);
                    create_broadCast_BTN.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Log.d("AsyncTask_channel_list", "JSONException: " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 새로고침 아이콘 없애기
        layout.setRefreshing(false);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        Log.d("프래그먼트", "onAttach");
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.d("프래그먼트", "onCreate");
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d("프래그먼트", "onActivityCreated");
//    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("프래그먼트", "onStart");
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("프래그먼트", "onPause");
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("프래그먼트", "onStop");
//    }
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.d("프래그먼트", "onDestroyView");
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("프래그먼트", "onDestroy");
//    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("프래그먼트", "onDetach");
        to_the_top_handler = null;
    }
}
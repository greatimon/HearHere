package com.example.jyn.hearhere;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class f_live extends Fragment {

    ListView listView;
    Adapter_room_list adapter_room_list;
    ArrayList<DataClass_room_list> roomList_aryLi;

    String get_channel_result;
    static final String TAG_JSON = "channel_list";

    LayoutInflater inflater;
    ViewGroup container;

    AsyncTask_enter_room task;
    Random random;

    // 리스트뷰 헤더 `뷰`
    View header, banner_img_2_alpha_V, divider_under_best_V;
    ImageView banner_img_1_IV, banner_img_2_IV, banner_img_2_1_IV;
    LinearLayout live_mark_Linlay, nickName_layout_Linlay;
    TextView title_TV, live_list_TV, nickName_TV;
    RelativeLayout default_live_Rel;

    int broadCast_no_listView_header;

    public f_live() {
        // Required empty public constructor
    }

    public static f_live newInstance() {
        Bundle args = new Bundle();

        f_live fragment = new f_live();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("A_Check_onAttach", "f_live");
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("A_Check_onCreate", "f_live");
    }

    //SwipeRefreshLayout -  당겨서 새로고침
    private SwipeRefreshLayout layout;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreateView
     ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("A_Check_onCreateView", "f_live");
        this.inflater = inflater;
        this.container = container;

        roomList_aryLi = new ArrayList<>();

        // 프래그먼트 인플레이팅
//        View view = inflater.inflate(R.layout.f_live, container, false);
        layout = (SwipeRefreshLayout) inflater.inflate(R.layout.f_live, container, false);

        // 리스트뷰 어댑터 (아이템, 정보 arr 추가)
        adapter_room_list = new Adapter_room_list(getActivity(), R.layout.i_room_list, roomList_aryLi, "main");

        // ListView header 인플레이팅
        header = inflater.inflate(R.layout.v_listview_header_live, null, false);
        // ListView header 안의 뷰 참조
        banner_img_1_IV = (ImageView)header.findViewById(R.id.banner_img_1);
        divider_under_best_V = (View)header.findViewById(R.id.divider_under_best);
        banner_img_2_IV = (ImageView)header.findViewById(R.id.banner_img_2);
        banner_img_2_alpha_V = (View)header.findViewById(R.id.banner_img_2_alpha);
        banner_img_2_1_IV = (ImageView)header.findViewById(R.id.banner_img_2_1);
        live_mark_Linlay = (LinearLayout)header.findViewById(R.id.live_mark);
        nickName_layout_Linlay = (LinearLayout)header.findViewById(R.id.nickName_layout);
        title_TV = (TextView)header.findViewById(R.id.title);
        nickName_TV = (TextView)header.findViewById(R.id.nickName);
        live_list_TV = (TextView)header.findViewById(R.id.live_list);
        default_live_Rel = (RelativeLayout)header.findViewById(R.id.default_live);

        // 뷰 ID 참조
        listView = (ListView)layout.findViewById(R.id.listView);

        // ListView에 header 추가
        listView.addHeaderView(header, null, false);

        // 리스트뷰 어댑터 적용
        listView.setAdapter(adapter_room_list);
        adapter_room_list.notifyDataSetChanged();

        // 리스트 클릭- a_room 액티비티로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 클릭한 item의 방송 번호를 가져옴
                int BroadCast_no = roomList_aryLi.get(position-1).getBroadCast_no();
                Log.d("listView_clicked", "BroadCast_no: "+String.valueOf(BroadCast_no));

                // 더미데이터 골라내기
                if(BroadCast_no!=1773 && BroadCast_no!=1772 && BroadCast_no!=1771 && BroadCast_no!=1770 && BroadCast_no!=1769 &&
                        BroadCast_no!=1766 && BroadCast_no!=1765 && BroadCast_no!=1764 && BroadCast_no!=1763 && BroadCast_no!=1762) {
                    Go_live(String.valueOf(BroadCast_no));
                }

            }
        });

        default_live_Rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 더미데이터 골라내기
                if(broadCast_no_listView_header!=1773 && broadCast_no_listView_header!=1772 && broadCast_no_listView_header!=1771 && broadCast_no_listView_header!=1770 && broadCast_no_listView_header!=1769 &&
                        broadCast_no_listView_header!=1766 && broadCast_no_listView_header!=1765 && broadCast_no_listView_header!=1764 && broadCast_no_listView_header!=1763 && broadCast_no_listView_header!=1762) {
                    Go_live(String.valueOf(broadCast_no_listView_header));
                }
            }
        });

        // 새로고침 아이콘 색깔 변경
        layout.setColorSchemeColors(Color.parseColor("#FFB300"), Color.parseColor("#FF6F00"));
//        layout.setColorSchemeResources();
        //리스너 정의
        layout.setOnRefreshListener(sRefresh);
        /***/
        layout.setEnabled(true);

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            boolean top_of_listview = false;
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0 && view.getChildAt(0) != null) {
                    Log.d("pullToRefresh", "view.getChildAt(0).getTop(): " + view.getChildAt(0).getTop());
                }

                top_of_listview = false;
//                if(firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 30){
                if(firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0){
                    top_of_listview = true;
                    Log.d("pullToRefresh", "top!!");
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                layout.setEnabled(false);
                Log.d("pullToRefresh", "scrolled!!");

                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && top_of_listview) {
                    layout.setEnabled(true);
                }
            }
        });

        return layout;


    }

    /**---------------------------------------------------------------------------
     메소드 ==> pull to refresh 리스너 -- get_Channel_list 실행
     ---------------------------------------------------------------------------*/
    private SwipeRefreshLayout.OnRefreshListener sRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            get_Channel_list();
//            adapter_room_list.notifyDataSetChanged();
        }
    };

    /**---------------------------------------------------------------------------
     액티비티 이동 ==> a_room
     ---------------------------------------------------------------------------*/
    public void Go_live(String BroadCast_no) {

        Log.d("A_Check_Go_live", "Go_live 메소드 들어옴");
        // 방송 채널 정보 서버 전달
        task = new AsyncTask_enter_room(getActivity());
        task.execute(a_main_after_login.user_no, BroadCast_no, "live");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("A_Check_onActivityCrea~", "f_live");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("A_Check_onStart", "f_live");
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onResume -- `방송채널 리스트` 가져오는 메소드 실행
     ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        Log.d("A_Check_onResume", "f_live");
        get_Channel_list();
//        adapter_room_list.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("A_Check_onPause", "f_live");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("A_Check_onStop", "f_live");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("A_Check_onDestroyView", "f_live");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("A_Check_onDestroy", "f_live");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("A_Check_onDetach", "f_live");
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 방송 리스트 정보 가져오기 -- AsyncTask_get_channel_list
     ---------------------------------------------------------------------------*/
    public void get_Channel_list() {
        try {
            get_channel_result = new AsyncTask_get_channel_list().execute().get();
//            Toast.makeText(container.getContext(), get_channel_result, Toast.LENGTH_SHORT).show();
//            Log.d("AsyncTask_channel_list", get_channel_result);

            try {
                // 읽어온 문자열을 JSONObject화 한다
                JSONObject jObject_channel_info = new JSONObject(get_channel_result);
                Log.d("AsyncTask_channel_list", "jObject_channel_info.length(): "+String.valueOf(jObject_channel_info.length()));

                // 다시 JSONArray화 한다
                JSONArray jArray_channel_info = jObject_channel_info.getJSONArray(TAG_JSON);
                Log.d("AsyncTask_channel_list", "jArray_channel_info.length(): "+String.valueOf(jArray_channel_info.length()));

                // 서버로부터 받은 live방송 데이터가 하나도 없을 떄
                if(jArray_channel_info.length()==0) {
                    banner_img_1_IV.setVisibility(View.GONE);
                    banner_img_2_IV.setVisibility(View.VISIBLE);
                    banner_img_2_alpha_V.setVisibility(View.VISIBLE);
                    banner_img_2_1_IV.setVisibility(View.VISIBLE);
                    live_mark_Linlay.setVisibility(View.GONE);
                    nickName_layout_Linlay.setVisibility(View.GONE);
                    divider_under_best_V.setVisibility(View.GONE);
                    title_TV.setVisibility(View.GONE);
                    live_list_TV.setVisibility(View.GONE);

                    Glide
                        .with(f_live.this)
                        .load(R.drawable.default_live)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(banner_img_2_IV);
                    Glide
                        .with(f_live.this)
                        .load(R.drawable.default_live_word)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(banner_img_2_1_IV);

                    roomList_aryLi.clear();
                    default_live_Rel.setClickable(false);
                    adapter_room_list.notifyDataSetChanged();
                }

                // 서버로부터 받은 live방송 데이터가 하나라도 있을 때
                else if(jArray_channel_info.length()!=0) {
                    banner_img_1_IV.setVisibility(View.VISIBLE);
                    banner_img_2_IV.setVisibility(View.GONE);
                    banner_img_2_alpha_V.setVisibility(View.GONE);
                    banner_img_2_1_IV.setVisibility(View.GONE);
                    live_mark_Linlay.setVisibility(View.VISIBLE);
                    nickName_layout_Linlay.setVisibility(View.VISIBLE);
                    divider_under_best_V.setVisibility(View.VISIBLE);
                    title_TV.setVisibility(View.VISIBLE);
                    live_list_TV.setVisibility(View.VISIBLE);

                    // 리스트를 초기화한다
                    roomList_aryLi.clear();
                    default_live_Rel.setClickable(true);

                    // 배너 올라갈 방송 랜덤 고르기
                    random = new Random();
                    int banner_broadCast_no = random.nextInt(jArray_channel_info.length()-1);
                    Log.d("banner_no", "banner_broadCast_no: " + banner_broadCast_no);

                    /**---------------------------------------------------------------------------
                     jArray_channel_info 담긴 JSONObject를 하나씩 가져와,
                     가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                     그 변수들을 토대로, 초기화한 roomList_aryLi에
                     새로운 'DataClass_channel_info' 데이터 객체를 하나씩 생성한다.
                     ---------------------------------------------------------------------------*/
                    for (int i=0; i<jArray_channel_info.length(); i++) {
                        JSONObject jGoup_channel_info = jArray_channel_info.getJSONObject(i);

                        int broadCast_no = jGoup_channel_info.getInt("broadCast_no");
                        int BJ_user_no = jGoup_channel_info.getInt("BJ_user_no");
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

                        /** 일단, 가장 최근 방송을 배너에 띄우는걸로
                            나중에는 좋아요 수를 많이 받은 순 or 현재 가장 많은 사람이 청취하고있는 순, 이런 기준을 정해서 배너에 띄우는걸로 */
//                        if(i==banner_broadCast_no) {
                        if(i==jArray_channel_info.length()-1) {

                            broadCast_no_listView_header = broadCast_no;

                            if(broadCast_img_fileName.equals("none")) {
                                Glide
                                    .with(f_live.this)
                                    .load(R.drawable.headphone2)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(banner_img_1_IV);
                            }
                            else if(!broadCast_img_fileName.equals("none")) {
                                Glide
                                    .with(f_live.this)
                                    .load(Static.SERVER_URL_BROADCAST_IMG_FOLDER + broadCast_img_fileName)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(banner_img_1_IV);
                            }
                            title_TV.setText(broadCast_title);
                            nickName_TV.setText(BJ_nickName);
                        }

//                roomList_aryLi.add(i+1, new DataClass_room_list(
                        roomList_aryLi.add(0, new DataClass_room_list(
                                broadCast_no,
                                BJ_user_no,
                                BJ_nickName,
                                broadCast_title,
                                welcome_word,
                                broadCast_img_fileName,
                                broadCast_save_orNot,
                                final_guest_count - 1, // 현재 게스트 수에서 호스트 본인은 제외
                                like_count,
                                received_report_count,
                                broadCast_start_time,
                                broadCast_end_time,
                                total_broadCast_time,
                                broadCast_end_orNot,
                                total_broadCast_time_mil
                        ));
//                    Log.d("jArray_channel_info", String.valueOf(i));
//                    Log.d("jArray_channel_info", "roomList_aryLi.get(i).getBroadCast_title(): "+roomList_aryLi.get(0).getBroadCast_title());
                    }
                    adapter_room_list.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                Log.d("AsyncTask_channel_list", "JSONException: " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 새로고침 아이콘 없애기
        layout.setRefreshing(false);

        /** DB데이터 없이 하드코딩_ 테스트용 DB데이터 없이 하드코딩_ 테스트용*/
        /** DB데이터 없이 하드코딩_ 테스트용 DB데이터 없이 하드코딩_ 테스트용*/
//        roomList_aryLi.add(new DataClass_room_list(1, 1,"그레아티","멍멍","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(2, 2,"티몬","세상 혼자 가짐","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(3, 3,"아이언맨","응? 튕긴거 실화냐?","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(4, 4,"토르","배고파서 슈퍼가는 방송","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(5, 5,"호크아이","그냥","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(6, 6,"헐크","조용한 잠방","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(7, 7,"블랙위도우","팅팅.. 나갈준비중","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(8, 8,"스파이더맨","400팬자축방송","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(9, 9,"로키","놀러가기전 잠깐 라이브","어서오세요~","","true",0,0,0,"","",""));
//        roomList_aryLi.add(new DataClass_room_list(10, 10,"품바","밤보다 아름다운 낮","어서오세요~","","true",0,0,0,"","",""));
        /** DB데이터 없이 하드코딩_ 테스트용 DB데이터 없이 하드코딩_ 테스트용*/
        /** DB데이터 없이 하드코딩_ 테스트용 DB데이터 없이 하드코딩_ 테스트용*/

    }
}

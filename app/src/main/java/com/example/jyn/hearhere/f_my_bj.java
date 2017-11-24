package com.example.jyn.hearhere;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class f_my_bj extends Fragment {

    ListView listView;
    TextView no_my_BJ_TV;
    Adapter_Fan_BJ adapter_fan_bj;
    ArrayList<DataClass_guest_list> FAN_BJ_aryLi;   // guest_list에 들어가는 데이터랑 똑같아서 그대로 사용

    String get_BJ_result;
    String goal;
    static final String TAG_JSON = "BJ";
    final String my_user_no = a_main_after_login.user_no;
    final int CONFIRM_SUBTRACT = 2000;

    String clicked_user_no; // 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 user_no
    String position;        // 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 listview position
    String clicked_nicnName;// 리스트뷰의 fan add/subtract 버튼 클릭한 대상의 nickName

    LayoutInflater inflater;
    ViewGroup container;

    public f_my_bj() {
        // Required empty public constructor
    }

    public static f_my_bj newInstance() {
        Bundle args = new Bundle();

        f_my_bj fragment = new f_my_bj();
        fragment.setArguments(args);
        return fragment;
    }

    //SwipeRefreshLayout -  당겨서 새로고침
    private SwipeRefreshLayout layout;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreateView
     ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("프래그먼트", "onCreateView");
        // Inflate the layout for this fragment
        this.inflater = inflater;
        this.container = container;

        FAN_BJ_aryLi = new ArrayList<>();

        /** 리스트뷰 버튼 클릭리스너 (Add/subtract FAN) */
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
                            String add_FAN_result = new AsyncTask_add_FAN().execute(my_user_no, clicked_user_no).get();
                            Log.d("fan", "add_FAN_result: " + add_FAN_result);
                            String[] after_split_result = add_FAN_result.split("&");

                            if(after_split_result[0].equals("success")) {
                                // 클릭한 대상의 버튼 상태 바꾸기
                                FAN_BJ_aryLi.get(Integer.parseInt(position)).setFan_orNot("true");
                                adapter_fan_bj.notifyDataSetChanged();
                                /** 서버로부터 회원 정보 가져와서 업데이트 - 액티비티 메소드 호출 */
                                /** subtract_FAN 시에는 팝업되는 다이얼로그 액티비티로 인해 이 fragment를
                                 * 포함하고 있는 Activity의 onResume이 호출되기 때문에,
                                 * onResume에서 유저 정보를 업데이트 하게 처리함*/
                                ((a_profile)getActivity()).update_user_profile();

                                // 노티피케이션 날리기
                                AsyncTask_push_notification task_2 = new AsyncTask_push_notification();
                                String temp = task_2.execute("fan", my_user_no, clicked_user_no).get();
                                Log.d("MyFirebase", "result: " + temp);
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
                        Intent subtract_FAN_confirm_intent = new Intent(getActivity(), v_subtract_FAN_confirm.class);
                        subtract_FAN_confirm_intent.putExtra("REQUEST_FROM", "f_my_BJ");
                        subtract_FAN_confirm_intent.putExtra("NICKNAME", clicked_nicnName);
                        startActivityForResult(subtract_FAN_confirm_intent, CONFIRM_SUBTRACT);
                        break;
                }

            }
        };

        /** 리스트뷰 프사, 닉네임 클릭리스너 - v_profile 다이얼로그 액티비티 팝업*/
        View.OnClickListener mOnClickListener_2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    default:
                        String clicked_user_no = v.getTag(R.string.user_no).toString();
                        Log.d("listView_clicked", "clicked_user_no: " + clicked_user_no);

                        Intent v_profile_intent = new Intent(getActivity(), v_profile.class);
                        v_profile_intent.putExtra("REQUEST_FROM", "f_my_BJ");
                        v_profile_intent.putExtra("CLICKED_USER_NO", clicked_user_no);
                        v_profile_intent.putExtra("MY_USER_NO", a_main_after_login.user_no);
                        startActivity(v_profile_intent);
                }
            }
        };

        // 프래그먼트 인플레이팅
        layout = (SwipeRefreshLayout)inflater.inflate(R.layout.f_my_bj, container, false);

        // 리스트뷰 어댑터 (아이템, 정보 arr 추가)
        adapter_fan_bj = new Adapter_Fan_BJ(getActivity(), FAN_BJ_aryLi, mOnClickListener, mOnClickListener_2);

        // 뷰 ID 참조
        listView = (ListView)layout.findViewById(R.id.listView);
        no_my_BJ_TV = (TextView)layout.findViewById(R.id.no_my_BJ);

        // 리스트뷰 어댑터 적용
        listView.setAdapter(adapter_fan_bj);
        adapter_fan_bj.notifyDataSetChanged();

        // 새로고침 아이콘 색깔 변경
        layout.setColorSchemeColors(Color.parseColor("#FFB300"), Color.parseColor("#FF6F00"));

        //리스너 정의
        layout.setOnRefreshListener(sRefresh);

        get_Fan_BJ();
        return layout;
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> onActivityResult -- 팬 끊기 확인 다이얼로그 액티비티 결과
     ---------------------------------------------------------------------------*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONFIRM_SUBTRACT && resultCode == RESULT_OK) {
            try {
                String subtract_FAN_result = new AsyncTask_subtract_FAN().execute(my_user_no, clicked_user_no).get();
                Log.d("fan", "subtract_FAN_result: " + subtract_FAN_result);
                String[] after_split_result = subtract_FAN_result.split("&");

                if (after_split_result[0].equals("success")) {
                    // 클릭한 대상의 버튼 상태 바꾸기
                    FAN_BJ_aryLi.get(Integer.parseInt(position)).setFan_orNot("false");
                    adapter_fan_bj.notifyDataSetChanged();

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
            Log.d("fan", "RESULT_CANCELED");
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> pull to refresh 리스너 -- get_Fan_BJ 실행
     ---------------------------------------------------------------------------*/
    private SwipeRefreshLayout.OnRefreshListener sRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            get_Fan_BJ();
            ((a_profile)getActivity()).update_user_profile();
        }
    };


    /**---------------------------------------------------------------------------
     메소드 ==> get_Fan_BJ()
     ---------------------------------------------------------------------------*/
    public void get_Fan_BJ() {
        try {
            // 서버로부터 Fan 정보를 가져온다
            get_BJ_result = new AsyncTask_get_Fan_BJ().execute(my_user_no, "BJ").get();
            Log.d("AsyncTask_get_Fan_BJ", "get_BJ_result: " + get_BJ_result);

            try {
                // 읽어온 문자열을 JSONObject화 한다
                JSONObject jObject_BJ_info = new JSONObject(get_BJ_result);

                // 다시 JSONArray화 한다
                JSONArray jArray_BJ_info = jObject_BJ_info.getJSONArray(TAG_JSON);
                Log.d("AsyncTask_get_Fan_BJ", "jArray_BJ_info.length(): " + jArray_BJ_info.length());

                // 서버로부터 받은 BJ_info 데이터가 하나도 없을 때
                // 즉 내가 팔로우 하고 있는 사람이 한 명도 없을 때
                if(jArray_BJ_info.length()==0) {
                    listView.setVisibility(View.GONE);
                    no_my_BJ_TV.setVisibility(View.VISIBLE);
                    FAN_BJ_aryLi.clear();
                }
                if(jArray_BJ_info.length()>0) {
                    listView.setVisibility(View.VISIBLE);
                    no_my_BJ_TV.setVisibility(View.GONE);
                    FAN_BJ_aryLi.clear();

                    /**---------------------------------------------------------------------------
                     jArray_BJ_info 담긴 JSONObject를 하나씩 가져와,
                     가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
                     그 변수들을 토대로, 초기화한 FAN_BJ_aryLi
                     새로운 'DataClass_guest_list' 데이터 객체를 하나씩 생성하여 add 한다
                     ---------------------------------------------------------------------------*/
                    for(int i=0; i<jArray_BJ_info.length(); i++) {
                        JSONObject jGroup_BJ_info = jArray_BJ_info.getJSONObject(i);

                        int user_no = jGroup_BJ_info.getInt("user_no");
                        String User_img_file = jGroup_BJ_info.getString("user_img_fileName");
                        String User_nicName = jGroup_BJ_info.getString("user_nickName");

                        // fan_orNot 체크
                        String Fan_orNot = check_FAN_orNot(String.valueOf(user_no), my_user_no);

                        FAN_BJ_aryLi.add(new DataClass_guest_list(
                                user_no,
                                User_img_file,
                                User_nicName,
                                Fan_orNot
                        ));
                    }
                    adapter_fan_bj.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // 새로고침 아이콘 없애기
        layout.setRefreshing(false);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> about_FAN -- FAN 여부 확인하기
     ---------------------------------------------------------------------------*/
    public String check_FAN_orNot(String clicked_user_no, String my_user_no) throws ExecutionException, InterruptedException {

        String result = new AsyncTask_about_FAN().execute("fan_orNot", clicked_user_no, my_user_no).get();
        Log.d("fan", "result: " + result);

        if(!result.equals("fail")) {
            return result;
        }
        if(result.equals("fail")) {
            return "error";
        }
        return null;
    }




//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
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
//    public void onResume() {
//        super.onResume();
//        Log.d("프래그먼트", "onResume");
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
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.d("프래그먼트", "onDetach");
//    }




    /**---------------------------------------------------------------------------
     콜백메소드 ==> setUserVisibleHint -- 프래그먼트 Load
     ---------------------------------------------------------------------------*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //화면에 실제로 보일때
            Log.d("프래그먼트", "setUserVisibleHint_ 화면에 실제로 보일 때");

            if(listView == null) {
                AsyncTast_get_Fan_BJ asyncTast_get_fan_bj = new AsyncTast_get_Fan_BJ();
                asyncTast_get_fan_bj.execute();
            }
            if(listView != null) {
                get_Fan_BJ();
            }
        }
        else {
            //preload 될때(전페이지에 있을때)
            Log.d("프래그먼트", "setUserVisibleHint_ preload 될때(전페이지에 있을때)");
        }
    }


    /**---------------------------------------------------------------------------
     스레드 ==> AsyncTast_get_Fan_BJ -- 프래그먼트가 아직 생성되지 않았을 때
                onCreateView 되는 시간을 주기 위해 딜레이 쓰레드를 줌
     ---------------------------------------------------------------------------*/
    private class AsyncTast_get_Fan_BJ extends AsyncTask<String, Void, String> {

        AsyncTast_get_Fan_BJ() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            get_Fan_BJ();
        }
    }








}

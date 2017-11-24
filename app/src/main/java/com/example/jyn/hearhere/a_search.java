package com.example.jyn.hearhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class a_search extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Adapter_view_pager_search adater;

    EditText search_keyword_ET;
    ImageView ini_editText_IV;

    String search_word_str = "";
    String user_no;

    // Tab 1. nickName
    public static ArrayList<DataClass_guest_list> search_user_aryLi  = new ArrayList<>();
    // Tab 2. live
    public static ArrayList<DataClass_room_list> roomList_aryLi = new ArrayList<>();
    // Tab 3. cast
    public static ArrayList<DataClass_room_list> roomList_aryLi_cast = new ArrayList<>();

    // 검색 눌렀었음!
    static boolean searched_before= false;


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_search);

        // 인텐트값 전달 받기
        Intent intent = getIntent();
        user_no = intent.getStringExtra("USER_NO");
        Log.d("retrofit_result", "USER_NO: " + user_no);

        // 뷰찾기 - findViewById
        tabLayout = (TabLayout)findViewById(R.id.tab);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        search_keyword_ET = (EditText)findViewById(R.id.search_keyword);
        ini_editText_IV = (ImageView)findViewById(R.id.ini_editText);

        // 초기 셋팅
        ini_editText_IV.setImageResource(R.drawable.cancel_none_activated);
        ini_editText_IV.setClickable(false);

        // 탭레이아웃, 뷰페이저 셋팅
        adater = new Adapter_view_pager_search(getSupportFragmentManager());
        viewPager.setAdapter(adater);
        tabLayout.setupWithViewPager(viewPager);

        // 뷰페이저 addOnPageChangeListener 리스너 등록
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d("viewPager", "onPageScrolled position: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("viewPager", "onPageSelected: " + position);
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

        // search_keyword_ET 텍스트 와쳐
        search_keyword_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    ini_editText_IV.setImageResource(R.drawable.cancel_activated);
                    ini_editText_IV.setClickable(true);
                }

                if(s.length() == 0) {
                    ini_editText_IV.setImageResource(R.drawable.cancel_none_activated);
                    ini_editText_IV.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



        // search_keyword_ET 소프트 키보드 앤터 속성 `검색`으로 바꾸기
        search_keyword_ET.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        search_keyword_ET.setInputType(InputType.TYPE_CLASS_TEXT);

        // search_editText 소프트 키보드 액션 리스너 정의, 등록
        EditText.OnEditorActionListener Edittext_Listener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 특정 동작 지정
                    onSearchTextClicked(getCurrentFocus());

                    //키보드 숨기기
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        };
        search_keyword_ET.setOnEditorActionListener(Edittext_Listener);

    }


    /**---------------------------------------------------------------------------
     메소드 ==> 검색 결과 가져오기
     ---------------------------------------------------------------------------*/
    public void onSearchTextClicked(View view) {

        search_word_str = search_keyword_ET.getText().toString();
        search_word_str = search_word_str.replace(" ", "");

        // 검색 미진행_ 입력값 없음
        if(search_word_str.equals("")) {
            Toast.makeText(a_search.this, "검색 단어가 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 검색어 길이 부족
        if(!search_word_str.equals("") && search_word_str.length()<=1) {
            Toast.makeText(a_search.this, "검색어는 2자이상 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        //검색 진행 - 코드작성해야함
        else {
            get_search_word_result(search_word_str);
            Log.d("retrofit_result", "search_word_str: " + search_word_str);
        }
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 뒤로가기(액티비티 이동) -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
        finish();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 검색 EditText 초기화, 이미지 리소스 변경 및 clickable 상태 변경
     ---------------------------------------------------------------------------*/
    public void ini_editText(View view) {
        search_keyword_ET.setText("");
        ini_editText_IV.setImageResource(R.drawable.cancel_none_activated);
        ini_editText_IV.setClickable(false);
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 검색키워드에 따른 결과 가져오기, 이미지 리소스 변경 및 clickable 상태 변경 (레트로핏2)
     ---------------------------------------------------------------------------*/
    public void get_search_word_result(String search_word_str) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

//        Call<ResponseBody> result = retrofitService.get_search_result(search_word_str, user_no);
        Call<ResponseBody> result = retrofitService.get_search_result(Static.GET_SEARCH_WORD_RESULT, search_word_str, user_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                searched_before = true;
                try {
                    String retrofit_result = response.body().string();
                    Log.d("retrofit_result", "retrofit_result: "+retrofit_result);

                    try {
                        // 읽어온 문자열을 JSONObject화 한다
                        JSONObject jObject_search_info = new JSONObject(retrofit_result);

                        // TAG별로  JSONArray화 하여 각 메소드에 매개변수로 넘겨준다
                        /** 1.nickName Tab */
                        JSONArray jArray_nickName_info = jObject_search_info.getJSONArray("nickName");
                        set_nickName_tab_info(jArray_nickName_info);

                        /** 2.live Tab */
                        JSONArray jArray_live_info = jObject_search_info.getJSONArray("live");
                        set_live_tab_info(jArray_live_info);

                        /** 3.cast Tab */
                        JSONArray jArray_cast_info = jObject_search_info.getJSONArray("cast");
                        set_cast_tab_info(jArray_cast_info);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
            }
        });
    }

    /**---------------------------------------------------------------------------
     메소드 ==> 검색정보 셋팅하기 -- Tab 1. nickName
     ---------------------------------------------------------------------------*/
    public void set_nickName_tab_info(JSONArray jsonArray) {
        Log.d("retrofit_result", "set_nickName_tab_info_jsonArray.length(): " + jsonArray.length());
        search_user_aryLi.clear();

        /**---------------------------------------------------------------------------
         jsonArray 담긴 JSONObject를 하나씩 가져와,
         가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
         그 변수들을 토대로, 초기화한 search_user_aryLi
         새로운 'DataClass_guest_list' 데이터 객체를 하나씩 생성하여 add 한다
         ---------------------------------------------------------------------------*/
        if(jsonArray.length() > 0) {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jGroup_nickName_info = null;
                try {
                    jGroup_nickName_info = jsonArray.getJSONObject(i);

                    int user_no = jGroup_nickName_info.getInt("user_no");
                    String User_img_file = new String(jGroup_nickName_info.getString("user_img_fileName").getBytes(), "utf-8");
                    String User_nicName = new String(jGroup_nickName_info.getString("user_nickName").getBytes(), "utf-8");
                    Log.d("retrofit_result", "User_img_file: " + User_img_file);
                    Log.d("retrofit_result", "User_nicName: " + User_nicName);

                    search_user_aryLi.add(new DataClass_guest_list(
                            user_no,
                            User_img_file,
                            User_nicName,
                            "no_need"
                    ));
                } catch (JSONException | UnsupportedEncodingException e) {
                    Log.d("retrofit_result", "Exception: " + e.getMessage());
                }
            }

            f_nickName.recyclerView.setVisibility(View.VISIBLE);
            f_nickName.no_result_TV.setVisibility(View.GONE);

            // f_nickName의 data 갱신
            f_nickName.recyclerAdapter.notifyDataSetChanged();
        }

        if(jsonArray.length() == 0) {
            f_nickName.recyclerView.setVisibility(View.GONE);
            f_nickName.no_result_TV.setVisibility(View.VISIBLE);
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 검색정보 셋팅하기 -- Tab 2. live
     ---------------------------------------------------------------------------*/
    public void set_live_tab_info(JSONArray jsonArray) {
        Log.d("retrofit_result", "set_live_tab_info_jsonArray.length(): " + jsonArray.length());
        roomList_aryLi.clear();

        /**---------------------------------------------------------------------------
         jsonArray 담긴 JSONObject를 하나씩 가져와,
         가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
         그 변수들을 토대로, 초기화한 roomList_aryLi에
         새로운 'DataClass_channel_info' 데이터 객체를 하나씩 생성한다.
         ---------------------------------------------------------------------------*/
        if(jsonArray.length() > 0) {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jGroup_live_info = null;
                try {
                    jGroup_live_info = jsonArray.getJSONObject(i);

                    int broadCast_no = jGroup_live_info.getInt("broadCast_no");
                    int BJ_user_no = jGroup_live_info.getInt("BJ_user_no");
                    String BJ_nickName = new String(jGroup_live_info.getString("BJ_nickName").getBytes(), "utf-8");
                    String broadCast_title = new String(jGroup_live_info.getString("broadCast_title").getBytes(), "utf-8");
                    String welcome_word = new String(jGroup_live_info.getString("welcome_word").getBytes(), "utf-8");
                    String broadCast_img_fileName = new String(jGroup_live_info.getString("broadCast_img_fileName").getBytes(), "utf-8");
                    String broadCast_save_orNot = new String(jGroup_live_info.getString("broadCast_save_orNot").getBytes(), "utf-8");
                    int final_guest_count = jGroup_live_info.getInt("final_guest_count");
                    int like_count = jGroup_live_info.getInt("like_count");
                    int received_report_count = jGroup_live_info.getInt("received_report_count");
                    String broadCast_start_time = new String(jGroup_live_info.getString("broadCast_start_time").getBytes(), "utf-8");
                    String broadCast_end_time = new String(jGroup_live_info.getString("broadCast_end_time").getBytes(), "utf-8");
                    String total_broadCast_time = new String(jGroup_live_info.getString("total_broadCast_time").getBytes(), "utf-8");
                    String broadCast_end_orNot =  new String(jGroup_live_info.getString("broadCast_end_orNot").getBytes(), "utf-8");
                    int total_broadCast_time_mil = jGroup_live_info.getInt("total_broadCast_time_mil");

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
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("retrofit_result", "Exception: " + e.getMessage());
                }
            }
            f_search_live.recyclerView.setVisibility(View.VISIBLE);
            f_search_live.no_result_TV.setVisibility(View.GONE);

            // f_search_live의 data 갱신
            f_search_live.recyclerAdapter.notifyDataSetChanged();
        }

        if(jsonArray.length() == 0) {
            f_search_live.recyclerView.setVisibility(View.GONE);
            f_search_live.no_result_TV.setVisibility(View.VISIBLE);
        }
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 검색정보 셋팅하기 -- Tab 3. cast
     ---------------------------------------------------------------------------*/
    public void set_cast_tab_info(JSONArray jsonArray) {
        Log.d("retrofit_result", "set_cast_tab_info_jsonArray.length(): " + jsonArray.length());
        roomList_aryLi_cast.clear();

        /**---------------------------------------------------------------------------
         jsonArray 담긴 JSONObject를 하나씩 가져와,
         가져온 JSONObject 안에 있는 키값을 통해 각각 맞는 자료형 변수에 저장한 후,
         그 변수들을 토대로, 초기화한 roomList_aryLi에
         새로운 'DataClass_channel_info' 데이터 객체를 하나씩 생성한다.
         ---------------------------------------------------------------------------*/
        if(jsonArray.length() > 0) {
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jGoup_cast_info = null;
                try {
                    jGoup_cast_info = jsonArray.getJSONObject(i);

                    int broadCast_no = jGoup_cast_info.getInt("broadCast_no");
                    int BJ_user_no = jGoup_cast_info.getInt("BJ_user_no");
                    String BJ_nickName = new String(jGoup_cast_info.getString("BJ_nickName").getBytes(), "utf-8");
                    String broadCast_title = new String(jGoup_cast_info.getString("broadCast_title").getBytes(), "utf-8");
                    String welcome_word = new String(jGoup_cast_info.getString("welcome_word").getBytes(), "utf-8");
                    String broadCast_img_fileName = new String(jGoup_cast_info.getString("broadCast_img_fileName").getBytes(), "utf-8");
                    String broadCast_save_orNot = new String(jGoup_cast_info.getString("broadCast_save_orNot").getBytes(), "utf-8");
                    int final_guest_count = jGoup_cast_info.getInt("final_guest_count");
                    int like_count = jGoup_cast_info.getInt("like_count");
                    int received_report_count = jGoup_cast_info.getInt("received_report_count");
                    String broadCast_start_time = new String(jGoup_cast_info.getString("broadCast_start_time").getBytes(), "utf-8");
                    String broadCast_end_time = new String(jGoup_cast_info.getString("broadCast_end_time").getBytes(), "utf-8");
                    String total_broadCast_time = new String(jGoup_cast_info.getString("total_broadCast_time").getBytes(), "utf-8");
                    String broadCast_end_orNot =  new String(jGoup_cast_info.getString("broadCast_end_orNot").getBytes(), "utf-8");
                    int total_broadCast_time_mil = jGoup_cast_info.getInt("total_broadCast_time_mil");

                    String temp = total_broadCast_time.substring(0,2);
                    if(temp.equals("00")) {
                        total_broadCast_time = total_broadCast_time.substring(3);
                    }
                    Log.d("substring", "total_broadCast_time: "+total_broadCast_time);

                    roomList_aryLi_cast.add(0, new DataClass_room_list(
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

                } catch (JSONException  | UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.d("retrofit_result", "Exception: " + e.getMessage());
                }
            }
            f_search_cast.recyclerView.setVisibility(View.VISIBLE);
            f_search_cast.no_result_TV.setVisibility(View.GONE);

            // f_search_cast의 data 갱신
            f_search_cast.recyclerAdapter.notifyDataSetChanged();
        }
        if(jsonArray.length() == 0) {
            f_search_cast.recyclerView.setVisibility(View.GONE);
            f_search_cast.no_result_TV.setVisibility(View.VISIBLE);
        }
    }
}

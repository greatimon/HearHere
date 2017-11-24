package com.example.jyn.hearhere;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class f_search_cast extends Fragment {

    // 프래그먼트 인플레이팅 관련 변수
    LayoutInflater inflater;
    ViewGroup container;
    LinearLayout layout;

    // 리사이클러뷰 관련 변수
    public static RecyclerView recyclerView;
    public static RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static TextView no_result_TV;

    public f_search_cast() {
        // Required empty public constructor
    }

    public static f_search_cast newInstance() {
        Bundle args = new Bundle();

        f_search_cast fragment = new f_search_cast();
        fragment.setArguments(args);
        return fragment;
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreateView
     ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        // 프레그먼트 인플레이팅
        layout = (LinearLayout)inflater.inflate(R.layout.f_search_cast, container, false);

        no_result_TV = (TextView)layout.findViewById(R.id.no_result);
        // 리사이클러뷰 관련
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerView);
          // use this setting to improve performance if you know that changes
          // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager 사용
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // 생성자 인수: 1.액티비티, 2.데이터셋, 3.인플레이팅되는 itemLayout 종류
        recyclerAdapter = new RecyclerAdapter(getActivity(), a_search.roomList_aryLi_cast, R.layout.i_room_list, "cast");
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        // 애니메이션 설정
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 클릭한 적이 있다면 - 기존 검색결과를 유지하기 위함(갱신 안하기 위함)
        if(a_search.searched_before) {
            // aryLi 사이즈가 0보다 크다면: 데이터가 있다면
            if(a_search.roomList_aryLi_cast.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                no_result_TV.setVisibility(View.GONE);
            }
            // aryLi 사이즈가 0이라면: 데이터가 없다면
            if(a_search.roomList_aryLi_cast.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                no_result_TV.setVisibility(View.VISIBLE);
            }
        }

        return layout;
    }


    /**---------------------------------------------------------------------------
     생명주기 ==> onDetach
     ---------------------------------------------------------------------------*/
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("프래그먼트", "onDetach");
        a_search.roomList_aryLi_cast.clear();
    }


    /**---------------------------------------------------------------------------
     콜백메소드 ==> 뷰페이저 focus 에 따른 콜백메소드 -- 미사용
     ---------------------------------------------------------------------------*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("프래그먼트", "setUserVisibleHint");
        if (isVisibleToUser) {
            //화면에 실제로 보일때
            Log.d("프래그먼트", "setUserVisibleHint_ 화면에 실제로 보일 때");
        }
        else {
            //preload 될때(전페이지에 있을때)
            Log.d("프래그먼트", "setUserVisibleHint_ preload 될때(전페이지에 있을때)");
        }

    }
}

package com.example.jyn.hearhere;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class f_nickName extends Fragment {

    // 프래그먼트 인플레이팅 관련 변수
    LayoutInflater inflater;
    ViewGroup container;
    LinearLayout layout;

    // 리사이클러뷰 관련 변수
    public static RecyclerView recyclerView;
    public static RecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static TextView no_result_TV;

    public f_nickName() {
        // Required empty public constructor
    }

    public static f_nickName newInstance() {
        Log.d("프래그먼트", "newInstance()");
        Bundle args = new Bundle();

        f_nickName fragment = new f_nickName();
        fragment.setArguments(args);
        return fragment;
    }

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreateView
     ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("프래그먼트", "onCreateView");

        this.inflater = inflater;
        this.container = container;

        // 프레그먼트 인플레이팅
        layout = (LinearLayout)inflater.inflate(R.layout.f_nickname, container, false);

        no_result_TV = (TextView)layout.findViewById(R.id.no_result);
        // 리사이클러뷰 관련
        recyclerView = (RecyclerView)layout.findViewById(R.id.recyclerView);
          // use this setting to improve performance if you know that changes
          // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // LinearLayoutManager 사용, 구분선 표시
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        // 생성자 인수: 1.액티비티, 2.arrayList, 3.인플레이팅되는 itemLayout 종류
        recyclerAdapter = new RecyclerAdapter(getActivity(), a_search.search_user_aryLi, R.layout.i_guest_list_item, "nickName");
        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();

        // 애니메이션 설정
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 클릭한 적이 있다면 - 기존 검색결과를 유지하기 위함(갱신 안하기 위함)
        if(a_search.searched_before) {
            // aryLi 사이즈가 0보다 크다면: 데이터가 있다면
            if(a_search.search_user_aryLi.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                no_result_TV.setVisibility(View.GONE);
            }
            // aryLi 사이즈가 0이라면: 데이터가 없다면
            if(a_search.search_user_aryLi.size() == 0) {
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
        a_search.search_user_aryLi.clear();
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

//    /**---------------------------------------------------------------------------
//     생명주기 ==> onCreate
//     ---------------------------------------------------------------------------*/
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        // 리사이클러뷰에 넣을 정보 가져올 로직(메소드) 넣기
////        search_user_aryLi.add(new DataClass_guest_list(
////                7,
////                "7team_nova_timon246398571_70.jpg",
////                "그레아티",
////                "no_need"
////        ));
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.d("프래그먼트", "onAttach");
//        /** search_user_aryLi - 테스트 코드 : ADD */
////        a_search.search_user_aryLi.add(new DataClass_guest_list(
////                7,
////                "7team_nova_timon246398571_70.jpg",
////                "그레아티티티티",
////                "no_need"
////        ));
//    }
//
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
}

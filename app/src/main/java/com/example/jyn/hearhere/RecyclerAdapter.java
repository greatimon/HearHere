package com.example.jyn.hearhere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DataClass_guest_list> search_user_aryLi;
    private ArrayList<DataClass_room_list> roomList_aryLi_live;
    private ArrayList<DataClass_room_list> roomList_aryLi_cast;
    private int itemLayout;
    private String request;

    /** 뷰홀더 */
    class ViewHolder extends RecyclerView.ViewHolder {
        View view;

        ImageView profile_IV;
        TextView nickName_TV;
        LinearLayout list_item_container_tab_1;

        ImageView broadCast_IV_live, live_count_IV_live, cast_count_IV_live;
        TextView title_TV_live, BJ_nic_TV_live, guest_count_TV_live, like_count_TV_live, broadCast_time_TV_live;
        LinearLayout broadCast_time_Linlay_live;
        LinearLayout list_item_container_tab_2;

        ImageView broadCast_IV_cast, live_count_IV_cast, cast_count_IV_cast;
        TextView title_TV_cast, BJ_nic_TV_cast, guest_count_TV_cast, like_count_TV_cast, broadCast_time_TV_cast;
        LinearLayout broadCast_time_Linlay_cast;
        LinearLayout list_item_container_tab_3;

        public ViewHolder(View view, int itemLayout) {
            super(view);
            Log.d("recyclerView", "ViewHolder");
            this.view = view;

            // 인플레이팅 되는 레이아웃 종류에 따라 구별
            switch(itemLayout) {
                case R.layout.i_guest_list_item:
                    Log.d("recyclerView", "ViewHolder_ R.layout.i_guest_list_item");
                    list_item_container_tab_1 = (LinearLayout)view.findViewById(R.id.list_item_container);
                    profile_IV = (ImageView)view.findViewById(R.id.sender_profile_img);
                    nickName_TV = (TextView)view.findViewById(R.id.nickName);

                    // 아이템 클릭 이벤트 설정
                    list_item_container_tab_1.setClickable(true);
                    list_item_container_tab_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = getAdapterPosition();
                            Log.d("recyclerView", "아이템 클릭_ position: " + position);
                            Log.d("recyclerView", "getUser_no: " + search_user_aryLi.get(position).getUser_no());
                            Log.d("recyclerView", "getUser_nicName: " + search_user_aryLi.get(position).getUser_nicName());
                            Log.d("recyclerView", "getUser_img_file: " + search_user_aryLi.get(position).getUser_img_file());
                            Log.d("recyclerView", "getFan_orNot: " + search_user_aryLi.get(position).getFan_orNot());

                            Intent v_profile_intent = new Intent(context, v_profile.class);
                            v_profile_intent.putExtra("REQUEST_FROM", "a_search");
                            v_profile_intent.putExtra("CLICKED_USER_NO", String.valueOf(search_user_aryLi.get(position).getUser_no()));
                            v_profile_intent.putExtra("MY_USER_NO", a_main_after_login.user_no);
                            context.startActivity(v_profile_intent);
                        }
                    });
                    break;

                case R.layout.i_room_list :
                    if(request.equals("live")) {
                        Log.d("recyclerView", "ViewHolder_ R.layout.i_room_list AND live");
                        list_item_container_tab_2 = (LinearLayout)view.findViewById(R.id.list_item_container);

                        broadCast_IV_live = (ImageView)view.findViewById(R.id.broadCast_img);
                        title_TV_live = (TextView)view.findViewById(R.id.title);
                        BJ_nic_TV_live = (TextView)view.findViewById(R.id.BJ_nic);
                        guest_count_TV_live = (TextView)view.findViewById(R.id.guest_count);
                        like_count_TV_live = (TextView)view.findViewById(R.id.like_count);
                        live_count_IV_live = (ImageView)view.findViewById(R.id.live_count);
                        cast_count_IV_live = (ImageView)view.findViewById(R.id.cast_count);
                        broadCast_time_Linlay_live = (LinearLayout)view.findViewById(R.id.broadCast_time_layout);
                        broadCast_time_TV_live = (TextView)view.findViewById(R.id.broadCast_time);

                        // 아이템 클릭 이벤트 설정
                        list_item_container_tab_2.setClickable(true);
                        list_item_container_tab_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = getAdapterPosition();
                                Log.d("recyclerView", "아이템 클릭_ position: " + position);
                                Log.d("recyclerView", "getBroadCast_title: " + roomList_aryLi_live.get(position).getBroadCast_title());
                                Log.d("recyclerView", "getBJ_user_no: " + roomList_aryLi_live.get(position).getBJ_user_no());
                                Log.d("recyclerView", "getBJ_nickName: " + roomList_aryLi_live.get(position).getBJ_nickName());
                                Log.d("recyclerView", "getBroadCast_no: " + roomList_aryLi_live.get(position).getBroadCast_no());

                                Go_live(String.valueOf(roomList_aryLi_live.get(position).getBroadCast_no()));
                            }
                        });
                    }
                    if(request.equals("cast")) {
                        Log.d("recyclerView", "ViewHolder_ R.layout.i_room_list AND cast");
                        list_item_container_tab_3 = (LinearLayout)view.findViewById(R.id.list_item_container);

                        broadCast_IV_cast = (ImageView)view.findViewById(R.id.broadCast_img);
                        title_TV_cast = (TextView)view.findViewById(R.id.title);
                        BJ_nic_TV_cast = (TextView)view.findViewById(R.id.BJ_nic);
                        guest_count_TV_cast = (TextView)view.findViewById(R.id.guest_count);
                        like_count_TV_cast = (TextView)view.findViewById(R.id.like_count);
                        live_count_IV_cast = (ImageView)view.findViewById(R.id.live_count);
                        cast_count_IV_cast = (ImageView)view.findViewById(R.id.cast_count);
                        broadCast_time_Linlay_cast = (LinearLayout)view.findViewById(R.id.broadCast_time_layout);
                        broadCast_time_TV_cast = (TextView)view.findViewById(R.id.broadCast_time);

                        // 아이템 클릭 이벤트 설정
                        list_item_container_tab_3.setClickable(true);
                        list_item_container_tab_3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = getAdapterPosition();
                                Log.d("recyclerView", "아이템 클릭_ position: " + position);
                                Log.d("recyclerView", "getBroadCast_title: " + roomList_aryLi_cast.get(position).getBroadCast_title());
                                Log.d("recyclerView", "getBJ_user_no: " + roomList_aryLi_cast.get(position).getBJ_user_no());
                                Log.d("recyclerView", "getBJ_nickName: " + roomList_aryLi_cast.get(position).getBJ_nickName());
                                Log.d("recyclerView", "getBroadCast_no: " + roomList_aryLi_cast.get(position).getBroadCast_no());

                                Go_cast(String.valueOf(roomList_aryLi_cast.get(position).getBroadCast_no()));
                            }
                        });
                    }
                    break;
            }
        }
    }

    /** RecyclerAdapter 생성자 */
    public RecyclerAdapter(Context context, ArrayList aryLi, int itemLayout, String request) {
        Log.d("recyclerView", "RecyclerAdapter 생성자");
        this.context = context;
        this.itemLayout = itemLayout;
        this.request = request;

        if(itemLayout == R.layout.i_guest_list_item) {
            this.search_user_aryLi = aryLi;
        }
        if(itemLayout == R.layout.i_room_list && request.equals("live")) {
            this.roomList_aryLi_live = aryLi;
        }
        if(itemLayout == R.layout.i_room_list && request.equals("cast")) {
            this.roomList_aryLi_cast = aryLi;
        }
    }

    /** onCreateViewHolder => 뷰 생성 - 인플레이팅 */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("recyclerView", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder vh = new ViewHolder(view, itemLayout);

        return vh;
    }

    /** onBindViewHolder => 리스트뷰의 getView 역할 */
    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Log.d("recyclerView", "onBindViewHolder");

        // 인플레이팅 되는 레이아웃 종류에 따라 구별
        /** Tab 1. nickName */
        if(itemLayout == R.layout.i_guest_list_item) {
            // Set 이미지
            String user_img_file_URL = search_user_aryLi.get(position).getUser_img_file();
            Log.d("recyclerView", "user_img_file_URL: " + user_img_file_URL);

            if(user_img_file_URL.equals("none")) {
                Glide
                    .with(context)
                    .load(R.drawable.default_profile)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(vh.profile_IV);
            }
            if(!user_img_file_URL.equals("none")) {

                if(user_img_file_URL.contains("http")) {
                    Glide
                        .with(context)
                        .load(user_img_file_URL)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(vh.profile_IV);
                }
                if(!user_img_file_URL.contains("http")) {
                    Glide
                        .with(context)
                        .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_file_URL)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(vh.profile_IV);
                }
            }

            // Set 닉네임
            vh.nickName_TV.setText(search_user_aryLi.get(position).getUser_nicName());
        }

        /** Tab 2. live */
        if(itemLayout == R.layout.i_room_list && request.equals("live")) {
            vh.title_TV_live.setText(roomList_aryLi_live.get(position).getBroadCast_title());
            vh.BJ_nic_TV_live.setText(roomList_aryLi_live.get(position).getBJ_nickName());
            vh.guest_count_TV_live.setText(String.valueOf(roomList_aryLi_live.get(position).getFinal_guest_count()));
            vh.like_count_TV_live.setText(String.valueOf(roomList_aryLi_live.get(position).getLike_count()));

            // live 프래그먼트인지, cast 프래그먼트인지 'BroadCast_end_orNot' 밸류 값을 가지고 판단하여 뷰 visibility를 조정
            // live 프래그먼트
            if(roomList_aryLi_live.get(position).getBroadCast_end_orNot().equals("false")) {
                vh.broadCast_time_Linlay_live.setVisibility(View.GONE);
                vh.live_count_IV_live.setVisibility(View.VISIBLE);
                vh.cast_count_IV_live.setVisibility(View.GONE);
            }
            // `broadCast_img_fileName` 밸류 값을 가지고, 서버로부터 이미지를 다운 받아 표시(glide)
            String broadCast_img_fileName = roomList_aryLi_live.get(position).getBroadCast_img_fileName();
            Log.d("adapter", "from: "+roomList_aryLi_live.get(position).getBroadCast_end_orNot() +"\nbroadCast_img_fileName: "+broadCast_img_fileName);
            if(broadCast_img_fileName.equals("none")) {
                Glide
                        .with(context)
                        .load(R.drawable.headphone2)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.broadCast_IV_live);
            }
            else {
                Glide
                        .with(context)
                        .load(Static.SERVER_URL_BROADCAST_IMG_FOLDER + broadCast_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.broadCast_IV_live);
            }
        }

        /** Tab 3. cast */
        if(itemLayout == R.layout.i_room_list && request.equals("cast")) {
            vh.title_TV_cast.setText(roomList_aryLi_cast.get(position).getBroadCast_title());
            vh.BJ_nic_TV_cast.setText(roomList_aryLi_cast.get(position).getBJ_nickName());
            vh.guest_count_TV_cast.setText(String.valueOf(roomList_aryLi_cast.get(position).getFinal_guest_count()));
            vh.like_count_TV_cast.setText(String.valueOf(roomList_aryLi_cast.get(position).getLike_count()));

            // live 프래그먼트인지, cast 프래그먼트인지 'BroadCast_end_orNot' 밸류 값을 가지고 판단하여 뷰 visibility를 조정
            // cast 프래그먼트
            if(roomList_aryLi_cast.get(position).getBroadCast_end_orNot().equals("true")) {
                vh.broadCast_time_Linlay_cast.setVisibility(View.VISIBLE);
                vh.live_count_IV_cast.setVisibility(View.GONE);
                vh.cast_count_IV_cast.setVisibility(View.VISIBLE);
                vh.broadCast_time_TV_cast.setText(roomList_aryLi_cast.get(position).getTotal_broadCast_time());
            }
            // `broadCast_img_fileName` 밸류 값을 가지고, 서버로부터 이미지를 다운 받아 표시(glide)
            String broadCast_img_fileName = roomList_aryLi_cast.get(position).getBroadCast_img_fileName();
            Log.d("adapter", "from: "+roomList_aryLi_cast.get(position).getBroadCast_end_orNot() +"\nbroadCast_img_fileName: "+broadCast_img_fileName);
            if(broadCast_img_fileName.equals("none")) {
                Glide
                        .with(context)
                        .load(R.drawable.headphone2)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.broadCast_IV_cast);
            }
            else {
                Glide
                        .with(context)
                        .load(Static.SERVER_URL_BROADCAST_IMG_FOLDER + broadCast_img_fileName)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.broadCast_IV_cast);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(itemLayout == R.layout.i_guest_list_item) {
            return search_user_aryLi.size();
        }
        if(itemLayout == R.layout.i_room_list && request.equals("live")) {
            return roomList_aryLi_live.size();
        }
        if(itemLayout == R.layout.i_room_list && request.equals("cast")) {
            return roomList_aryLi_cast.size();
        }
        else {
            return 0;
        }
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> live
     ---------------------------------------------------------------------------*/
    public void Go_live(String BroadCast_no) {

        Log.d("A_Check_Go_live", "Go_live 메소드 들어옴");
        // 방송 채널 정보 서버 전달
        AsyncTask_enter_room task = new AsyncTask_enter_room(context);
        task.execute(a_main_after_login.user_no, BroadCast_no, "live");
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> cast
     ---------------------------------------------------------------------------*/
    public void Go_cast(String BroadCast_no) {
        Log.d("A_Check_Go_cast", "Go_cast 메소드 들어옴");
        // 방송 채널 정보 서버 전달
        AsyncTask_enter_room task = new AsyncTask_enter_room(context);
        task.execute(a_main_after_login.user_no, BroadCast_no, "cast");
    }
}

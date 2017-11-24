package com.example.jyn.hearhere;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class Adapter_room_list extends ArrayAdapter {

    private Context context;
    private String requestFrom;
    private ArrayList<DataClass_room_list> roomList_aryLi;

    public Adapter_room_list(Context context, int resource, ArrayList<DataClass_room_list> roomList_aryLi, String requestFrom) {
        super(context, resource, roomList_aryLi);
        this.context = context;
        this.roomList_aryLi = roomList_aryLi;
        this.requestFrom = requestFrom;
    }

    private class ViewHolder {
        ImageView broadCast_IV, live_count_IV, cast_count_IV;
        TextView title_TV, BJ_nic_TV, guest_count_TV, like_count_TV, broadCast_time_TV;
        LinearLayout broadCast_time_Linlay;
    }


    @Override
    public int getCount() {
        return this.roomList_aryLi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.roomList_aryLi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.i_room_list, null);

            vh = new ViewHolder();

            vh.broadCast_IV = (ImageView)convertView.findViewById(R.id.broadCast_img);
            vh.title_TV = (TextView)convertView.findViewById(R.id.title);
            vh.BJ_nic_TV = (TextView)convertView.findViewById(R.id.BJ_nic);
            vh.guest_count_TV = (TextView)convertView.findViewById(R.id.guest_count);
            vh.like_count_TV = (TextView)convertView.findViewById(R.id.like_count);

            vh.live_count_IV = (ImageView)convertView.findViewById(R.id.live_count);
            vh.cast_count_IV = (ImageView)convertView.findViewById(R.id.cast_count);
            vh.broadCast_time_Linlay = (LinearLayout)convertView.findViewById(R.id.broadCast_time_layout);
            vh.broadCast_time_TV = (TextView)convertView.findViewById(R.id.broadCast_time);

            convertView.setTag(vh);
//            Log.d("메소드", "getView_ 뷰 새로 만들었다!");
        }
        else {
            vh = (ViewHolder)convertView.getTag();
//            Log.d("메소드", "getView_ 뷰 재활용함!");
        }

        // 생방송 중인 채널이 하나도 없을 때
        if(roomList_aryLi.size()==0) {

        }
        // 생방송 중인 채널이 하나라도 있을 때
        else if(roomList_aryLi.size()!=0) {
            // roomList_aryLi로 부터 데이터 가져와 표시
            vh.title_TV.setText(roomList_aryLi.get(position).getBroadCast_title());
            vh.BJ_nic_TV.setText(roomList_aryLi.get(position).getBJ_nickName());
            vh.guest_count_TV.setText(String.valueOf(roomList_aryLi.get(position).getFinal_guest_count()));
            vh.like_count_TV.setText(String.valueOf(roomList_aryLi.get(position).getLike_count()));

            // live 프래그먼트인지, cast 프래그먼트인지 'BroadCast_end_orNot' 밸류 값을 가지고 판단하여 뷰 visibility를 조정
            // live 프래그먼트
            if(roomList_aryLi.get(position).getBroadCast_end_orNot().equals("false")) {
                vh.broadCast_time_Linlay.setVisibility(View.GONE);
                vh.live_count_IV.setVisibility(View.VISIBLE);
                vh.cast_count_IV.setVisibility(View.GONE);
            }
            // cast 프래그먼트
            if(roomList_aryLi.get(position).getBroadCast_end_orNot().equals("true")) {

                vh.broadCast_time_Linlay.setVisibility(View.VISIBLE);
                vh.live_count_IV.setVisibility(View.GONE);
                vh.cast_count_IV.setVisibility(View.VISIBLE);
                vh.broadCast_time_TV.setText(roomList_aryLi.get(position).getTotal_broadCast_time());
            }

            // `broadCast_img_fileName` 밸류 값을 가지고, 서버로부터 이미지를 다운 받아 표시(glide)
            String broadCast_img_fileName = roomList_aryLi.get(position).getBroadCast_img_fileName();
            Log.d("adapter", "from: "+roomList_aryLi.get(position).getBroadCast_end_orNot() +"\nbroadCast_img_fileName: "+broadCast_img_fileName);
            if(broadCast_img_fileName.equals("none")) {
                Glide
                    .with(context)
                    .load(R.drawable.headphone2)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh.broadCast_IV);
            }
            else {
                Glide
                    .with(context)
                    .load(Static.SERVER_URL_BROADCAST_IMG_FOLDER + broadCast_img_fileName)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh.broadCast_IV);
            }
        }


        // 값이 다 셋팅된 view 객체를 반환
        return convertView;
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }
}

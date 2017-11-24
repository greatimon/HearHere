package com.example.jyn.hearhere;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class Adapter_noti_list extends BaseAdapter {

    private Context context;
    private ArrayList<DataClass_noti> noti_aryLi;

    public Adapter_noti_list(Context context, ArrayList<DataClass_noti> noti_aryLi) {
        this.context = context;
        this.noti_aryLi = noti_aryLi;
    }

    private class ViewHolder {
        LinearLayout container_LIN;
        ImageView noti_user_profile_IV, type_IV;
        TextView noti_comment_TV, timeStamp_TV;
    }


    @Override
    public int getCount() {
        return this.noti_aryLi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.noti_aryLi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.i_noti, null);

            vh = new ViewHolder();

            vh.container_LIN = (LinearLayout)convertView.findViewById(R.id.container);
            vh.noti_user_profile_IV = (ImageView)convertView.findViewById(R.id.noti_user_profile_img);
            vh.type_IV = (ImageView)convertView.findViewById(R.id.type);
            vh.noti_comment_TV = (TextView)convertView.findViewById(R.id.noti_comment);
            vh.timeStamp_TV = (TextView)convertView.findViewById(R.id.timeStamp);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder)convertView.getTag();
        }

        boolean broadCast_end_orNot = false;

        /** 아이템 백그라운드 색깔 */
        // 읽은 알림일 때
        if(noti_aryLi.get(position).isRead()) {
            vh.container_LIN.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        // 읽지않은 알림일 때
        if(!noti_aryLi.get(position).isRead()) {
            vh.container_LIN.setBackgroundColor(Color.parseColor("#eceff5"));
        }

        /** 알림 타입에 따른 아이콘 이미지 */
        String temp_noti_type = noti_aryLi.get(position).getNoti_type();

        // fan 관련 알림일 때
        if(temp_noti_type.equals("fan")) {
            Glide
                .with(context)
                .load(R.drawable.earphones)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(vh.type_IV);
        }
        // broadCast 관련 알림일 때
        if(temp_noti_type.equals("broadCast")) {

            broadCast_end_orNot = noti_aryLi.get(position).isBroadCast_onAir();
            // 현재 생방송중인 방
            if(!broadCast_end_orNot) {
                Glide
                    .with(context)
                    .load(R.drawable.record)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh.type_IV);
            }
            // 방송이 종료된 방
            if(broadCast_end_orNot) {
                Glide
                    .with(context)
                    .load(R.drawable.tape3)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(vh.type_IV);
            }
        }

        /** 알림 회원 프로필 사진 넣기 */
        String user_img_file_URL = noti_aryLi.get(position).getNoti_user_img_url();
        if(user_img_file_URL.equals("none")) {
            Glide
                .with(context)
                .load(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(vh.noti_user_profile_IV);
        }
        if(!user_img_file_URL.equals("none")) {

            if(user_img_file_URL.contains("http")) {
                Glide
                    .with(context)
                    .load(user_img_file_URL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(vh.noti_user_profile_IV);
            }
            if(!user_img_file_URL.contains("http")) {
                Glide
                    .with(context)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_file_URL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(vh.noti_user_profile_IV);
            }
        }

        /** comment 만들어서, setText */
        String temp_user_nickName = noti_aryLi.get(position).getNoti_user_nickName();
        String temp_broadCast_title = noti_aryLi.get(position).getBroadCast_title();
        String temp_comment="";
        if(temp_noti_type.equals("fan")) {
//            temp_comment = temp_user_nickName + "님이 당신의 Fan이 되었습니다.";
            temp_comment = "<strong>" + temp_user_nickName + "</strong>님이 당신의 Fan이 되었습니다.";
        }
        if(temp_noti_type.equals("broadCast")) { {
//            temp_comment = temp_user_nickName + "님이,\n  " + temp_broadCast_title + " - 라이브 방송을 시작했습니다."; <br>&nbsp;&nbsp;
            if(!broadCast_end_orNot) {
                temp_comment = "<strong>" + temp_user_nickName + "</strong>님이, " + temp_broadCast_title + " - 라이브 방송을 시작했습니다. (방송중)";
            }
            if(broadCast_end_orNot) {
                temp_comment = "<strong>" + temp_user_nickName + "</strong>님이, " + temp_broadCast_title + " - 라이브 방송을 시작했습니다. (방송 종료)";
            }

        }}
//        vh.noti_comment_TV.setText(temp_comment);
        vh.noti_comment_TV.setText(Html.fromHtml(temp_comment));

        /** timeStamp setText */
        vh.timeStamp_TV.setText(noti_aryLi.get(position).getTimeStamp());


        // 값이 다 셋팅된 view 객체를 반환
        return convertView;
    }

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }





















}

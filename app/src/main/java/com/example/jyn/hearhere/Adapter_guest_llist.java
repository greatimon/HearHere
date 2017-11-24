package com.example.jyn.hearhere;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class Adapter_guest_llist extends BaseAdapter {

    private Context context;
    private ArrayList<DataClass_guest_list> guest_aryLi;
    private View.OnClickListener mOnClickListener;

    private class ViewHolder {
        ImageView sender_profile_IV;
        TextView nickName_TV;
        Button add_FAN_BTN, subtract_FAN_BTN, force_to_out_BTN;
    }

    public Adapter_guest_llist(Context context, ArrayList<DataClass_guest_list> guest_aryLi, View.OnClickListener onClickListener) {
        this.context = context;
        this.guest_aryLi = guest_aryLi;
        mOnClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return this.guest_aryLi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.guest_aryLi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.i_guest_list_item, null);
            vh = new ViewHolder();

            vh.sender_profile_IV = (ImageView)convertView.findViewById(R.id.sender_profile_img);
            vh.nickName_TV = (TextView)convertView.findViewById(R.id.nickName);
            vh.add_FAN_BTN = (Button)convertView.findViewById(R.id.add_FAN);
            vh.subtract_FAN_BTN = (Button)convertView.findViewById(R.id.subtract_FAN);
            vh.force_to_out_BTN = (Button)convertView.findViewById(R.id.force_to_out);

            convertView.setTag(vh);
            Log.d("gust_list_adapter", "getView_ view created");
        }
        else {
            vh = (ViewHolder)convertView.getTag();
            Log.d("gust_list_adapter", "getView_ view recycled");
        }

        /** user_no 전달 + 클릭리스너 set */
        if(mOnClickListener != null) {
            vh.add_FAN_BTN.setTag(R.string.user_no,
                    guest_aryLi.get(position).getUser_no() + "&"        // user_no
                    + String.valueOf(position) + "&"                    // listView_ position
                    + guest_aryLi.get(position).getUser_nicName());     // nickName
            vh.add_FAN_BTN.setOnClickListener(mOnClickListener);

            vh.subtract_FAN_BTN.setTag(R.string.user_no,
                    guest_aryLi.get(position).getUser_no() + "&"        // user_no
                    + String.valueOf(position) + "&"                    // listView_ position
                    + guest_aryLi.get(position).getUser_nicName());     // nickName
            vh.subtract_FAN_BTN.setOnClickListener(mOnClickListener);

            vh.force_to_out_BTN.setTag(R.string.user_no,
                    guest_aryLi.get(position).getUser_no() + "&"        // user_no
                            + String.valueOf(position) + "&"                    // listView_ position
                            + guest_aryLi.get(position).getUser_nicName());     // nickName
            vh.force_to_out_BTN.setOnClickListener(mOnClickListener);
        }

        /** Set 이미지 */
        String user_img_file_URL = guest_aryLi.get(position).getUser_img_file();
        if(user_img_file_URL.equals("none")) {
            Glide
                .with(context)
                .load(R.drawable.default_profile)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(vh.sender_profile_IV);
        }
        if(!user_img_file_URL.equals("none")) {

            if(user_img_file_URL.contains("http")) {
                Glide
                    .with(context)
                    .load(user_img_file_URL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(vh.sender_profile_IV);
            }
            if(!user_img_file_URL.contains("http")) {
                Glide
                    .with(context)
                    .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_file_URL)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(vh.sender_profile_IV);
            }
        }

        /** Set 닉네임 */
        vh.nickName_TV.setText(guest_aryLi.get(position).getUser_nicName());

        /** 강퇴모드 구별*/
        if(!v_show_guests.force_to_out_mode) {
            vh.add_FAN_BTN.setVisibility(View.VISIBLE);
            vh.subtract_FAN_BTN.setVisibility(View.VISIBLE);
            vh.force_to_out_BTN.setVisibility(View.GONE);
        }
        if(v_show_guests.force_to_out_mode) {
            vh.add_FAN_BTN.setVisibility(View.GONE);
            vh.subtract_FAN_BTN.setVisibility(View.GONE);
            vh.force_to_out_BTN.setVisibility(View.VISIBLE);
        }

        /** Set 버튼 - Fan_onNot 확인 */
        String Fan_orNot = guest_aryLi.get(position).getFan_orNot();
        if(Fan_orNot.equals("true") && !v_show_guests.force_to_out_mode) {
            vh.add_FAN_BTN.setVisibility(View.GONE);
            vh.subtract_FAN_BTN.setVisibility(View.VISIBLE);
        }
        if(Fan_orNot.equals("false")&& !v_show_guests.force_to_out_mode) {
            vh.add_FAN_BTN.setVisibility(View.VISIBLE);
            vh.subtract_FAN_BTN.setVisibility(View.GONE);
        }

        //값이 다 셋팅된 view 객체를 반환
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

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


public class Adapter_Fan_BJ extends BaseAdapter {

    private Context context;
    private ArrayList<DataClass_guest_list> FAN_BJ_aryLi;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnClickListener_2;

    private class ViewHolder {
        ImageView sender_profile_IV;
        TextView nickName_TV;
        Button add_FAN_BTN, subtract_FAN_BTN;
    }

    public Adapter_Fan_BJ(Context context, ArrayList<DataClass_guest_list> FAN_BJ_aryLi,
                          View.OnClickListener onClickListener,
                          View.OnClickListener onClickListener_2) {
        this.context = context;
        this.FAN_BJ_aryLi = FAN_BJ_aryLi;
        mOnClickListener = onClickListener;
        mOnClickListener_2 = onClickListener_2;
    }

    @Override
    public int getCount() {
        return this.FAN_BJ_aryLi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.FAN_BJ_aryLi.get(position);
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

            convertView.setTag(vh);
            Log.d("Adapter_Fan_BJ_adapter", "getView_ view created");
        }
        else {
            vh = (ViewHolder)convertView.getTag();
            Log.d("Adapter_Fan_BJ_adapter", "getView_ view recycled");
        }

        /** user_no 전달 + 클릭리스너 SET - 1 */
        if(mOnClickListener != null) {
            vh.add_FAN_BTN.setTag(R.string.user_no,
                    FAN_BJ_aryLi.get(position).getUser_no() + "&"       // user_no
                    + String.valueOf(position) + "&"                    // listView_ position
                    + FAN_BJ_aryLi.get(position).getUser_nicName());    // nickName
            vh.add_FAN_BTN.setOnClickListener(mOnClickListener);

            vh.subtract_FAN_BTN.setTag(R.string.user_no,
                    FAN_BJ_aryLi.get(position).getUser_no() + "&"        // user_no
                    + String.valueOf(position) + "&"                     // listView_ position
                    + FAN_BJ_aryLi.get(position).getUser_nicName());     // nickName
            vh.subtract_FAN_BTN.setOnClickListener(mOnClickListener);
        }


        /** user_no 전달 + 클릭리스너 SET - 2 */
        if(mOnClickListener_2 != null) {
            vh.sender_profile_IV.setTag(R.string.user_no, FAN_BJ_aryLi.get(position).getUser_no());
            vh.sender_profile_IV.setOnClickListener(mOnClickListener_2);
            vh.nickName_TV.setTag(R.string.user_no, FAN_BJ_aryLi.get(position).getUser_no());
            vh.nickName_TV.setOnClickListener(mOnClickListener_2);
        }


        /** SET 이미지 */
        String user_img_file_URL = FAN_BJ_aryLi.get(position).getUser_img_file();
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

        /** SET 닉네임 */
        vh.nickName_TV.setText(FAN_BJ_aryLi.get(position).getUser_nicName());

        /** SET 버튼 - Fan_orNot 확인 */
        String Fan_orNot = FAN_BJ_aryLi.get(position).getFan_orNot();
        if(Fan_orNot.equals("true")) {
            vh.add_FAN_BTN.setVisibility(View.GONE);
            vh.subtract_FAN_BTN.setVisibility(View.VISIBLE);
        }
        if(Fan_orNot.equals("false")) {
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

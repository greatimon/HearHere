package com.example.jyn.hearhere;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class Adapter_msg extends BaseAdapter {

    private Context context;
    private ArrayList<DataClass_msg> msg_aryLi;
    private View.OnClickListener mOnClickListener;

    private static class ViewHolder {
        ImageView sender_profile_IV,send_img_IV;
        View serial_msg_profile_V, serial_msg_above_content_V, serial_msg_below_content_V, for_img_V;
        TextView im_BJ_TV, im_ME_TV, sender_nicName_TV, msg_content_TV, server_msg_TV;
        LinearLayout sender_info_LIN, layout_for_only_msg_content_Lin;
    }

    public Adapter_msg(Context context, ArrayList<DataClass_msg> msg_aryLi, View.OnClickListener onClickListener) {
        this.context = context;
        this.msg_aryLi = msg_aryLi;
        mOnClickListener = onClickListener;

    }

    @Override
    public int getCount() {
        return this.msg_aryLi.size();
    }

    @Override
    public Object getItem(int position) {
        return this.msg_aryLi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder vh;
        boolean cast_mode_endOrNoT = false;
        boolean cast_mode_startOrNoT = false;

        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.i_chat_message, null);
            vh = new ViewHolder();

            vh.sender_profile_IV = (ImageView)convertView.findViewById(R.id.sender_profile_img);
            vh.serial_msg_profile_V = (View)convertView.findViewById(R.id.serial_msg_profile_img);
            vh.sender_info_LIN = (LinearLayout)convertView.findViewById(R.id.sender_info);
            vh.layout_for_only_msg_content_Lin = (LinearLayout)convertView.findViewById(R.id.layout_for_only_msg_content);
            vh.im_BJ_TV = (TextView)convertView.findViewById(R.id.im_BJ);
            vh.im_ME_TV = (TextView)convertView.findViewById(R.id.im_ME);
            vh.sender_nicName_TV = (TextView)convertView.findViewById(R.id.sender_nicName);
            vh.serial_msg_above_content_V = (View)convertView.findViewById(R.id.serial_msg_above_content);
            vh.msg_content_TV = (TextView)convertView.findViewById(R.id.msg_content);
            vh.serial_msg_below_content_V = (View)convertView.findViewById(R.id.serial_msg_below_content);
            vh.server_msg_TV = (TextView)convertView.findViewById(R.id.server_msg);
            vh.send_img_IV = (ImageView)convertView.findViewById(R.id.send_img);
            vh.for_img_V = (View)convertView.findViewById(R.id.for_img);

            convertView.setTag(vh);
            Log.d("메소드", "getView_ 뷰 새로 만들었다!");
        }
        else {
            vh = (ViewHolder)convertView.getTag();
            Log.d("메소드", "getView_ 뷰 재활용함!");
        }

        /** user_no 전달 + 클릭리스너 set */
        if(mOnClickListener != null) {
            vh.sender_profile_IV.setTag(R.string.user_no, msg_aryLi.get(position).getUser_no());
            vh.sender_profile_IV.setOnClickListener(mOnClickListener);
        }

        /** img_url 전달 + 클릭리스너 set */
        if(mOnClickListener != null) {
            vh.send_img_IV.setTag(R.string.send_img_url, msg_aryLi.get(position).getMsg_content());
            vh.send_img_IV.setTag(R.string.send_user_nickName, msg_aryLi.get(position).getUser_nicName());
            vh.send_img_IV.setTag(R.string.send_user_profile_img_url, msg_aryLi.get(position).getUser_img_file());
            vh.send_img_IV.setOnClickListener(mOnClickListener);
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 onAir인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("onAir")) {
            vh.sender_profile_IV.setVisibility(View.GONE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
            vh.server_msg_TV.setVisibility(View.VISIBLE);
            vh.send_img_IV.setVisibility(View.GONE);

            if(a_room.from.equals("live")) {
                // 받은 메시지의 발송인이 나라면
                if(String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    String server_msg = "[안내] 방송이 시작되었습니다.";
                    vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                    vh.server_msg_TV.setText(server_msg);
                }
                // 받은 메시지의 발송인이 내가 아니라면
                if(!String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    String server_msg = msg_aryLi.get(position).getUser_nicName() + "님이 입장하셨습니다.";
                    vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                    vh.server_msg_TV.setText(server_msg);
                }
            }
            if(a_room.from.equals("cast")) {

                for(int i=0; i<msg_aryLi.indexOf(getItem(position)); i++) {
                    if(msg_aryLi.get(i).getType().equals("onAir")) {
                        cast_mode_startOrNoT = true;
                        break;
                    }
                }

                if(!cast_mode_startOrNoT) {
                    String server_msg = "[안내] 방송이 시작되었습니다.";
                    vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                    vh.server_msg_TV.setText(server_msg);
                }
                if(cast_mode_startOrNoT) {
//                    vh.sender_profile_IV.setVisibility(View.GONE);
//                    vh.serial_msg_profile_V.setVisibility(View.GONE);
//                    vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
//                    vh.server_msg_TV.setVisibility(View.GONE);
                    String server_msg = msg_aryLi.get(position).getUser_nicName() + "님이 입장하셨습니다.";
                    vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                    vh.server_msg_TV.setText(server_msg);
                }
            }

        }


        /**---------------------------------------------------------------------------
         서버 메시지가 continue인 경우 -- 아무것도 출력하지 않는다
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("continue")) {
            vh.sender_profile_IV.setVisibility(View.GONE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
            vh.server_msg_TV.setVisibility(View.GONE);
            vh.send_img_IV.setVisibility(View.GONE);
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 retry인 경우 -- 재접속한 본인에게만, 재접속 하였다는 메시지를 띄운다
                                     다른 사람들에게는 그냥 접속하였다는 원래 메시지를 띄운다
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("retry")) {
            if(msg_aryLi.get(position).getUser_no() == Integer.parseInt(a_main_after_login.user_no)) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.VISIBLE);
                vh.send_img_IV.setVisibility(View.GONE);

                String server_msg = "[안내] 네트워크가 재연결 되었습니다";
                vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                vh.server_msg_TV.setText(server_msg);
            }
            if(msg_aryLi.get(position).getUser_no() != Integer.parseInt(a_main_after_login.user_no)) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.VISIBLE);
                vh.send_img_IV.setVisibility(View.GONE);

                String server_msg = msg_aryLi.get(position).getUser_nicName() + "님이 재접속하였습니다";
                vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                vh.server_msg_TV.setText(server_msg);
            }
        }

        /**---------------------------------------------------------------------------
         서버 메시지가 end인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("end")) {
            vh.sender_profile_IV.setVisibility(View.GONE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
            vh.server_msg_TV.setVisibility(View.VISIBLE);
            vh.send_img_IV.setVisibility(View.GONE);

            String server_msg = "[안내] 방송이 종료되었습니다.";
            vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
            vh.server_msg_TV.setText(server_msg);
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 out인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("out")) {

            for(int i=0; i<msg_aryLi.indexOf(getItem(position)); i++) {
                if(msg_aryLi.get(i).getType().equals("end")) {
                    cast_mode_endOrNoT = true;
                    break;
                }
            }

            if(!cast_mode_endOrNoT) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.VISIBLE);
                vh.send_img_IV.setVisibility(View.GONE);

                String server_msg = msg_aryLi.get(position).getUser_nicName() + "님이 나가셨습니다.";
                vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
                vh.server_msg_TV.setText(server_msg);
            }
            if(cast_mode_endOrNoT) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.GONE);
                vh.send_img_IV.setVisibility(View.GONE);
            }
        }

        /**---------------------------------------------------------------------------
         서버 메시지가 red_card인 경우 - 강퇴인경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("red_card")) {
            for(int i=0; i<msg_aryLi.indexOf(getItem(position)); i++) {
                if(msg_aryLi.get(i).getType().equals("end")) {
                    cast_mode_endOrNoT = true;
                    break;
                }
            }

            if(!cast_mode_endOrNoT) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.VISIBLE);
                vh.send_img_IV.setVisibility(View.GONE);

                String server_msg = msg_aryLi.get(position).getUser_nicName() + "님이 강제 퇴장되었습니다.";
                vh.server_msg_TV.setTextColor(Color.parseColor("#f26857"));
                vh.server_msg_TV.setText(server_msg);
            }
            if(cast_mode_endOrNoT) {
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.GONE);
                vh.send_img_IV.setVisibility(View.GONE);
            }
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 normal인 경우, welcome_word인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("normal") || msg_aryLi.get(position).getType().equals("welcome_word")) {
            vh.sender_profile_IV.setVisibility(View.VISIBLE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.VISIBLE);
            vh.server_msg_TV.setVisibility(View.GONE);
            vh.send_img_IV.setVisibility(View.GONE);

            // 같은 사람이 연속으로 메시지를 보냈을때
            if((msg_aryLi.get(position).getSerial_orNot().equals("true") && getCount()>1 && msg_aryLi.get(position-1).getType().equals("normal")) ||
                    (msg_aryLi.get(position).getSerial_orNot().equals("true") && msg_aryLi.get(position-1).getType().equals("v_for_img") && msg_aryLi.get(position-1).getUser_no() == Integer.parseInt(a_main_after_login.user_no))) {
//            if(position-1 >= 0 && msg_aryLi.get(position).getSerial_orNot().equals("true") && getCount()>1 && (msg_aryLi.get(position-1).getType().equals("normal") || msg_aryLi.get(position-1).getType().equals("send_img"))) {

                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.VISIBLE);
                vh.sender_info_LIN.setVisibility(View.GONE);
                vh.serial_msg_above_content_V.setVisibility(View.VISIBLE);
                vh.msg_content_TV.setVisibility(View.VISIBLE);
//            vh.serial_msg_below_content_V.setVisibility(View.VISIBLE);
            }

           // 같은 사람이 연속으로 메시지를 보내지 않았을 때
            else if(msg_aryLi.get(position).getSerial_orNot().equals("false") || !msg_aryLi.get(position-1).getType().equals("normal") || getCount()<=1
                    || !msg_aryLi.get(position-1).getType().equals("send_img") || msg_aryLi.get(position-1).getType().equals("v_for_img")) {
                Log.d("ADD_", "msg_aryLi.get(position).getMsg_content(): " + msg_aryLi.get(position).getMsg_content());
                Log.d("ADD_", "연속 아니다~~~~~~~~~~~연속 아니니다~~~~~~~~");

                vh.sender_profile_IV.setVisibility(View.VISIBLE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.sender_info_LIN.setVisibility(View.VISIBLE);
                vh.serial_msg_above_content_V.setVisibility(View.GONE);
                vh.msg_content_TV.setVisibility(View.VISIBLE);
//                vh.serial_msg_below_content_V.setVisibility(View.GONE);

                /** 사진 파일이름을 가지고 서버로 부터 이미지 다운로드 */
                String user_img_fileName = msg_aryLi.get(position).getUser_img_file();
                Log.d("사진체크", "==============================");
                Log.d("사진체크", "user_img_fileName: " + user_img_fileName);
                Log.d("사진체크", "position: " + position);
                Log.d("사진체크", "==============================");
                if(user_img_fileName.equals("none")) {
                    Glide
                        .with(context)
                        .load(R.drawable.default_profile)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(vh.sender_profile_IV);
                }
                if(!user_img_fileName.equals("none")) {

                    if(user_img_fileName.contains("http")) {
                        Glide
                            .with(context)
                            .load(user_img_fileName)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(vh.sender_profile_IV);
                    }
                    if(!user_img_fileName.contains("http")) {
                        Glide
                            .with(context)
                            .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(vh.sender_profile_IV);
                    }
                }

                // sender가 호스트일 때
                if(msg_aryLi.get(position).getHost_orNot().equals("true")) {
                    vh.im_BJ_TV.setVisibility(View.VISIBLE);
                }
                // sender가 호스트가 아닐 때
                else if(msg_aryLi.get(position).getHost_orNot().equals("false")) {
                    vh.im_BJ_TV.setVisibility(View.GONE);
                }

                // sender가 나일때
                if(String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    vh.im_ME_TV.setVisibility(View.VISIBLE);
                }
                // sender가 내가 아닐때
                if(!String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    vh.im_ME_TV.setVisibility(View.GONE);
                }
            }

            if(!cast_mode_endOrNoT && !cast_mode_startOrNoT) {
                // 닉네임이랑, 메시지 내용 표시
                vh.sender_nicName_TV.setText(msg_aryLi.get(position).getUser_nicName());
                vh.msg_content_TV.setText(msg_aryLi.get(position).getMsg_content());
            }

        }

        /**---------------------------------------------------------------------------
         서버 메시지가 disconnect인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("disconnect")) {
            vh.sender_profile_IV.setVisibility(View.GONE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
            vh.server_msg_TV.setVisibility(View.VISIBLE);
            vh.send_img_IV.setVisibility(View.GONE);

            String User_nicName = msg_aryLi.get(position).getUser_nicName();

            String server_msg = String.valueOf(User_nicName)+"님의 접속이 끊겼습니다.";
            vh.server_msg_TV.setTextColor(Color.parseColor("#ffffff"));
            vh.server_msg_TV.setText(server_msg);
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 send_img인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("send_img")) {
            vh.layout_for_only_msg_content_Lin.setVisibility(View.VISIBLE);
            vh.msg_content_TV.setVisibility(View.GONE);
            vh.send_img_IV.setVisibility(View.VISIBLE);
            vh.server_msg_TV.setVisibility(View.GONE);

            // 같은 사람이 연속으로 메시지를 보냈을때
            if(msg_aryLi.get(position).getSerial_orNot().equals("true") && getCount()>1 &&
                    (msg_aryLi.get(position-1).getType().equals("normal")) || msg_aryLi.get(position-1).getType().equals("v_for_img")) {
//                if(msg_aryLi.get(position-1).getType().equals("v_for_img")) {
//                    if(msg_aryLi.get(position-2).getUser_no() == Integer.parseInt(a_main_after_login.user_no)) {
//                        Log.d("ADD_", "연속메시지");
//                        vh.sender_profile_IV.setVisibility(View.GONE);
//                        vh.serial_msg_profile_V.setVisibility(View.VISIBLE);
//                        vh.sender_info_LIN.setVisibility(View.GONE);
//                        vh.serial_msg_above_content_V.setVisibility(View.VISIBLE);
//                    }
//                }
                Log.d("ADD_", "연속메시지");
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.VISIBLE);
                vh.sender_info_LIN.setVisibility(View.GONE);
                vh.serial_msg_above_content_V.setVisibility(View.VISIBLE);
            }

            // 같은 사람이 연속으로 메시지를 보내지 않았을 때
//            if(msg_aryLi.get(position).getSerial_orNot().equals("false") || !msg_aryLi.get(position-1).getType().equals("normal")
//                    || !msg_aryLi.get(position-1).getType().equals("send_img") ||getCount()<=1) {
            if(msg_aryLi.get(position).getSerial_orNot().equals("false") || getCount()<=1) {
                Log.d("ADD_", "비비비연속메시지");
                vh.sender_profile_IV.setVisibility(View.VISIBLE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.sender_info_LIN.setVisibility(View.VISIBLE);
                vh.serial_msg_above_content_V.setVisibility(View.GONE);

                /** 사진 파일이름을 가지고 서버로 부터 이미지 다운로드 */
                String user_img_fileName = msg_aryLi.get(position).getUser_img_file();
                Log.d("사진체크", "==============================");
                Log.d("사진체크", "user_img_fileName: " + user_img_fileName);
                Log.d("사진체크", "position: " + position);
                Log.d("사진체크", "==============================");
                if(user_img_fileName.equals("none")) {
                    Glide
                        .with(context)
                        .load(R.drawable.default_profile)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .bitmapTransform(new CropCircleTransformation(context))
                        .into(vh.sender_profile_IV);
                }
                if(!user_img_fileName.equals("none")) {

                    if(user_img_fileName.contains("http")) {
                        Glide
                            .with(context)
                            .load(user_img_fileName)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(vh.sender_profile_IV);
                    }
                    if(!user_img_fileName.contains("http")) {
                        Glide
                            .with(context)
                            .load(Static.SERVER_URL_PROFILE_FOLDER + user_img_fileName)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .bitmapTransform(new CropCircleTransformation(context))
                            .into(vh.sender_profile_IV);
                    }
                }

                // sender가 호스트일 때
                if(msg_aryLi.get(position).getHost_orNot().equals("true")) {
                    vh.im_BJ_TV.setVisibility(View.VISIBLE);
                }
                // sender가 호스트가 아닐 때
                else if(msg_aryLi.get(position).getHost_orNot().equals("false")) {
                    vh.im_BJ_TV.setVisibility(View.GONE);
                }

                // sender가 나일때
                if(String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    vh.im_ME_TV.setVisibility(View.VISIBLE);
                }
                // sender가 내가 아닐때
                if(!String.valueOf(msg_aryLi.get(position).getUser_no()).equals(a_main_after_login.user_no)) {
                    vh.im_ME_TV.setVisibility(View.GONE);
                }
            }

            // 닉네임표시
            vh.sender_nicName_TV.setText(msg_aryLi.get(position).getUser_nicName());

            // 이미지 표시
            String temp = (String)vh.send_img_IV.getTag();
            Log.d("same_img", "getTag_temp: " + temp);
            if(temp == null) {
                temp = "none";
            }
            Log.d("same_img", "msg_aryLi.get(position).getMsg_content(): " + msg_aryLi.get(position).getMsg_content());


            // 이전 이미지의 파일 URL과 다르다면 이미지를 다운받아 셋팅하고, 해당 파일 URL이름으로 태그를 붙인다
            if(!temp.equals(msg_aryLi.get(position).getMsg_content())) {
//                vh.send_img_IV.setVisibility(View.GONE);
                /** 이미지 표시 */
                // 1. Glide
//                Glide
//                    .with(context)
//                    .load(Static.SERVER_URL_ROOM_SENDING_IMG_FOLDER + msg_aryLi.get(position).getMsg_content()) //vh.send_img_IV
//                    .asBitmap()
//                    .placeholder(R.drawable.loading)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                    .diskCacheStrategy(DiskCacheStrategy.NONE)
////                    .skipMemoryCache(true)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            vh.send_img_IV.setImageBitmap(resource);
//                            vh.send_img_IV.setTag(msg_aryLi.get(position).getMsg_content());
////                            vh.send_img_IV.setVisibility(View.VISIBLE);
//                            Log.d("same_img", "새 이미지!! 다운받아서 넣음!!");
//                        }
//                    });

                // 2. Picasso
                Picasso.with(context)
                        .load(Static.SERVER_URL_ROOM_SENDING_IMG_FOLDER + msg_aryLi.get(position).getMsg_content())
//                        .placeholder(R.drawable.loading1)
                        .into(vh.send_img_IV);
            }

            // 이전 이미지의 파일 URL String 값이 같다면 아무것도 안한다
            if(temp.equals(msg_aryLi.get(position).getMsg_content())) {
                Log.d("same_img", "이미지 재활용!! 새로 다운 받아 넣지 않고, 뷰 그대로 활용!!");
            }

        }


        /**---------------------------------------------------------------------------
         서버 메시지가 v_for_img 인 경우 -- 마지막 아이템 포커스를 위해서 height가 얇은 View를 추가함
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("v_for_img")) {
            if(msg_aryLi.get(position-1).getType().equals("send_img")) {
                Log.d("send_img", "v_for_img!!!!!!!!!!!!!!!!!!!");
                vh.sender_profile_IV.setVisibility(View.GONE);
                vh.serial_msg_profile_V.setVisibility(View.GONE);
                vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
                vh.server_msg_TV.setVisibility(View.GONE);
                vh.for_img_V.setVisibility(View.VISIBLE);
            }
        }


        /**---------------------------------------------------------------------------
         서버 메시지가 like_clicked 인 경우
         ---------------------------------------------------------------------------*/
        if(msg_aryLi.get(position).getType().equals("like_clicked")) {
            vh.sender_profile_IV.setVisibility(View.GONE);
            vh.serial_msg_profile_V.setVisibility(View.GONE);
            vh.layout_for_only_msg_content_Lin.setVisibility(View.GONE);
            vh.server_msg_TV.setVisibility(View.VISIBLE);
            vh.send_img_IV.setVisibility(View.GONE);

            String like_clicked_user_nicnName = msg_aryLi.get(position).getMsg_content();
            String server_msg = like_clicked_user_nicnName + "님이 좋아요를 누르셨습니다.";
//            vh.server_msg_TV.setBackgroundColor(Color.parseColor("#AA000000"));
            vh.server_msg_TV.setTextColor(Color.parseColor("#FFD54F"));
            vh.server_msg_TV.setText(server_msg);
        }


        else {}

        //값이 다 셋팅된 view 객체를 반환
        Log.d("ADD_", "------------------------------------");
        return convertView;

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}

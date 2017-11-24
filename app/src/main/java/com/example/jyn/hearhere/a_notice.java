package com.example.jyn.hearhere;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JYN on 2017-07-02.
 */

public class a_notice extends Activity{
    LinearLayout notice_1_title_LinLay, notice_1_content_LinLay;
    LinearLayout notice_2_title_LinLay, notice_2_content_LinLay;
    LinearLayout notice_3_title_LinLay, notice_3_content_LinLay;
    LinearLayout notice_4_title_LinLay, notice_4_content_LinLay;
    LinearLayout notice_5_title_LinLay, notice_5_content_LinLay;

    ImageView bracket_1_imgV;
    ImageView bracket_2_imgV;
    ImageView bracket_3_imgV;
    ImageView bracket_4_imgV;
    ImageView bracket_5_imgV;

    TextView title_1_txtV, date_1_txtV, content_1_txtV;
    TextView title_2_txtV, date_2_txtV, content_2_txtV;
    TextView title_3_txtV, date_3_txtV, content_3_txtV;
    TextView title_4_txtV, date_4_txtV, content_4_txtV;
    TextView title_5_txtV, date_5_txtV, content_5_txtV;

    int notice_1_int=0;
    int notice_2_int=0;
    int notice_3_int=0;
    int notice_4_int=0;
    int notice_5_int=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_notice);

        /** 뷰찾기 */
        notice_1_title_LinLay = (LinearLayout)findViewById(R.id.notice_1_title);
        notice_2_title_LinLay = (LinearLayout)findViewById(R.id.notice_2_title);
        notice_3_title_LinLay = (LinearLayout)findViewById(R.id.notice_3_title);
        notice_4_title_LinLay = (LinearLayout)findViewById(R.id.notice_4_title);
        notice_5_title_LinLay = (LinearLayout)findViewById(R.id.notice_5_title);
        notice_1_content_LinLay = (LinearLayout)findViewById(R.id.notice_1_content);
        notice_2_content_LinLay = (LinearLayout)findViewById(R.id.notice_2_content);
        notice_3_content_LinLay = (LinearLayout)findViewById(R.id.notice_3_content);
        notice_4_content_LinLay = (LinearLayout)findViewById(R.id.notice_4_content);
        notice_5_content_LinLay = (LinearLayout)findViewById(R.id.notice_5_content);

        bracket_1_imgV = (ImageView)findViewById(R.id.bracket_1);
        bracket_2_imgV = (ImageView)findViewById(R.id.bracket_2);
        bracket_3_imgV = (ImageView)findViewById(R.id.bracket_3);
        bracket_4_imgV = (ImageView)findViewById(R.id.bracket_4);
        bracket_5_imgV = (ImageView)findViewById(R.id.bracket_5);

        title_1_txtV = (TextView)findViewById(R.id.title_1);
        title_2_txtV = (TextView)findViewById(R.id.title_2);
        title_3_txtV = (TextView)findViewById(R.id.title_3);
        title_4_txtV = (TextView)findViewById(R.id.title_4);
        title_5_txtV = (TextView)findViewById(R.id.title_5);

        date_1_txtV = (TextView)findViewById(R.id.date_1);
        date_2_txtV = (TextView)findViewById(R.id.date_2);
        date_3_txtV = (TextView)findViewById(R.id.date_3);
        date_4_txtV = (TextView)findViewById(R.id.date_4);
        date_5_txtV = (TextView)findViewById(R.id.date_5);

        content_1_txtV = (TextView)findViewById(R.id.content_1);
        content_2_txtV = (TextView)findViewById(R.id.content_2);
        content_3_txtV = (TextView)findViewById(R.id.content_3);
        content_4_txtV = (TextView)findViewById(R.id.content_4);
        content_5_txtV = (TextView)findViewById(R.id.content_5);

        title_1_txtV.setText(R.string.title_1_txtV);
        date_1_txtV.setText(R.string.date_1_txtV);
        content_1_txtV.setText(R.string.content_1_txtV);
        title_2_txtV.setText(R.string.title_2_txtV);
        date_2_txtV.setText(R.string.date_2_txtV);
        content_2_txtV.setText(R.string.content_2_txtV);
        title_3_txtV.setText(R.string.title_3_txtV);
        date_3_txtV.setText(R.string.date_3_txtV);
        content_3_txtV.setText(R.string.content_3_txtV);
        title_4_txtV.setText(R.string.title_4_txtV);
        date_4_txtV.setText(R.string.date_4_txtV);
        content_4_txtV.setText(R.string.content_4_txtV);
        title_5_txtV.setText(R.string.title_5_txtV);
        date_5_txtV.setText(R.string.date_5_txtV);
        content_5_txtV.setText(R.string.content_5_txtV);
    }

    /** 공지사항 1 클릭*/
    public void on_Notice_1_Clicked(View view) {
        notice_1_int++;

        // 홀수_ 선택
        if(notice_1_int%2!=0) {
            notice_1_title_LinLay.setBackgroundColor(Color.parseColor("#FFC107"));
            title_1_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            date_1_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            bracket_1_imgV.setImageResource(R.drawable.down_bracket);

            notice_1_content_LinLay.setVisibility(View.VISIBLE);
        }
        // 짝수_ 선택해제
        if(notice_1_int%2==0) {
            notice_1_title_LinLay.setBackgroundColor(Color.parseColor("#fafcfc"));
            title_1_txtV.setTextColor(Color.parseColor("#BB000000"));
            date_1_txtV.setTextColor(Color.parseColor("#BB000000"));
            bracket_1_imgV.setImageResource(R.drawable.right_bracket);

            notice_1_content_LinLay.setVisibility(View.GONE);
        }
    }

    /** 공지사항 2 클릭*/
    public void on_Notice_2_Clicked(View view) {
        notice_2_int++;

        // 홀수_ 선택
        if(notice_2_int%2!=0) {
            notice_2_title_LinLay.setBackgroundColor(Color.parseColor("#FFC107"));
            title_2_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            date_2_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            bracket_2_imgV.setImageResource(R.drawable.down_bracket);

            notice_2_content_LinLay.setVisibility(View.VISIBLE);
        }
        // 짝수_ 선택해제
        if(notice_2_int%2==0) {
            notice_2_title_LinLay.setBackgroundColor(Color.parseColor("#fafcfc"));
            title_2_txtV.setTextColor(Color.parseColor("#BB000000"));
            date_2_txtV.setTextColor(Color.parseColor("#BB000000"));
            bracket_2_imgV.setImageResource(R.drawable.right_bracket);

            notice_2_content_LinLay.setVisibility(View.GONE);
        }
    }

    /** 공지사항 3 클릭*/
    public void on_Notice_3_Clicked(View view) {
        notice_3_int++;

        // 홀수_ 선택
        if(notice_3_int%2!=0) {
            notice_3_title_LinLay.setBackgroundColor(Color.parseColor("#FFC107"));
            title_3_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            date_3_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            bracket_3_imgV.setImageResource(R.drawable.down_bracket);

            notice_3_content_LinLay.setVisibility(View.VISIBLE);
        }
        // 짝수_ 선택해제
        if(notice_3_int%2==0) {
            notice_3_title_LinLay.setBackgroundColor(Color.parseColor("#fafcfc"));
            title_3_txtV.setTextColor(Color.parseColor("#BB000000"));
            date_3_txtV.setTextColor(Color.parseColor("#BB000000"));
            bracket_3_imgV.setImageResource(R.drawable.right_bracket);

            notice_3_content_LinLay.setVisibility(View.GONE);
        }
    }

    /** 공지사항 4 클릭*/
    public void on_Notice_4_Clicked(View view) {
        notice_4_int++;

        // 홀수_ 선택
        if(notice_4_int%2!=0) {
            notice_4_title_LinLay.setBackgroundColor(Color.parseColor("#FFC107"));
            title_4_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            date_4_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            bracket_4_imgV.setImageResource(R.drawable.down_bracket);

            notice_4_content_LinLay.setVisibility(View.VISIBLE);
        }
        // 짝수_ 선택해제
        if(notice_4_int%2==0) {
            notice_4_title_LinLay.setBackgroundColor(Color.parseColor("#fafcfc"));
            title_4_txtV.setTextColor(Color.parseColor("#BB000000"));
            date_4_txtV.setTextColor(Color.parseColor("#BB000000"));
            bracket_4_imgV.setImageResource(R.drawable.right_bracket);

            notice_4_content_LinLay.setVisibility(View.GONE);
        }
    }

    /** 공지사항 5 클릭*/
    public void on_Notice_5_Clicked(View view) {
        notice_5_int++;

        // 홀수_ 선택
        if(notice_5_int%2!=0) {
            notice_5_title_LinLay.setBackgroundColor(Color.parseColor("#FFC107"));
            title_5_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            date_5_txtV.setTextColor(Color.parseColor("#FFFFFF"));
            bracket_5_imgV.setImageResource(R.drawable.down_bracket);

            notice_5_content_LinLay.setVisibility(View.VISIBLE);
        }
        // 짝수_ 선택해제
        if(notice_5_int%2==0) {
            notice_5_title_LinLay.setBackgroundColor(Color.parseColor("#fafcfc"));
            title_5_txtV.setTextColor(Color.parseColor("#BB000000"));
            date_5_txtV.setTextColor(Color.parseColor("#BB000000"));
            bracket_5_imgV.setImageResource(R.drawable.right_bracket);

            notice_5_content_LinLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**---------------------------------------------------------------------------
     액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     ---------------------------------------------------------------------------*/
    public void backClicked(View view) {
        onBackPressed();
    }
}

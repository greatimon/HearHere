package com.example.jyn.hearhere;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYN on 2017-08-16.
 */

public class a_setting_noti extends Activity {

    final String TAG = "a_setting_noti";

    Switch Fcm_live_switch, Fcm_Fan_switch, Fcm_time_switch;
    LinearLayout start_time_LIN, end_time_LIN;
    TextView text_start_TV, text_start_time_TV, text_end_TV, text_end_time_TV, notice_time_range_TV;
    ImageView img_start_down_triangle_IV, img_end_down_triangle_IV;
    View underView_for_notice_time_range;

    CustomTimePickerDialog customTimePickerDialog_start_time;
    CustomTimePickerDialog customTimePickerDialog_end_time;

    String Fcm_start_Time_hour, Fcm_end_Time_hour;
    String Fcm_start_Time_min, Fcm_end_Time_min;

    String temp_start_Time_hour, temp_end_Time_hour;
    String temp_start_Time_min, temp_end_Time_min;


    /**
     * ---------------------------------------------------------------------------
     * 생명주기 ==> onCreate
     * ---------------------------------------------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_setting_noti);

        /** 테스트 코드 -- 쉐어드 초기화*/
        SharedPreferences Fcm_time_shared = getSharedPreferences("Fcm_time", MODE_PRIVATE);
//        SharedPreferences.Editor Fcm_time_edit = Fcm_time_shared.edit();
//        Fcm_time_edit.clear().apply();

        /** 뷰 찾기 */
        Fcm_live_switch = (Switch)findViewById(R.id.Fcm_live);
        Fcm_Fan_switch = (Switch)findViewById(R.id.Fcm_Fan);
        Fcm_time_switch = (Switch)findViewById(R.id.Fcm_time);
        start_time_LIN = (LinearLayout)findViewById(R.id.start_time_layout);
        end_time_LIN = (LinearLayout)findViewById(R.id.end_time_layout);
        text_start_TV = (TextView)findViewById(R.id.text_start);
        text_start_time_TV = (TextView)findViewById(R.id.text_start_time);
        text_end_TV = (TextView)findViewById(R.id.text_end);
        text_end_time_TV = (TextView) findViewById(R.id.text_end_time);
        img_start_down_triangle_IV = (ImageView)findViewById(R.id.img_start_down_triangle);
        img_end_down_triangle_IV = (ImageView)findViewById(R.id.img_end_down_triangle);
        notice_time_range_TV = (TextView)findViewById(R.id.notice_time_range);
        underView_for_notice_time_range = (View)findViewById(R.id.underView_for_notice_time_range);


        /** 설정 Shared 확인 */
        // 1. Fcm_live
        SharedPreferences Fcm_live_shared = getSharedPreferences("Fcm_live", MODE_PRIVATE);
        String Fcm_live_Accept = Fcm_live_shared.getString("Fcm_live", "true");
        Log.d("setting_shared", "OnCreate_Fcm_live_state: " + Fcm_live_Accept);
        if(Fcm_live_Accept.equals("true")) {
            Fcm_live_switch.setChecked(true);
        }
        if(Fcm_live_Accept.equals("false")) {
            Fcm_live_switch.setChecked(false);
        }

        // 2. Fcm_Fan
        SharedPreferences Fcm_Fan_shared = getSharedPreferences("Fcm_Fan", MODE_PRIVATE);
        String Fcm_Fan_Accept = Fcm_Fan_shared.getString("Fcm_Fan", "true");
        Log.d("setting_shared", "OnCreate_Fcm_Fan_state: " + Fcm_Fan_Accept);
        if(Fcm_Fan_Accept.equals("true")) {
            Fcm_Fan_switch.setChecked(true);
        }
        if(Fcm_Fan_Accept.equals("false")) {
            Fcm_Fan_switch.setChecked(false);
        }

        // 3. Fcm_time
//        SharedPreferences Fcm_time_shared = getSharedPreferences("Fcm_time", MODE_PRIVATE);
        String Fcm_Time_filter = Fcm_time_shared.getString("Fcm_time", "false");
        Fcm_start_Time_hour = Fcm_time_shared.getString("start_hour", "22");
        Fcm_start_Time_min = Fcm_time_shared.getString("start_min", "0");
        Fcm_end_Time_hour = Fcm_time_shared.getString("end_hour", "8");
        Fcm_end_Time_min = Fcm_time_shared.getString("end_min", "0");

        Log.d("setting_shared", "OnCreate_Fcm_Time_state: " + Fcm_Time_filter);
        if(Fcm_Time_filter.equals("true")) {
            Fcm_time_switch.setChecked(true);
        }
        if(Fcm_Time_filter.equals("false")) {
            Fcm_time_switch.setChecked(false);
        }


        /** Fcm_time_switch 설정 */
        if (Fcm_time_switch.isChecked()) {
            text_start_TV.setTextColor(Color.parseColor("#262626"));
            text_start_time_TV.setTextColor(Color.parseColor("#4a99a5"));
            img_start_down_triangle_IV.setImageResource(R.drawable.down_triangle_active2);

            text_end_TV.setTextColor(Color.parseColor("#262626"));
            text_end_time_TV.setTextColor(Color.parseColor("#4a99a5"));
            img_end_down_triangle_IV.setImageResource(R.drawable.down_triangle_active2);

            start_time_LIN.setClickable(true);
            end_time_LIN.setClickable(true);
            notice_time_range_TV.setVisibility(View.VISIBLE);
            underView_for_notice_time_range.setVisibility(View.VISIBLE);
        }
        if (!Fcm_time_switch.isChecked()) {
            text_start_TV.setTextColor(Color.parseColor("#d1d1d1"));
            text_start_time_TV.setTextColor(Color.parseColor("#d1d1d1"));
            img_start_down_triangle_IV.setImageResource(R.drawable.down_triangle_inactive);

            text_end_TV.setTextColor(Color.parseColor("#d1d1d1"));
            text_end_time_TV.setTextColor(Color.parseColor("#d1d1d1"));
            img_end_down_triangle_IV.setImageResource(R.drawable.down_triangle_inactive);

            start_time_LIN.setClickable(false);
            end_time_LIN.setClickable(false);
            notice_time_range_TV.setVisibility(View.GONE);
            underView_for_notice_time_range.setVisibility(View.GONE);
        }


        /** customTimePickerDialog 생성 */
        customTimePickerDialog_start_time = new CustomTimePickerDialog(
                this, Integer.parseInt(Fcm_start_Time_hour), Integer.parseInt(Fcm_start_Time_min), false, "start");
        customTimePickerDialog_end_time = new CustomTimePickerDialog(
                this, Integer.parseInt(Fcm_end_Time_hour), Integer.parseInt(Fcm_end_Time_min), false, "end");
    }


    /**
     * ---------------------------------------------------------------------------
     * 생명주기 ==> onResume
     * ---------------------------------------------------------------------------
     */
    @Override
    protected void onResume() {
        super.onResume();

        /** time 셋팅 */
        time_setting();

        // 스위치 리스너 1. Fcm_live_switch
        Fcm_live_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences Fcm_live_shared = getSharedPreferences("Fcm_live", MODE_PRIVATE);
            SharedPreferences.Editor Fcm_live_edit = Fcm_live_shared.edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Fcm_live_edit.putString("Fcm_live", "true").apply();
                    Log.d("setting_shared", "Fcm_live_On으로 바꿈: " + Fcm_live_shared.getString("Fcm_live", "true"));
                }
                if(!isChecked) {
                    Fcm_live_edit.putString("Fcm_live", "false").apply();
                    Log.d("setting_shared", "Fcm_live_Off로 바꿈: " + Fcm_live_shared.getString("Fcm_live", "true"));
                }


            }
        });

        // 스위치 리스너 2. Fcm_live_switch
        Fcm_Fan_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences Fcm_Fan_shared = getSharedPreferences("Fcm_Fan", MODE_PRIVATE);
            SharedPreferences.Editor Fcm_Fan_edit = Fcm_Fan_shared.edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Fcm_Fan_edit.putString("Fcm_Fan", "true").apply();
                    Log.d("setting_shared", "Fcm_Fan_On으로 바꿈: " + Fcm_Fan_shared.getString("Fcm_Fan", "true"));
                }
                if(!isChecked) {
                    Fcm_Fan_edit.putString("Fcm_Fan", "false").apply();
                    Log.d("setting_shared", "Fcm_Fan_Off으로 바꿈: " + Fcm_Fan_shared.getString("Fcm_Fan", "true"));
                }
            }
        });

        // 스위치 리스너 3. Fcm_live_switch
        Fcm_time_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SharedPreferences Fcm_time_shared = getSharedPreferences("Fcm_time", MODE_PRIVATE);
            SharedPreferences.Editor Fcm_time_edit = Fcm_time_shared.edit();
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Fcm_time_edit.putString("Fcm_time", "true").apply();
                    Log.d("setting_shared", "Fcm_time_On으로 바꿈: " + Fcm_time_shared.getString("Fcm_time", "false"));

                    text_start_TV.setTextColor(Color.parseColor("#262626"));
                    text_start_time_TV.setTextColor(Color.parseColor("#4a99a5"));
                    img_start_down_triangle_IV.setImageResource(R.drawable.down_triangle_active2);

                    text_end_TV.setTextColor(Color.parseColor("#262626"));
                    text_end_time_TV.setTextColor(Color.parseColor("#4a99a5"));
                    img_end_down_triangle_IV.setImageResource(R.drawable.down_triangle_active2);

                    start_time_LIN.setClickable(true);
                    end_time_LIN.setClickable(true);
                    notice_time_range_TV.setVisibility(View.VISIBLE);
                    underView_for_notice_time_range.setVisibility(View.VISIBLE);
                }
                if (!isChecked) {
                    Fcm_time_edit.putString("Fcm_time", "false").apply();
                    Log.d("setting_shared", "Fcm_time_Off으로 바꿈: " + Fcm_time_shared.getString("Fcm_time", "false"));

                    text_start_TV.setTextColor(Color.parseColor("#d1d1d1"));
                    text_start_time_TV.setTextColor(Color.parseColor("#d1d1d1"));
                    img_start_down_triangle_IV.setImageResource(R.drawable.down_triangle_inactive);

                    text_end_TV.setTextColor(Color.parseColor("#d1d1d1"));
                    text_end_time_TV.setTextColor(Color.parseColor("#d1d1d1"));
                    img_end_down_triangle_IV.setImageResource(R.drawable.down_triangle_inactive);

                    start_time_LIN.setClickable(false);
                    end_time_LIN.setClickable(false);
                    notice_time_range_TV.setVisibility(View.GONE);
                    underView_for_notice_time_range.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * ---------------------------------------------------------------------------
     * 클릭이벤트 ==> 방해금지 시작시간 설정
     * ---------------------------------------------------------------------------
     */
    public void setting_start_time(View view) {
        Log.d(TAG, "start_time_setting_clicked");

        customTimePickerDialog_start_time.setTitle("시작 시간");
        customTimePickerDialog_start_time.btn_clicked = 1;
        customTimePickerDialog_start_time.setCanceledOnTouchOutside(false);
        customTimePickerDialog_start_time.show();
    }


    /**
     * ---------------------------------------------------------------------------
     * 클릭이벤트 ==> 방해금지 종료시간 설정
     * ---------------------------------------------------------------------------
     */
    public void setting_end_time(View view) {
        Log.d(TAG, "end_time_setting_clicked");

        customTimePickerDialog_end_time.setTitle("종료 시간");
        customTimePickerDialog_end_time.btn_clicked = 1;
        customTimePickerDialog_end_time.setCanceledOnTouchOutside(false);
        customTimePickerDialog_end_time.show();
    }


    /**
     * ---------------------------------------------------------------------------
     * 액티비티 이동 ==> 뒤로가기 -- 소프트 키보드 백버튼 매소드 연결
     * ---------------------------------------------------------------------------
     */
    public void backClicked(View view) {
        onBackPressed();
    }


    /**
     * ---------------------------------------------------------------------------
     * 메소드 ==> time 셋팅
     * ---------------------------------------------------------------------------
     */
    public void time_setting() {
        Log.d("TImePicker", "Fcm_start_Time_hour: " + Fcm_start_Time_hour);
        Log.d("TImePicker", "Fcm_start_Time_min: " + Fcm_start_Time_min);
        Log.d("TImePicker", "Fcm_end_Time_hour: " + Fcm_end_Time_hour);
        Log.d("TImePicker", "Fcm_end_Time_min: " + Fcm_end_Time_min);

        temp_start_Time_hour = Fcm_start_Time_hour;
        temp_start_Time_min = Fcm_start_Time_min;
        temp_end_Time_hour = Fcm_end_Time_hour;
        temp_end_Time_min = Fcm_end_Time_min;

        if(Fcm_start_Time_hour.equals("0")) {
            temp_start_Time_hour = "00";
        }
        if(Fcm_start_Time_hour.length() == 1) {
            temp_start_Time_hour = "0"+Fcm_start_Time_hour;
        }
        if(Fcm_start_Time_min.equals("0")) {
            temp_start_Time_min = "00";
        }

        if(Fcm_end_Time_hour.equals("0")) {
            temp_end_Time_hour = "00";
        }
        if(Fcm_end_Time_hour.length() == 1) {
            temp_end_Time_hour = "0"+Fcm_end_Time_hour;
        }
        if(Fcm_end_Time_min.equals("0")) {
            temp_end_Time_min = "00";
        }


        int total_hour = 0;
        int total_min = 0;

        // ex> 22:00 ~ 08:00
        if(Integer.parseInt(temp_start_Time_hour) > Integer.parseInt(temp_end_Time_hour)) {
            total_hour = (24 - Integer.parseInt(temp_start_Time_hour)) + Integer.parseInt(temp_end_Time_hour);
        }
        // ex> 08:00 ~ 22:00
        if(Integer.parseInt(temp_start_Time_hour) < Integer.parseInt(temp_end_Time_hour)) {
            total_hour = Integer.parseInt(temp_end_Time_hour) - Integer.parseInt(temp_start_Time_hour);
        }
        // ex> 00:40 ~ 00:10
        if(Integer.parseInt(temp_start_Time_min) > Integer.parseInt(temp_end_Time_min)) {
            total_min = (60 - Integer.parseInt(temp_start_Time_min)) + Integer.parseInt(temp_end_Time_min);
            total_hour = total_hour - 1;
        }
        // ex> 00:10 ~ 00:40
        if(Integer.parseInt(temp_start_Time_min) < Integer.parseInt(temp_end_Time_min)) {
            total_min = Integer.parseInt(temp_end_Time_min) - Integer.parseInt(temp_start_Time_min);
        }
        Log.d("시간차이값", "total_hour: " + total_hour);
        Log.d("시간차이값", "total_min: " + total_min);

        String time_interval = "";
        if(total_hour == 0) {
            time_interval = total_min + "분 동안 알림을 받지 않습니다.";
        }
        if(total_min == 0) {
            time_interval = total_hour + "시간 동안 알림을 받지 않습니다.";
        }
        if(total_hour != 0 && total_min != 0) {
            time_interval = total_hour + "시간 " + total_min + "분 동안 알림을 받지 않습니다.";
        }



        if(Integer.parseInt(temp_start_Time_hour) >= 0 && Integer.parseInt(temp_start_Time_hour) <= 12) {
            text_start_time_TV.setText("AM " + temp_start_Time_hour + ":" + temp_start_Time_min);
        }
        if(Integer.parseInt(temp_start_Time_hour) >= 13 && Integer.parseInt(temp_start_Time_hour) <= 24) {
            text_start_time_TV.setText("PM " + (Integer.parseInt(temp_start_Time_hour)-12) + ":" + temp_start_Time_min);
        }
        if(Integer.parseInt(temp_end_Time_hour) >= 0 && Integer.parseInt(temp_end_Time_hour) <= 12) {
            text_end_time_TV.setText("AM " + temp_end_Time_hour + ":" + temp_end_Time_min);
        }
        if(Integer.parseInt(temp_end_Time_hour) >= 13 && Integer.parseInt(temp_end_Time_hour) <= 24) {
            text_end_time_TV.setText("PM " + (Integer.parseInt(temp_end_Time_hour)-12) + ":" + temp_end_Time_min);
        }

        String notice_time_range = "(" + temp_start_Time_hour + ":" + temp_start_Time_min + " ~ "
                + temp_end_Time_hour + ":" + temp_end_Time_min + ")\n" + time_interval;

        notice_time_range_TV.setText(notice_time_range);
    }



    /**
     * ---------------------------------------------------------------------------
     * 클래스 ==> 커스텀 타임피커 다이얼로그
     * ---------------------------------------------------------------------------
     */
    private class CustomTimePickerDialog extends TimePickerDialog {

        private final static int TIME_PICKER_INTERVAL = 10;
        private TimePicker mTimePicker;
        OnTimeSetListener mTimeSetListener;
        OnCancelListener onCancelListener;
        int btn_clicked = 0;
        String type;

        SharedPreferences Fcm_time_shared = getSharedPreferences("Fcm_time", MODE_PRIVATE);
        SharedPreferences.Editor Fcm_time_edit = Fcm_time_shared.edit();

        CustomTimePickerDialog(Context context, int hourOfDay, int minute, boolean is24HourView, final String type) {
            super(context, TimePickerDialog.THEME_HOLO_LIGHT, null, hourOfDay, minute/TIME_PICKER_INTERVAL, is24HourView);
            this.type = type;

            mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Log.d("TImePicker", "CustomTimePickerDialog_ onTimeSet: " + hourOfDay + ":" + minute);
                    if(type.equals("start")) {
                        Fcm_start_Time_hour = String.valueOf(hourOfDay);
                        Fcm_start_Time_min = String.valueOf(minute);

                        Fcm_time_edit.putString("start_hour", Fcm_start_Time_hour).apply();
                        Fcm_time_edit.putString("start_min", Fcm_start_Time_min).apply();
                    }
                    if(type.equals("end")) {
                        Fcm_end_Time_hour = String.valueOf(hourOfDay);
                        Fcm_end_Time_min = String.valueOf(minute);

                        Fcm_time_edit.putString("end_hour", Fcm_end_Time_hour).apply();
                        Fcm_time_edit.putString("end_min", Fcm_end_Time_min).apply();
                    }
                }
            };
            onCancelListener = new TimePickerDialog.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.d("TImePicker", "CustomTimePickerDialog_ onCancel _Hour: " + mTimePicker.getCurrentHour());
                    Log.d("TImePicker", "CustomTimePickerDialog_ onCancel _Min: " + mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                }
            };

        }

        @Override
        public void dismiss() {
            switch (btn_clicked) {
                case 1:
                    super.dismiss();
                    time_setting();
                    break;
                case 0:
                    Log.d("TImePicker", "CustomTimePickerDialog_ dismiss _Hour: " + mTimePicker.getCurrentHour());
                    Log.d("TImePicker", "CustomTimePickerDialog_ dismiss _Min: " + mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                    break;
            }
        }

        @Override
        public void updateTime(int hourOfDay, int minuteOfHour) {
            mTimePicker.setCurrentHour(hourOfDay);
            mTimePicker.setCurrentMinute(minuteOfHour / TIME_PICKER_INTERVAL);
            Log.d("TImePicker", "CustomTimePickerDialog_ updateTime: " + hourOfDay + ":" + minuteOfHour);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            Log.d("TImePicker", "mTimePicker.getCurrentHour(): " + mTimePicker.getCurrentHour());
            Log.d("TImePicker", "mTimePicker.getCurrentMinute(): " + mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);

            if(type.equals("start")) {
                switch(which) {
                    case BUTTON_POSITIVE:
                        if(mTimePicker.getCurrentHour() == Integer.parseInt(Fcm_end_Time_hour)
                                && mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL == Integer.parseInt(Fcm_end_Time_min)) {
                            btn_clicked = 0;
                            Toast.makeText(a_setting_noti.this, "종료시간과 다르게 설정해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            btn_clicked = 1;
                            mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                            break;
                        }
                    case BUTTON_NEGATIVE:
                        btn_clicked = 1;
                        cancel();
                        onCancelListener.onCancel(dialog);
                        break;
                }
            }

            if(type.equals("end")) {
                switch(which) {
                    case BUTTON_POSITIVE:
                        if(mTimePicker.getCurrentHour() == Integer.parseInt(Fcm_start_Time_hour)
                                && mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL == Integer.parseInt(Fcm_start_Time_min)) {
                            btn_clicked = 0;
                            Toast.makeText(a_setting_noti.this, "시작시간과 다르게 설정해주세요.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        else {
                            btn_clicked = 1;
                            mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute() * TIME_PICKER_INTERVAL);
                            break;
                        }
                    case BUTTON_NEGATIVE:
                        btn_clicked = 1;
                        cancel();
                        onCancelListener.onCancel(dialog);
                        break;
                }
            }
        }

        @Override
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            try {
                Class<?> classForid = Class.forName("com.android.internal.R$id");
                Field timePickerField = classForid.getField("timePicker");
                mTimePicker = (TimePicker) findViewById(timePickerField.getInt(null));
                Field field = classForid.getField("minute");

                NumberPicker minuteSpinner = (NumberPicker) mTimePicker.findViewById(field.getInt(null));
                minuteSpinner.setMinValue(0);
                minuteSpinner.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
                List<String> displayedValues = new ArrayList<>();
                for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                    displayedValues.add(String.format("%02d", i));
                }
                minuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(type.equals("start")) {
                updateTime(Integer.parseInt(Fcm_start_Time_hour), Integer.parseInt(Fcm_start_Time_min));
            }
            if(type.equals("end")) {
                updateTime(Integer.parseInt(Fcm_end_Time_hour), Integer.parseInt(Fcm_end_Time_min));
            }

        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Log.d("TImePicker", "TimePickerDialog onBackPressed 클릭!!!!");
            if(btn_clicked==0) {
                btn_clicked = 1;
                dismiss();
            }
        }
    }
}

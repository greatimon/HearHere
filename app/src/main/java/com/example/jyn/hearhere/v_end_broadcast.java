package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class v_end_broadcast extends Activity {

    Button confirm_btn;
    Handler handler;
    Click_Runnable runnable;
    TextView sec_count_TV, out_for_host_TV;

    boolean isRunning = true;
    int time_min = 5;

    String BJ_out = "";
    String cast_orNot="";


    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_end_broadcast);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(false);

        confirm_btn = (Button)findViewById(R.id.confirm_btn);
        sec_count_TV = (TextView)findViewById(R.id.sec_count);
        out_for_host_TV = (TextView)findViewById(R.id.out_for_host);
        sec_count_TV.setText(String.valueOf(time_min));

        Intent intent = getIntent();

        BJ_out = intent.getExtras().getString("BJ_out");

        if(BJ_out.equals("true")) {
            out_for_host_TV.setText("방송이 비정상 종료 되었습니다.");
        }

        if(BJ_out.equals("false")) {
            cast_orNot = intent.getExtras().getString("cast");
            if(cast_orNot.equals("true")) {
                out_for_host_TV.setText("캐스트가 종료되었습니다.");
            }
        }

        // 일정 시간 이후에 '확인'버튼 자동 클릭
        handler = new Handler();
        runnable = new Click_Runnable();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunning) {
                    try {
                        Thread.sleep(60*1000);
                        time_min--;
                        handler.post(runnable);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송 종료 확인
     ---------------------------------------------------------------------------*/
    public void confirm_clicked(View view) {

        if(BJ_out.equals("true")) {
            Intent intent_main = new Intent(v_end_broadcast.this, a_main_after_login.class);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_main);
            finish();
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    /**---------------------------------------------------------------------------
     클래스 ==> '확인'버튼 자동 클릭 Runnable -- a_main_after_login 액티비티로 이동
     ---------------------------------------------------------------------------*/
    private class Click_Runnable implements Runnable {
        public void run() {
            if(time_min >0) {
                sec_count_TV.setText(String.valueOf(time_min));
            }
            if(time_min ==0) {
                sec_count_TV.setText(String.valueOf(time_min));
                try {
                    Thread.sleep(100);
                    confirm_btn.performClick();
                    isRunning = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null) {
            handler.removeCallbacks(runnable);
        }
    }
}

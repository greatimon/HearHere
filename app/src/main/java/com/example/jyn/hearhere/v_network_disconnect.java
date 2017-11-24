package com.example.jyn.hearhere;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class v_network_disconnect extends Activity{

    TextView notice_network_status_TV;
    TextView reconnect_try_TV;
    int try_count=0;
    String again="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_network_disconnect);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(false);

        notice_network_status_TV = (TextView)findViewById(R.id.notice_network_status);
        reconnect_try_TV = (TextView)findViewById(R.id.reconnect_try);

        Intent intent = getIntent(); //again

        again = intent.getExtras().getString("again");
        if(again.equals("again")) {
            notice_network_status_TV.setText("재접속이 실패하였습니다,\n인터넷 연결을 재확인해 주세요.");
        }
        if(again.equals("network_unstable")) {
            notice_network_status_TV.setText("네트워크가 불안정하여 서버와의 접속이 끊겼습니다,\n재접속 해주세요");
        }


    }

    public void terminate_app_clicked(View view) {
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void reconnect_try_clicked(View view) {

//        if(result.equals("true")) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
//        }

//        else if(result.equals("false")) {
//            try_count++;
//            notice_network_status_TV.setText("재접속이 실패하였습니다("+String.valueOf(try_count)+"),\n인터넷 연결을 재확인해 주세요.");
//        }


    }
}

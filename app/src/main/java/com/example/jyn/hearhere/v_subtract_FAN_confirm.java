package com.example.jyn.hearhere;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class v_subtract_FAN_confirm extends Activity {

    String request_from;
    String nickName;
    TextView comment_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_subtract_fan_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(true);

        comment_TV = (TextView)findViewById(R.id.comment);

        Intent intent = getIntent();
        request_from = intent.getExtras().getString("REQUEST_FROM");

        if(request_from.equals("v_show_guests") || request_from.equals("f_my_fans")
                || request_from.equals("f_my_BJ")) {
            nickName = intent.getExtras().getString("NICKNAME");
            String str = nickName + "님의 FAN을 취소하시겠습니까?";
            comment_TV.setText(str);
        }

    }

    public void no(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void yes(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

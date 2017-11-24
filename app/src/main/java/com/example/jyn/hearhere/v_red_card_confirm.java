package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by JYN on 2017-08-23.
 */

public class v_red_card_confirm extends Activity {

    String request_from;
    String nickName;
    String user_no;
    TextView comment_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_red_card_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(true);

        comment_TV = (TextView)findViewById(R.id.comment);

        Intent intent = getIntent();
        request_from = intent.getExtras().getString("REQUEST_FROM");

        if(request_from.equals("v_show_guests")) {
            nickName = intent.getExtras().getString("NICKNAME");
            user_no = intent.getExtras().getString("USER_NO");
            Log.d("강퇴", "nickName: " + nickName);
            Log.d("강퇴", "user_no: " + user_no);

            String str = nickName + "님을 강퇴합니다.";
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#d13b00")), 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            comment_TV.setText(ssb);
        }

    }

    public void no(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void yes(View view) {
        Intent intent = new Intent();
        intent.putExtra("user_no", user_no);
        intent.putExtra("nickName", nickName);
        setResult(RESULT_OK, intent);
        finish();
    }

}

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

public class v_out_confirm extends Activity {

    TextView out_for_host_TV;
    String host_orNot="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_out_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(false);

        out_for_host_TV = (TextView)findViewById(R.id.out_for_host);

        Intent intent = getIntent();
        host_orNot = intent.getExtras().getString("host_orNot");

        if(host_orNot.equals("true")) {
            out_for_host_TV.setText("방송을 종료하시겠습니까?");
        }
        if(host_orNot.equals("false")) {
            out_for_host_TV.setText("방송을 나가시겠습니까?");
        }
        if(host_orNot.equals("cast")) {
            out_for_host_TV.setText("다시듣기를 종료하시겠습니까?");
        }

    }

    public void out_no_clicked(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void out_yes_clicked(View view) {

        if(!host_orNot.equals("cast")) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        if(host_orNot.equals("cast")) {
            Intent intent = new Intent();
//            setResult(a_room.REQUEST_OUT_FROM_CAST, intent);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}

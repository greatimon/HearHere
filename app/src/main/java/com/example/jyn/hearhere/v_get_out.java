package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by JYN on 2017-08-23.
 */

public class v_get_out extends Activity {

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_get_out);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(false);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송 종료 확인
     ---------------------------------------------------------------------------*/
    public void confirm_clicked(View view) {

//        if(BJ_out.equals("true")) {
            Intent intent_main = new Intent(v_get_out.this, a_main_after_login.class);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent_main);
            finish();
//        }
//        Intent intent = new Intent();
//        setResult(RESULT_OK, intent);
//        finish();
    }

}

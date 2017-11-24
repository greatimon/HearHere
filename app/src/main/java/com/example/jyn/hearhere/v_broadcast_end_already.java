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
 * Created by JYN on 2017-07-04.
 */

public class v_broadcast_end_already extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_broadcast_end_already);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(false);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> a_main_after_login 액티비티로 이동
     ---------------------------------------------------------------------------*/
    public void to_main_clicked(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}

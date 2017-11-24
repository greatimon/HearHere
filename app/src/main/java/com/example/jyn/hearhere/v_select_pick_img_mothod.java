package com.example.jyn.hearhere;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class v_select_pick_img_mothod extends Activity {

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_select_pick_img_mothod);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(true);
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 앨범에서 사진 가져오기 선택 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void pick_from_album(View view) {
        Intent intent = new Intent();
        setResult(a_profile.SELECT_PICK_FROM_ALBUM, intent);
        finish();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 사진 촬영 선택 -- 액티비티 종료
     ---------------------------------------------------------------------------*/
    public void take_photo(View view) {
        Intent intent = new Intent();
        setResult(a_profile.SELECT_TAKE_PHOTO, intent);
        finish();
    }
}

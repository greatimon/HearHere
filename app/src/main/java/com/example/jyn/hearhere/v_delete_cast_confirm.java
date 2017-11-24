package com.example.jyn.hearhere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JYN on 2017-08-24.
 */

public class v_delete_cast_confirm extends Activity {

    String broadCast_no;
    Handler after_delete_my_cast_handler;

    /**---------------------------------------------------------------------------
     생명주기 ==> onCreate
     ---------------------------------------------------------------------------*/
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_delete_cast_confirm);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setFinishOnTouchOutside(true);

        Intent intent = getIntent();
        broadCast_no = intent.getExtras().getString("broadCast_no");

        after_delete_my_cast_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 내 캐스트 삭제 완료 시
                if(msg.what == 0) {
                    Log.d("내캐스트", "캐스트 삭제 성공");
                    Toast.makeText(v_delete_cast_confirm.this, "캐스트가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                // 내 캐스트 삭제 실패 시
                if(msg.what == 1) {
                    Log.d("내캐스트", "캐스트 삭제 실패");
                    Toast.makeText(v_delete_cast_confirm.this, "캐스트 삭제 실패", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송삭제 YES
     ---------------------------------------------------------------------------*/
    public void yes(View view) {
        delete_my_cast();
    }


    /**---------------------------------------------------------------------------
     클릭이벤트 ==> 방송삭제 NO
     ---------------------------------------------------------------------------*/
    public void no(View view) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }


    /**---------------------------------------------------------------------------
     메소드 ==> 방송 삭제하기 -- 캐스트일 때 (레트로핏2)
     ---------------------------------------------------------------------------*/
    public void delete_my_cast() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Static.SERVER_URL_HEADER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);

        Call<ResponseBody> result = retrofitService.delete_my_cast(Static.DELETE_MY_CAST, broadCast_no);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Log.d("내캐스트", "delete_my_cast_result: " + result);
                    if(result.equals("deleted")) {
                        after_delete_my_cast_handler.sendEmptyMessage(0);
                    }
                    else if(result.equals("fail")) {
                        after_delete_my_cast_handler.sendEmptyMessage(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("retrofit_result", "onFailure_result: " + t.getMessage());
            }
        });
    }
}

package com.example.jyn.hearhere;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class v_report extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_report);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.setFinishOnTouchOutside(true);
    }

    public void obscenity_clicked(View view) {
        Toast.makeText(v_report.this, "음란성 신고", Toast.LENGTH_SHORT).show();
    }

    public void impersonation_clicked(View view) {
        Toast.makeText(v_report.this, "도용 및 사칭 신고", Toast.LENGTH_SHORT).show();
    }

    public void calumny_clicked(View view) {
        Toast.makeText(v_report.this, "비방 신고", Toast.LENGTH_SHORT).show();
    }

    public void etc_clicked(View view) {
        Toast.makeText(v_report.this, "기타 신고", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

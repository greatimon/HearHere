package com.example.jyn.hearhere;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class a_find_pw extends Activity{

    EditText email_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_find_pw);

        email_edit = (EditText)findViewById(R.id.input_email);

        // editText 밑줄 색 커스텀
        int color = Color.parseColor("#fbfbfb");
        email_edit.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);

    }

    public void backClicked(View view) {
        onBackPressed();
    }

    public void get_temp_pw_clicked(View view) {

    }
}

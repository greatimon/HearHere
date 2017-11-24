package com.example.jyn.hearhere;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class f_fan_board extends Fragment {

    public f_fan_board() {
        // Required empty public constructor
    }

    public static f_fan_board newInstance() {
        Bundle args = new Bundle();

        f_fan_board fragment = new f_fan_board();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.f_fan_board, container, false);
    }
}

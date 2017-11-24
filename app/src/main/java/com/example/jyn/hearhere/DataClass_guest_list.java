package com.example.jyn.hearhere;

import android.app.Application;

// guest 리스트를 보여줄때도 사용하고
// FAN_BJ 리스트를 보여줄때도 사용한다
// (아이템 필요 정보가 같음)
public class DataClass_guest_list extends Application {

    private int user_no;            // 회원 번호
    private String User_img_file;   // 이미지 파일
    private String User_nicName;    // 닉네임
    private String Fan_orNot;       // 팬 여부

    DataClass_guest_list() {}

    public DataClass_guest_list(int user_no, String user_img_file, String user_nicName, String fan_orNot) {
        this.user_no = user_no;
        User_img_file = user_img_file;
        User_nicName = user_nicName;
        Fan_orNot = fan_orNot;
    }

    public int getUser_no() {
        return user_no;
    }

    public void setUser_no(int user_no) {
        this.user_no = user_no;
    }

    public String getUser_img_file() {
        return User_img_file;
    }

    public void setUser_img_file(String user_img_file) {
        User_img_file = user_img_file;
    }

    public String getUser_nicName() {
        return User_nicName;
    }

    public void setUser_nicName(String user_nicName) {
        User_nicName = user_nicName;
    }

    public String getFan_orNot() {
        return Fan_orNot;
    }

    public void setFan_orNot(String fan_orNot) {
        Fan_orNot = fan_orNot;
    }
}
